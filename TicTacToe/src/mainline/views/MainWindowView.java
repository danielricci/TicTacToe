package mainline.views;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import mainline.controllers.BoardGameController;
import mainline.controllers.MainWindowController;

@SuppressWarnings("serial")
public final class MainWindowView extends JPanel {

	// TODO - why the hell do we have controllers in our view!
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