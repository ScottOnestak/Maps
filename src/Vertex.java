import java.util.LinkedList;

public class Vertex {
	
	public int vNum;
	public String name;
	public double latitude, longitude, distance;
	public boolean known;
	public Vertex path;
	public LinkedList<Adjacents> adjacencies;
	
	public Vertex(int vNum, String name, double latitude, double longitude){
		this.vNum = vNum;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		known = false;
		path = null;
		adjacencies = new LinkedList<Adjacents>();
		distance = Double.POSITIVE_INFINITY;
	}

	//insert into adjacencies linked list
	public void insert(Adjacents a){
		adjacencies.add(a);
	}

	
}
