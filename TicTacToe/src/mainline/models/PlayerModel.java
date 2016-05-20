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

	public void resetWins() {
		_score = 0;
	}
}