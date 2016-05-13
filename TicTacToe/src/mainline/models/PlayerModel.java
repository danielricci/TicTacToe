package mainline.models;

import java.awt.Color;
import java.util.Observable;

public class PlayerModel extends Observable implements Comparable<PlayerModel> {
	
	public final String _tokenPath;
	public final Team _team;
	public final String _name;

	public enum Team {
		
		PlayerX(new Color(255, 255, 255), "resources_marker_x"),
		PlayerY(new Color(0, 0, 0), "resources_marker_o");
		
		public final Color _team;
		public final String _tokenName;
		
		private Team(Color team, String tokenName) {
			_team = team;
			_tokenName = tokenName;
		}
	}
	
	public PlayerModel(String name, Team team) { 
		_name = name;
		_team = team;
		_tokenPath = "/resources/" + _team._tokenName + ".png";
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
		//notifyObservers(getPlayerIdentification()); 
	}
	
	public void refresh() {
		update();
	}

	@Override
	public int compareTo(PlayerModel o) {
		return 0;
	}	
}