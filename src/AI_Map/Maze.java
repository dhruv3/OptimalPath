package AI_Map;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Point;

import java.util.HashMap;

import java.io.*;
import java.lang.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JFrame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Maze 
{
	public static int MazeColCount = 160;
	public static int MazeRowCount = 120;
	private static ArrayList<Node> highway = new ArrayList<Node> ();
	ArrayList<Node> eightCoordinates = new ArrayList<Node> ();;
	public static boolean validHighway = true;
	protected Node[][] nodes;
	public Draw draw = new Draw();
	Node startMaze = new Node();
	Node goalMaze = new Node();
	ArrayList<Node> printgraph = new ArrayList<Node>();
	

	public Maze()
	{
		nodes = new Node[MazeRowCount][MazeColCount];
		
		for (int i = 0; i < MazeRowCount; i++) 
		{
			for (int j = 0; j < MazeColCount; j++) 
			{
				nodes[i][j] = new Node(i,j,1);
			}
		}
		
		//genMaze();
		
	}
	
	public void genMaze()
	{
		eightCoordinates = getEightCoordinates();
		for (int i =0; i < 8; i++)
		{
			setHardToTraverse(eightCoordinates.get(i));
		}
		
		
		//set 20% cell as blocked
		
		for (int highwayNumber = 1; highwayNumber <=4 ; highwayNumber++)
		{
			boolean highwayDone = buildHighway(highwayNumber);
			highway.clear();
		}
		
		int [] startCoordinates = getStartCoordinates();
		
		setStartNode(startMaze, startCoordinates[0], startCoordinates[1] );
		
		int [] goalCoordinates = getGoalCoordinates();
		
		setGoalMaze(goalMaze, goalCoordinates[0], goalCoordinates[1] );
		
		setBlockedCells();
	}
	
	
	public void setStartNode(Node startMaze, int x, int y){
		this.startMaze.setX(x);
		this.startMaze.setY(y);
		this.startMaze = nodes [x][y];
		
	}
	
	public int[] getStartCoordinates () {
		int[] startCoordinates = {};
		boolean startGoalFound = false;
		while (startGoalFound == false) {
			startCoordinates =  getXYCoordinates();
			if (nodes[startCoordinates[0]][startCoordinates[1]].isBlocked() == 1) {
				startGoalFound = true;
			}
		}
		return startCoordinates;
	}

	public void setGoalMaze(Node goalMaze, int x, int y){
		
		this.goalMaze.setX(x);
		this.goalMaze.setY(y);
		this.goalMaze = nodes [x][y];
	}
	
	public int[] getGoalCoordinates() {
		boolean flag = false;
		int goalCoordinates[] = {};
		int goalX = 0,goalY = 0;
		double distGoalAndStart;
		while(flag == false){
			goalCoordinates =  getXYCoordinates();	
			goalX = goalCoordinates[0];
			goalY = goalCoordinates[1];
			distGoalAndStart = Math.sqrt((startMaze.getX() - goalX)*(startMaze.getX() - goalX) +(startMaze.getY() - goalY)*(startMaze.getY() - goalY));
			if( distGoalAndStart > 100 && nodes[goalX][goalY].isBlocked() == 1){
				flag = true;
			} 
		}
		return goalCoordinates;
	}

	public int[] getXYCoordinates(){
		float rnd = (float)(Math.random());
		 int x=0,y=0;
		 if (rnd <= 0.25) {
		 		x = (int)(Math.random()*20);
		 		y = (int)(Math.random()*160); 
		 }
		 else if (rnd > 0.25 && rnd <=0.5) {
		 		x = (int)(Math.random()*120);
		 		y = (int)(Math.random()*20 + 139); 
		 }
		 else if (rnd > 0.5 && rnd <=0.75) {
		 		x = (int)(Math.random()*20 + 99);
		 		y = (int)(Math.random()*160); 
		 }
		 else {
		 		x = (int)(Math.random()*120);
		 		y = (int)(Math.random()*20); 
		 } 
		 int[] arr = {x,y};
		 return arr;
	}
	
	//highway
	public boolean buildHighway(int highwayNumber) {
		Node startNode = getHighwayStartNode();
		//System.out.println("highway start node" + startNode.getX() + "-"+ startNode.getY());
		boolean highwayIncomplete = true;
		
		while (highwayIncomplete ) {
			validHighway = true;
			boolean done = makeHighway(startNode);
			if (done == false) {
				clear();
			}
			if (done == true) {
//				System.out.println("highway done");
//				System.out.println("end point "+highway.get(highway.size()-1).getX() +"--"+ highway.get(highway.size()-1).getY());
				reflectHighway(highway, highwayNumber);
				highway.clear();
				highwayIncomplete = false;
			}
		}
		if (highwayIncomplete == false){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setBlockedCells(){
		for (int i = 0; i < MazeRowCount; i++) {
			for (int j = 0; j < MazeColCount; j++) {
				float rnd = (float) Math.random();
				if(rnd <= 0.2 && nodes[i][j].ifHighway() == false){
					nodes[i][j].setBlocked(0);										
				}
			}
		}		
	}
	
	
	// new\
	public ArrayList<Node> getEightCoordinates()
	{

			
		for (int i=0; i<8; i++)
		{
			Node current = nodes[(int)((Math.random()*(MazeRowCount-30))+15)][(int)((Math.random()*(MazeColCount-30))+15)];
			eightCoordinates.add(current);
		}
		return eightCoordinates;
	}

	public void reflectHighway(ArrayList<Node> highway, int highwayNumber) {
		for(int i = 0; i<=highway.size()-1;i++) {
			highway.get(i).setHighway(true);
			highway.get(i).highwayNumber = highwayNumber;
		}
	}
	

	public void setHardToTraverse(Node current) 
	{
		int x1 = current.getX() - 15;
		int x2 = current.getX() + 15; 
		int y1 = current.getY() - 15;
		int y2 = current.getY() + 15;
		for (int i = x1; i <= x2; i++)
		{
			for (int j = y1; j <= y2; j++)
			{
				double randProb = Math.random();
				if (randProb < 0.5) {
					nodes[i][j].setToHardToTraverse();
				}
			}
		}

	}
	
	
	private Node getHighwayStartNode() {
		boolean startNodeFound = false;
		int x=0,y=0;
		while (startNodeFound == false) {
			float rnd = (float) Math.random();
			
			if(rnd<=0.25){
				 x = 0;
				 y = (int)(Math.random()*159);
			}
			else if(0.25<rnd && rnd <=0.50){
				 y = 0;
				 x = (int)(Math.random()*119);
			}
			else if(0.50<rnd && rnd <=0.75){
				 x = 119;
				 y = (int)(Math.random()*159);
			}
			else{
				 y = 159;
				 x = (int)(Math.random()*119);
			}
			if (nodes[x][y].ifHighway() == false && !(nodes[x][y].equals(nodes[0][0]) || nodes[x][y].equals(nodes[MazeRowCount -1][0]) || nodes[x][y].equals(nodes[0][MazeColCount -1]) || nodes[x][y].equals(nodes[MazeRowCount -1][MazeColCount -1])))
			{
				startNodeFound = true;
			}
		}
		return nodes[x][y];
	}
	
	
	
	boolean makeHighway(Node startNode) {
		boolean boundaryCondition =  true;
		
		Node start = startNode;
		char prevDirection = 'c';
		while (boundaryCondition) {
			if (highway.size() != 0) {start = highway.get(highway.size() - 1);};
			char c = getDirection(prevDirection, startNode);
			if (c == 'r' && prevDirection == 'l' ||  c == 'l' && prevDirection == 'r' ||c == 'u' && prevDirection == 'd' ||c == 'd' && prevDirection == 'u') {
				return false;
			}
			prevDirection = c;
			boolean valid = true;
			switch (c) {
			case 'l' :
				valid = moveLeft(start);
				if (valid == false) {
					boundaryCondition = false;
				}
				break;
			case 'r' :
				valid = moveRight(start);
				if (valid == false) {
					boundaryCondition = false;
				}
				break;
			case 'u' :
				valid = moveUp(start);
				if (valid == false) {
					boundaryCondition = false;
				}
				break;
			case 'd' :
				valid = moveDown(start);
				if (valid == false) {
					boundaryCondition = false;
				}
				break;
				
			default:
				break;
			}
		}
		if(validHighway == false) {
			clear();
			return false;
		}
		if(highway.size() < 100 && boundaryCondition ==  false)
		{
			clear();
			return false;
		}
		else if (boundaryCondition == false && highway.size() > 100) {
		return true;
		}
		return false;
	}
	
	void clear() {
		highway.clear();
	}
	boolean moveLeft(Node startNode) {
		if (startNode.ifHighway() == true)
		{
			validHighway= false;
			return false;
		}
		for (int y = startNode.getY()- 1; y >= (startNode.getY()- 20); y--) {
			if (y < 0 && highway.size() < 100 ) {
				return false;
			}
			else if (y < 0 && highway.size() > 100) {
				return false;
			}
			else if (isHighway(nodes[startNode.getX()][y] )== true || nodes[startNode.getX()][y].ifHighway()== true){
				validHighway = false;
				return false;
			}
			else {
				highway.add(nodes [startNode.getX()][startNode.getY()]);
				highway.add(nodes[startNode.getX()][y]);
			}
			
		}
		return true;
	}
	
	boolean moveRight(Node startNode) {
		if (startNode.ifHighway() == true)
		{
			validHighway= false;
			return false;
		}
		for (int y = startNode.getY() + 1; y <= (startNode.getY()+ 20); y++) {
			if (y >= 160 && highway.size() < 100 ) {
				return false;
			}
			else if (y >= 160 && highway.size() > 100) {
				return false;
			}
			else if (isHighway(nodes[startNode.getX()][y])== true || nodes[startNode.getX()][y].ifHighway()== true){
				validHighway = false;
				return false;
			}
			else {
				highway.add(nodes [startNode.getX()][startNode.getY()]);

				highway.add(nodes[startNode.getX()][y]);
			}
			
		}
		return true;
	}
	
	boolean moveUp(Node startNode) {
		if (startNode.ifHighway() == true)
		{
			validHighway= false;
			return false;
		}
		for (int x = startNode.getX()- 1; x >= (startNode.getX()- 20); x--) {
			if (x < 0 && highway.size() < 100) {
				return false;
			}
			else if (x < 0 && highway.size() > 100) {
				return false;
			}
			else if (isHighway(nodes[x][startNode.getY()])== true || nodes[x][startNode.getY()].ifHighway()== true){
				validHighway = false;
				return false;
			}
			else {
				highway.add(nodes [startNode.getX()][startNode.getY()]);

				highway.add(nodes[x][startNode.getY()]);
			}
		}
		return true;
	}
	
	boolean moveDown(Node startNode) {
		if (startNode.ifHighway() == true)
		{
			validHighway= false;
			return false;
		}
		for (int x = startNode.getX() + 1; x <= (startNode.getX()+ 20); x++) {
			if (x >= 120 && highway.size() < 100 ) {
				return false;
			}
			else if (x >= 120 && highway.size() > 100) {
				return false;
			}
			else if (x <= 120 && isHighway(nodes[x][startNode.getY()])== true || nodes[x][startNode.getY()].ifHighway()== true){
				validHighway = false;
				return false;
			}
			else {
				highway.add(nodes [startNode.getX()][startNode.getY()]);
				highway.add(nodes[x][startNode.getY()]);
			}
		}
		return true;
	}
	
	
	char getDirection(char c, Node startNode) {
		float rnd = (float) Math.random();
		switch (c) {
		case 'c':
			if (startNode.getX()==0) {
				return 'd';
			}
			else if (startNode.getX()==119) {
				return 'u';
			}
			else if (startNode.getY()==0) {
				return 'r';
			}
			else if (startNode.getY()==159) {
				return 'l';
			}
			else if(rnd<=0.25){
				 return 'l';
			}
			else if(0.25<rnd && rnd <=0.50){
				return 'r';
			}
			else if(0.50<rnd && rnd <=0.75){
				return 'u';
			}
			else{
				return 'd';
			}
		case 'l' : 
			if (rnd <=0.6) {
				return 'l';
			}
			else if (0.6 < rnd && rnd <= 0.8 ) {
				return 'u';
			}
			else {
				return 'd';
			}
		case 'r' : 
			if (rnd <=0.6) {
				return 'r';
			}
			else if (0.6 < rnd && rnd <= 0.8 ) {
				return 'u';
			}
			else {
				return 'd';
			}
		case 'u' : 
			if (rnd <=0.6) {
				return 'u';
			}
			else if (0.6 < rnd && rnd <= 0.8 ) {
				return 'l';
			}
			else {
				return 'r';
			}
		case 'd' : 
			if (rnd <=0.6) {
				return 'd';
			}
			else if (0.6 < rnd && rnd <= 0.8 ) {
				return 'l';
			}
			else {
				return 'r';
			}
			default:
				return 'f';
		}
	}
		
	
	//check if cell is already a highway
	boolean isHighway(Node N) {
		for (Node m : highway) {
			if (N.getX() == m.getX() && N.getY() == m.getY()) {
				return true;
			}
		}
		return false;
	}
	
	
	public JFrame renderMaze() {

		JFrame frame = new JFrame("A* Algorithm");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		List<Point> grayCells = new ArrayList<Point>();
		List<Point> blackCells = new ArrayList<Point>();
		List<Point> highwayCells = new ArrayList<Point>();
		for (int i = 0; i < MazeRowCount; i++) {
			for (int j = 0; j < MazeColCount; j++) {
				if (nodes[i][j].ifHighway() == true) {
					highwayCells.add(new Point(i, j));
				}
				else if (nodes[i][j].isBlocked() == 0) {
					blackCells.add(new Point(i, j)); 
				}
				else if (nodes[i][j].ifHardToTraverse()) {
					grayCells.add(new Point(i, j)); 
				}
			}
		}
		draw.setGrayCells(grayCells);
		draw.setBlackCells(blackCells);
		draw.setHighwayCells(highwayCells);
		draw.printGS(startMaze.getX(), startMaze.getY(), goalMaze.getX(), goalMaze.getY());
		

		frame.add(draw);
		frame.pack();
		frame.setVisible(true);
		frame.addMouseListener(new MouseListener() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        // do your action here
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        // do your action here
		    }

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				int x = (e.getY() - 32)/5;
				int y = (e.getX() - 9)/5;
				System.out.println("FCost :" + nodes[x][y].getFCost());
				System.out.println("GCost :" + nodes[x][y].getgCost());
				System.out.println("HCost :" + (nodes[x][y].getFCost() - nodes[x][y].getgCost()));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
		});
		return frame;
	}
	
	
	public void print() {
	try {
		System.out.println("Save as: ");
		Scanner sc1 = new Scanner(System.in);
		String filename1 = sc1.next();
        BufferedWriter out = new BufferedWriter(new FileWriter(filename1 +".txt"));
        out.write(startMaze.getX() + "," + startMaze.getY());
        out.newLine();
        out.write(goalMaze.getX() + "," + goalMaze.getY());
        out.newLine();
        for(int index=0; index < eightCoordinates.size() ;index++){
        	out.write(eightCoordinates.get(index).getX() + "," + eightCoordinates.get(index).getY());
        	out.newLine();
        }
        out.write(MazeRowCount + "," + MazeColCount+"\n");
        for (int i = 0; i < MazeRowCount; i++) {
			for (int j = 0; j < MazeColCount; j++) {
				if (nodes[i][j].isBlocked() == 0) {
					out.write("0");
					nodes[i][j].setCost(Integer.MAX_VALUE);
				}
				else if (nodes[i][j].ifHighway() == false && nodes[i][j].ifHardToTraverse()== false) {
					out.write("1");
					nodes[i][j].setCost(1);
				}
				else if (nodes[i][j].ifHighway() == false && nodes[i][j].ifHardToTraverse()== true) {
					out.write("2");
					nodes[i][j].setCost(2);
				}
				else if (nodes[i][j].ifHighway() == true && nodes[i][j].ifHardToTraverse()== false) {
					out.write("a"+nodes[i][j].highwayNumber);
					nodes[i][j].setCost(0.25f);
				}
				else if (nodes[i][j].ifHighway() == true && nodes[i][j].ifHardToTraverse()== true) {
					out.write("b"+nodes[i][j].highwayNumber);
					nodes[i][j].setCost(0.5f);
				}
				//dhruv
				if(j != MazeColCount-1){
					out.write(",");					
				}
            }
			 out.write("\n");
        }
            out.close();
        } catch (IOException e) {}
    }
	
	//check if nodes are equal. Used by all children
	public boolean equals(Node a, Node b) {
		if (a.getX() == b.getX() && a.getY() == b.getY()) {
			return true;
		}
		return false;
	}
	
	public Node initiateNeighbors(Node n)
	{
		
		ArrayList<Node> nbrs = new ArrayList<Node>();
		ArrayList<Node> temp = new ArrayList<Node>();
	
		if(n.getX() > 0 && n.getY() > 0)
			nbrs.add(nodes[n.getX()-1][n.getY()-1]) ; // upper-left
		
		if(n.getX() > 0 && n.getY() < Maze.MazeColCount -1)
			nbrs.add(nodes[n.getX()-1][n.getY()+1]) ; //Upper right
		
		if(n.getX() < Maze.MazeRowCount -1 && n.getY() > 0)
			nbrs.add(nodes[n.getX()+1][n.getY()-1]) ; //Down left
			
		if(n.getX() < Maze.MazeRowCount -1 && n.getY() < Maze.MazeColCount -1)
			nbrs.add(nodes[n.getX()+1][n.getY()+1]) ; //Down right
			
		if(n.getY() < Maze.MazeColCount -1)
			nbrs.add(nodes[n.getX()][n.getY()+1]) ; //Right
		
		if(n.getY() > 0)
			nbrs.add(nodes[n.getX()][n.getY()-1]) ; //L
			
		if(n.getX() > 0)
			nbrs.add(nodes[n.getX()-1][n.getY()]) ;//U
		
		if(n.getX() < Maze.MazeRowCount -1)
			nbrs.add(nodes[n.getX()+1][n.getY()]) ; //D
		
		n.setNeighbors(nbrs);
		
		return n;
	}
	
	public float getDistanceCurrentAndNeighbor(Node current, Node neighbor){
		//horizontal or vertical transition 
		float cost = 0.0f;
		if(current.ifHardToTraverse() && neighbor.ifHardToTraverse()){
			cost = 2f;
		}
		else if(!current.ifHardToTraverse() && neighbor.ifHardToTraverse() || current.ifHardToTraverse() && !neighbor.ifHardToTraverse()){
			cost = 1.5f;
		}
		else{
			cost = 1f;
		}
		
		//if traversing between diagonals then multiply cost by sqrt 2
		if(!(current.getX() == neighbor.getX() || current.getY() == neighbor.getY())){
			cost = (float) Math.sqrt(2.0) * cost;
		}
		//if traversing between diagonals then multiply cost by sqrt 2
		
		//if highway divide cost by 4
		if(current.ifHighway() && neighbor.ifHighway()){
			cost = cost/4;
		}
		else if (current.ifHighway() && !neighbor.ifHighway() || !current.ifHighway() && neighbor.ifHighway()) {
			cost = 0.625f * cost;
		}
		//if highway divide cost by 4
		return cost;
	}
	
}
