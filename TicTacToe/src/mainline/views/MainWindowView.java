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

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import mainline.controllers.BoardGameController;
import mainline.controllers.MainWindowController;

@SuppressWarnings("serial")
public final class MainWindowView extends JPanel {

	private MainWindowController _mainWindowController = null;	
	private BoardGameController _boardGameController = null;
	
	public final JPanel _gamePanel = new JPanel(new BorderLayout());
    
    public MainWindowView() {
		super(new GridLayout());
		this.add(_gamePanel);
	}
	
	public void addController(MainWindowController controller) {
		if(_mainWindowController  == null) {
			_mainWindowController = controller;
		}
	}	
		
	public void render() {
		_boardGameController = new BoardGameController(_gamePanel);
		_boardGameController.execute();
	}
}