class Queue{
  
    private int maxSize; // Queue capacity
    private int front;   // Queue front
    private int rear;    // Queue rear
    private int nItems;  //#items in the queue
    private Object[] array; // array that holds the items


    //// Constructors
    public Queue(int maxSize) 
    {
	this.maxSize=maxSize;    // set array size
	array = new Object[maxSize];//create array 
	nItems = 0;              // no item yet
	front = 0;            
	rear = -1;                
    }
    
    public Queue(Queue myqueue)  // duplicate constuctor
    {
	this.maxSize=myqueue.maxSize;
	this.front=myqueue.front;
	this.rear=myqueue.rear;
	this.nItems=myqueue.nItems;
	this.array = new Object[maxSize];//create array
	for (int i=0;i<nItems;i++) this.array[i]=myqueue.array[i];
    }



    ///// Methods    
    public boolean isEmpty() 
    {
	return(nItems==0);//true if queue is empty
    }
  
  
    public boolean isFull() 
    {
	return(nItems==maxSize);//true if queue is full
    }
  
  
    public int size() 
    {
	return(nItems);// return curent number of items
    }
  
    public Object peekFront() { //peek front item
	if (isEmpty()) 
	    throw new IllegalStateException("Queue is empty");
	return array[front]; //return front item
    }
  
    ////////////////////////////////////////////////
    public Object dequeue() { //remove item
	if (isEmpty())
	    throw new IllegalStateException("Queue is empty"); 
	Object temp=array[front];//get front item
	front=(front+1)%maxSize; //increment front wraparound
	nItems--;                //decrement #items
	return temp;   
    }
  
    ////////////////////////////////////////////////
    public void enqueue(Object item) { //insert item
	if (isFull())
	    throw new IllegalStateException("Queue is full");
	if(rear==maxSize-1) rear=-1;//increment rear wrap
	rear++;
	array[rear]= item;  //insert item 
	nItems++;            //increment #items
    }

    
    ///////////////////////////////////////////////////////
    //// Add more methods if needed, content, display, etc.
    //////////////////////////////////////////////////////





    
    
    
}





















