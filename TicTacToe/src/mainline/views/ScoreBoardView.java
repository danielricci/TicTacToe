package mainline.views;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import mainline.controllers.ScoreBoardController;
import mainline.controls.PlayerSetupDialogBox;
import mainline.globals.SpringUtilities;

/**
 * Holds the view information about the players that are playing the game
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
@SuppressWarnings("serial")
public class ScoreBoardView extends JPanel implements Observer {

	/**
	 * The controller associated to this view
	 */
	private ScoreBoardController _controller = null;
	
	/**
	 * Holds the encapsulated spring layout of this view
	 */
	private JPanel _panel = new JPanel(new SpringLayout());
	
	/**
	 * Holds a quick reference table to be able to associate the players information in the view
	 */
	private Dictionary<Integer, JLabel> _playersTokens = new Hashtable<Integer, JLabel>();
	
	/**
	 * Constructs a new object of this class
	 */
	public ScoreBoardView() {
		super(new GridLayout());
	}
	
	@Override
	public void update(Observable obs, Object object) { 
		if(object instanceof Integer)
		{
			// Update the players tokens on this view w.r.t its pIDß
			int pID = (Integer)object;
			if(pID > 0)
			{
				JLabel label = _playersTokens.get(pID);
				if(label != null) {
					label.setText("" + _controller.getPlayersTokens(pID));
				}
			}
		}
	}
	
	/**
	 * Populates the data of this view
	 */
	private void populateData() {
		
		// Create the headers
        populateHeaders();
       
        // We use the player setup
        PlayerSetupDialogBox[] players = _controller.getPlayers();
        
        
        for(PlayerSetupDialogBox player : players) {
        	
        	// Setup the labels for the particular player
        	JLabel playerTeam = new JLabel(player.getTeam(), SwingConstants.CENTER); 	
        	JLabel playerName = new JLabel(player.getName(), SwingConstants.CENTER);
        	JLabel playerHasAI = new JLabel(player.getHasAI() ? "AI" : "Human", SwingConstants.CENTER);
        	JLabel playerTokens = new JLabel("" + _controller.getPlayersMaxTokens(), SwingConstants.CENTER);
        	
        	// Add them to our panel
        	_panel.add(playerTeam);
        	_panel.add(playerName);
        	_panel.add(playerHasAI);
        	_panel.add(playerTokens);
        	
        	// Add it to our table
        	_playersTokens.put(player.getPID(), playerTokens);
        }
        
        // Form the grid 
        SpringUtilities.makeCompactGrid(_panel, 
                players.length + 1,      // row
                4,      // column
                12,     // initX
                12,     // initY
                15,     // xPad 
                15);    // yPad
        add(_panel);
	}

	/**
	 * Adds a controller to our view
	 * 
	 * @param controller The controller to associate this view to
	 */
	public void addController(ScoreBoardController controller) {
		if(_controller == null) {
			_controller = controller;
		}
	}
	
	/**
	 * Renders the data in this view
	 */
	public void render() {
		populateData();
	}
	
	/**
	 * Populates the headers of this view
	 * 
	 * @param _panel The panel to attach to
	 */
	private void populateHeaders() {
		
		JLabel playerTeamHeader = new JLabel("Player Team", SwingConstants.CENTER);
		playerTeamHeader.setFont(new Font(playerTeamHeader.getFont().getFontName(), Font.BOLD, playerTeamHeader.getFont().getSize()));
		
		JLabel playerNameHeader = new JLabel("Player Name", SwingConstants.CENTER);
		playerNameHeader.setFont(new Font(playerNameHeader.getFont().getFontName(), Font.BOLD, playerNameHeader.getFont().getSize()));
	
		JLabel playerTypeHeader = new JLabel("Player Type", SwingConstants.CENTER);
		playerTypeHeader.setFont(new Font(playerTypeHeader.getFont().getFontName(), Font.BOLD, playerTypeHeader.getFont().getSize()));
		
		JLabel playerTokensHeader = new JLabel("Player Tokens", SwingConstants.CENTER);
		playerTokensHeader.setFont(new Font(playerTokensHeader.getFont().getFontName(), Font.BOLD, playerTokensHeader.getFont().getSize()));
				
		_panel.add(playerTeamHeader);
		_panel.add(playerNameHeader);
		_panel.add(playerTypeHeader);
		_panel.add(playerTokensHeader);
	}
}