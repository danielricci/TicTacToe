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

	private MainWindowView _view = null;
	
	public MainWindowController(JFrame source) {
		addView(new MainWindowView());
		source.add(_view);
	}
	
	public void startGame() {
		_view.render();
	}

	private void addView(MainWindowView view) {
		if(_view == null) {
			_view = view;
			_view.addController(this);
		}
	}
}