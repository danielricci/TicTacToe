package mainline.models;

import java.awt.Color;
import java.util.Observable;

/**
 * 
 * The model that represents a player, used as an observable
 * object to the views listening in on this class
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class PlayerModel extends Observable implements Comparable<PlayerModel> {
	
	private static final int _totalTokens = 2;
	private static int playerIdentifiers = 1;

	private int _pID = 0;	
	private int _tokens = _totalTokens;	
	private boolean _hasAI = false;
	
	private Team _team = null; 

	private String _tokenPath = "/resources";
	private String _name = null;
		
	/**
	 * The players team representation
	 * 
	 * @author Daniel Ricci <thedanny09@gmail.com>
	 *
	 */
	public enum Team {
		
		WHITE(new Color(255, 255, 255)),
		BLACK(new Color(0, 0, 0));
		
		/**
		 * The configuration representation of the team
		 * 
		 * @author Daniel Ricci <thedanny09@gmail.com>
		 *
		 */
		public enum Configuration {
			ROW,
			COLUMN;
			
			/**
			 * The toString of this enum
			 */
			@Override
			public String toString() {
				return this.name().toLowerCase();
			}
		}
		
		/**
		 * The color of the team
		 */
		private Color _team = null;
		
		/**
		 * Constructs a new type for the team
		 * 
		 * @param team The team color
		 */
		private Team(Color team) {
			_team = team;
		}
		
		/**
		 * Gets the teams color
		 * 
		 * @return The teams color
		 */
		public Color getColor() {
			return _team;
		}
		
		/**
		 * Gets the configuration of this team
		 * 
		 * @return The configuration of this team
		 */
		public Configuration getConfiguration() {
			if(this == Team.WHITE) {
				return Configuration.COLUMN;
			} else {
				return Configuration.ROW;
			}
		}
	}
	
	/**
	 * Gets the players identification number
	 * 
	 * @return The players identification number
	 */
	public int getPlayerIdentification() {
		return _pID;
	}
	
	public PlayerModel(String name, String team) {
		
		_name = name;
		_team = Team.valueOf(team.toUpperCase());
		
		// Setup the identification of the player
		if(_team == Team.WHITE) {
			_pID = 1;
		} else {
			_pID = ++playerIdentifiers;
		}

		// Loads the token path
		_tokenPath += "/" + _team.name().toLowerCase() + "_peg.png";
	}
	
	/**
	 * Gets the maximum number of tokens allowed
	 * 
	 * @return The maximum number of tokens allowed
	 */
	public static int maxToken() {
		return _totalTokens;
	}
	
	/**
	 * Gets the token path of this player
	 * 
	 * @return the token path of this player
	 */
	public String getTokenPath() {
		return _tokenPath;
	}
	
	/**
	 * Resets this player model 
	 */
	public void resetTokens() {
		_tokens = _totalTokens;
		update();
	}
	
	/**
	 * Adds a token to this player w.r.t the maximum number of tokens 
	 * allowed
	 */
	public void addToken() {
		_tokens = Math.min(_tokens + 1, _totalTokens);
		update();
	}
	
	/**
	 * Removes a token from this player w.r.t the minimum number of tokens
	 * allowed
	 */
	public void removeToken() {
		_tokens = Math.max(0, _tokens - 1);
		update();
	}

	/**
	 * Gets the number of tokens that this player currently has
	 * 
	 * @return The number of tokens that this player currently has
	 */
	public int getTokens() {
		return _tokens; 
	}
	
	/**
	 * The toString of this class
	 */
	@Override
	public String toString() {
		return _hasAI ? "AI" : "Human";
	}
	
	/**
	 * Gets the name of this player
	 * 
	 * @return The name of this player
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Gets the team configuration of this player
	 * 
	 * @return The team configuration of this player
	 */
	public Team.Configuration getPlayerConfiguration() {
		return _team.getConfiguration();
	}
	
	/**
	 * Gets the team name of this player
	 * 
	 * @return The team name of this player
	 */
	public String getTeamName() {
		return _team.name();
	}
	
	/**
	 * Gets the team color of this player
	 * 
	 * @return The team color of this player
	 */ 
	public Color getTeamColor() {
		return _team.getColor();
	}
	
	/**
	 * Gets if the player has AI
	 * 
	 * @return If the player has AI
	 */
	public boolean getHasAI() {
		return _hasAI;
	}
	
	/**
	 * Performs an update operation on this player model in terms
	 * of notifying its listeners
	 * 
	 * Note: This uses the identification as a 'soft reference' so 
	 * you would use this identification with the controller to 
	 * perform model operations.  This is done such that the view will
	 * never be coupled to the model.
	 */
	private void update() {
		setChanged();
		notifyObservers(getPlayerIdentification()); 
	}
	
	/**
	 * Implements the compare to of being able to compare
	 * two player models.  This is used mainly for sorting reasons
	 * so it should not be used to compare if two player models
	 * are the same
	 */
	@Override
	public int compareTo(PlayerModel playerModel) {
		// TODO - re-write this to properly do a comparison
		if(this._pID == 1) {
			return -1;
		} 
		if(this._pID > playerModel._pID) {
			return 1;
		}
		return 0; 
	}

	public void refresh() {
		update();
	}	
}