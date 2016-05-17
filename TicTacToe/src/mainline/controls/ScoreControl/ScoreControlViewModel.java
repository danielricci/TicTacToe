package mainline.controls.ScoreControl;

import java.util.ArrayList;
import java.util.List;

import mainline.models.PlayerModel;

public class ScoreControlViewModel {

	private final List<PlayerModel> _players;
	
	public ScoreControlViewModel(List<PlayerModel> players)
	{
		_players = players;
	}	
}