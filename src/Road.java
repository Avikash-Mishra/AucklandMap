import java.util.ArrayList;

/**
 * A class which represents a Road in the map
 * */
public class Road {
	private int roadID;
	private int type;
	private String label;
	private String city;
	private int oneWay;
	private int speed;
	private int roadClass;
	private int notForCar;
	private int notForPed;
	private int notForBicy;
	private ArrayList<Segment> segments;

	public Road(int roadID, int type, String label, String city, int oneWay,
			int speed, int roadClass, int notForCar, int notForPed,
			int notForBicy) {
		this.roadID = roadID;
		this.type = type;
		this.label = label;
		this.city = city;
		this.oneWay = oneWay;
		this.speed = speed;
		this.roadClass = roadClass;
		this.notForCar = notForCar;
		this.notForPed = notForPed;
		this.notForBicy = notForBicy;
		this.segments = new ArrayList<Segment>();
	}
	
	/**
	 * Method which takes in a segment checks if 
	 * the id matches this road and adds it to the 
	 * array list segments
	 * */
	public void addSegment(Segment s){
		if(s.getRoadID() == this.roadID){
			segments.add(s);
		}
	}
	
	
	public ArrayList<Segment> getRoadSegments(){
		return this.segments;
	}

	/**
	 * @return the roadID
	 */
	public int getRoadID() {
		return roadID;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the oneWay
	 */
	public int getOneWay() {
		return oneWay;
	}

	/**
	 * @return the speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * @return the roadClass
	 */
	public int getRoadClass() {
		return roadClass;
	}

	/**
	 * @return the notForCar
	 */
	public int getNotForCar() {
		return notForCar;
	}

	/**
	 * @return the notForPed
	 */
	public int getNotForPed() {
		return notForPed;
	}

	/**
	 * @return the notForBicy
	 */
	public int getNotForBicy() {
		return notForBicy;
	}

}
