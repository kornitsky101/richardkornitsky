module test2(frame, dataIn, clk, change);
	input [7:0] dataIn;
	input clk;
	output [0:1] frame;
	output change;
	
	reg [0:1]frame;
	wire change;
	previousFrame P1 (change, dataIn, clk);
	
	always @ (posedge change) begin
		if(frame == 2'd1)begin
			frame = 2'd2;
		end
		else if(frame == 2'd2)begin
			frame = 2'd3;
		end
		else if(frame == 2'd3)begin
			frame = 2'd1;
		end
		else begin
			frame = 2'd3;
		end
	end
endmodule

module previousFrame(change, data, clk);
	input [7:0] data;
	input clk;
	output change;
	
	reg change;
	reg [7:0]oldNote;
	
	always @ (posedge clk)begin
		if(oldNote == data)begin
			change = 1'b0;
		end
		else begin
			oldNote = data;
			change = 1'b1;
		end
	end
endmodule
