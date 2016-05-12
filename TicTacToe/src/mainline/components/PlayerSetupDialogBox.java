package mainline.components;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mainline.models.PlayerModel;

/**
 * The player setup helper object that represents the business logic
 * of the configured data of a player
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class PlayerSetupDialogBox {

	/**
	 * The text field for the name of the player setup component
	 */
	private JTextField _name = new JTextField();
	
	/**
	 * The team drop down component of the player setup
	 */
	private JComboBox<String> _team = new JComboBox<String>();
    
	/**
	 * The AI of the player setup component
	 */
	private JCheckBox _hasAI = new JCheckBox();
	
	/**
	 * The grid size combo box
	 */
	private static JComboBox<String> _gridSizes = new JComboBox<String>();
	
	/**
	 * The width we use for our components
	 */
	private static final int WIDTH = 150;
		
	/**
	 * The player id
	 */
	private int _pID = 0;
	
	/**
	 * Constructs a new object of this class
	 */
	public PlayerSetupDialogBox() {
		_name.setPreferredSize(new Dimension(WIDTH, 0));
		_team.setPreferredSize(new Dimension(WIDTH, 0));
	}
	
	/**
	 * Sets the grid range in our drop down component
	 * 
	 * @param min start
	 * @param max end
	 */
	public static void setGridRange(int min, int max) {
		// Ensures that we are working with a clean list 
		// because of its static nature
		_gridSizes.removeAllItems();
		for(int i = min; i <= max; ++i) {
			_gridSizes.addItem(String.valueOf(i));
		}
	}
	
	/**
	 * Sets the players identification
	 * 
	 * @param pID The players identification to set
	 */
	public void setPID(int pID) {
		_pID = pID;
	}
	
	/**
	 * Gets the pID of the player, this is usually only
	 * possible if it is encapsulated by a player model
	 * 
	 * @return The player identification
	 */
	public int getPID() {
		return _pID;
	}
	
	/**
	 * Gets the size of the grid
	 * 
	 * @return The size of the grid
	 */
	public static int getGridSize() {
		return Integer.parseInt(_gridSizes.getSelectedItem().toString());
	}

	/**
	 * Adds a player models team to the drop down list component
	 * 
	 * @param teams The teams to add
	 */
	public void addTeam(PlayerModel.Team[] teams) {
		if(teams != null) {
			for(PlayerModel.Team team : teams) {
				_team.addItem(team.toString());			
			}
		}
	}

	//TODO - put this in the dialog
	/**
	 * Attach's our contents to our panel
	 * 
	 * @param panel The panel to attach to
	 */
	public void attachToPanel(JPanel panel) {
		if(panel != null) {
			panel.add(_name);
			panel.add(_team);
			panel.add(_hasAI);
		}
	}    
	
	//TODO - put this in the dialog
	/**
	 * Attach's the grid to our dialog panel
	 * 
	 * @param panel The panel to attach to
	 */
	public static void attachGridToPanel(JPanel panel) {
		if(panel != null) {
			panel.add(_gridSizes);
		}
	}
	
	/**
	 * Gets the players name
	 * 
	 * @return The players name
	 */
	public String getName() {
		return _name.getText().trim();
	}
	
	/**
	 * Gets the team of this player
	 * 
	 * @return The players team
	 */
	public String getTeam() {
		return _team.getSelectedItem().toString();
	}
	
	/**
	 * Gets if the player has AI
	 * 
	 * @return If the player has AI
	 */
	public boolean getHasAI() {
		return _hasAI.isSelected();
	}
	
	@Override
	public String toString() {
		return "Name: " + getName() + "Team: " + getTeam() + "hasAI " + getHasAI();
	}
}