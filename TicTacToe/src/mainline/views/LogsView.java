package mainline.views;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;

import mainline.controllers.LogsController;

/**
 * The view for showing the logs that have been entered throughout this
 * application
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
@SuppressWarnings("serial")
public class LogsView extends JPanel implements Observer {

	// The inner panel associated to this view
	private JPanel _panel = new JPanel();
	
	/**
	 * The controller associated to this view
	 */
	private LogsController _controller = null;
	
	// TODO - this should be in the controller and it should 
	// hold models not strings
	/**
	 * The list of log messages that this view holds
	 */
    private JList<String> _list = new JList<String>();
    
    /**
     * The visual model formatter associated to the list
     */
    private DefaultListModel<String> _listModel = new DefaultListModel<String>();
    
    /**
     * The tab for this view
     */
    private JTabbedPane tab = new JTabbedPane();
    
    /**
     * Constructs a new object of this class
     */
    public LogsView() {
    	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    	_panel.setLayout(new BoxLayout(_panel, BoxLayout.Y_AXIS));
    }
    
	@Override
	public void update(Observable o, Object arg) {
		
		// If its null we assume it means to reset
		if(arg == null) {
			_listModel.clear();
		} else {
			_listModel.addElement(arg.toString());
			
			// Makes sure that newly added text is always visible
			_list.ensureIndexIsVisible(_list.getModel().getSize()-1);
		}
	}
	
	/**
	 * Renders this view
	 */
	public void render() {
		populateData();
	}
	
	/**
	 * Populates the data of this view
	 */
	private void populateData() {
		
		// Set up the list with its list model and some other
		// formatting and initial configuration options
        _list.setModel(_listModel);
        _list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _list.setLayoutOrientation(JList.VERTICAL);
        
        // Set the scroll option to our list
        JScrollPane listScroller = new JScrollPane(_list);
        
        // Set up the clearing button of our logs
		JButton button = new JButton("Clear Logs");
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				_controller.clearLogs();
			}
		});
		
		// Add our components
        tab.addTab("Game Logger", _panel);
		_panel.add(listScroller);
		_panel.add(button);
		add(tab);
		
		// Validate and repaint this view
		validate();
        repaint();        
	}
	
	/**
	 * Adds a controller to this view
	 * 
	 * @param controller The controller to add
	 */
	public void addController(LogsController controller) {
		if(_controller == null) {
			_controller = controller;
		}
	}
}