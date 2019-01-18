class Stack{
    
    //variables
    private int maxSize; // Stack capacity initialized by user
    private int top;     // top of stack
    private Object[] array; // array that holds the items
  
    // constructors
    public Stack(int maxSize) 
    {
	this.maxSize=maxSize;    // set array size
	array = new Object[maxSize];// create array 
	top = -1;                // no item yet
    }
  
    //methods
    public boolean isEmpty() 
    {
	return(top==-1);//true if stack is empty
    }
  
  
    public boolean isFull() 
    {
	return(top==maxSize-1);//true if stack is full
    }
  
  
    public int size() 
    {
	return(top+1);// return curent number of items
    }

    ////////////////////////////////////////////////
    public Object peek() {
	if (!isEmpty())
	    return array[top];     //object at the top 
	else
	    throw new IllegalStateException ("Stack is empty");
    }
  
    ////////////////////////////////////////////////
    public Object pop() {
	if (!isEmpty()){
	    top--;                 //decrement top
	    return(array[top+1]);
	}
	else
	    throw new IllegalStateException ("Stack is empty");
    }
  
    ////////////////////////////////////////////////
    public void push(Object item) {
	if (!isFull()){
	    top++;             //increment top
	    array[top]= item;  //insert item 
	}
    }    

    /// swap the item on top and top-1
    public void swap() {
	if (size()>=2){
	    Object o1=pop();
	    Object o2=pop();
	    push(o1);
	    push(o2);
	}
    }

    //// duplicate the top item   
    public void copy() {
	if (!isEmpty()){
	    push(peek());            
	}
    }    

    
    //// display the queue    
    public void display(){
        int temp=top;
	for (top=0;top<=temp;top++) System.out.println(top+"                     "+peek());
        top=temp;
    }
   

    
}
////////////////////////////////////
