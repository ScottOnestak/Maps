public class Edge {
	public Vertex v, w;
	double weight;
	
	public Edge(Vertex v, Vertex w){
		this.v = new Vertex(v.vNum, v.name, v.latitude, v.longitude);
		this.w = new Vertex(w.vNum, w.name, w.latitude, w.longitude);
		this.weight = weight(v.latitude, v.longitude, w.latitude, w.longitude);
	}
	
	//implement Haversine formula to get mileage
	//formula for converting to miles found at: http://andrew.hedges.name/experiments/haversine/
	private double weight(double vlat, double vlon, double wlat, double wlon){
		//displacement of longitude and latitude
		double displacementLatitude = Math.toRadians(wlat - vlat);
		double displacementLongitude = Math.toRadians(wlon - vlon);
		vlat = Math.toRadians(vlat);
		wlat = Math.toRadians(wlat);
		//earth's radius (in miles)
		double earthRadius = 3961;
		
		double a = Math.pow((Math.sin(displacementLatitude/2)),2) + Math.cos(vlat) * Math.cos(wlat) * Math.pow(Math.sin(displacementLongitude/2), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		
		//the distance in miles traveled
		double distance = earthRadius * c;
		
		return distance;
	}
}
