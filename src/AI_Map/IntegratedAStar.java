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

public class IntegratedAStar {
	public long totalTime = 0;
	public boolean pathFound = false;
	public float pathCost = 0f;
	public Node start;
	public Node target;
	Node current;
	Node finalTarget;
	Node initialStart;
	float hDistance;
	static int counter;
	float totalgCost =0;
	long startTime;
	long endTime;
	int a1, a2, a3, a4;
	
	public float w1 = 1.25f;
	public float w2 = 1.5f;
	
	Stack<Node> path = new Stack<Node>();
	ArrayList<Node> printer = new ArrayList<Node>();
	
	float Hcost [] = new float[5];
	
	BinaryHeapPhaseTwo[] Fringe = new BinaryHeapPhaseTwo[5]; //= new BinaryHeap(20000);
	
	Stack<Node> CloseInad = (Stack<Node>) new Stack();
	
	Stack<Node> CloseAnchor = (Stack<Node>) new Stack();
	
	Maze globalMaze;
	
	private ArrayList<Node> BlockedRec = new ArrayList<Node>();
	
	
	public IntegratedAStar(Maze m) {
		startTime = System.currentTimeMillis();
		this.start = m.startMaze;
		this.target = m.goalMaze;
		this.initialStart = start;
		this.finalTarget = target;

		a1 = m.startMaze.getX() * 10;
		a2 = m.startMaze.getY() * 10;

		a3 = target.getX() * 10;

		a4 = target.getY() * 10;
		int result = mainCompute(m);
		endTime = System.currentTimeMillis();
		totalTime = endTime - startTime;
	}
	
	
	int mainCompute(Maze m) {
		createBinaryHeap();
		globalMaze = m; //check 
		start.setgCost(0);
		target.setgCost(Float.MAX_VALUE);
		Node emptyNode = null;
		start.setParent(emptyNode);
		target.setParent(emptyNode);
		for(int i = 0; i < 5; i++){
			Fringe[i].clear();
			float fCostStart = key(start, i);
//			startArr[i].setFCost(fCostStart);
			start.f[i] = fCostStart;
			Fringe[i].insert(start);
		}
		CloseInad.clear();
		CloseAnchor.clear();
		
		
		while(Fringe[0].getRoot().f[0] < Float.MAX_VALUE) {
			for(int i = 1; i < 5; i++) {
				if (Fringe[i].getRoot().f[i] < w2*Fringe[0].getRoot().f[0]) {
					if(target.getgCost() < Fringe[i].getRoot().f[i]) {
						if (target.getgCost() < Float.MAX_VALUE) {
							System.out.println("Path found");
							System.out.println("i=" + i);
							constructPath(i);
							return 0;
						}
					}
					else {
						Node current = Fringe[i].getRoot();
						current.visited =  true;//top and min must mean same as top shall return highest priority elem
						ExpandState(current);
						CloseInad.add(current);
						
					}
					
				}
				else{
					if(target.getgCost() <= Fringe[0].getRoot().f[0]){
//						System.out.println("inside else");
//						count2++;
						if(target.getgCost() < Float.MAX_VALUE){
//							System.out.println(target.g[i]);
							System.out.println("Path found");
							System.out.println("i=" + i);
							constructPath(i);
							return 0;
						}
					}
					else{
						Node current = Fringe[0].getRoot();
						current.visited = true;
						ExpandState(current);
						CloseAnchor.add(current);						
					}
				}
			}
			
		}
		return 1;
		
	}
	
	void constructPath(int i) {
		System.out.println("i=" + i);
		pathCost = target.getgCost();
		path.push(target);
		printer.add(target);
		
		Node node = target;
		while (!node.equals(start)){
			node = node.getParent();
			path.push(node);
			printer.add(node);
			
		}
		globalMaze.draw.setprintgraph(printer);
	}
	
	private void ExpandState(Node current) {
		for (int i = 0; i < 5; i++) {
			int currentIndex = Fringe[i].contains(current);
			if(currentIndex != 0) {
				Fringe[i].remove(currentIndex);
			}
		}
		
		float currentGCOST = current.getgCost();
		
		globalMaze.initiateNeighbors(current);
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
			if(nextNode.visited == true) {
				continue;
			}
			if(Fringe[0].contains(nextNode) == 0 && Fringe[1].contains(nextNode) == 0 && Fringe[2].contains(nextNode) == 0 && Fringe[3].contains(nextNode) == 0 && Fringe[4].contains(nextNode) == 0){
				nextNode.setgCost(Float.MAX_VALUE);
				Node emptyNode = null;
				nextNode.setParent(emptyNode);
			}
			if(nextNode.getgCost() > current.getgCost() + globalMaze.getDistanceCurrentAndNeighbor(current, nextNode)){
				nextNode.setgCost((current.getgCost() + globalMaze.getDistanceCurrentAndNeighbor(current, nextNode)));
				nextNode.setParent(current);
//				
				if(CloseAnchor.search(nextNode) == -1){
					float fCostNode = key(nextNode, 0);
					nextNode.f[0] = fCostNode;
					Fringe[0].insert(nextNode);
					
					if(CloseInad.search(nextNode) == -1){
						for(int i =1; i <5; i++) {
							if (key(nextNode, i) < w2* key(nextNode,0)) {
								float fCost = key(nextNode, 0);
								nextNode.f[i] = fCost;
								Fringe[i].insert(nextNode);
							}
					}
					}
				}
			}
		}
	}



	public boolean equals(Node a, Node b) {
		if (a.getX() == b.getX() && a.getY() == b.getY()) {
			return true;
		}
		return false;
	}
	
	private float key(Node tempNode, int i) {
		float gCost = tempNode.getgCost();
		float hCost = calculateDistance(tempNode, target, i);
		return gCost + w1*hCost;
	}
	
	public float calculateDistance(Node a, Node b, int c) {
		
		int aX = a.getX(), aY = a.getY(),bX = b.getX(),bY=b.getY();
		int P1 = Math.abs(aX - bX);
		int P2 = Math.abs(aY - bY);
		float h_diagonal = P1 > P2?P2:P1;
		float h_straight = P1  + P2;
		
		switch (c) {
		case 4 : 
			return Math.abs(a.getX()-b.getX()) + Math.abs(b.getY()-b.getY()); // manhattan distance
		case 1:
			//System.out.println("euclidean distance");
			return (float) (Math.sqrt( (a.getX() - b.getX())*(a.getX() - b.getX())) +  ((a.getY() - b.getY())*(a.getY() - b.getY())));
		case 0 :
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
		case 3 :
			//euclidean squared
			return (float)((a.getX() - b.getX())*(a.getX() - b.getX())) +  ((a.getY() - b.getY())*(a.getY() - b.getY()));
		case 2 :
			//Chebyshev distance
			return (float)(h_straight - h_diagonal);
		default :
			return 0;
		}
	}
	
	private void createBinaryHeap() {
		for(int i=0; i<5; i++){
			Fringe[i] = new BinaryHeapPhaseTwo(20000, i);
		}
		
	}
}


