package AI_Map;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner; 
import javax.swing.*;
import AI_Map.Draw;
import java.awt.*;
import java.io.FileWriter;


public class mainFile {

	public static void main(String[] args) throws NumberFormatException, IOException, InterruptedException {
		int choice;
		boolean flag = true;
		boolean mapGenerated =  false;

		Maze m = new Maze();
		while(flag == true){
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("******************************************************");
			System.out.println("Please input your choice:");
			System.out.println("1. Create new map");
			System.out.println("2. Read map from file");
			System.out.println("3. Execute A* (WA* etc)");
			System.out.println("4. Save result in file");
			System.out.println("5. Save map in file");
			System.out.println("6. Run Experiments");
			System.out.println("7. Exit");
			System.out.println("******************************************************");
			System.out.print("Enter your choice: ");
	
			choice = Integer.parseInt(br.readLine());
	
			switch(choice){
				case 1:
					m.genMaze();
					mapGenerated = true;
					m.renderMaze();
					break;
					
				case 6:
					System.out.println("Read From: ");
					Scanner sc = new Scanner(System.in);
					String filename = sc.next();
					long totalTime = 0;
					float totalPathCost = 0;
					long totalNoNodesExpanded = 0;
					long totalMemoryRequirement= 0;
					long totalPathLength = 0;
					int counter = 0;
					
					BufferedReader readStartEnd = new BufferedReader(new FileReader(filename + "_co.txt"));
					for (int map= 0; map< 10; map++) {
							Maze m9 = new Maze();
							String sCurrentLine;
							
		
							BufferedReader read = new BufferedReader(new FileReader(filename + ".txt"));
							
							sCurrentLine = readStartEnd.readLine();
							String startCoordinates[] = sCurrentLine.split(",");
							int startx= Integer.valueOf(startCoordinates[0]);
							int starty= Integer.valueOf(startCoordinates[1]);
							m9.startMaze.setX(startx);
							m9.startMaze.setY(starty);
							m9.startMaze =  m9.nodes[startx][starty];
							
							sCurrentLine = readStartEnd.readLine();
							String goalCoordinates[] = sCurrentLine.split(",");
							int goalx= Integer.valueOf(goalCoordinates[0]);
							int goaly= Integer.valueOf(goalCoordinates[1]);
							m9.goalMaze.setX(goalx);
							m9.goalMaze.setY(goaly);
							m9.goalMaze =  m9.nodes[goalx][goaly];
						
							sCurrentLine = read.readLine();
							sCurrentLine = read.readLine();
												
							for (int i=0; i <8; i++) {
								sCurrentLine = read.readLine();
							
		
								String coordinates[] = sCurrentLine.split(",");
								int x= Integer.valueOf(coordinates[0]);
								int y= Integer.valueOf(coordinates[1]);
								m9.eightCoordinates.add(m9.nodes[x][y]);
							}
							
							sCurrentLine = read.readLine();
							
							String mazeSize[] = sCurrentLine.split(",");
							m9.MazeRowCount =  Integer.valueOf(mazeSize[0]);
							m9.MazeColCount =  Integer.valueOf(mazeSize[1]);
							
							for (int i = 0; i< m9.MazeRowCount; i++) {
								sCurrentLine = read.readLine();
								String mazeRow[] = sCurrentLine.split(",");
								for (int j = 0; j < m9.MazeColCount; j++) {
									switch(mazeRow[j]) {
									case "0":
										m9.nodes[i][j].setBlocked(0);
										m9.nodes[i][j].setHighway(false);
										break;
									case "1":
										m9.nodes[i][j].setBlocked(1);
										m9.nodes[i][j].setHighway(false);
										break;
									case "2" :
										m9.nodes[i][j].setToHardToTraverse();
										m9.nodes[i][j].setBlocked(1);
										m9.nodes[i][j].setHighway(false);
										break;
									case "a1":
										m9.nodes[i][j].setHighway(true);
										m9.nodes[i][j].setBlocked(1);
										m9.nodes[i][j].highwayNumber = 1;
										break;
									case "a2":
										m9.nodes[i][j].setHighway(true);
										m9.nodes[i][j].setBlocked(1);
										m9.nodes[i][j].highwayNumber = 2;
										break;
									case "a3":
										m9.nodes[i][j].setHighway(true);
										m9.nodes[i][j].setBlocked(1);
										m9.nodes[i][j].highwayNumber = 3;
										break;
									case "a4":
										m9.nodes[i][j].setHighway(true);
										m9.nodes[i][j].setBlocked(1);
										m9.nodes[i][j].highwayNumber = 4;
										break;
									case "b1":
										m9.nodes[i][j].setHighway(true);
										m9.nodes[i][j].setBlocked(1);
										m9.nodes[i][j].setToHardToTraverse();
										m9.nodes[i][j].highwayNumber = 1;
										break;
									case "b2":
										m9.nodes[i][j].setHighway(true);
										m9.nodes[i][j].setBlocked(1);
										m9.nodes[i][j].setToHardToTraverse();
										m9.nodes[i][j].highwayNumber = 2;
										break;
									case "b3":
										m9.nodes[i][j].setHighway(true);
										m9.nodes[i][j].setBlocked(1);
										m9.nodes[i][j].setToHardToTraverse();
										m9.nodes[i][j].highwayNumber = 3;
										break;
									case "b4":
										m9.nodes[i][j].setHighway(true);
										m9.nodes[i][j].setBlocked(1);
										m9.nodes[i][j].setToHardToTraverse();
										m9.nodes[i][j].highwayNumber = 4;
										break;
										
									}
								}
							}
							
								char c = 'c';
								float weight = 1f;
								
								SequentialAStar seqastar = new SequentialAStar(m9);
								totalTime = totalTime + seqastar.getFinalTime();
								
								if (seqastar.pathFound = true) {
									counter++;
									totalPathCost = totalPathCost + seqastar.pathCost;
									System.out.println("path cost = " + seqastar.pathCost);
									totalNoNodesExpanded = totalNoNodesExpanded+ seqastar.CloseSize; 
									totalMemoryRequirement = totalMemoryRequirement + (seqastar.CloseSize + seqastar.OpenSize);
									totalPathLength = totalPathLength + seqastar.path.size();
								}
								m9.renderMaze();
								
								read.close();
								
					}
					float averagePathCost = totalPathCost/counter;
					System.out.println("Average PathCost =" + averagePathCost);					
					long averageTime = totalTime/10;
					System.out.println("Average Time =" + averageTime);
					float averageNoNodesExpanded = totalNoNodesExpanded/counter;
					System.out.println("Average Number of nodes expanded =" + averageNoNodesExpanded);
					float averageMemoryRequirement = totalMemoryRequirement/counter;
					System.out.println("Average memory requirement =" + averageMemoryRequirement);
					mapGenerated = true;
					
					break;
				
				case 2: 
					System.out.println("Read From: ");
					Scanner scan = new Scanner(System.in);
					String filename2 = scan.next();
					
					String sCurrentLine;
					

					BufferedReader read = new BufferedReader(new FileReader(filename2 + ".txt"));
					sCurrentLine = read.readLine();
					String startCoordinates[] = sCurrentLine.split(",");
					int startx= Integer.valueOf(startCoordinates[0]);
					int starty= Integer.valueOf(startCoordinates[1]);
					m.startMaze.setX(startx);
					m.startMaze.setY(starty);
					m.startMaze =  m.nodes[startx][starty];
					
					sCurrentLine = read.readLine();
					String goalCoordinates[] = sCurrentLine.split(",");
					int goalx= Integer.valueOf(goalCoordinates[0]);
					int goaly= Integer.valueOf(goalCoordinates[1]);
					m.goalMaze.setX(goalx);
					m.goalMaze.setY(goaly);
					m.goalMaze =  m.nodes[goalx][goaly];
					
					for (int i=0; i <8; i++) {
						sCurrentLine = read.readLine();
						String coordinates[] = sCurrentLine.split(",");
						int x= Integer.valueOf(coordinates[0]);
						int y= Integer.valueOf(coordinates[1]);
						m.eightCoordinates.add(m.nodes[x][y]);
					}
					
					sCurrentLine = read.readLine();
					String mazeSize[] = sCurrentLine.split(",");
					m.MazeRowCount =  Integer.valueOf(mazeSize[0]);
					m.MazeColCount =  Integer.valueOf(mazeSize[1]);
					
					for (int i = 0; i< m.MazeRowCount; i++) {
						sCurrentLine = read.readLine();
						String mazeRow[] = sCurrentLine.split(",");
						for (int j = 0; j < m.MazeColCount; j++) {
							switch(mazeRow[j]) {
							case "0":
								m.nodes[i][j].setBlocked(0);
								m.nodes[i][j].setHighway(false);
								break;
							case "1":
								m.nodes[i][j].setBlocked(1);
								m.nodes[i][j].setHighway(false);
								break;
							case "2" :
								m.nodes[i][j].setToHardToTraverse();
								m.nodes[i][j].setBlocked(1);
								m.nodes[i][j].setHighway(false);
								break;
							case "a1":
								m.nodes[i][j].setHighway(true);
								m.nodes[i][j].setBlocked(1);
								m.nodes[i][j].highwayNumber = 1;
								break;
							case "a2":
								m.nodes[i][j].setHighway(true);
								m.nodes[i][j].setBlocked(1);
								m.nodes[i][j].highwayNumber = 2;
								break;
							case "a3":
								m.nodes[i][j].setHighway(true);
								m.nodes[i][j].setBlocked(1);
								m.nodes[i][j].highwayNumber = 3;
								break;
							case "a4":
								m.nodes[i][j].setHighway(true);
								m.nodes[i][j].setBlocked(1);
								m.nodes[i][j].highwayNumber = 4;
								break;
							case "b1":
								m.nodes[i][j].setHighway(true);
								m.nodes[i][j].setBlocked(1);
								m.nodes[i][j].setToHardToTraverse();
								m.nodes[i][j].highwayNumber = 1;
								break;
							case "b2":
								m.nodes[i][j].setHighway(true);
								m.nodes[i][j].setBlocked(1);
								m.nodes[i][j].setToHardToTraverse();
								m.nodes[i][j].highwayNumber = 2;
								break;
							case "b3":
								m.nodes[i][j].setHighway(true);
								m.nodes[i][j].setBlocked(1);
								m.nodes[i][j].setToHardToTraverse();
								m.nodes[i][j].highwayNumber = 3;
								break;
							case "b4":
								m.nodes[i][j].setHighway(true);
								m.nodes[i][j].setBlocked(1);
								m.nodes[i][j].setToHardToTraverse();
								m.nodes[i][j].highwayNumber = 4;
								break;
								
							}
						}
					}
					m.renderMaze();
					mapGenerated = true;
					read.close();
					break;
				case 3:
					float weight;
					float memoryRequirement;
					if (mapGenerated == false) {
						System.out.println("Create a map to save it. Select Option 1 Or Option 2 to read a map from file");
						break;
					}
					
					while(flag == true){
						BufferedReader breader = new BufferedReader(new InputStreamReader(System.in));
						System.out.println("========================================");
						System.out.println("Please input your choice of algorithm:");
						System.out.println("1. Uniform Cost Search");
						System.out.println("2. A* ");
						System.out.println("3. Weighted A*");
						System.out.println("4. Sequential Heuristic A*");
						System.out.println("5. Integrated Heuristic A*");
						System.out.println("6. Go back");
						System.out.print("Enter your choice: ");
				
						choice = Integer.parseInt(breader.readLine());
						
						switch(choice) {
						case 1: 
							weight = 0.0f;
							char c = 'c';
							AStar UCS = new AStar(m, weight, c);
							System.out.println("------RESULTS-----");
							System.out.println("Run time = " + UCS.totalTime);
							System.out.println("Path Cost = " + UCS.pathCost);
							System.out.println("Path length = " + UCS.path.size());
							System.out.println("Total number of nodes expanded = " + UCS.Close.size());
							memoryRequirement = (UCS.Close.size() + UCS.Fringe.getSize());
							System.out.println("Total memory requirement = " + memoryRequirement);
							System.out.println("Run time = " + UCS.totalTime);

							m.renderMaze();
							break;
						
						case 2:
							System.out.println("Please input your choice of heuristic:");
							System.out.println("a. Manhattan Distance");
							System.out.println("b. Euclidean Distance");
							System.out.println("c. Diagonal Distance");
							System.out.println("d. Euclidean Square distance");
							System.out.println("e. Chebyshev distance");
							System.out.print("Enter your choice: ");
							
							c = (char)breader.read();
							weight = 1.0f;
							AStar aastar = new AStar(m, weight, c);
							System.out.println("------RESULTS-----");
							System.out.println("Run time = " + aastar.totalTime);
							System.out.println("Path Cost = " + aastar.pathCost);
							System.out.println("Path length = " + aastar.path.size());
							System.out.println("Total number of nodes expanded = " + aastar.Close.size());
							memoryRequirement = (aastar.Close.size() + aastar.Fringe.getSize());
							System.out.println("Total memory requirement = " + memoryRequirement);
							System.out.println("Run time = " + aastar.totalTime);
							m.renderMaze();
							break;
							
						case 3:
							System.out.println("Enter weight: ");
							Scanner sc2 = new Scanner(System.in);
							String weightString = sc2.next();
							weight = Float.valueOf(weightString);
							
							System.out.println("Please input your choice of heuristic:");
							System.out.println("a. Manhattan Distance");
							System.out.println("b. Euclidean Distance");
							System.out.println("c. Diagonal Distance");
							System.out.println("d. Euclidean Square distance");
							System.out.println("e. Chebyshev distance");
							System.out.print("Enter your choice: ");
							
							c = (char)breader.read();
							AStar weightAStar = new AStar(m, weight, c);
							System.out.println("------RESULTS-----");
							System.out.println("Run time = " + weightAStar.totalTime);
							System.out.println("Path Cost = " + weightAStar.pathCost);
							System.out.println("Path length = " + weightAStar.path.size());
							System.out.println("Total number of nodes expanded = " + weightAStar.Close.size());
							memoryRequirement = (weightAStar.Close.size() + weightAStar.Fringe.getSize());
							System.out.println("Total memory requirement = " + memoryRequirement);
							System.out.println("Run time = " + weightAStar.totalTime);
							m.renderMaze();
							break;
						case 4:
							SequentialAStar sequentialAStar = new SequentialAStar(m);
							System.out.println("------RESULTS-----");
							System.out.println("Run time = " + sequentialAStar.totalTime);
							System.out.println("Path Cost = " + sequentialAStar.pathCost);
							System.out.println("Path length = " + sequentialAStar.path.size());
							m.renderMaze();
							break;
						case 5:
							IntegratedAStar integratedAStar = new IntegratedAStar(m);
							System.out.println("------RESULTS-----");
							System.out.println("Run time = " + integratedAStar.totalTime);
							System.out.println("Path Cost = " + integratedAStar.pathCost);
							System.out.println("Path length = " + integratedAStar.path.size());
							m.renderMaze();
							break;
						case 6:
							flag = false;
							break;
						default:
							System.out.println("Invalid Choice");
							break;
			
						}
					}
				
					break;
				case 4:
//					m.print();
				case 5:
					if (mapGenerated == false) {
						System.out.println("Create a map to save it. Select Option 1 or Option 2 to read a map from a file");
						break;
					}
					m.print();
					break;
				case 7:
					flag = false;
					break;
			}
		}
	}
}

