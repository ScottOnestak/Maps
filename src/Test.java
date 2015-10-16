import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JFrame;

public class Test {

	//create global variables 
	public static String container;
	public static String[] holder;
	public static String a,b,c,d;
	public static int vCount = 0;
	public static int eCount = 0;
	public static double latitude, longitude;
	public static double latMax = Double.NEGATIVE_INFINITY;
	public static double latMin = Double.POSITIVE_INFINITY;
	public static double lonMax = Double.NEGATIVE_INFINITY;
	public static double lonMin = Double.POSITIVE_INFINITY;
	public static Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();
	public static Map<Integer, Vertex> countVertexMap = new HashMap<Integer, Vertex>();
	public static Map<Integer, Edge> countEdgeMap = new HashMap<Integer,Edge>();
	public static ArrayList<Edge> DsValues = new ArrayList<Edge>();
	public static ArrayList<Edge> dijkstraPath = new ArrayList<Edge>();
	public static ArrayList<String> knownVertices = new ArrayList<String>();
	public static ArrayList<String> thePath = new ArrayList<String>();
	public static ArrayList<Edge> meridianPath = new ArrayList<Edge>();
	
	//expanding heap size citiation...http://stackoverflow.com/questions/4304312/max-value-of-xmx-and-xms-in-eclipse
	public static void main(String[] args) {
		
		//get args length for later
		int count = args.length;
		
		//create try...catch for reading in data through buffered reader - like so many projects before
		try{
			//create buffered reader
			BufferedReader read = new BufferedReader(new FileReader(args[0]));
			
			container = read.readLine();
			while(container != null){
				
				//split the string on the spaces and go to the appropriate test cases
				holder = container.split("\t");
				
				//store Strings
				a = holder[0];
				b = holder[1];
				c = holder[2];
				d = holder[3];
				
				//check to make sure none are null
				if(!a.equals(null) && !b.equals(null) && !c.equals(null) && !d.equals(null)){
					//if intersection, insert a vertex
					if(a.equals("i")){
						
							//parse the longitude and latitude
							latitude = Double.parseDouble(c);
							longitude = Double.parseDouble(d);
							//check to see if the vertex is not already in the hashmap
							Vertex v = vertexMap.get(b);
							if(v == null){
								//if not, create new vertex
								v = new Vertex(vCount,b, latitude, longitude);
								vertexMap.put(b, v);
								countVertexMap.put(vCount,v);
								vCount++;
					
							//find the max's and min's of longitude and latitude
							if(latitude > latMax)
								latMax = latitude;
							if(latitude < latMin)
								latMin = latitude;
							if(longitude > lonMax)
								lonMax = longitude;
							if(longitude < lonMin)
								lonMin = longitude;
						}
					}
					//if it is a road, insert appropriate edge
					else if(a.equals("r")){
						//check to make sure the vertices are actually in the hashmap
						if(!vertexMap.get(c).equals(null) && !vertexMap.get(d).equals(null)){
							//get the vertices and enter into appropriate maps and vertices appropriately to creat connections
							Vertex v1 = vertexMap.get(c);
							Vertex v2 = vertexMap.get(d);
							Edge e = new Edge(v1,v2);
							countEdgeMap.put(eCount, e);
							vertexMap.get(e.v.name).insert(new Adjacents(e.w, e.weight));
							vertexMap.get(e.w.name).insert(new Adjacents(e.v, e.weight));
							eCount++;
						}
					
					}
				} 
				//if neither road or edge or does not meet other criteria, print out a line stating the invalide entry
				else
					System.out.println("Invalid entry for: " + a + " " + b + " " + c + " " + d);
				//read in the next line
				container = read.readLine();	
			}
			//close buffered reader
			read.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + args[0]);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e){
			e.printStackTrace();
		}		
		
		if(count == 2){
			if(args[1].equals("-show")){
				//if just "-show", draw map
				JFrame frame = new JFrame();
				frame.setPreferredSize(new Dimension(700,700));
				DrawMap draw = new DrawMap();
				frame.add(draw);
				frame.pack();
				frame.setVisible(true);	
				//add the edges to the GUI
				draw.addEdges(countEdgeMap, latMax, latMin, lonMax, lonMin);
			}
			else if(args[1].equals("-meridianmap")){
				//simply just display the meridian edges
				getMeridianPath(countVertexMap.get(countVertexMap.size()-1), vCount-1);
			}
			else
				System.out.println("Invalid entry for: " + args[0] + " " + args[1]);
		} else if(count == 3){
			if(args[1].equals("-show") || args[2].equals("-show")){
				if(args[1].equals("-meridianmap") || args[2].equals("-meridianmap")){
					//get meridian map
					DsValues = getMeridianPath(countVertexMap.get(countVertexMap.size()-1), vCount-1);
					//create draw
					JFrame frame = new JFrame();
					frame.setPreferredSize(new Dimension(700,700));
					DrawMap draw = new DrawMap();
					frame.add(draw);
					frame.pack();
					frame.setVisible(true);	
					//draw the graph and meridian map
					draw.drawMeridian(countEdgeMap,DsValues,latMax,latMin,lonMax,lonMin);
				}
			} else
				System.out.println("Invalid entry for: " + args[0] + " " + args[1] + " and " + args[2]);
		} else if(count == 4){
			if(args[1].equals("-directions") && vertexMap.containsKey(args[2]) && vertexMap.containsKey(args[3])){
				//endpoints of directions
				String from = args[2];
				String to = args[3];
			    //print out the directions
				System.out.println("Directions from " + vertexMap.get(from).name + " to " + vertexMap.get(to).name + ":\n");
				//get the path
				getPath(vertexMap.get(from),vertexMap.get(to));
				
			} else
				System.out.println("Invalid entry for: " + args[0] + " " + args[1] + " " + args[2] + " " + args[3]);
		} else if(count == 5){
			if(args[1].equals("-show") && args[2].equals("-directions") && vertexMap.containsKey(args[3]) && vertexMap.containsKey(args[4])){
				//endpoints of directions
				String from = args[3];
				String to = args[4];
			    //print out the directions
				System.out.println("Directions from " + vertexMap.get(from).name + " to " + vertexMap.get(to).name + ":\n");
				//call method
				DsValues = getPath(vertexMap.get(from),vertexMap.get(to));
				//set up frame
				JFrame frame = new JFrame();
				frame.setPreferredSize(new Dimension(700,700));
				DrawMap draw = new DrawMap();
				frame.add(draw);
				frame.pack();
				frame.setVisible(true);	
				//draw map and path
				draw.drawDijkstra(countEdgeMap,DsValues,latMax,latMin,lonMax,lonMin);
			}
			else if(args[1].equals("-directions") && vertexMap.containsKey(args[2]) && vertexMap.containsKey(args[3]) && args[4].equals("-show")){
				//endpoints of directions
				String from = args[2];
				String to = args[3];
			    //print out the directions
				System.out.println("Directions from " + vertexMap.get(from).name + " to " + vertexMap.get(to).name + ":\n");
				//call method
				DsValues = getPath(vertexMap.get(from),vertexMap.get(to));
				//set up fram
				JFrame frame = new JFrame();
				frame.setPreferredSize(new Dimension(700,700));
				DrawMap draw = new DrawMap();
				frame.add(draw);
				frame.pack();
				frame.setVisible(true);	
				//draw map and path
				draw.drawDijkstra(countEdgeMap,DsValues,latMax,latMin,lonMax,lonMin);
			} else
				System.out.println("Invalid entry for: " + args[0] + " " + args[1] + " " + args[2] + " " + args[3] + " " + args[4]);
		} else {
			//print out message of invalid entry for anything about five arguments
			System.out.print("Invalid entry for: ");
			for(int i = 0; i < args.length; i++){
				System.out.print(args[i] + " ");
			}
		}
	}
	
	 //getPath gets the path of dijkstra's algorithm
	 public static ArrayList<Edge> getPath(Vertex v, Vertex w){
		//call method to implement dijkstra
		 dijkstra(v,w);

		 Vertex s = w;
		 thePath.add(w.name);
		 
		 //backtrack through the parents to get the path
		 while(s != v){
			 Edge e = new Edge(s,s.path);
			 dijkstraPath.add(e);
			 thePath.add(s.path.name);
			 s = s.path;
		 }
		 
		 //print out directions
		 int arrows = thePath.size() - 1;
		 for(int i = 0; i < thePath.size(); i++){
				System.out.print(thePath.get(i));
				if(arrows > 0){
					System.out.print(" -> ");
				}
				arrows--;
			}
		 
		 //print out distance
		 System.out.println("\nThe total distance traveled from " + v.name + " to " + w.name + " is " + w.distance + " miles");
		 
		 return dijkstraPath;
	 }
	 
	 //dijkstra's algorithm...pseudocode provided in lab 20
	 private static void dijkstra(Vertex v, Vertex w){
		 //
		 v.distance = 0;
		 v.known = true;
		 knownVertices.add(v.name);
		 
		 //initialze smallestVertex to v
		 Vertex smallestVertex = v;
		 Vertex ve;
		 double weight;
		 
		 try{
			 //run through while loop until the node you want to get to is true... then tree must be built already
			 while(!w.known){
				 smallestVertex.known = true;
				 LinkedList<Adjacents> theAdjacencies = smallestVertex.adjacencies;
				 for(int i = 0; i < theAdjacencies.size(); i++){
					 ve = vertexMap.get(smallestVertex.name).adjacencies.get(i).v;
					 weight = smallestVertex.adjacencies.get(i).w;
					 knownVertices.add(ve.name);
					 if(!vertexMap.get(theAdjacencies.get(i).v.name).known){
						 if(smallestVertex.distance + weight < vertexMap.get(ve.name).distance){
							 vertexMap.get(ve.name).distance = smallestVertex.distance + weight;
							 vertexMap.get(ve.name).path = smallestVertex;
						 }
					 }
				 }
				 smallestVertex = vertexMap.get(findSmallestVertex(knownVertices));
			 }
		 }catch(NullPointerException n){
			 System.out.println("The vertices " + v.name + " and " + w.name + " lie on two separate islands.");
		 }
	 }
	 
	 //find the smallest vertex for dijkstra's
	 private static String findSmallestVertex(ArrayList<String> knownVertices){
		 String smallestVertex = "";
		 double smallestDistance = Double.POSITIVE_INFINITY;
		 ArrayList<String> remove = new ArrayList<String>();
		 
		 //find the smallest vertex
		 for(int i = 0; i < knownVertices.size(); i++){
			 //for known vertices
			if(!vertexMap.get(knownVertices.get(i)).known && vertexMap.get(knownVertices.get(i)).distance < smallestDistance){
				smallestDistance = vertexMap.get(knownVertices.get(i)).distance;
				smallestVertex = vertexMap.get(knownVertices.get(i)).name;
			} 
			//get the names of vertices to remove
			else if(vertexMap.get(knownVertices.get(i)).known){
				 remove.add(vertexMap.get(knownVertices.get(i)).name);
			 }
		 }
		 //remove known vertices to decrease arraylist size
		 if(remove.size() > 0){
			 for(int j = 0; j < remove.size(); j++){
				 knownVertices.remove(remove.get(j));
			 }
		 }
		 return smallestVertex;
	 }
	 
	 
	 //print out the meridian path edges
	 public static ArrayList<Edge> getMeridianPath(Vertex v, int vertexCount){
		 //call prim's algorithm
		 prim(v, vertexCount);
		 
		 //for every connection made, put those edges into an arraylist
		 for(int i = 0; i <= vertexCount; i++){
			 if(countVertexMap.get(i).path != null){
				 Edge e = new Edge(countVertexMap.get(i), countVertexMap.get(i).path);
				 meridianPath.add(e);
			 }
		 }
		 
		 //interate through arraylist and print out connections
		 for(int j = 0; j < meridianPath.size(); j++){
			 System.out.println(meridianPath.get(j).v.name + " to " + meridianPath.get(j).w.name);
		 }
		 
		 //pass back edges for drawing
		 return meridianPath;
	 }
	 
	 //prim's algorithm...basically dijkstra's (except I backtrack differently and must continue until all vertices are true)
	 private static void prim(Vertex v, int vertexCount){
		 //initialize to 0, true, and add to known vertices
		 v.distance = 0;
		 v.known = true;
		 knownVertices.add(v.name);
		 
		 //set smallest vertex to v
		 Vertex smallestVertex = v;
		 Vertex ve;
		 double weight;
		 
		 //run through while loop until all the vertices have been set to true
		 while( smallestVertex != null ){ 
			 smallestVertex.known = true;
			 LinkedList<Adjacents> theAdjacencies = smallestVertex.adjacencies;
			 for(int i = 0; i < theAdjacencies.size(); i++){
				ve = vertexMap.get(smallestVertex.name).adjacencies.get(i).v;
				weight = smallestVertex.adjacencies.get(i).w;
				knownVertices.add(ve.name);
				if(!vertexMap.get(theAdjacencies.get(i).v.name).known){
					if(smallestVertex.distance + weight < vertexMap.get(ve.name).distance){
						vertexMap.get(ve.name).distance = smallestVertex.distance + weight;
						vertexMap.get(ve.name).path = smallestVertex;
					}
				}
			 }
			 smallestVertex = vertexMap.get(findSmallestVertex(knownVertices));
		 }
	 }
}
