package AI_Map;
import java.util.ArrayList;


public class Node {
	private int x;
	private int y;
	public char value;
	private float fCost;
	private int hNewVal;
	float search=0f;
	public float Cost = 1;
	private float gCost;
	public float [] g = new float[5];
	public float [] h = new float[5];
	public float [] f = new float[5];
	private float hDistance;
	private int blocked; //int 1 = unblocked; int 0 = blocked
	private boolean hardToTraverse;
	private boolean containsHighway;
	public int highwayNumber = 0;
	//private int highwayLength;
	public boolean visited;
	public boolean isVisited[] = {false, false, false, false, false};
	private boolean traversed;
	private boolean closed = false;
	
	public boolean isTraversed() {
		return traversed;
	}

	public void setTraversed(boolean traversed) {
		this.traversed = traversed;
	}
	
	/*blocked*/
	public int isBlocked() {
		return blocked;
	}

	public void setBlocked(int blocked) {
		this.blocked = blocked;
	}
	/*blocked*/
	
	/*closed list flag*/
	public void setClosedFlag(boolean flag){
		this.closed = flag;
	}
	
	public boolean getClosedFlag(){
		return this.closed ;
	}
	/*closed list flag*/
	
	/*hard to traverse*/
	public void setToHardToTraverse(){
		this.hardToTraverse = true;
	}
	
	public boolean ifHardToTraverse(){
		return hardToTraverse;
	}
	/*hard to traverse*/
	
	/*Highway*/
	public boolean ifHighway(){
		return containsHighway;
	}
	
	public void setHighway(boolean containsHighway) {
		this.containsHighway = containsHighway;
	}
	/*Highway*/
	
	/*public int getHighwayLength(){
		return highwayLength;
	}*/
	
	private Node parent ;
	public Node [] prnt = new Node[5]; 
	
	private ArrayList<Node> neighbors ;
	


	public Node(int x,int y){
		this.x= x;
		this.y= y;
	}

	public Node(){
		this.x = 0;
		this.y = 0;
		this.blocked = 0;
	}
	
	public Node(int x, int y, int blocked){
		this.x = x;
		this.y = y;
		this.blocked = blocked;
		this.neighbors = new ArrayList<Node>();
			
	}	
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public float getCost() {
		return Cost;
	}
	public void setCost(float Cost) {
		this.Cost = Cost;
	}
	public float getgCost() {
		return gCost;
	}
	public void setgCost(float gCost) {
		this.gCost = gCost;
	}
	public float gethDistance() {
		return hDistance;
	}
	public void sethDistance(float hDistance) {
		this.hDistance = hDistance;
	}

	public void setFCost(float fCost) {
		// TODO Auto-generated method stub
		this.fCost=fCost;

	}
	public float getFCost() {
		// TODO Auto-generated method stub
		return fCost;

	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public ArrayList<Node> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(ArrayList<Node> neighbors) {
		this.neighbors = neighbors;
	}
	
	
	
}