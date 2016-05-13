package mainline.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mainline.GameInstance;
import mainline.components.PlayerSetupDialogBox;
import mainline.models.PlayerModel;
import mainline.views.ScoreBoardView;

/**
 * The controller associated to the scoreboard view
 * 
 * @author Daniel Ricci - thedanny09@gmail.com
 *
 */
public class ScoreBoardController implements ActionListener {

	/**
	 * The view associated to this controller
	 */
	private ScoreBoardView _view = null;
	
	/**
	 * The players associated to this controller in terms of their setup
	 */
	private PlayerSetupDialogBox[] _players = null;
	
	/**
	 * The board game controller reference
	 */
	// TODO - this isnt good code, it would mean that if the board game hasnt been created at least once than 
	// this would not be able to work (dependency)
	private BoardGameController _controller = (BoardGameController)GameInstance.getInstance().getController(BoardGameController.class.getName());
	
	/**
	 * Constructs a new object of this class
	 */
	public ScoreBoardController() {
		populatePlayers();
		addView(new ScoreBoardView());
		registerPlayerModels();
	}
	
	/**
	 * Gets the maximum tokens that the players can have
	 * 
	 * @return The maximum tokens that the players can have
	 */
	public int getPlayersMaxTokens() {
		return 1;
	}
	
	/**
	 * Gets the tokens of a player
	 * 
	 * @param pID The player identification
	 * 
	 * @return The number of tokens that the player is holding
	 */
	public int getPlayersTokens(int pID) {
		return 0;//return _controller.getPlayerTokens(pID);
	}
	
	/**
	 * Populates the players
	 */
	private void populatePlayers() {
		//_players = _controller.getPlayersSetup();
	}
	
	/**
	 * Gets the players setup
	 * 
	 * @return The players setup
	 */
	public PlayerSetupDialogBox[] getPlayers() {
		return _players;
	}

	/**
	 * Registers the player models
	 */
	private void registerPlayerModels() {
		_controller.registerListener(this);
	}
	
	/**
	 * Adds the view to this controller
	 * 
	 * @param view The view to add to this controller
	 */
	private void addView(ScoreBoardView view) {
		if(_view == null) {
			_view = view;
			_view.addController(this);
			_view.render();
		}
	}

	/**
	 * Gets the view associated to this controller
	 * 
	 * @return The view associated to this controller
	 */
	public ScoreBoardView getView() {
		return _view;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) { }
}
