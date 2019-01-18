import java.io.*;
import java.util.*;


////////////////////////////////////////////////////////////////
class GraphApp1
{


    public static void main(String[] args)
    {
        EasyIn easy = new EasyIn();
       
	
	System.out.println("\nWelcome to Graph App 1");
	System.out.println("=======================\n");

      
	 
	//Generate a Graph using a Grid with uniform weights
	System.out.println("Enter Total Grid Size x: ");
	int nx=easy.readInt();
	System.out.println("Enter Total Grid Size y: ");
	int ny=easy.readInt(); 
	Graph theGraph=new Graph(nx,ny);
	theGraph.uniformGridWeight();

	// display the graph info
	 theGraph.displayInfoGraph();
	// if Nvertex<=9, display the matrix
	if (theGraph.getnVertex()<=9) theGraph.displayAdjMat();
       
		    
		    
	/// Prepare plot
	StdDraw.setCanvasSize(nx*60,ny*60); // size canvas
	StdDraw.setXscale(-1, nx);    //  x scale
	StdDraw.setYscale(-1, ny);    //  y scale
	
	/// Plot the main graph
	theGraph.plot("GRAY");

	String pause;
	int command=-1;
	while (command!=0){
	    System.out.println("\nWhat do you want to do? (1:DFS, 2:BFS, 0: Exit)?");
	    command=easy.readInt();
	    if (command==1) theGraph.dfs();     // call DFS algo
	    else if (command==2) theGraph.bfs();// call BFS algo
	    else if (command==0) System.exit(0); // Leave the code

	    System.out.println("\nPress return to continue");
	    pause=easy.readString(); // make a pause
	    StdDraw.clear();  // clear the canvas
	    theGraph.plot("GRAY"); // replot the original graph
		
	}
		
		    
		   
    }
}


