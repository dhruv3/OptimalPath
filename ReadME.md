# Optimal Path Finder

## Summary 
This is a AI based search program. The agent is located in a 120 * 160 grid consisting of highways, hard to traverse terrain and blocked cells. Agents needs to travel from start to end node using different search methods.

## Problem Setup
An agent operating in a grid-world has to quickly compute a path
from the current cell it occupies to a target goal cell. Different cells in the grid have different
costs for traversing them. For instance, some may correspond to impassable obstacles, others
to flat and easy to traverse regions. There may also be passable but difficult to traverse regions
(e.g., rocky, granular terrain or swamps) as well as regions that can accelerate the motion of a
character (e.g., highways or navigable regions, etc.).

## Description of Discretized Map
We will consider grid maps of dimension 160 columns and 120 rows with square cells that can be blocked, unblocked,
partially blocked and directions in the grid where the cost can be decreased.
* **Green** cell: Start Vertex
* **Red** cell: End Vertex
* **White** cells: Unblocked
* **Light** gray cells: Partially blocked
* **Dark gray** cells: Blocked
* **Blue** line indicates a direction of motion along which the motion of the agent can be
accelerated
* **Yellow** line indicates a path taken by the agent

A sample map is shown below: 
<img width="806" alt="ai_map" src="https://user-images.githubusercontent.com/13077629/42135870-647af90a-7d1f-11e8-8e7c-bddc96f9a589.png">

Cells are selected on a predefined conditions which you can read from attached assignment description file.

## Algorithms
Following graph traversal algorithms have been implemented:
1. Uniform Cost Search
2. A* 
3. Weighted A*
4. Sequential Heuristic A*
5. Integrated Heuristic A*

### Fringe(Binary Heap)
To implement these algorithm we created our own version of binary heap instead of using standard libraries. We call this special data structure **Fringe**. It is a priority queue which supports the following operations:
* insert(element, queue): add element and return the resulting queue
* remove(queue): remove and return the first element in the queue
* empty(queue): return true if no elements
* initialize(element): create a queue that contains element

### Uniform Cost Search
Uniform Cost Search (UCS) calculates the path cost of all the nodes in the fringe and expands the one that has the
minimum cost.

<img width="610" alt="ucs" src="https://user-images.githubusercontent.com/13077629/42136243-ac4fe906-7d25-11e8-8947-c7e1b302e234.png">

Figure above describes the operation of the algorithm. After the initial node S is expanded, three nodes
are generated: A, B and C. Among them node A has the smallest path cost and is selected for expansion, generating
node G, a goal node. Then node B is the node with the minimum path cost and is expanded first, also resulting in a
goal node. The goal node produced by B is the optimal solution and the node that will be visited first by UCS.

### A*
The A* search algorithm is defined as:
>f(n) = g(n) + h(n)

The evaluation function is the sum of the true path from the initial node to node n and the heuristic value at n. In
this way A* selects nodes that optimize the estimate for the complete path cost.

A* maintains two global data structures:
1. First, the fringe (or open list) is a priority queue that contains the vertices that A∗ considers
to expand. A vertex that is or was in the fringe is called generated. The fringe provides the
following procedures:
    * Procedure *fringe.Insert(s, x)* inserts vertex s with key x into the priority queue fringe.
    * Procedure *fringe.Remove(s)* removes vertex s from the priority queue fringe.
    * Procedure *fringe.Pop()* removes a vertex with the smallest key from priority queue fringe
and returns it.
2. Second, the closed list is a set that contains the vertices that A* has expanded and ensures
that A* expands every vertex at most once.

A* uses a user-provided constant h-value (= heuristic value) h(s) for every vertex s ∈ S to focus
the search, which is an estimate of the distance to the goal, i.e., an estimate of the distance from
vertex s to the goal vertex. A* uses the h-value to calculate an ƒ-value ƒ (s) = g(s) + h(s) for every
vertex s, which is an estimate of the distance from the start vertex via vertex s to the goal vertex.

### Weighted A*
Weighted A* expands the states in the order of ƒ = g + w · h values, where w ≥ 1.
In the case that w = 1, Weighted A* is identical to the original A* algorithm. When w > 1, the
search is biased towards vertices that are closer to the goal according to the heuristic.

### Sequential Heuristic A*
This approach aims to utilize information from
many different heuristics, which are considered by running n + 1 searches in a rather sequential
manner. Each search uses its own, separate priority queue. Therefore, in addition to the different
h values, each state uses a different g value for each search. We use g0 to denote the g for an
anchor search process, which must use an admissible/consistent heuristic for the overall process
to return an optimal solution. We use g_i, (i = 1, 2, ..., n) for the other search procedures, which can
make use of any type of heuristic including inadmissible ones.

### Integrated Heuristic A*
This algorithm takes the above idea one step further. In particular, the current path for a given state is
shared among all the search procedures, i.e., if a better path to a state is discovered by any of the
search processes, the information is updated in all priority queues. As the paths are shared, this
Integrated Heuristic A* uses a single g value for each state, unlike the previous process in which
every search maintains its own g value. Furthermore, this path integration allows the algorithm
to expand each state at most twice, in contrast to the Sequential approach, which may expand
a state up to n + 1 times (once in each search). Yet, the Integrated Heuristic A* can achieve the
same bounds as the Sequential one.

## Output
Path from start to end node is calculated and displayed on the map. Other things displayed are:
- Run time
- Path Cost
- Path length
- Total number of nodes expanded
- Total memory requirement
