package mainline.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import mainline.GameInstance;
import mainline.models.LogsModel;
import mainline.views.LogsView;

/**
 * The controller managing the logs view
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class LogsController implements ActionListener {
	
	/**
	 * The view associated to this controller
	 */
	private LogsView _view = null;
	
	/**
	 * The model that this controller is managing
	 */
	private LogsModel _model = null;
	
	/**
	 * Instantiates a new object of this class
	 */
	// TODO - make this protected soon once we can accept Components in our overloader
	public LogsController() {
		registerController();		
		addView(new LogsView());
		registerModels();
	}
	
	/**
	 * Instantiates a new object of this class
	 * 
	 * @param source The source to link this controllers' view to
	 */
	public LogsController(JPanel source) {
		this();
		source.add(_view);
	}
	
	/**
	 * Registers the models of this controller
	 */
	public void registerModels() {
		_model = new LogsModel();
		_model.addObserver(_view);
	}
	
	/**
	 * Registers this controller
	 */
	private void registerController() {
		GameInstance.getInstance().registerController(this);
	}
	
	/**
	 * Gets the view associated to this controller
	 * 
	 * @return The view associated to this controller
	 */
	public LogsView getView() {
		return _view;
	}
	
	/**
	 * Adds text to the log structure
	 * 
	 * @param text The text to add
	 */
	public void addLog(String text) {
		_model.addLog(text);
	}
	
	/**
	 * Adds the view to this controller
	 * 
	 * @param view The view to associate to this controller
	 */
	private void addView(LogsView view) {
		if(_view == null) {
			_view = view;
			_view.addController(this);
			_view.render();
		}
	}

	public void actionPerformed(ActionEvent event) { }

	/**
	 * Clears the logs of all its text
	 */
	public void clearLogs() {
		_model.clearLogs();
	}
}