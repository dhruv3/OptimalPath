package AI_Map;


import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import javax.swing.*;

import AI_Map.Node;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class AStar {
	public long totalTime = 0;
	public boolean pathFound = false;
	public float pathCost = 0f;
	public Node start;
	public Node target;
	Node current;
	Node finalTarget;
	Node initialStart;
	float hDistance;
	public float weight;
	static int counter;
	float totalgCost =0;
	Stack<Node> path = new Stack<Node>();
	ArrayList<Node> printer = new ArrayList<Node>();

	BinaryHeap Fringe = new BinaryHeap(20000);
	
	Stack<Node> Close = new Stack<Node>();
	
	long startTime;
	long endTime;

	int a1, a2, a3, a4;

	private ArrayList<Node> BlockedRec = new ArrayList<Node>();
	
	public AStar(Maze m, float weight, char c) {
		startTime = System.currentTimeMillis();
		this.start = m.startMaze;
		this.target = m.goalMaze;
		this.initialStart = start;
		this.finalTarget = target;
		this.weight = weight;

		a1 = m.startMaze.getX() * 10;
		a2 = m.startMaze.getY() * 10;

		a3 = target.getX() * 10;

		a4 = target.getY() * 10;
		mainCompute(m, c);
		endTime = System.currentTimeMillis();
		totalTime = endTime - startTime;
	}

	public boolean equals(Node a, Node b) {
		if (a.getX() == b.getX() && a.getY() == b.getY()) {
			return true;
		}
		return false;
	}

	public void comparator(){
		
	}
	
	public float calculateDistance(Node a, Node b, char c) {
		
		int aX = a.getX(), aY = a.getY(),bX = b.getX(),bY=b.getY();
		int P1 = Math.abs(aX - bX);
		int P2 = Math.abs(aY - bY);
		float h_diagonal = P1 > P2?P2:P1;
		float h_straight = P1  + P2;
		
		switch (c) {
		case 'a' : 
			return Math.abs(a.getX()-b.getX()) + Math.abs(b.getY()-b.getY()); // manhattan distance
		case 'b':
			//euclidean distance
			return (float) (Math.sqrt( (a.getX() - b.getX())*(a.getX() - b.getX())) +  ((a.getY() - b.getY())*(a.getY() - b.getY())));
		case 'c' :
			// diagonal distance
			float cost = Math.abs(P1 - P2);
			float diagCost;
			if (P1 > P2) {
				diagCost = (float) Math.sqrt(2)*P2;
			}
			else {
				diagCost = (float) Math.sqrt(2)*P1;
			}
			return (float) 0.25*(diagCost + cost);
		case 'd' :
			//euclidean squared
			return (float)((a.getX() - b.getX())*(a.getX() - b.getX())) +  ((a.getY() - b.getY())*(a.getY() - b.getY()));
		case 'e' :
			//Chebyshev distance
			return (float)(h_straight - h_diagonal);
		default :
			return 0;
			
		}
	}

	public void mainCompute(Maze m, char c) {

		hDistance = calculateDistance(start, target, c);
		start.sethDistance(hDistance);
		start.setgCost(0);
		start.setFCost(start.getgCost() + weight * start.gethDistance());
		start.setParent(start);
		
		Fringe.clear();
		Fringe.insert(start);
		Close.clear();

		m.printgraph.add(start);
		m.printgraph.add(target);
		
		
		while (Fringe.isEmpty() == false) {
			current = Fringe.getRoot();
			int currentIndex = Fringe.contains(current);
			Fringe.remove(currentIndex);

			if(current.equals(target)){
				constructPath (target);
				System.out.println("path found");
				pathFound = true;
				pathCost = current.getgCost();
				break;
			}
			
			
			Close.add(current);
			
			m.initiateNeighbors(current);
			ArrayList<Node> successors = current.getNeighbors();
			Iterator<Node> ite = successors.iterator();
			Node temp;
			while (ite.hasNext()) {
				temp = ite.next();
				if (temp.isBlocked() == 0) {
					BlockedRec.add(temp);
					ite.remove();
				}
			}
			
			for (Node nextNode : successors) {
				
				if(Close.contains(nextNode) == false){
					if(Fringe.contains(nextNode) == 0){
						Node emptyNode = null;
						nextNode.setgCost(Float.MAX_VALUE);
						nextNode.setParent(emptyNode);
					}
					UpdateVertex(current, nextNode, m, c);
				}
			}
		}	
		
		m.draw.setprintgraph(printer);
	}

	
	public void UpdateVertex(Node current, Node nextNode, Maze m, char c){
		float tentativeCost = current.getgCost() + m.getDistanceCurrentAndNeighbor(current, nextNode);
		if(tentativeCost < nextNode.getgCost()){
			nextNode.setgCost(tentativeCost);
			nextNode.setParent(current);
			float hDistance = calculateDistance(nextNode, target, c);
			float hCost = weight*hDistance;
			nextNode.setFCost(nextNode.getgCost() + hCost);

			
			int nodeIndex = Fringe.contains(nextNode);
			if (Fringe.contains(nextNode) != 0){
				Fringe.remove(nodeIndex);
			}
			Fringe.insert(nextNode);
			
		}
	}

	public long getFinalTime() {
		return totalTime;
	}

	public int getFinalCounter() {
		return counter;
	}
	void constructPath(Node node) {
		path.push(node);
		printer.add(node);
		while (!node.getParent().equals(start)){
			node = node.getParent();
			path.push(node);
			printer.add(node);
		}
		
	}
	
	public void print (Maze m) {
		try {
	        BufferedWriter out = new BufferedWriter(new FileWriter("output.txt"));
	        out.write(totalgCost + "\n");
	        out.newLine();
	        for (Node i : m.printgraph) {
	        	out.write("("+i.getX()+"," + i.getY() +")"+"\n");
	        	out.newLine();
	        	out.write("\n");
	        }
	        
            out.close();
	        } catch (IOException e) {}
	}
}



