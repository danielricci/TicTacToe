/**
* Daniel Ricci <thedanny09@gmail.com>
*
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without restriction,
* including without limitation the rights to use, copy, modify, merge,
* publish, distribute, sublicense, and/or sell copies of the Software,
* and to permit persons to whom the Software is furnished to do so, subject
* to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
* THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
* IN THE SOFTWARE.
*/

package mainline.views;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import mainline.models.PlayerModel;

@SuppressWarnings("serial")
public class ScoreboardView extends JPanel {

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