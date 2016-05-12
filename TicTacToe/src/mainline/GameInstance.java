package mainline;


import java.awt.Desktop;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import mainline.controllers.BoardGameController;
import mainline.controllers.LogsController;
import mainline.controllers.MainWindowController;

/**
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 * This class initializes the main window environment along with all
 * functionalities attached to it, including but not limited to menu systems
 * and other bindings and handles how the application as a whole should be managed
 */
@SuppressWarnings("serial")
public final class GameInstance extends JFrame {
	
	private static GameInstance _instance = null;

	//TODO - Off-load this to the controller factory
	/**
	 * Holds the list of registered controllers
	 */
	private ArrayList<Object> _controllers = new ArrayList<Object>();
	
	private JMenuBar _menu = new JMenuBar();
	
	//TODO - can we rid this and just reference it by name
	JCheckBoxMenuItem _viewMenuShowPlayablePositions = null;
	
	private GameInstance() {
		// TODO - put this in config file
		super("COMP 6721 W15 - Game of Ganji-Ho");
		setExtendedState(Frame.MAXIMIZED_BOTH);
		SetWindowedInstanceListeners();
		SetWindowedInstanceMenu();
	}
	
	// TODO this needs to be replaced with proper factory
	/**
	 * Registers a controller to the game instance
	 * 
	 * @param object The object to register
	 */
	public void registerController(Object object) {
		if(!_controllers.contains(object)) {
			_controllers.add(object);
		}
	}
	
	// TODO this needs to be replaced with proper factory
	/**
	 * Gets the controller that has been registered
	 * 
	 * @param type The type of the controller to get
	 * 
	 * @return The controller
	 */
	public Object getController(String type) {
		for(Object controller : _controllers) {
			if(controller.getClass().getName().equals(type)) {
				return controller;
			}
		}
		return null;
	}
	
	// TODO - this should go into the configuration area
	// TODO - fetch from the factory controller
	/**
	 * Adds a log text to the log controller
	 * 
	 * @param text The text to add
	 */
	public void addLog(String text) {
		if(text != null) {
			Object controller = getController(LogsController.class.getName());
			if(controller instanceof LogsController) {
				((LogsController) controller).addLog(text);
			}
		}
	}

	/**
	 * Gets the the singleton instance of this class
	 * 
	 * @return MainWindowInstance
	 */
	public static GameInstance getInstance() {
		if(_instance == null) {
			_instance = new GameInstance();
		}
		return _instance;
	}
	
	/**
	 * Sets the listener event handlers for this frame
	 */
	private void SetWindowedInstanceListeners() {
		
		// Needed to manually handle closing of the window
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		// Add a listener to whenever the window is closed
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				int response= JOptionPane.showConfirmDialog(null, "Are you sure that you wish to exit the game?", "Exit Game", JOptionPane.YES_NO_OPTION);
				if(response == JOptionPane.YES_OPTION) {
					dispose();
				}
			}
		});
	}
	
	/**
	 * Sets the window instance menu and all of its
	 * items
	 */
	private void SetWindowedInstanceMenu() {
		 PopulateFileMenu(_menu);
		 PopulateViewMenu(_menu);
		 PopulateHelpMenu(_menu);
		 setJMenuBar(_menu);
	}
	
	/**
	 * Populates the file menu header
	 * 
	 * @param menu The menu to attach to
	 */
	private void PopulateFileMenu(JMenuBar menu) {

		// Create the file menu 
		JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
			        
        // Set the event handler
        JMenuItem fileMenuNew = new JMenuItem(new AbstractAction("New") { 
        	
        	@Override
        	public void actionPerformed(ActionEvent event) {	
        		int response= JOptionPane.showConfirmDialog(null, "Starting a new game will cancel any current game in progress, are you sure?", "New Game", JOptionPane.YES_NO_OPTION);
				if(response == JOptionPane.YES_OPTION) {
					
					// Reset our guidelines option
					_viewMenuShowPlayablePositions.setSelected(false);
					
	        		// Clears all references to our controller that this
	        		// instance may hold
	        		_controllers.clear();
	        		
	        		// Removes any lingering panels without having to worry
	        		// about who owns what
	        		getContentPane().removeAll();
	        		
	        		// Create a new controller and start the game
	        		 MainWindowController _controller = new MainWindowController(getInstance());
	        		_controller.startGame();
	        		validate(); 
				}
			}	
        });

        // Set the shortcut
        fileMenuNew.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.add(fileMenuNew);
        fileMenu.addSeparator();
        
        // Set the event handler
        JMenuItem fileMenuExit = new JMenuItem(new AbstractAction("Exit") {
        	
        	@Override
        	public void actionPerformed(ActionEvent event) {
        		_instance.dispatchEvent(new WindowEvent(_instance, WindowEvent.WINDOW_CLOSING));
			}	
		});
        fileMenu.add(fileMenuExit);
        menu.add(fileMenu);
	}	
	
	/**
	 * Populates the view menu
	 * 
	 * @param menu The menu to attach to
	 */
	private void PopulateViewMenu(JMenuBar menu) {
		// Create the file menu 
		JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic('V');
        
        // Set the event handler
        _viewMenuShowPlayablePositions = new JCheckBoxMenuItem(new AbstractAction("Show Playable Positions") {
        	
        	@Override
			public void actionPerformed(ActionEvent event) {
				AbstractButton aButton = (AbstractButton) event.getSource();		        
	        	BoardGameController controller = (BoardGameController)getController(BoardGameController.class.getName());
	        	if(controller == null) {
	        		aButton.setSelected(false);
	        		return;
	        	}	   	
	       		//controller.populatePlayerGlobalGuides(aButton.getModel().isSelected());
	        }	
        });
        _viewMenuShowPlayablePositions.setAccelerator(KeyStroke.getKeyStroke('P', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        viewMenu.add(_viewMenuShowPlayablePositions);
        
        menu.add(viewMenu);
	}

	/**
	 * Populates the help menu
	 * 
	 * @param menu The menu to attach to
	 */
	private void PopulateHelpMenu(JMenuBar menu) {
		// Create the file menu 
		JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        
        // Set the event handler
        JMenuItem helpMenuRepository = new JMenuItem(new AbstractAction("Repository") {
        	
        	@Override
			public void actionPerformed(ActionEvent event) {
				Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			        try {
			        	// TODO - get this value from the Config file
			            desktop.browse(new URI("https://github.com/danielricci/comp472_6721"));
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
			    }
			}	
        });
        helpMenuRepository.setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        helpMenu.add(helpMenuRepository);
        
        menu.add(helpMenu);
	}
}