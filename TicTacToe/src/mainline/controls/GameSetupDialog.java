package mainline.controls;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import mainline.globals.SpringUtilities;
import mainline.models.PlayerModel;

@SuppressWarnings("serial")

/**
 * The dialog box that creates the players
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class GameSetupDialog extends JDialog { 
    
	/**
	 * The list of player setup representing a player in the dialog box
	 */
	private ArrayList<PlayerSetupDialogBox> _playerSetup = new ArrayList<PlayerSetupDialogBox>();
	
	/**
	 * The identity panel
	 */
	private JPanel _identityPanel = new JPanel(new SpringLayout());
	
	/**
	 * The Actions panel
	 */
    private JPanel _actions = new JPanel(new SpringLayout());
    
    /**
     * The Grids panel
     */
    private JPanel _grids = new JPanel(new SpringLayout());
    
    /**
     * The flag for verifying that the dialog is done
     */
    private boolean _dialogResult = false;
	
    /**
     * Constructs a new object of this class
     */
    public GameSetupDialog() {
        setTitle("Game Setup");
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);		        
    }
        
    /**
     * Populates the view to allow a user to select the number of countries
     * 
     * @param actionEvent The event that triggered this action
     */
    public void populateData() {  
    	// Set the content pane and its visual behavior
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
             
        // Populate Headers
        populateHeaders(_identityPanel);
        
        // Populate Actions
        populateActions(_actions);
    
        // Populate Grids
        populateGridSelection(_grids);
        
        // TODO - this needs to be based on the number of teams available!
        for(int i = 0; i < 2; ++i) { 
        	PlayerSetupDialogBox player = new PlayerSetupDialogBox();
        	player.addTeam(PlayerModel.Team.values());
        	player.attachToPanel(_identityPanel);
        	_playerSetup.add(player);
        }
    
        SpringUtilities.makeCompactGrid(_identityPanel, 
                3,      // row
                3,      // column
                12,    	// initX
                12,     // initY
                15,     // xPad 
                15);    // yPad
         
        // Add our panel to our content pane
        contentPane.add(_identityPanel);
        
        SpringUtilities.makeCompactGrid(_grids, 
                1,      // row
                2,      // column
                12,     // initX
                12,     // initY
                15,     // xPad 
                15);    // yPad
        contentPane.add(_grids);
        
        // Make our layout a grid
        SpringUtilities.makeCompactGrid(_actions, 
                1,      // row
                2,      // column
                12,     // initX
                12,     // initY
                15,     // xPad 
                15);    // yPad
        contentPane.add(_actions);
                
        // Pack our panel together, center and show
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Populates the header items of the dialog
     * 
     * @param panel The panel to attach to
     */
    private void populateHeaders(JPanel panel) {
        panel.add(new JLabel("Full Name", SwingConstants.CENTER));
        panel.add(new JLabel("Team Color", SwingConstants.CENTER));
        panel.add(new JLabel("AI", SwingConstants.CENTER));
    }
    
    /**
     * Populates the action item of the dialog
     * 
     * @param panel The panel to attach to
     */
    private void populateActions(JPanel panel) {
    	
        JButton okButton = new JButton("OK");
        
        okButton.addActionListener(new ActionListener() {
        
        	@Override
        	public void actionPerformed(ActionEvent event) {  
            	if(verifyDialog()) {
                	_dialogResult = true;
                	setVisible(false);            		
            	} 
            }

        	/**
        	 * Verifies that the dialog box has met a bunch of requisites
        	 * 
        	 * @return If the dialog box has met a bunch of requisites
        	 */
			private boolean verifyDialog() {
				
				// Ensure that our teams are unique in our set
				// so we can determine team uniqueness
				Set<String> teams = new HashSet<String>();
				for(PlayerSetupDialogBox player : _playerSetup) {
				
					// A small check to ensure that names are not empty
					if(player.getName().isEmpty()) {
						return false;
					}
					teams.add(player.getTeam().toLowerCase());
				}
				return teams.size() == _playerSetup.size();
			}
        });
        
        // Add the button to the panel
        panel.add(okButton);
        
        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
        	
        	@Override
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        	
        });
        panel.add(cancelButton);
    }
        
    /**
     * Populates the grid drop down list
     * 
     * @param panel The panel to attach to
     */
    private void populateGridSelection(JPanel panel) {
        
        // Add our label
        panel.add(new JLabel("Grid Size"));
        
        // TODO - this should be configurable
        PlayerSetupDialogBox.setGridRange(2, 10);
        
        // Add our selection to our panel
        PlayerSetupDialogBox.attachGridToPanel(panel);
    }

    /**
     * If the dialog is done
     * 
     * @return If the dialog is done
     */
	public boolean isDialogDone() {
		return _dialogResult;
	} 
	
	/**
	 * Gets the list of player setups
	 * 
	 * @return The list of player setups
	 */
	public PlayerSetupDialogBox[] getPlayerSetup() {
		return _playerSetup.toArray(new PlayerSetupDialogBox[_playerSetup.size()]);
	}
}