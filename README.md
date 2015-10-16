# Maps

In this project, I implemented a graph to perform such algorithms as Dijkstra's and shortest path on data for the University of Rochester, Monroe County, and New York state.

(monroe.txt and nys.txt too large to upload.)

TEST CLASS (THE MEAT):

To begin, I began by reading in the in the data line by line and storing it either as a vertex (intersections) or
and edge (roads).  This was done by creating the vertex and then storing it in the vertexMap and countVertex hashmaps.
Edges were created and stored in the countEdgeMap.  In addition to that, I create an Adjacent with the edges.  These
store in a linked list in each of its vertices the vertex it connects to and the weight from itself to that vertex.
As I'm going through the vertices, I also keep track of the largest and smallest longitudes and latitudes to draw
later.  Believe it or not, this is the operation that takes the longest time according to my timing.  It has a O(n)
runtime.

Below reading in the file, I have the program call the correct operations based on the count of command line arguments
and the commands passed in.  Since no operations besides if...if else statements and assignments are performed here, 
these seeming intricate commands have a constant time runtime.  What they call however, do not.

Below that, outside the main method is my getPath, Dijkstra, and findSmallestVertex method - all part of finding the
shortest path between two points.  I'm not exactly sure of the runtime, but I can explain why I think it has a 
O(n log n) runtime, but almost always runs faster.  The method begins with the getPath method (the wrapper) calling
dijkstra.  I used the pseudocode provided from lab 20 to implement dijkstra's.  However, I cut the method short by
passing in the vertex we're going to.  Therefore, it cuts the method short from finding every single node.  So, if
two vertices are side by side, the method can have a constant runtime.  Also, the only way this part of the method
gets to O(n) is if the two vertices passed in are the farthest vertices from each other in the entire graph.

For finding the smallest vertex, I keep track of vertices on the edge of the graph.  When I was keeping track of all
the vertices, Monroe county ran in 3 minutes to find the smallest path.  Therefore, by keeping the arraylist of 
reached vertices, I am able to drastically reduce the runtime.  To the point where the algorithm runs and displays
in under 8 seconds for New York State.  This is done by keeping track of known vertices, and if they are known,
removing them from the arraylist.  Therefore, the arraylist only keeps track of the current boundaries of the
branching graph.  Instead of having potentially millions of elements stored in the arraylist, the example provided 
in the output (which goes essentially across New York State only reaches a size of about 130 by the end of the 
algorithm.

After the vertex we're going to is known, dikstra stops and the algorithm goes back to getPath, where it unwraps it.
This is done by starting at the vertex we're going to and working our way back up the tree through the parents.
It's not an AVL tree, but we can expect this unwrapping to take on a log n runtime like trees do.

Below is my getMeridianPath method and prim.  Prim runs essentially the same as Dijkstra's, except we must hit every
node, so it has a O(n) runtime.  Additionally, we must hit every node unwrapping it, so this also has a O(n) runtime,
giving the entire runtime of implementation a O(n^2 runtime).

EDGE CLASS:

The edge class is a fairly simple class, as almost all from here on out will.  The edge class is a storage container
for edges.  It takes in two vertices.  From here, we can calculate the weight of that road using Haversine's formula.
Citation provided in the code.

VERTEX CLASS:

The vertex class is also another storage container.  It stores vertices by taking in a number, name, latitude,
and longitude.  I initialize it's known to false and path to null.  Additionally, I create an arrayList of Adjacents
for each vertex.

ADJACENTS CLASS:

Adjacents is a simple class.  It takes in a vertex and weight of the edge.  It then is stored in the appropriate 
vertex arrayList.

DRAWMAP CLASS:

DrawMap has three different initializing methods depending on what parameters are passed in - one to just show the 
graph, another for the shortest path, and another for the minimum weight spanning tree.  Based on which is called
determines the runtime.  Just showing the graph should take O(n).  However, the amount of lines to be inserted get
larger depending on the size of the other array passed in, so the other two may be something like O(n+m), which is
still O(n), but will take longer because there are more elements.  For the 2D drawing, I cited the stackOverflow
website I found information on that.  Additionally, I cited the site that helped me with color combinations.

The math for drawing the lines probably looks insane.  However, I sat down and calculated that, and it does draw it
correctly.  However, I don't think there's enough time to explain or space to explain the math. I have it though.
From there, I loop through the list and print out the the appropriate lines depending on which arraylists are filled.
If I am drawing Dijkstra's, I also put endpoints on the lines.
