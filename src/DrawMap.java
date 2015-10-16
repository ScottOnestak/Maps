import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;


public class DrawMap extends JPanel {
	
	 //global variables
	 private static final long serialVersionUID = 1L;
	 private Map<Integer,Edge> edges = new HashMap<Integer, Edge>();
	 private ArrayList<Edge> dijkstraEdges = new ArrayList<Edge>();
	 private ArrayList<Edge> meridianPath = new ArrayList<Edge>();
	 final int width = 700;
	 final int height = 700;
	 double latDistance, lonDistance, lonMax, latMin, wlat, wlon, vlat, vlon;
	 Edge drawLine;
	 Shape line;
	 
	 //add an edge to the graph
	 public void addEdges(Map<Integer,Edge> map, double maxLat, double minLat, double maxLon, double minLon) {
		
		latDistance = Math.abs(maxLat - minLat);
		lonDistance = Math.abs(maxLon - minLon);
		latMin = Math.abs(minLat);
		lonMax = Math.abs(maxLon);
	    	
		edges.putAll(map);
		
	    repaint();
	 }
	 
	 //draw dijkstra's
	 public void drawDijkstra(Map<Integer,Edge> map,ArrayList<Edge> e, double maxLat, double minLat, double maxLon, double minLon){
		    
		 latDistance = Math.abs(maxLat - minLat);
		 lonDistance = Math.abs(maxLon - minLon);
		 latMin = Math.abs(minLat);
		 lonMax = Math.abs(maxLon);
		 
		 edges.putAll(map);
		 dijkstraEdges.addAll(e);
		 
		 repaint();
	 }
	 
	 public void drawMeridian(Map<Integer,Edge> map, ArrayList<Edge> e, double maxLat, double minLat, double maxLon, double minLon){
		 latDistance = Math.abs(maxLat - minLat);
		 lonDistance = Math.abs(maxLon - minLon);
		 latMin = Math.abs(minLat);
		 lonMax = Math.abs(maxLon);
		 
		 edges.putAll(map);
		 meridianPath.addAll(e);
		 
		 repaint();
	 }
	 
	 //drawing graphics using doubles from http://stackoverflow.com/questions/7759549/java-draw-line-based-on-doubles-sub-pixel-precision
	 //color codes made with the assistance of http://www.javascripter.net/faq/rgbtohex.htm
	 public void paintComponent(Graphics graphics) {
		 	Graphics2D g2 = (Graphics2D) graphics;
		 	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
		 
	        super.paintComponent(graphics);

	        //get width and height of screen
	         int width = getHeight();
	         int height = getWidth();
	       
	        
	        //set background to black
	        this.setBackground(Color.WHITE);
	        
	        //draw entire map
	        //if you want to see the math for the lines, I have it... but way to long to explain here
	        for(int i = 0; i < edges.size(); i++){
	        	graphics.setColor(new Color(0,0,40));
	            line = new Line2D.Double(height -(1-((edges.get(i).v.longitude + lonMax) * height / lonDistance)),
	            		(width-((edges.get(i).v.latitude - latMin) * width / latDistance)), 
	            		(height-(1-((edges.get(i).w.longitude + lonMax) * height / lonDistance))),
	            		(width-((edges.get(i).w.latitude - latMin) * width / latDistance)));
	            g2.draw(line);
	        }
	        
	        //draw dijkstra's
	        if(dijkstraEdges.size() != 0){
	        	//set storke size and color
	        	g2.setStroke(new BasicStroke(5));
	        	graphics.setColor(Color.RED);
	        	for(int j = 0; j < dijkstraEdges.size(); j++){
	        		 line = new Line2D.Double(height -(1-((dijkstraEdges.get(j).v.longitude + lonMax) * height / lonDistance)),
	 	            		(width-((dijkstraEdges.get(j).v.latitude - latMin) * width / latDistance)), 
	 	            		(height-(1-((dijkstraEdges.get(j).w.longitude + lonMax) * height / lonDistance))),
	 	            		(width-((dijkstraEdges.get(j).w.latitude - latMin) * width / latDistance)));

	 	            g2.draw(line);
	        	}
	        	g2.setStroke(new BasicStroke(10));
	        	g2.setColor(Color.GREEN);
	        	g2.draw(new Line2D.Double(height -(1-((dijkstraEdges.get(0).v.longitude + lonMax) * height / lonDistance)),
 	            		(width-((dijkstraEdges.get(0).v.latitude - latMin) * width / latDistance)),
 	            		height -(1-((dijkstraEdges.get(0).v.longitude + lonMax) * height / lonDistance)),
 	            		(width-((dijkstraEdges.get(0).v.latitude - latMin) * width / latDistance))));
	        	g2.draw(new Line2D.Double(height -(1-((dijkstraEdges.get(dijkstraEdges.size()-1).w.longitude + lonMax) * height / lonDistance)),
 	            		(width-((dijkstraEdges.get(dijkstraEdges.size()-1).w.latitude - latMin) * width / latDistance)),
 	            		height -(1-((dijkstraEdges.get(dijkstraEdges.size()-1).w.longitude + lonMax) * height / lonDistance)),
 	            		(width-((dijkstraEdges.get(dijkstraEdges.size()-1).w.latitude - latMin) * width / latDistance))));
	        }
	        if(meridianPath.size() != 0){
	        	g2.setStroke(new BasicStroke(4));
	        	g2.setColor(Color.YELLOW);
	        	for(int j = 0; j < meridianPath.size(); j++){
	        		 line = new Line2D.Double(height -(1-((meridianPath.get(j).v.longitude + lonMax) * height / lonDistance)),
	 	            		(width-((meridianPath.get(j).v.latitude - latMin) * width / latDistance)), 
	 	            		(height-(1-((meridianPath.get(j).w.longitude + lonMax) * height / lonDistance))),
	 	            		(width-((meridianPath.get(j).w.latitude - latMin) * width / latDistance)));

	 	            g2.draw(line);
	        	}
	        }
	    }
}
