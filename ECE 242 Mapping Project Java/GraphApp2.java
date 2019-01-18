import java.io.*;
import java.util.*;


////////////////////////////////////////////////////////////////
class GraphApp2
{


    public static void main(String[] args)
    {
        EasyIn easy = new EasyIn();
	String pause;
       
	
	System.out.println("\nWelcome to Graph App 2");
	System.out.println("=======================\n");

        //Generate a Graph using a Grid with random weights (from 1 to 4)
	System.out.println("Enter Total Grid Size x: ");
	int nx=easy.readInt();
	System.out.println("Enter Total Grid Size y: ");
	int ny=easy.readInt(); 
	Graph theGraph=new Graph(nx,ny);
	theGraph.randomGridWeight(); 
	 
	
        System.out.println("INFO FOR MAIN GRAPH");
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


	System.out.println("\nPress return to continue");
	pause=easy.readString(); // make a pause
	    
        /////////////////////////////////////////////////////////
	///  Compute the Minimum Spanning Tree (create new Graph)
	/////////////////////////////////////////////////////////
	Graph mst=theGraph.mstw();            // minimum spanning tree

	System.out.println("INFO FOR MST");
	// display the mst info
	mst.displayInfoGraph();
	// if Nvertex<=9, display the new mst matrix
	if (mst.getnVertex()<=9) mst.displayAdjMat();
       
        /// Plot the mst 
	mst.plot("BLUE");


	System.out.println("\nPress return to continue");
	pause=easy.readString(); // make a pause
	
	
        //////////////////// DFS and BFS (on the unweighted MST)
	int command=-1;
	while (command!=0){
	    System.out.println("\nWhat do you want to do? (1:DFS, 2:BFS, 0: Exit)?");
	    StdDraw.clear();  // clear the canvas
	    mst.plot("BLUE"); // replot the mst
	    command=easy.readInt();
	    if (command==1) mst.dfs();     // call DFS algo
	    else if (command==2) mst.bfs();// call BFS algo
	    else if (command==0) System.exit(0); // Leave the code
       System.out.println("\nPress return to continue");
	    pause=easy.readString(); // make a pause
		
	}		
		    
		   
    }
}


