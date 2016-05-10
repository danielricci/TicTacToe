package mainline.controllers;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import mainline.GameInstance;
import mainline.controls.PlayerSetupDialogBox;
import mainline.models.PlayerModel;
import mainline.models.PlayerModel.Team.Configuration;
import mainline.views.BoardGameView;
import mainline.views.BoardGameView.BoardPosition;

/**
 * Represents the controller that holds actions for serving the board game related 
 * queries 
 * 
 * @author Daniel Ricci<thedanny09@gmail.com>
 * 
 */
public class BoardGameController implements ActionListener {
	
	/**
	 * The player models for this controller
	 */
	private PlayerModel[] _players = null;
	
	/**
	 * The queue of references to manage the players turns
	 */
	private Queue<PlayerModel> _turns = new LinkedList<PlayerModel>();
	
	/**
	 * Flag indicating if the game is over
	 */
	private boolean _isGameOver = false;
	
	/**
	 * The view that this controller represents
	 */
	private BoardGameView _view = null;
	
	/**
	 * Flag indicating if the available position guide show be enabled
	 */
	// TODO - can we not have this and just have the menu contact the method directly
	// perhaps through chaining
	private boolean _showAvailablePositionGuides = false;
	
	/**
	 * Represents the grid size of our board
	 */
	// TODO - do we need this or can we create a model like a BoardModel that holds
	// attributes relevant to the board
	private int _gridSize = 0;
		
	/**
	 * Constructs a new object of this class
	 */
	private BoardGameController() {
		registerController();
		addView(new BoardGameView());
	}
	
	/**
	 * Construct a new object of this class
	 * 
	 * @param source The component to attach this controllers view to 
	 */
	public BoardGameController(JPanel source) {
		this();
		source.add(_view);
	}
	
	/**
	 * Registers this controller
	 */
	// TODO - remove once we have factory (and all other ones, i wont put a comment in the others unless it cant be done and
	// just in this one)
	private void registerController() {
		GameInstance.getInstance().registerController(this);
	}
	
	/**
	 * Gets the current players configuration
	 * 
	 * @return The current players configuration, such as if its
	 * a row or column configuration
	 */
	public String getCurrentPlayerConfig() {
		return _turns.peek().getPlayerConfiguration().toString();
	}
	
	/**
	 * Creates the player models based on the player dialog box
	 * 
	 * @param players The list of players from the dialog control
	 */
	public void createPlayers(PlayerSetupDialogBox[] players) {
		_players = new PlayerModel[players.length];
		for(int i = 0; i < players.length; ++i) {
			PlayerModel model = new PlayerModel(players[i]);
			_players[i] = model;
			model.addObserver(_view);
		}
		
		// Sort the array of player models, for convenience
		// purposes and then add them to the turns list
		Arrays.sort(_players);
		for(PlayerModel player : _players) {
			_turns.add(player);
		}
		
		// Set the grid size for this game to be played on
		_gridSize = PlayerSetupDialogBox.getGridSize();
	}
	
	/**
	 * Registers the views that wish to listen to the player models
	 * 
	 * @param controller The controllers we wish to use to register, we grab their views
	 */
	// TODO - can't we just pass an IView once it is created
	public void registerListener(ScoreBoardController controller) {
		for(PlayerModel model : _players) {
			model.addObserver(controller.getView());
		}	
	}
	
	/**
	 * Gets the list of available board positions
	 * 
	 * @param root The root of our panels
	 * 
	 * @return The list of available board positions
	 */
	public ArrayList<BoardPosition> getAvailableBoardPositions(JPanel root) {
		
		ArrayList<BoardPosition> positions = new ArrayList<BoardPosition>();
		Component[] components = root.getComponents();
		
		for(Component component : components) {
			if(component instanceof BoardPosition) {
				populateNeighbouringPositions(_turns.peek().getPlayerConfiguration(), (BoardPosition)component, positions);
			}
		}
		
		return positions;
	}
	
	/**
	 * Gets the list of direct path associated neighbors based on a board position recursively 
	 * 
	 * @param configuration The configuration to go against such as a row or column based schema
	 * @param currentPosition The current position we are at
	 * @param allPositions The list of all the positions we are filling up
	 */
	private void populateNeighbouringPositions(Configuration configuration, BoardPosition currentPosition, ArrayList<BoardPosition> allPositions) {
		
		// If the currentPosition is not valid, is locked, or has already been looked at 
		// then stop processing
		if(currentPosition == null || currentPosition.isLocked() || allPositions.contains(currentPosition)) {
			return;
		}
		
		// Based on the configuration try and get the two neighboring 
		// positions and try to add them
		if(configuration == Configuration.ROW) {
			BoardPosition east = currentPosition.getNeighbourRight();
			BoardPosition west = currentPosition.getNeighbourLeft();
			if((east != null && !east.isLocked()) || (west != null && !west.isLocked())) {
				
				// Add our position
				allPositions.add(currentPosition);
				
				populateNeighbouringPositions(configuration, east, allPositions);
				populateNeighbouringPositions(configuration, west, allPositions);	
			}
		} else if(configuration == Configuration.COLUMN) {
			BoardPosition north = currentPosition.getNeighbourTop();
			BoardPosition south = currentPosition.getNeighbourBottom();
			if((north != null && !north.isLocked()) || (south != null && !south.isLocked())) {
				
				// Add our position
				allPositions.add(currentPosition);
				
				populateNeighbouringPositions(configuration, north, allPositions);
				populateNeighbouringPositions(configuration, south, allPositions);	
			}
		}
	}
		
	/**
	 * Validates if a position specified is valid for use 
	 * 
	 * @param child The position inside of our root
	 *
	 * @return If the position is valid
	 */
	public boolean isValidPosition(BoardGameView.BoardPosition child) {
		if(getIsGameOver()) {
			return false;
		}
		
		// Get the player playing
		PlayerModel player = _turns.peek();
		if(player != null && player.getTokens() > 0) {
			
			// Get how many tokens are left
			int tokensSpent = PlayerModel.maxToken() - player.getTokens();
			if(tokensSpent == 0) { // no moves make
				return true;
			} else if(tokensSpent > 0) { // at least one move made
				
				boolean isValid = false;
				
				// Based on the player configuration, fetch its neighbours accordingly
				Configuration configuration = player.getPlayerConfiguration();
				if(configuration == Configuration.COLUMN) {
					BoardGameView.BoardPosition north = child.getNeighbourTop();
					BoardGameView.BoardPosition south = child.getNeighbourBottom();
					
					if(north != null && !north.isLocked() && north.getOwner() == getCurrentPlayerIdentification()) {
						isValid = true;
					} 
					
					if(south != null && !south.isLocked() && south.getOwner() == getCurrentPlayerIdentification()) {
						isValid = true;
					}
					
				} else if(configuration == Configuration.ROW) {
					BoardGameView.BoardPosition east = child.getNeighbourRight();
					BoardGameView.BoardPosition west = child.getNeighbourLeft();
	
					if(east != null && !east.isLocked() && east.getOwner() == getCurrentPlayerIdentification()) {
						isValid = true;
					} 
					
					if(west != null && !west.isLocked() && west.getOwner() == getCurrentPlayerIdentification()) {
						isValid = true;
					}
				}
				return isValid;
			}	
		}
		
		GameInstance.getInstance().addLog("Invalid token position...");
		return false;
	}
	
	/**
	 * Gets the path of the players token, used for rendering
	 * 
	 * @return The players token path
	 */
	public String getPlayerToken() {
		return _turns.peek().getTokenPath();
	}
	
	/**
	 * Gets the current players' player identification tag 
	 * 
	 * @return The current players identification
	 */
	public int getCurrentPlayerIdentification() {
		return _turns.peek().getPlayerIdentification();
	}
	
	/**
	 * Cycles the players
	 */
	private void nextPlayer() {
		_turns.add(_turns.poll());
		GameInstance.getInstance().addLog("Player " + _turns.peek().getName() + " is now up");
	}
	
	/**
	 * Initiates the dialog component for setting up the players
	 *
	 * @return The result of the dialog box
	 */
	public boolean startPlayerSetup() {	
		boolean done = _view.showPlayerDialog();
		if(done) {
			_view.render();
		}
		return done;
	}
	
	/**
	 * Updates the players tokens in a specific direction
	 * 
	 * @param direction 
	 * 					less than 0 to reduce the tokens by 
	 * 					greater than 0 to increase the tokens by 1
	 */
	public void updatePlayerTokens(int direction) {
		if(getIsGameOver()) {
			return;
		}
		
		if(direction < 0) {
			_turns.peek().removeToken();
		} else if(direction > 0) {
			_turns.peek().addToken();
		}
		GameInstance.getInstance().addLog("Player " + _turns.peek().getName() + " has " + _turns.peek().getTokens() + " tokens remaining now");
		if(_turns.peek().getTokens() == 0) {
			GameInstance.getInstance().addLog("No more tokens remaining for this round...");
		}
	}
	
	/**
	 * Gets the size of the grid, easier than getting all the components and 
	 * getting its size.  If the size changes dynamically then just remove this 
	 * and get the list of components
	 * 
	 * @return The size of the grid in one dimension
	 */
	public int getGridSize() {
		return _gridSize;
	}
	
	public boolean getPlayerHasAI(int pID) {
		PlayerModel player = _turns.peek();
		if(player != null && player.getPlayerIdentification() == pID && player.getHasAI()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Adds a view to this controller
	 * 
	 * @param view The view to add
	 */
	private void addView(BoardGameView view) {
		if(_view == null) {
			_view = view;
			_view.addController(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) { }

	/**
	 * Performs a move based on the tokens that the player has entered
	 * 
	 * @param root The game panel
	 */
	public void performMove(JPanel root) {
		
		// Make sure the game isn't finished before proceeding
		if(getIsGameOver()) {
			return;
		}
		
		PlayerModel player = _turns.peek();
		if(player != null) {
			
			if(player.getHasAI()) {
				GameInstance.getInstance().addLog("TODO - AI Player Move");
			} else if(player.getTokens() > 0) {
				GameInstance.getInstance().addLog("There are still tokens left to play, please use them all up.");
			} else {	
			    Component[] components = root.getComponents();
			    for (int i = 0; i < components.length; i++) {
			    	if(components[i] instanceof BoardPosition) {
			    		BoardPosition position = (BoardPosition) components[i];
			    		if(position.getOwner() == player.getPlayerIdentification()) {
			    			position.finalize();
			    		}
			    	}	
			    }	
			    player.resetTokens();
			    nextPlayer();
			    ArrayList<BoardPosition> positions = getAvailableBoardPositions(root);
			    if(positions.size() == 0) {
			    	GameInstance.getInstance().addLog("Player " + _turns.peek().getName() + " has lost the game because there are no more moves left");
			    	GameInstance.getInstance().addLog("Player " + player.getName() + " has won the game");
			    	JOptionPane.showOptionDialog(
			    			_view, 
	    	    		 	"Player " + player.getName() + " has won the game",
			    			"Game Over", 
			    			JOptionPane.PLAIN_MESSAGE, 
			    			JOptionPane.INFORMATION_MESSAGE, 
			    			null, 
			    			new Object[]{"OK"}, 
			    			null);
					_isGameOver = true;
			    }
			    
			    if(_showAvailablePositionGuides) {
			    	refreshGuides(root);
			    }
			    _turns.peek().refresh();
			}
		}
	}
	
	/**
	 * Gets if the game is over
	 * @return
	 */
	private boolean getIsGameOver() {
		return _isGameOver;
	}

	/**
	 * Populates the positions background guides based on if the position is playable
	 * 
	 * @param show Show the helper guides or not since this is a toggle function
	 */
	public void populatePlayerGlobalGuides(boolean show) {
		if(getIsGameOver()) {
			return;
		}
		
		_showAvailablePositionGuides = show;
		if(!show) {
			clearGuides(_view.getGamePanel());
		} else {
		    ArrayList<BoardPosition> positions = getAvailableBoardPositions(_view.getGamePanel());
		    for(BoardPosition position : positions) {
		    	position.setBackground(Color.LIGHT_GRAY);
		    }
		}
	}	
	
	/**
	 * Refreshes the guides after moves have been made
	 * 
	 * @param root The root of our game
	 */
	private void refreshGuides(JPanel root) {
		if(_showAvailablePositionGuides) {
			clearGuides(root);
			populatePlayerGlobalGuides(true);
		}
	}
	
	/**
	 * Clears the guides
	 * 
	 * @param root The root of our game
	 */
	private void clearGuides(JPanel root) {
		Component[] components = root.getComponents();
	    for (int i = 0; i < components.length; i++) {
	    	if(components[i] instanceof BoardPosition) {
	    		BoardPosition position = (BoardPosition) components[i];
	    		position.setBackground(UIManager.getColor("Panel.background"));
	    	}	
	    }	
	}

	/**
	 * Gets if the player is done playing
	 * 
	 * @param pID The player id
	 * 
	 * @return If the player is done playing
	 */
	public boolean isPlayerDone(int pID) {
		if(getCurrentPlayerIdentification() == pID)
		{
			return _turns.peek().getTokens() == 0;
		}
		
		return false;
	}

	/**
	 * Gets the setup representation of our players
	 * 
	 * @return The setup representation of our players
	 */
	public PlayerSetupDialogBox[] getPlayersSetup() {
		PlayerSetupDialogBox[] players = new PlayerSetupDialogBox[_players.length];
		for(int i = 0; i < players.length; ++i) {
			players[i] = _players[i].getPlayerSetup();
		}
		return players;
	}
	
	/**
	 * Gets the setup representation of a single player
	 * 
	 * @param pID The players identification
	 * 
	 * @return The player setup
	 */
	public PlayerSetupDialogBox getPlayerSetup(int pID) {
		PlayerSetupDialogBox[] players = getPlayersSetup();
		for(PlayerSetupDialogBox player : players) {
			if(player.getPID() == pID) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * Gets the tokens of the player
	 * 
	 * @param pID The player identification
	 * 
	 * @return The number of tokens that the player currently holds
	 */
	public int getPlayerTokens(int pID) {
		if(getCurrentPlayerIdentification() == pID) {
			return _turns.peek().getTokens(); 
		}
		return 0;
	}
	
	/*
	private ArrayList<BoardPosition> populateNeighbouringPositions(BoardPosition position, Configuration configuration) {
	
		ArrayList<BoardPosition> positions = new ArrayList<BoardGameView.BoardPosition>();
		if(position != null && configuration != null) {
			if(configuration == Configuration.ROW) {
				positions.add(position.getNeighbourLeft());
				positions.add(position.getNeighbourRight());
			} else if(configuration == Configuration.COLUMN) {
				positions.add(position.getNeighbourTop());
				positions.add(position.getNeighbourBottom());				
			}
		}
		return positions;
	}
	*/
	public void populatePlayerLocalGuides(BoardPosition position) {

		return;/*
		PlayerModel player =  _turns.peek();
		
		if(player != null && position != null && position.getOwner() == player.getPlayerIdentification()) {	
			ArrayList<BoardPosition> children = populateNeighbouringPositions(position, player.getPlayerConfiguration());
			if(children != null) {
				
				ArrayList<BoardPosition> availablePositions = getAvailableBoardPositions((JPanel)position.getParent());
				for(BoardPosition child : children) {
					if(child != null && availablePositions.contains(child)) {
						Border border = child.getBorder();
						if(border instanceof MatteBorder) {
							Insets insets = ((MatteBorder)border).getBorderInsets();
							child.setBorder(new MatteBorder(
									insets.top,
									insets.left,
									insets.bottom,
									insets.right,
									Color.RED));
						}
					}
				}
			}
			*/
		//}
	}
}