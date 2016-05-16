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
			
	public String getTeamName() {
		return _team.name();
	}

	private void update() {
		setChanged();
	}
	
	public void refresh() {
		update();
	}

	@Override
	public int compareTo(PlayerModel o) {
		return 0;
	}	
}