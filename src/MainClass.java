
/**
 * @author Avi
 *	The main class gets called when the program runs
 */

import java.awt.Graphics;
import java.awt.Point;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.util.*;
import java.io.*;

public class MainClass {
	private JFrame frame;
	private JComponent drawing;
	private int windowSize = 800;
	private JTextField textField;
	private JTextArea textOutput;
	private JScrollPane scrollPane = new JScrollPane(textOutput);
	private File dirFolder;
	private Graph graph;
	private boolean loaded = false;
	private boolean pathFind = false;
	public int curx;
	public int cury;
	public int prevy;
	public int prevx;

	public int xdiff;
	public int ydiff;

	private int checkPath = 0;
	Intersection path1Node;
	Intersection path2Node;
	public boolean articulationPoint = false;
	/** Constructor */
	public MainClass() {
		startUpInterface();
		drawing.repaint();
	}

	/** This method sets up the interface */
	private void startUpInterface() {
		/* Construct the JFrame and set the size */
		frame = new JFrame();
		frame.setSize(windowSize, windowSize);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up a JPanel that will be at the top of the frame
		JPanel panel = new JPanel();
		JButton loadButton = new JButton("Load");
		panel.add(loadButton);

		// Listener for loading the map, calls loadDir method
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				textOutput.setText("Loading...");
				loadDir();
				graph = new Graph(dirFolder, drawing);
				loaded = true;
				textOutput.setText("Loading...Done ");
			}
		});

		// Add a Quit button the the top in the panel
		JButton quitButton = new JButton("Quit");
		panel.add(quitButton);
		JButton pathFindButton = new JButton("Path Find");
		panel.add(pathFindButton);
		JButton articulationFindButton = new JButton("Articulation Points");
		panel.add(articulationFindButton);
		JLabel topText = new JLabel("Enter road name");
		panel.add(topText);

		/*
		 * Costructs a text field with a document listener and calls search
		 * method in the graph class depending on what is typed in
		 */
		textField = new JTextField(15);
		panel.add(textField);
		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				if (loaded) {
					graph.search(textField.getText(), textOutput);
				}
			}

			public void removeUpdate(DocumentEvent e) {
				if (loaded) {
					graph.search(textField.getText(), textOutput);
				}
			}

			public void insertUpdate(DocumentEvent e) {
				if (loaded) {
					graph.search(textField.getText(), textOutput);
				}
			}
		});

		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (loaded) {
					graph.search(textField.getText(), textOutput);
				}
			}
		});

		// adds the panel and sets the layout as North
		frame.add(panel, BorderLayout.NORTH);
		pathFindButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				pathFind = true;
				//System.out.println("pressed");
			}
		});

		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				System.exit(0);
			}
		});
		
		articulationFindButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if(loaded){
					articulationPoint = true; 
				}
			}
		});

		// construct a panel to draw on set size
		drawing = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				redraw(g);
			}
		};
		drawing.setSize(windowSize, windowSize);
		drawing.setBackground(Color.white);

		// construct a jtextarea to display the availble roads entered accroding
		// to prefix
		textOutput = new JTextArea(2, 0);
		frame.add(textOutput, BorderLayout.SOUTH);
		drawing.addMouseListener(new MouseAdapter() {
			@Override
			// Method when mouse is pressed
			public void mousePressed(MouseEvent e) {
				// store where the mouse was clicked
				curx = e.getX();
				cury = e.getY();
				// if the map is loaded on the screen
				if (loaded) {

					// construct a new point
					Point p1 = new Point();
					p1.x = curx;
					p1.y = cury; 

					// double to keep track of minimum distance
					double distance = -1;
					// get all the nodes from the map
					ArrayList<Intersection> intersections = graph
							.getIntersections();
					// set the closest node to the first one in the list
					Intersection closest = intersections.get(0);

					// loop through each intersection, checking the distance
					// between Point p1 and 'i'
					// if it is less then the distance double set closest node
					// to i and distance to lowest distance
					for (Intersection i : intersections) {
						i.setColor(Color.blue);
						Point iPoint = i.getLocation().getPoint(
								graph.getOrigin(), graph.getScale());
						iPoint.x += xdiff;
						iPoint.y += ydiff;
						if (distance == -1) {
							closest = i;
							distance = p1.distance(iPoint);
						} else if (p1.distance(iPoint) < distance) {
							closest = i;
							distance = p1.distance(iPoint);
						}
					}

					if (pathFind) {
						if (checkPath == 0) {
							path1Node = closest;
							path1Node.setColor(Color.GREEN);
							checkPath = 1;
							System.out.println("First check = 0");
						}
						else if(checkPath == 1){
							System.out.println("First check = 1");
							path2Node = closest;
							path1Node.setColor(Color.GREEN);
							path2Node.setColor(Color.GREEN);
							System.out.println("after set both color to green");
							graph.aSearch(path1Node, path2Node);
							System.out.println("after a*");
							pathFind = false;
							checkPath = 0;
						} 
					}
					else if(articulationPoint){
						graph.interDFSArticulaionPoints(closest);
						articulationPoint = false;
					}

					else {
						// highlight the intersection red
						closest.setColor(Color.red);
						if (closest != null) {
							// list info of the intersection
							graph.listInfo(closest, textOutput);
						}
					}
				}
			}

		});

		/**
		 * Adds a motionListener to the drawing pane and triggers the
		 * mouseDragged event Which controls the panning of the screen
		 */
		drawing.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				// set the previous x and y to the current one
				prevx = curx;
				prevy = cury;
				// set the current x and y of the mouse
				curx = e.getX();
				cury = e.getY();

				/*
				 * Following set of logical statement sets the difference of x
				 * and y and adds it to the fields xdiff and ydiff which then
				 * gets added to the arguments in the draw method
				 */
				if (prevx > curx) {
					int minus = curx - prevx;
					xdiff += minus;
				} else if (prevx < curx) {
					int plus = curx - prevx;
					xdiff += plus;
				}

				if (cury > prevy) {
					int plus = cury - prevy;
					ydiff += plus;
				} else if (cury < prevy) {
					int minus = cury - prevy;
					ydiff += minus;
				}

			}
		});

		/**
		 * Add mouseWheelListener to drawing pane set the scale of the map
		 * depending on the rotation of the mouse wheel
		 * */
		drawing.addMouseWheelListener(new MouseAdapter() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int move = e.getWheelRotation();
				double scale = graph.getScale();
				scale -= move;
				// so the map doesnt invert
				if (scale < 1) {
					scale = 1;
				}
				graph.setScale(scale);
			}
		});
		frame.add(drawing);
		frame.setVisible(true);
	}

	/**
	 * This method gets called from the paint component If the graph is loaded
	 * calls the draw method to the graph
	 */
	public void redraw(Graphics g) {
		g.setColor(Color.black);

		g.drawLine(0, g.getClipBounds().height - 1,
				g.getClipBounds().width - 1, g.getClipBounds().height - 1);
		if (loaded) {
			graph.draw(g, graph.getOrigin(), graph.getScale(), Color.blue,
					xdiff, ydiff);
		}
		drawing.repaint();
	}

	/** Method to prompt the user for the directory file */
	private void loadDir() {
		JFileChooser openFile = new JFileChooser();
		openFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		openFile.setAcceptAllFileFilterUsed(false);
		openFile.showOpenDialog(null);
		File folder = new File(openFile.getSelectedFile().toString());
		this.dirFolder = folder;

	}

	/**
	 * Main class
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new MainClass();
	}

}
