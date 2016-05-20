package mainline;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import mainline.controllers.BoardGameController;
import mainline.controllers.MainWindowController;

@SuppressWarnings("serial")
public final class GameInstance extends JFrame {
	
	private static GameInstance _instance = null;

	private final ArrayList<Object> _controllers = new ArrayList<Object>();
	private final JMenuBar _menu = new JMenuBar();
	
	private GameInstance() {
		super("Tic-Tac-Toe");
		setSize(new Dimension(400, 400));
		setMaximizedBounds(new Rectangle(getSize()));
		setResizable(false);
		setMaximumSize(getSize());
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(
			(int)screenSize.getWidth() / 2 - getSize().width / 2,
			(int)screenSize.getHeight() / 2 - getSize().height / 2
		);

		SetWindowedInstanceListeners();
		SetWindowedInstanceMenu();
	}
	
	public void registerController(Object object) {
		if(!_controllers.contains(object)) {
			_controllers.add(object);
		}
	}
	
	public Object getController(String type) {
		for(Object controller : _controllers) {
			String split[] = controller.getClass().getName().split("\\.");
			if(split[split.length - 1].equals(type)) {
				return controller;
			}
		}
		return null;
	}

	public static GameInstance getInstance() {
		if(_instance == null) {
			_instance = new GameInstance();
		}
		return _instance;
	}
	
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
	
	private void SetWindowedInstanceMenu() {
		 PopulateFileMenu(_menu);
		 PopulateOptionsMenu(_menu);
		 setJMenuBar(_menu);
	}
	
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
	
					if(_controllers.size() > 0)
					{
						BoardGameController controller = (BoardGameController)getController("BoardGameController");
						controller.reload();						
					}
					else
					{
		        		// Clears all references to our controller that this
		        		// instance may hold
		        		_controllers.clear();
		        		
		        		// Removes any lingering panels without having to worry
		        		// about who owns what
		        		getContentPane().removeAll();
		        		
		        		// Create a new controller and start the game
		        		MainWindowController controller = new MainWindowController(getInstance());
		        		registerController(controller);
		        		
		        		controller.startGame();
		        		
	        			validate();						
					}
	
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
	
	private void PopulateOptionsMenu(JMenuBar menu) {
		// Create the file menu 
		JMenu optionsMenu = new JMenu("Options");
		optionsMenu.setMnemonic('O');
        
        // Set the event handler
        JMenuItem optionsMenuRepository = new JMenuItem(new AbstractAction("Reset Score") {
        	
        	@Override
			public void actionPerformed(ActionEvent event) {
        		BoardGameController controller = ((BoardGameController)getController("BoardGameController"));
        		if(controller != null)
        		{
        			controller.resetScore();			
        		}
			}	
        });
        optionsMenu.add(optionsMenuRepository);
        
        menu.add(optionsMenu);
	}
}