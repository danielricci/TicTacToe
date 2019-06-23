package game;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;

import game.controllers.BoardGameController;
import game.controllers.MainWindowController;

public final class GameInstance extends JFrame {
	
	private static GameInstance _instance = null;

	private final ArrayList<Object> _controllers = new ArrayList<Object>();
	
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
	
	
	public void newGame() {
		if(_controllers.size() > 0) {
			BoardGameController controller = (BoardGameController)getController("BoardGameController");
			controller.reload();						
		}
		else {
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