package mainline.views;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import mainline.controllers.BoardGameController;
import mainline.controllers.MainWindowController;

/**
 *  The view directly associated to the frame of the application; this view is the 
 *  classical body tag of this application if it were an HTML document.
 *  
 *  This view is in charge of setting up the initial structure that the other views will 
 *  follow and will determine its formatting
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
@SuppressWarnings("serial")
public class MainWindowView extends JPanel {
	
	private MainWindowController _mainWindowController = null;	
	private BoardGameController _boardGameController = null;
	private JPanel _gamePanel = new JPanel(new BorderLayout());
    
    public MainWindowView() {
		super(new GridLayout());
		this.add(_gamePanel);
	}
		
	private void populateData() {
		_boardGameController = new BoardGameController(_gamePanel);
	}
	
	/**
	 * Adds the direct controller of this view
	 * 
	 * @param controller The controller to add
	 */
	public void addController(MainWindowController controller) {
		if(_mainWindowController  == null) {
			_mainWindowController = controller;
		}
	}	
	
	/**
	 * Renders this view
	 */
	public void render() {
		populateData();
	}
}