package mainline.models;

public class PlayerModel {
	
	private final Team _team;
	private int _score = 0;

	public enum Team {
		
		PlayerX("resources_marker_x"),
		PlayerY("resources_marker_o");
		
		public final String _tokenName;
		public final String _tokenPath;
		
		private Team(String tokenName) {
			_tokenName = tokenName;
			_tokenPath = "/resources/" + _tokenName + ".png";
		}
	}
	
	public PlayerModel(Team team) { 
		_team = team;
	}
			
	public String getTeamName() { return _team.name(); }
	public String getTokenPath() { return _team._tokenPath; }
	public int getWins() { return _score; }
	public void incrementWins() { ++_score; }
	
	@Override public String toString()
	{
		return _team.name() + ": " + _score;
	}
}