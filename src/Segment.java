import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**Class which represents a segment on the map*/
public class Segment {	
	private int roadID;
	private double length;
	private Intersection nodeID1 = null;
	private Intersection nodeID2 = null;
	private ArrayList<Location> locationPoints = new ArrayList<Location>();
	private Color color;
	
	public Segment(int roadID, double length, Intersection nodeID1, Intersection nodeID2,
			ArrayList<Location> locationPoints) {
		this.roadID = roadID;
		this.length = length;
		this.nodeID1 = nodeID1;
		this.nodeID2 = nodeID2;
		this.locationPoints = locationPoints;
		this.color = color.blue;
	}
	
	public void draw(Graphics g,Location origin, double scale, Color color, int xdiff, int ydiff){
		g.setColor(this.color);
		
		for(int i = 0; i < locationPoints.size()-1; i++){
			Location loc1 = locationPoints.get(i);
			Location loc2 = locationPoints.get(i+1);
			Point p1 = loc1.getPoint(origin, scale);
			Point p2 = loc2.getPoint(origin, scale);
			g.drawLine(p1.x + xdiff, p1.y+ydiff, p2.x+xdiff, p2.y+ydiff);
			
		}
	}
	/**
	 * @return the roadID
	 */
	public int getRoadID() {
		return roadID;
	}

	public double getLength() {
		return length;
	}

	/**
	 * @return the nodeID1
	 */
	public Intersection getNodeID1() {
		return nodeID1;
	}

	/**
	 * @return the nodeID2
	 */
	public Intersection getNodeID2() {
		return nodeID2;
	}

	/**
	 * @return the locationPoints
	 */
	public ArrayList<Location> getLocationPoints() {
		return locationPoints;
	}
	public void setColor(Color c){
		this.color = c;
	}
}
