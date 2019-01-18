//Richard Kornitsky ID:29834305 rkornitsky@umass.edu
import java.io.*;
import java.util.*;


////////////////////////////////////////////////////////////////
class Edge
{
    private int start;   // index of a vertex starting edge
    private int end;     // index of a vertex ending edge
    private int weight;  // weight from start to end
    // -------------------------------------------------------------
    public Edge(int sv, int dv, int d)  // constructor
    {
	start = sv;
	end = dv;
	weight = d;
    }

    public int getWeight(){return weight;}
    public int getSource(){return start;}
    public int getDest(){return end;}
    
    
    // -------------------------------------------------------------
}  // end class Edge


////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////


class Vertex
{
    public int i,j;       // points coordinate (i,j)
    public boolean isVisited;
    public boolean hasConnected;
    // -------------------------------------------------------------
    public Vertex(int i,int j)   // constructor
    {
	this.i = i;
	this.j=j;
	isVisited = false;
   hasConnected = false;
    }
    // -------------------------------------------------------------
    public void display()
    {
	System.out.print("("+i+","+j+")");
    }
}  // end class Vertex


////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////


class Graph
{
    private Vertex nodes[];       // list of vertices
    private int adjMat[][];       // adjacency matrix
    private int nVertex;          // Number of vertices/nodes
    private int nEdges;           // Number of vertices/nodes
    private int nx,ny; // nx,ny size of the rectangular Graph
    private Heap theHeap;
    private int currentVert;
    private boolean even;
    
    public Graph(int nx, int ny){
      this.nx = nx;
      this.ny = ny;
      nVertex = nx*ny;
      int counter = 0;
      adjMat = new int[nVertex][nVertex];
      nodes = new Vertex[nVertex];
      for(int i=0; i<nVertex; i++){
         for(int j=0; j<nVertex; j++){
            adjMat[i][j]=0;               //initializing all spots in the matrix as 0
         }
      }
      int cnt = 0;
      for(int y=0; y<ny; y++){
         for(int x=0; x<nx; x++){
            nodes[cnt] = new Vertex(x,y);    //defining all nodes
            cnt++;
         }
      }
      theHeap = new Heap(nVertex);     //creating a new heap
    }
    
    public int getnVertex(){
      return nVertex;
    }
    
    public void addEdge(int x, int y, int weight){
      adjMat[x][y] = weight;     //adding edges at both positions to make the matrix accurate
      adjMat[y][x] = weight;
    }
    
    public void uniformGridWeight(){      //checks all 4 places adjacent to the node
      even = true;
      for(int i=0; i<nVertex; i++){
         if(i-nx>0){    //if there is a node above
            addEdge(i,i-nx,1);
         }
         if(i+nx<nVertex){ //if there is a node below
            addEdge(i,i+nx,1);
         }
         if(i%nx != 0){    //if there is a node to the left
            addEdge(i,i-1,1);
         }
         if((i+1)%nx != 0){      //if there is a node to the right
            addEdge(i,i+1,1);
         }
      }
    }
    
    public void displayInfoGraph(){
      System.out.println("");
      System.out.println("List of edges + weights:");
      int counter = 0;
      for(int i=0; i<nVertex; i++){
         for(int j=0; j<nVertex; j++){ //running through the entire matrix
            if(adjMat[i][j]!=0 && (j%nx>i%nx || j/ny>i/ny)){   //printing out the higher values of i and j only in the matrix
               System.out.println("(" + j%nx + "," + j/ny + ") <--> (" + i%nx + "," + i/ny + ") " + adjMat[i][j]);   //using this print out since display would not work in this implamentaion
               counter += adjMat[i][j];
            }
         }
      }
      System.out.println("Total weight: " + counter);
      System.out.println("");
    }
    
    public void displayAdjMat(){
      System.out.println("Matrix:");
      System.out.println("");
      for(int i=0; i<nVertex; i++){
         for(int j=0; j<nVertex; j++){    //running through the matrix in order to print
            System.out.print(adjMat[j][i] + " ");
         }
         System.out.println("");
      }
    }
    
    public void dfs(){
      nodes[0].isVisited = true;    //initializing search
      Stack s = new Stack(nVertex);
      s.push(0);
      
      while(!s.isEmpty()){
         StdDraw.setPenColor(StdDraw.RED);   //for app1 drawing freatures
         int currentNode = (int) s.peek();
         int v = getAdjUnvisitedNode(currentNode);
         if(v == -1){      //if there is an edge already
            s.pop();
         }
         else{
            nodes[v].isVisited = true;    //setting visited to true
            s.push(v);
            System.out.print("From ");
            nodes[currentNode].display();
            System.out.print(" to ");
            nodes[v].display();
            System.out.println("");    //printing what-not
            if(even){
               StdDraw.filledCircle(nodes[v].i,nodes[v].j,0.25);
               StdDraw.filledCircle(nodes[currentNode].i,nodes[currentNode].j,0.25);
               StdDraw.line(nodes[v].i,nodes[v].j,nodes[currentNode].i,nodes[currentNode].j);      //diplaying graphics quick for app1
            }
         }
      }
      if(!even){
         plot("RED");      //displaying graphics using the slower method for app2
      }
      for(int a=0; a<nVertex; a++){
         nodes[a].isVisited = false;      //resetting the the visit satus afterwawrds
      }
    }
    
    public void bfs(){
      nodes[0].isVisited = true;    //iniitializing the search
      Queue q = new Queue(nVertex);
      StdDraw.setPenColor(StdDraw.RED);
      q.enqueue(0);
      int v2;
      while(!q.isEmpty()){
         int v1 = (int) q.dequeue();
         while((v2=getAdjUnvisitedNode(v1))!=-1){  //while there is a place to add an edge for the value of v1
            nodes[v2].isVisited=true;
            System.out.print("From "); //printing what-not
            nodes[v1].display();
            System.out.print(" to ");
            nodes[v2].display();
            System.out.println("");
            if(even){
               StdDraw.filledCircle(nodes[v1].i,nodes[v1].j,0.25);   //displaying graphics quick for app1
               StdDraw.filledCircle(nodes[v2].i,nodes[v2].j,0.25);
               StdDraw.line(nodes[v1].i,nodes[v1].j,nodes[v2].i,nodes[v2].j);
            }
            q.enqueue(v2);
         }
      }
      if(!even){
         plot("RED");   //displaying graphics using the slower method for app2
      }
      for(int a=0; a<nVertex; a++){
         nodes[a].isVisited = false;   //resetting the visit satatus
      }
    }
    
    public int getAdjUnvisitedNode(int v){
      for(int i=0; i<nVertex; i++){
         if(adjMat[v][i]!=0 && !nodes[i].isVisited){  //checking to see if there is an unpaired edge to the node
            return i;   //returns the node if found
         }
      }
      return -1;
    }
    
    //////////////////////////////////////
    //////////App 2//////////////////////
    ////////////////////////////////////
    
    public void randomGridWeight(){
      even = false;
      Random rand = new Random();   //random weight set up
      for(int i=0; i<nVertex; i++){ //checks all 4 adjacent spots to the node
         int n = rand.nextInt(4)+1; //setting random number each sweep
         if(i-nx>0){    //checking if node above
            addEdge(i,i-nx,n);
         }
         if(i+nx<nVertex){ //checking if node below
            addEdge(i,i+nx,n);
         }
         if(i%nx != 0){    //checking if node to the left
            addEdge(i,i-1,n);
         }
         if((i+1)%nx != 0){   //checking if node to the right
            addEdge(i,i+1,n);
         }
      }
    }
    
    public Graph mstw(){
      Graph mst = new Graph(nx,ny);    //creating new graph and other initializing
      int current = 0;
      int inTree = 0;
      while(inTree<nVertex){  //runs until the tree is full
         nodes[current].isVisited = true; //more initializing within the while loop
         inTree++;
         for(int j=0; j<nVertex; j++){ //running through the row of the matrix
            if(j==current){//skips the value of j if one of these cases are met
               continue;
            }
            if(nodes[j].isVisited){
               continue;
            }
            int weight = adjMat[current][j];
            if(weight == 0){
               continue;
            }
            theHeap.insertHeap(current,j,weight);  //if none of the conditions are met, insert the edge into the heap
         }
         Edge theEdge = theHeap.remove();
         int dest = theEdge.getDest();
         while(nodes[dest].hasConnected && !theHeap.isEmpty()){   //checks for the next place a connection is needed
            theEdge = theHeap.remove();
            dest = theEdge.getDest();
         }
         current = theEdge.getDest();
         mst.addEdge(theEdge.getSource(), current,adjMat[current][theEdge.getSource()]);  //creating the connection
         nodes[current].hasConnected = true;    //setting the node to connected
      }
      
      return mst;
    }
    

    

    
    ///////////////////////////////////////////////////////
    //// Plot the Graph using the StdDraw.java library
    ///////////////////////////////////////////////////////
    public void plot(String color){
	
	if (color.equals("BLUE"))
	    StdDraw.setPenColor(StdDraw.BLUE);  // change pen color
	else if (color.equals("GRAY"))
	    StdDraw.setPenColor(StdDraw.GRAY);  // change pen color
	else if (color.equals("RED"))
	    StdDraw.setPenColor(StdDraw.RED);  // change pen color
    
	for (int i=0;i<nVertex;i++)
	    for (int j=0;j<=i;j++)
		if(adjMat[i][j]!=0){
		    StdDraw.setPenRadius(adjMat[i][j]*adjMat[i][j]*0.0025);
		    StdDraw.filledCircle(nodes[i].i,nodes[i].j,0.25);  // plot node
		    StdDraw.filledCircle(nodes[j].i,nodes[j].j,0.25);  // plot node
		    StdDraw.line(nodes[i].i,nodes[i].j,nodes[j].i,nodes[j].j); //plot edges
		}
       
    }
    
   
    

}  // end class Graph

