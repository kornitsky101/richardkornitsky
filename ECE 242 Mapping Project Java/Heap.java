//Richard Kornitsky ID:29834305 rkornitsky@umass.edu
class Heap
{
    private Edge[] heapArray; 
    private int N; // current size
    private int MaxSize;
    
    public Heap(int MaxSize)            // constructor
    {
	heapArray = new Edge[MaxSize];
	N = 0;
   this.MaxSize = MaxSize;
    }
    
   public boolean isEmpty(){
      return(N==0);
   }

	public Edge remove(){
		Edge root = heapArray[0];  //taken from text
		heapArray[0] = heapArray[--N];
		trickleDown(0);
		return root;
	}

	public void trickleDown(int index){ //taken from text
		int largerChild;
		Edge top = heapArray[index];
		while(index < N/2){
			int leftChild = 2*index + 1;
			int rightChild = leftChild + 1;

			if(rightChild < N &&    //takes the larger weight and puts it to the lowest priority in the heap
				heapArray[leftChild].getWeight()>heapArray[rightChild].getWeight()){
				largerChild = rightChild;
			}
			else{
				largerChild = leftChild;
			}

			if(top.getWeight()<=heapArray[largerChild].getWeight()){
				break;
			}
			heapArray[index] = heapArray[largerChild];
			index = largerChild;
		}
		heapArray[index] = top;
	}
   
	public boolean insertHeap(int x, int y, int weight){  //taken from text
		if(N == MaxSize){
         return false;
      }
      Edge newEdge = new Edge(x,y,weight);   //creating a new egde to go into the edge array that is the heapArray
      heapArray[N] = newEdge;
      trickleUp(N++);
      return true;
      
   }
   
   public int find(int findIndex){  //find in heap
      for(int i=0; i<N; i++){
         if(heapArray[i].getDest() == findIndex){  //using linear search to find in the heap
            return i;
         }
      }
      return -1;
   }
   
   
   public void trickleUp(int index){//taken from text
      int parent = (index-1)/2;
      Edge bottom = heapArray[index];
      while(index > 0 && heapArray[parent].getWeight() > bottom.getWeight()){ //takes the smallest weights and puts it to the highest priority in the heap
         heapArray[index] = heapArray[parent];
         index = parent;
         parent = (parent-1)/2;
      }
      heapArray[index] = bottom;
   }
}


    

