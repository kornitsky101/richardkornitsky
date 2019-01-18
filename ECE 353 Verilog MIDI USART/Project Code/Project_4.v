//keep module named test since it's the file name
module Project_4(LED, dataOut, clk, MIDI, clkin);	//dataValid -- need to add
	input clkin, MIDI;
	output [7:0] LED, dataOut;
	output clk;
	
	wire clk;
	wire MIDIbuff;
	wire [7:0] data;
	wire [7:0] dataOut;
	wire start;
	wire [1:0] frame;
	reg onOff;
	wire save;
	wire change;
	
	clkScale C1 (clk, clkin);
	inputBuffer I1 (MIDIbuff, MIDI, clk);
	decoder D1 (data, clk, MIDIbuff);
	record R1 (dataOut, save, data, clk);
	frameStatus F1 (change, frame, save, clk);
	LEDlit L1 (LED, clk, change, frame, dataOut);
endmodule

module LEDlit(LED, clk, change, frame, dataOut);
	input clk, change;
	input [1:0] frame;
	input [7:0] dataOut;
	output [7:0] LED;
	reg onOff;
	reg [7:0] LED;
	
	always @(posedge clk)begin
		if(change == 1'b1)begin
			if(frame == 2'b01)begin
				if(dataOut == 8'h9)begin
					onOff = 1'b1;
				end
				else begin
					onOff = 1'b0;
				end
			end
			else if(frame == 2'b10) begin
				if(onOff == 1'b1)begin
					LED = dataOut;
				end
				else begin
					LED = 8'b0;
				end
			end
			else begin
			end
		end
	end
endmodule


module frameStatus(change, frame, save, clk);
	input save, clk;
	output change;
	output [1:0] frame;
	
	reg [1:0] frame;
	previousFrame P1 (change, save, clk);
	always @ (negedge clk)begin
		if(save == 1'b1)begin
			if(frame == 2'b01)begin
				frame = 2'b10;
			end
			else if(frame == 2'b10)begin
				frame = 2'b11;
			end
			else if(frame == 2'b11)begin
				frame = 2'b01;
			end
			else begin
				frame = 2'b11;
			end
		end
	end
endmodule

module previousFrame(change, save, clk);
	input save, clk;
	output change;
	reg change;
	always @ (negedge clk)begin
		if(save == 1'b1)begin
			change = 1'b1;
		end
		else begin
			change = 1'b0;	
		end
	end
endmodule


module record(dataOut, save, data, clk);
	input [7:0] data;
	input clk;
	output [7:0] dataOut;
	output save;
	
	reg start;
	reg startLast;
	reg save;
	wire [3:0]cnt;
	wire [7:0]dataOut;
	counter C1 (cnt, clk, start);
	holdData H1 (dataOut, data, save, clk);
	
	always @ (*)begin
		if(data[7:0] == 8'b11111111)begin
			start <= 1'b0;
		end
		else begin
			start <= 1'b1;
		end
		
		if(cnt == 4'b1000)begin
			save <= 1'b1;
			start <= 1'b0;
		end
		else begin
			save <= 1'b0;
		end
	end
endmodule

module holdData(stored, data, save, clk);
	input save, clk;
	input [7:0]data;
	output [7:0] stored;
	reg [7:0]stored;
	
	always@(negedge clk)begin
		if(data != 8'hFF)begin
			if(save == 1'b1)begin
				stored = data;
			end
		end
	end
endmodule


module counter (cnt, clk, start);							//Counter module used for record
	input clk, start;
	output [3:0]cnt;
	reg [3:0] cnt;
	wire [3:0] cnt_next;
	assign cnt_next = cnt + 4'b1;
	
	always @ (posedge clk) begin
		if(start)begin
			cnt = cnt_next;
		end
		else begin
			cnt = 4'b0;
		end
	end
endmodule

module inputBuffer(dataOut, dataIn, clk);
  input dataIn, clk;
  output dataOut;
  wire transfer;
  Dflip D1 (transfer, dataIn, clk);
  Dflip D2 (dataOut, transfer, clk);
endmodule//end inputBuffer



module decoder(data, clk, MIDI);
  input clk, MIDI;
  output [7:0] data;
  wire [7:0] trans;
  Dflip D1 (trans[7], MIDI, clk);					//DFF locking in each bit an passing it along from the serial data
  Dflip D2 (trans[6], trans[7], clk);
  Dflip D3 (trans[5], trans[6], clk);
  Dflip D4 (trans[4], trans[5], clk);
  Dflip D5 (trans[3], trans[4], clk);
  Dflip D6 (trans[2], trans[3], clk);
  Dflip D7 (trans[1], trans[2], clk);
  Dflip D8 (trans[0], trans[1], clk);
  assign data = trans;								//Transfering the outputs from the DFF into the output wires
endmodule//end decoder



module Dflip(q, data, clk);   //d flip flop -----corrected
  input data, clk;
  output q;
  reg q;
  always @ (posedge clk)
      q <= data;
endmodule//end 

module DflipR(q, data, clk, r);   //d flip flop -----corrected
  input clk, r;
  input [3:0] data;
  output [3:0]q;
  reg [3:0]q;
  always @ (posedge clk)
	if(r)begin
		q <= 4'b0;
	end
	else begin
		q <= data;
	end
endmodule//end

module ALU(out, s, in1, in0);
	input [3:0] in1, in0;
	input s;
	output [3:0] out;
	reg [3:0]out;
	always @ (*)begin
		if(s)begin
			out <= in1;
		end
		else begin
			out <= in0;
		end
	end
endmodule 


module clkScale(clkout, clkin);						//module adjusts the clock from 4MHz to 31.25 KHz clock
	input clkin;
	output clkout;
	
	reg clkout;
	reg [6:0]cnt = 7'b0;							//Uses a count to scale frequency 
	reg clkreg = 1'b0;
	wire [6:0]cnt_next;
	assign cnt_next = cnt + 1'b1;
	
	always @(posedge clkin)begin					//32uS clock has period 32uS with 16uS of low voltage followed by 16uS of high voltage
		if(cnt == 7'b0111111)begin					//If halfway through the count -- 16uS
			clkreg = 1'b1;							//Change the output clock from 0 to 1
		end//end if
		if(cnt == 7'b1111111)begin					//If at the end of the count -- 32uS
			clkreg = 1'b0;							//Change the output clock from 1 to 0
		end//end if
		cnt <= cnt_next;							//Incrament the counter
		clkout <= clkreg;							//Output the clock
  end//end always
endmodule//end clkScale

