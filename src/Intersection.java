import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Class which represents a intersection in the map
 * */
public class Intersection {
	private Location location;
	private int ID;
	private double latitude;
	private double longitude;
	private Color color; 
	private ArrayList<Segment> edges = new ArrayList<Segment>();
	boolean visited;
	double depth = Double.POSITIVE_INFINITY;
	public Intersection(int id, double latitude, double longitude) {
		this.ID = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.location = Location.newFromLatLon(latitude, longitude);
		this.color = Color.blue;
		this.visited = false;
	}
	
	public void addSegment(Segment s){
		this.edges.add(s);
	}
	
	/**
	 * @return the edges
	 */
	public ArrayList<Segment> getEdges() {
		return edges;
	}

	/**
	 * @param edges the edges to set
	 */
	public void setEdges(ArrayList<Segment> edges) {
		this.edges = edges;
	}

	public void setColor(Color c){
		this.color = c;
	}

	public void draw(Graphics g, Location origin, double scale, Color color, int xdiff, int ydiff) {
		g.setColor(this.color);
		Point p1 = location.getPoint(origin, scale);
		g.drawRect(p1.x-1+xdiff, p1.y-1+ydiff, 1, 1);

	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

}
