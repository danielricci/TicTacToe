package mainline.controllers;

import javax.swing.JPanel;

import mainline.views.GameManagerView;

/**
 * Manages the manager views that track the game
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class GameManagerController {

	/**
	 * The view associated to this controller
	 */
	private GameManagerView _view = null;
	
	/**
	 * Constructs a new instance of this class
	 */
	public GameManagerController() {
		addView(new GameManagerView());
	}
	
	/**
	 * Constructs a new instance of this class
	 * 
	 * @param source The component to link this controllers' view to
	 */
	public GameManagerController(JPanel source) {
		this();	
		source.add(_view);
		_view.render();
	}
	
	/**
	 * Adds the view to this controller
	 * 
	 * @param view The view to add to this controller
	 */
	private void addView(GameManagerView view) {
		if(_view == null) {
			_view = view;
			_view.addController(this);
			_view.render();
		}
	}

	/**
	 * Gets the view associated to this controller
	 * 
	 * @return The view associated to this controller
	 */
	public GameManagerView getView() {
		return _view;
	}
}