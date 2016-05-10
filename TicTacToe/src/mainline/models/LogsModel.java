package mainline.models;

import java.util.ArrayList;
import java.util.Observable;

/**
 * This model represents all the log information that will happen in the 
 * application.
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class LogsModel extends Observable {
	
	/**
	 * The list of logs
	 */
	private ArrayList<String> _logs = new ArrayList<String>();

	/**
	 * Adds a new log to the list of logs
	 * 
	 * @param text The text to add
	 */
	public void addLog(String text) {
		if(text != null) {
			_logs.add(_logs.size() + 1 + ":\t" + text);
			update(_logs.get(_logs.size() - 1));
		}
	}
	
	/**
	 * Updates the listeners with the new log
	 * 
	 * @param returnValue The new log that was entered
	 */
	private void update(Object returnValue) {
		setChanged();
		notifyObservers(returnValue); 
	}

	/**
	 * Clears the logs
	 */
	public void clearLogs() {
		
		// Clear the logs
		_logs.clear();
		
		// Notify listeners that the log is null, meaning 
		// that it should be cleared; this is to differentiate 
		// from adding empty lines which is entirely possible 
		// and sometimes necessary
		update(null);
	}
}