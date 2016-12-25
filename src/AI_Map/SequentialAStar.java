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

public class SequentialAStar {
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
	long CloseSize= 0;
	long OpenSize = 0;
	public float w1 = 1.25f;
	public float w2 = 1.5f;
	
	Stack<Node> path = new Stack<Node>();
	ArrayList<Node> printer = new ArrayList<Node>();
	float Gcost[] = new float[5];
	float Hcost [] = new float[5];
	
	BinaryHeapPhaseTwo[] Fringe = new BinaryHeapPhaseTwo[5]; //= new BinaryHeap(20000);
	
	Stack<Node>[] Close = (Stack<Node>[]) new Stack[5];//= new Stack<Node>();
	
	Maze globalMaze;
	
	private ArrayList<Node> BlockedRec = new ArrayList<Node>();
	
	
	public SequentialAStar(Maze m) {
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
	
	int count1 = 0;
	int count2 = 0;
	
	
	int mainCompute(Maze m){
		createBinaryHeap();
		createCloseStack();
		globalMaze = m; //check 
		Node emptyNode = null;
		for(int i = 0; i < 5; i++){
			Fringe[i].clear();
			Close[i].clear();
			start.g[i] = 0f;
			target.g[i] = Float.MAX_VALUE;
			start.prnt[i] = emptyNode;
			target.prnt[i] = emptyNode;
			float fCostStart = key(start, i);
			start.f[i] = fCostStart;
			Fringe[i].insert(start);
		}
		while(Fringe[0].getRoot().f[0] < Float.MAX_VALUE){
			for(int i = 1; i < 5; i++){
				if(Fringe[i].getRoot().f[i] <= w2*Fringe[0].getRoot().f[0]){
					count1++;
					if(target.g[i] <= Fringe[i].getRoot().f[i]){ //check fcost vs gcost
						if(target.g[i] < Float.MAX_VALUE){
							System.out.println("Path found");
							constructPath(i);
							return 0;
						}
					}
					else{
						Node current = Fringe[i].getRoot();
						current.isVisited[i] =  true;//top and min must mean same as top shall return highest priority elem
						ExpandState(current, i);
						Close[i].add(current);
					}
				}
				else{
					count2++;

					if(target.g[0] <= Fringe[0].getRoot().f[0]){
						if(target.g[0] < Float.MAX_VALUE){
							System.out.println("Path found");
							constructPath(0);
							return 0;
						}
					}
					else{
						Node current = Fringe[0].getRoot();
						current.isVisited[i] = true;
						ExpandState(current, 0);
						Close[0].add(current);						
					}
				}
			}
		}
		return 1;
	}
	
	private void ExpandState(Node current, int i) {
		int currentIndex = Fringe[i].contains(current);
		if(currentIndex != 0) {
			Fringe[i].remove(currentIndex);
		}
		
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
			if(nextNode.isVisited[i] == true) {
				continue;
			}
			if(Fringe[i].contains(nextNode) == 0){
				nextNode.g[i] = Float.MAX_VALUE;
				Node emptyNode = null;
				nextNode.prnt[i] = emptyNode;
			}
			if(nextNode.g[i] > current.g[i] + globalMaze.getDistanceCurrentAndNeighbor(current, nextNode)){
				nextNode.g[i] = (current.g[i] + globalMaze.getDistanceCurrentAndNeighbor(current, nextNode));
				if(Close[i].search(nextNode) == -1){
					float fCostNode = key(nextNode, i);
					nextNode.f[i] = fCostNode;
					Fringe[i].insert(nextNode);
				}
			}
		}
	}
	

	void constructPath(int i) {
		pathFound = true;
		for (int k=0; k<5; k++) {
			CloseSize = CloseSize + Close[k].size();
			OpenSize = OpenSize + Fringe[k].getSize();
		}
		
		pathCost = target.g[i];
		path.push(target);
		printer.add(target);
		Node node = target;
		
		while (!node.equals(start)){
		
			node = node.prnt[i];
			path.push(node);
			printer.add(node);
			
		}
		globalMaze.draw.setprintgraph(printer);
	}
	
	public boolean equals(Node a, Node b) {
		if (a.getX() == b.getX() && a.getY() == b.getY()) {
			return true;
		}
		return false;
	}

	private float key(Node tempNode, int i) {
		float gCost = tempNode.g[i];
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
public long getFinalTime() {
	return totalTime;
}

	private void createBinaryHeap() {
		for(int i=0; i<5; i++){
			Fringe[i] = new BinaryHeapPhaseTwo(20000, i);
		}
		
	}

	private void createCloseStack() {
		for(int i=0; i<5; i++){
			Close[i] = new Stack<Node>();
		}
	}
}



