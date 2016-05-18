package mainline.views;

import java.awt.BorderLayout;
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

		for(int i = 0; i < _players.size(); ++i)
		{
			content += _players.get(i).toString();
			if(i + 1 < _players.size())
				content += "<br />";
		}
		
		Label.setText("<html>" + content + "</html>");
	}
}