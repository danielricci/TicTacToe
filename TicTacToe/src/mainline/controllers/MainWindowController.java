package mainline.controllers;

import javax.swing.JFrame;

import mainline.views.MainWindowView;

/**
 * The controller representing the main window view
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * 
 */
public class MainWindowController {

	/**
	 * The view associated to this controller
	 */
	private MainWindowView _view = null;
	
	/**
	 * Constructs a new object of this class
	 * 
	 * @param source The frame associated to this controllers' view
	 */
	public MainWindowController(JFrame source) {
		addView(new MainWindowView());
		source.add(_view);
	}
	
	/**
	 * Starts the game
	 */
	public void startGame() {
		_view.render();
	}

	/**
	 * Adds the view associated to this controller and links it to this controller
	 * 
	 * @param view The view for this controller
	 */
	private void addView(MainWindowView view) {
		if(_view == null) {
			_view = view;
			_view.addController(this);
		}
	}
}