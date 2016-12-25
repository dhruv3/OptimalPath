package AI_Map;

import java.util.Arrays;

import AI_Map.Node;

public class BinaryHeap {
	
    private int index=0;
    public Node[] heap;
    
    public BinaryHeap(int size){
        heap = new Node[size+1];
    }
    
	public void insert(Node element){
        heap[++index] = element;
        travelUp(index);
	}
	

	private void travelUp(int newPosition) {
		Node temp = heap[newPosition];
	      int parent = (int)Math.ceil(((double)(newPosition-1)/2));
	      while (newPosition > 1 && heap[parent].getFCost() > temp.getFCost()){
	    	 heap[newPosition] = heap[parent];
	         newPosition = parent;
	         parent = (int)Math.ceil(((double)(newPosition-1)/2));
	      }
	      heap[newPosition] = temp;
	      //separate case for index 2 and 3
	      if(index == 2 || index == 3){
	    	  if(heap[1].getFCost() > temp.getFCost()){
	    		  heap[index] = heap[1];
	    	  	  heap[1] = temp;
	    	  }
	    	  else
	    		  heap[index] = temp;
	      }
	}
	
	public int getIndex(Node element) {
		for(int i = 1; i < heap.length; i++){
			if(heap[i].getX() == element.getX() && heap[i].getY() == element.getY())
				return i;
		}
		return 0;
	}

	public Node getNode(int index) {
		return heap[index];
	}
	
	public void remove(int nodeIndex) {
		if(nodeIndex != 0){
			heap[nodeIndex] = heap[index];
			index--;		
			travelDown(nodeIndex);
		}
	}
	
	public int getSize() {
		return index;
	}

	private void travelDown(int currPos) {
		int leftNode= 2*currPos;
        int rightNode = (2*currPos)+1;
        Node temp = heap[currPos];
        
        while(leftNode<=index){
        	
        	if(rightNode<=index){
        		if (temp.getFCost() > heap[leftNode].getFCost() && temp.getFCost() > heap[rightNode].getFCost()) {
        			if(heap[leftNode].getFCost() >= heap[rightNode].getFCost()){
        				heap[currPos] = heap[rightNode];
        				currPos = rightNode;
        			}
        			else{
        				heap[currPos] = heap[leftNode];
        				currPos = leftNode;
        			}
        		}
        		else if ((temp.getFCost() >= heap[leftNode].getFCost() && temp.getFCost() < heap[rightNode].getFCost())) {
        			heap[currPos] = heap[leftNode];
        			currPos = leftNode;
        		}
        	    else if (temp.getFCost() < heap[leftNode].getFCost() && temp.getFCost() >= heap[rightNode].getFCost()) {
        			heap[currPos] = heap[rightNode];
        			currPos = rightNode;
        		}
        	    else{
        	    	break;
        	    }
        	}
        	else{
        		if (temp.getFCost() >= heap[leftNode].getFCost()) {
        			heap[currPos] = heap[leftNode];
        			currPos = leftNode;
        		}
        	    else{
        	    	break;
        	    }
        	}
        	leftNode = 2*currPos;
        	rightNode = (2*currPos)+1;
        
        }
        heap[currPos] = temp;
	}
	
	public void clear() {
		index=0;
	}
	static int rcounter = 0, wcounter = 0;
	public Node getRoot() {
		return heap[1];
	} 
	
	public int contains(Node nextNode) {
		int temp=0;
		if(isEmpty()){
			return 0;
		}
		else{
			for (int i=1;i<=index;i++){
				if(heap[i].getX() == nextNode.getX() && heap[i].getY() == nextNode.getY()){
					temp=i;
					break;
				}
			}
		}
		return temp;
	}

	public boolean isEmpty() {
		if(index == 0){
    		return true;
    	}
		return false;
	}

	public int getIndex() {
		return index;
	}
	
}