import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Graph {
	private ArrayList<Intersection> intersections = new ArrayList<Intersection>();
	private ArrayList<Road> roads;
	private ArrayList<Segment> segments;
	private File dir;
	File[] listOfFiles;
	private Trie trie;
	private double east;
	private double south;
	private double north;
	private double west;
	private double scale;
	private Location origin;
	private JComponent drawing;
	private ArrayList<Intersection> articulationPoints;

	public Graph(File dir, JComponent d) {
		this.drawing = d;
		intersections = new ArrayList<Intersection>();
		roads = new ArrayList<Road>();
		segments = new ArrayList<Segment>();
		this.dir = dir;
		listOfFiles = this.dir.listFiles();
		loadIntersections();
		loadRoads();
		loadSegments();
		setMapBoundries();
		loadTrie();
		connectSegToNode();
	}
	//TODO display articulation points
	public void findArticulationPoints(){
		for(Intersection i : intersections){
			i.depth = Double.POSITIVE_INFINITY;
		}
		
		this.articulationPoints = new ArrayList<Intersection>();
		
		Intersection start = intersections.get(0);
		start.depth = 0;
		int numSubtrees = 0;
		ArrayList<Intersection> neighbours = getNeighbours(start);
		for(Intersection neigh : neighbours){
			if(neigh.depth == Double.POSITIVE_INFINITY){
				recArtPts(neigh,1.0,start);
				numSubtrees++;
			}
		}
		if(numSubtrees > 1){
			this.articulationPoints.add(start);
		}
	}
	
	
	private double recArtPts(Intersection node, double depth, Intersection fromNode) {
		node.depth = depth;
		double reachBack = depth;
		
		for(Intersection neighbour : getNeighbours(node)){
			if(!(neighbour.equals(fromNode))){
				if(neighbour.depth < Double.POSITIVE_INFINITY){
					reachBack = Math.min(neighbour.depth,reachBack);
				}
				else{
					double childReach = recArtPts(neighbour, depth+1, node);
					reachBack = Math.min(childReach, reachBack);
					if(childReach >= depth){
						articulationPoints.add(node);
					}
					
				}
			}
		}
		
		return reachBack;
		
	}
	
	public ArrayList<Intersection> getNeighbours(Intersection node){
		ArrayList<Intersection> neighbours = new ArrayList<Intersection>();
		for(Segment seg : node.getEdges()){
			if(seg.getNodeID1().equals(node)){
				if(seg.getNodeID2() != null){
					neighbours.add(seg.getNodeID2());
				}
			}
			else if(seg.getNodeID2().equals(node)){
				if(seg.getNodeID1() != null){
					neighbours.add(seg.getNodeID1());
				}
			}
		}
		return neighbours;
	}

	/** A* Search looking for the shortest path*/
	public void aSearch(Intersection start, Intersection goal) {
		Comparator<fringeObject> comparator = new fringeObjectComparator();
		PriorityQueue<fringeObject> fringe = new PriorityQueue<fringeObject>(10,comparator);
		
		for(Intersection i : intersections){
			i.visited = false;
		}
		fringeObject pathFrom = null;
		fringe.add(new fringeObject(start,pathFrom,0,estimatePathLengh(start, goal)));
		while(!(fringe.isEmpty())){
			fringeObject fringeNode = fringe.remove();
			Intersection node = fringeNode.getNode();
			if(!(node.visited)){
				node.visited = true;
				pathFrom = fringeNode;
				if(node.equals(goal)){
					this.displayPathFromFringeObject(fringeNode);
					break;
				}
				ArrayList<Segment> edges = new ArrayList<Segment>();
				edges = node.getEdges();
				for(Segment seg : edges){
					Intersection neigh = null;
					if(seg.getNodeID1().equals(node)){
						if(seg.getNodeID2() != null){
							neigh = seg.getNodeID2();
						}
					}
					else if(seg.getNodeID2().equals(node)){
						if(seg.getNodeID1() != null){
							neigh = seg.getNodeID1();
						}
					}
					if(neigh != null){
						if(!(neigh.visited)){
							double costToNeigh = fringeNode.getCostToHere() + seg.getLength();
							double estTotal = costToNeigh + estimatePathLengh(neigh,goal);
							fringe.add(new fringeObject(neigh, fringeNode , costToNeigh, estTotal));
						}
					}
				}
			}
			
		}
		
		
	}

	public double estimatePathLengh(Intersection from, Intersection to) {
		return from.getLocation().distanceTo(to.getLocation());
	}
	
	public void displayPathFromFringeObject(fringeObject frin){
		for(Segment seg : segments){
			seg.setColor(Color.blue);
		}
		while(frin.getFrom()!=null){
			frin.getNode().setColor(Color.GREEN);
			ArrayList<Segment> edges = frin.getNode().getEdges();
			for(Segment seg : edges){
				if(seg.getNodeID1().equals(frin.getFrom().getNode())
						|| seg.getNodeID2().equals(frin.getFrom().getNode())){
					seg.setColor(Color.GREEN);					
				}
			}
			frin = frin.getFrom();
		}
	}

	/**
	 * Searches the all the words which matches the given prefix and displays
	 * the city and label on the text area given
	 */
	public void search(String prefix, JTextArea text) {
		// set all the colors to blue
		if (prefix.length() > 0) {
			for (Segment seg : segments) {
				seg.setColor(Color.blue);
			}
			// get the subsTrie matching given prefix
			Trie substree = trie.getTrieFromPrefix(prefix);
			// set the text to empty
			text.setText("");
			// if the substree is not null
			if (substree != null) {
				// get the list of words from the root of the subtrie
				ArrayList<String> list = substree.getRoot().getWords();
				int count = 0;
				// loop through the list and display the word on the text pane,
				// to a maximum of ten possibilities
				for (String s : list) {
					if (count == 9) {
						break;
					}
					text.append(s + '\n');
					count++;

				}
				if (count == 1) {
					// display the name of the road to the text pane
					String name = text.getText();
					// an array list to store the roadIds
					ArrayList<Integer> roadIds = new ArrayList<Integer>();
					/*
					 * Loop through each road getting the name, and if the name
					 * equals to the name in the text add the road id to the
					 * list
					 */
					for (Road road : roads) {
						String s = road.getLabel() + " " + road.getCity()
								+ '\n';
						if (name.equals(s)) {
							roadIds.add(road.getRoadID());
						}
					}

					/*
					 * Loop through all the roadIds and loop through the
					 * segments while changing the color of the segment if the
					 * id of the segment matches the int in the array list
					 */
					if (roadIds.size() > 0) {
						for (Integer i : roadIds) {
							for (Segment seg : segments) {
								if (seg.getRoadID() == i) {
									seg.setColor(Color.red);
								}
							}
						}
					}
				}
			}
		}
	}

	private void connectSegToNode(){
		for(Segment seg : segments){
			if(seg.getNodeID1() != null){
				seg.getNodeID1().addSegment(seg);
			}
			
			if(seg.getNodeID2() != null){
				seg.getNodeID2().addSegment(seg);
			}
		}
	}
	
	/**
	 * @return the intersections
	 */
	public ArrayList<Intersection> getIntersections() {
		return intersections;
	}

	/**
	 * @return the segments
	 */
	public ArrayList<Segment> getSegments() {
		return segments;
	}

	/** Loads all the road names to the trie */
	private void loadTrie() {
		this.trie = new Trie();
		for (Road r : roads) {
			trie.insertWord(r.getLabel() + " " + r.getCity());
		}

	}

	public double getScale() {
		return this.scale;
	}

	public Location getOrigin() {
		return this.origin;
	}

	/**
	 * Sets the map boundries by finding the left, top ,right and botton most
	 * nodes and sets the origin by (leftmost,topmost) also sets the scale by
	 * windowsize/(leftmost-rightmost)
	 */
	private void setMapBoundries() {
		if (!(intersections.isEmpty())) {
			boolean first = true;
			for (Intersection i : intersections) {
				if (first) {
					north = i.getLocation().y;
					south = i.getLocation().y;
					west = i.getLocation().x;
					east = i.getLocation().x;
					first = false;

				} else {
					if (north < i.getLocation().y) {
						north = i.getLocation().y;
					}
					if (south > i.getLocation().y) {
						south = i.getLocation().y;
					}
					if (west > i.getLocation().x) {
						west = i.getLocation().x;
					}
					if (east < i.getLocation().x) {
						east = i.getLocation().x;
					}
				}
			}
			this.origin = new Location(west, north);
			this.scale = 800 / (east - west);
		}

	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	/**
	 * Draw method for this class which calls all the draw methods for segments
	 * and intersections
	 */
	public void draw(Graphics g, Location origin, double scale, Color color,
			int xdiff, int ydiff) {
		for (Segment s : segments) {
			s.draw(g, origin, scale, color, xdiff, ydiff);
		}

		for (Intersection i : intersections) {
			i.draw(g, origin, scale, color, xdiff, ydiff);
		}

	}

	/** Read and load the intersections from file */
	private void loadIntersections() {
		File file = null;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].getName().equals("nodeID-lat-lon.tab")) {
				file = listOfFiles[i];
				break;
			}
		}

		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				if (sc.hasNext()) {
					int id = sc.nextInt();
					double lat = sc.nextDouble();
					double lon = sc.nextDouble();
					intersections.add(new Intersection(id, lat, lon));
					sc.nextLine();
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/** Read and load the roads from file */
	private void loadRoads() {
		File file = null;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].getName().equals("roadID-roadInfo.tab")) {
				file = listOfFiles[i];
				break;
			}
		}

		try {
			Scanner sc = new Scanner(file);
			sc.nextLine();
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] values = line.split("\\t");
				int id = Integer.parseInt(values[0]);
				int type = Integer.parseInt(values[1]);
				String label = values[2];
				String city = values[3];
				int oneway = Integer.parseInt(values[4]);
				int speed = Integer.parseInt(values[5]);
				int roadclass = Integer.parseInt(values[6]);
				int notforcar = Integer.parseInt(values[7]);
				int notforped = Integer.parseInt(values[8]);
				int norforbicy = Integer.parseInt(values[9]);
				Road r = new Road(id, type, label, city, oneway, speed,
						roadclass, notforcar, notforped, norforbicy);
				roads.add(r);
				// sc.nextLine();

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/** Read and load the segments from file */
	private void loadSegments() {
		File file = null;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].getName().equals(
					"roadSeg-roadID-length-nodeID-nodeID-coords.tab")) {
				file = listOfFiles[i];
				break;
			}
		}

		try {
			Scanner sc = new Scanner(file);
			sc.nextLine();
			while (sc.hasNextLine()) {
				int roadid = sc.nextInt();
				double length = sc.nextDouble();
				int inter1id = sc.nextInt();
				int inter2id = sc.nextInt();
				ArrayList<Location> loc = new ArrayList<Location>();
				String line = sc.nextLine();
				String[] values = line.split("\\t");
				for (int i = 1; i < values.length; i += 2) {
					double lat = Double.parseDouble(values[i]);
					double lon = Double.parseDouble(values[i + 1]);
					loc.add(Location.newFromLatLon(lat, lon));
				}
				Intersection int1 = null;
				Intersection int2 = null;
				for (Intersection i : intersections) {
					if (i.getID() == inter1id) {
						int1 = i;
					} else if (i.getID() == inter2id) {
						int2 = i;
					}
				}
				segments.add(new Segment(roadid, length, int1, int2, loc));

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * A method to display the information of the node that is currently
	 * selected
	 */
	public void listInfo(Intersection closest, JTextArea textOutput) {
		Set<Integer> roadIds = new HashSet();
		double lat = closest.getLatitude();
		double lon = closest.getLongitude();
		ArrayList<String> roadNames = new ArrayList<String>();
		for (Segment seg : segments) {
			if ((seg.getNodeID1().getID() == closest.getID())
					|| (seg.getNodeID2().getID() == closest.getID())) {
				roadIds.add(seg.getRoadID());
			}
		}

		for (Integer i : roadIds) {
			for (Road road : roads) {
				if (i == road.getRoadID()) {
					roadNames.add(road.getLabel() + " " + road.getCity());
				}
			}
		}
		textOutput.setText("");
		textOutput.append("Info for intersection " + '\n');
		textOutput.append("Latitude: " + lat + " Longitude: " + lon + '\n');
		textOutput
				.append("Names of roads connecting to this intersection:" + '\n');
		for (String s : roadNames) {
			textOutput.append(s + '\n');
		}
	}

}
