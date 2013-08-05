/* Code for COMP261 Assignment
 */


// call repaint() on this object to invoke the drawing.
package nameCompleter;
import javax.swing.JComponent;
import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.*;


/** PathDrawer is an example of drawing graphics in
 *  Java without using the UI class.
 *  It constructs a list of cities at random locations, drawn as circles.
 *  The user can select start and goal cities, and it then draws a path
 *  from the start to the goal via the closest other point.
 *  Has a button to reset, which creates a new set of cities.
 *  Cities are positioned on some 2D coordinate system.
 *  Has a text box for selecting a start city
 *
 *  Locations of the cities have to be converted to window (pixel based) positions
 *  and vice versa.
 *  The Location class handles these conversions, but requires an origin and a scale.
 */

public class NameCompleter{

    private JFrame frame;
    private JTextArea textOutput; 
    private int windowSize = 400;

    public NameCompleter(){
	// Set up a window .
	frame = new JFrame("Text entry field example");
	frame.setSize(windowSize,windowSize);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	textOutput = new JTextArea(20, 100);
	JScrollPane textSP = new JScrollPane(textOutput);
	frame.add(textSP, BorderLayout.SOUTH);

	//set up code for auto-suggest text field
	AutoSuggestionTextField<String> textField = new AutoSuggestionTextField<String>();
	textField.setAutoSuggestor(new SuburbAutoSuggestor());
	textField.setSuggestionListener(new SuggestionListener<String>() {
                        
		@Override
		    public void onSuggestionSelected(String item) {
		    textOutput.setText("\nSelected: " + item);
		}
                        
		@Override
		    public void onEnter(String query) {
		    textOutput.setText("\nQuery: " + query);
		}
	    });

	//Set up a panel 
	JPanel panel = new JPanel();
	frame.add(panel, BorderLayout.NORTH);
	panel.add(new JLabel("Type suburb name: "));

	panel.add(textField);

	JButton button = new JButton("Quit");
	panel.add(button);
	button.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ev){System.exit(0);}});

	textOutput.setText("Possible Suburbs\n");
	textOutput.append(SuburbAutoSuggestor.suburbList());
	
	frame.setVisible(true);
    }


    public static void main(String[] arguments){
       new NameCompleter();
    }	


}
