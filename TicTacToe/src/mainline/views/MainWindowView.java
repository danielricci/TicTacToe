package mainline.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import mainline.controllers.BoardGameController;
import mainline.controllers.GameManagerController;
import mainline.controllers.MainWindowController;

/**
 *  The view directly associated to the frame of the application; this view is the 
 *  classical body tag of this application if it were an HTML document.
 *  
 *  This view is in charge of setting of the initial structure that the other views will 
 *  follow and will determine its formatting
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
@SuppressWarnings("serial")
public class MainWindowView extends JPanel {
	
	/**
	 * Reference to its direct controller
	 */
	private MainWindowController _mainWindowController = null;	
	
	/**
	 * Secondary reference to the board game controller
	 */
	private BoardGameController _boardGameController = null;
	
	/**
	 * Secondary reference to the game manager controller
	 */
	private GameManagerController _gameManagerController = null;
	
	/**
	 * The game panel associated to this view
	 */
    private JPanel _gamePanel = new JPanel(new BorderLayout());
    
    /**
     * The splitter for this view
     */
    private JSplitPane _mainWindowViewSplitter = null;
    
    /**
     * Constructs a new object of this class
     */
	public MainWindowView() {
		super(new GridLayout());
		
		// Set up the splitter for this view
		_mainWindowViewSplitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		_mainWindowViewSplitter.setVisible(false);
		_mainWindowViewSplitter.setEnabled(false);
		_mainWindowViewSplitter.setResizeWeight(0.7); 
		_mainWindowViewSplitter.setDividerLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().width * .70));
		_mainWindowViewSplitter.setUI(new BasicSplitPaneUI() {
			
			@Override
            public BasicSplitPaneDivider createDefaultDivider() {
	            return new BasicSplitPaneDivider(this) {
	        
	            	/**
	            	 * We do this to control how the rendering should be done for this splitter
	            	 */
	            	@Override
	                public void paint(Graphics graphics) {
	            		graphics.setColor(Color.BLACK);
	            		graphics.fillRect(0, 0, getSize().width / 2, getSize().height);
	                    super.paint(graphics);
	                }
	            };
            }
        });
		
		// Add the splitter
		add(_mainWindowViewSplitter);		
	}
		
	/**
	 * Populates this view
	 */
	private void populateData() {
		
		// Set the left side of this view
		_mainWindowViewSplitter.setLeftComponent(_gamePanel);
		_boardGameController = new BoardGameController(_gamePanel);
		
		// Call the player creation process
		if(_boardGameController.startPlayerSetup()) {
			_gameManagerController = new GameManagerController();
			_mainWindowViewSplitter.setRightComponent(_gameManagerController.getView());
			_mainWindowViewSplitter.setVisible(true);
		}
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