package mainline.views;

import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import mainline.controllers.GameManagerController;
import mainline.controllers.LogsController;
import mainline.controllers.ScoreBoardController;

/**
 * The view that manages the secondary views that describe the game from a non-game play point of view
 * such as the player view and the logging view
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
@SuppressWarnings("serial")
public class GameManagerView extends JPanel {

	/**
	 * The main controller of this view
	 */
	private GameManagerController _gameManagerController = null;
	
	/**
	 * The split pane of this view
	 */
	private JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	 
	/**
	 * Constructs a new object of this class
	 */
	public GameManagerView() {
		
		// Set up the layout
		super(new GridLayout());

		// Set up our split pane
		splitPane.setResizeWeight(0.5); 
		splitPane.setDividerLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().height * .20));
		
		// Add the split pane to our panel
		add(splitPane);	
	}
		
	/**
	 * Populates the data of this view
	 */
	private void populateData() {
		
		// Create our player controller and set its main associated view
		// to the top part of this view
		// TODO - couldn't we just pass in the top part to this
		ScoreBoardController controller = new ScoreBoardController();
		splitPane.setTopComponent(controller.getView());
		
		// Create the log controller and set its view to the bottom
		// portion of our view
		// TODO - couldn't we just pass in the top part to this
		LogsController logsController = new LogsController();
		splitPane.setBottomComponent(logsController.getView());
	}	
		
	/**
	 * Adds a controller to this view
	 * 
	 * @param gameManagerController The controller to use for this view
	 */
	public void addController(GameManagerController gameManagerController) {
		if(_gameManagerController  == null) {
			_gameManagerController = gameManagerController;
		}
	}
	
	/**
	 * Renders this view
	 */
	public void render() {
		populateData();
	}
	
	/**
	 * Gets the controller associated to this view
	 * 
	 * @return The controller associated to this view
	 */
	public GameManagerController getController() {
		return _gameManagerController;
	}
}