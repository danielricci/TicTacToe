package mainline.views;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import mainline.models.PlayerModel;

public class ScoreboardView extends JPanel {

	public int PlayerX = 0;
	public int PlayerY = 0;
	
	public final JLabel Label = new JLabel();
		
	public ScoreboardView() {
		add(Label);
	}
	
	public void update(ArrayList<PlayerModel> _players) {
		String content = "";

		for(PlayerModel model : _players)
		{
			content += model.toString() + "<br />";
		}
		Label.setText("<html>" + content + "</html>");
	}
}