package mainline.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;
import java.util.Observable;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import mainline.controllers.BoardGameController;

/**
 * The view that authors the visual representation of the board and its components
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
@SuppressWarnings("serial")
public class BoardGameView extends JPanel implements Observer {
	
	/**
	 * The controller associated to this view
	 */
	private BoardGameController _controller = null;
	
	/**
	 * The individual tile positions for this view
	 */
	private JPanel _gamePanel = new JPanel(new GridBagLayout());
	
	/**
	 * The panel that holds the component actions
	 */
	private JPanel _actionPanel = new JPanel();
	
	/**
	 * Constructs a new object of this class
	 */
	public BoardGameView() {	
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	/**
	 * Gets the game panel of this view
	 */
	public JPanel getGamePanel() {
		return _gamePanel;
	}
	
	/**
	 * Displays the dialog component for the setup of our players
	 * 
	 * @return If the dialog is finished
	 */
	public boolean showPlayerDialog() {
		_controller.createPlayers();
		return true;
	}
	
	/**
	 * Populates the data of this view
	 */
	private void populateData() {
		
		// get the grid selection of our user control
		int gridSize = _controller.getGridSize();
		
		// Create a list of panels based on the n-by-n grid selection
		GridBagConstraints gbc = new GridBagConstraints();
		ArrayList<ArrayList<BoardPosition>> positions = new ArrayList<ArrayList<BoardPosition>>();
		for (int row = 0, letter = 65; row < gridSize; ++row, ++letter) {
			
			ArrayList<BoardPosition> rowPositions = new ArrayList<BoardPosition>();
			for (int col = 0; col < gridSize; ++col) {
				gbc.gridx = col;
				gbc.gridy = row;

				BoardPosition position = new BoardPosition((char)letter + "" + (col+1));
				Border border = null;
				if (row < (gridSize - 1)) {
					if (col < (gridSize - 1)) {
						border = new MatteBorder(1, 1, 0, 0, Color.BLACK);
					} else {
						border = new MatteBorder(1, 1, 0, 1, Color.BLACK);
					}
				} else {
					if (col < (gridSize - 1)) {
						border = new MatteBorder(1, 1, 1, 0, Color.BLACK);
					} else {
						border = new MatteBorder(1, 1, 1, 1, Color.BLACK);
					}
				}
            
				// Set the border and add the panel to our game panel
				position.setBorder(border);
				_gamePanel.add(position, gbc);
				
				
				// Links them as a row, this is done so that we can associate their neighbors
				// accordingly
				if(!rowPositions.isEmpty()) {
					rowPositions.get(rowPositions.size() - 1).addRight(position);
					position.addLeft(rowPositions.get(rowPositions.size() - 1));
				}
				rowPositions.add(position);
			}
			
			// if we have rows in our list
			if(!positions.isEmpty()) {
				
				// Get the last row that has been rendered and link them together by 
				// reference each others top and bottom.  Once this block gets executed
				// they will be able to reference each other as neighbors
				ArrayList<BoardPosition> previous = positions.get(positions.size() - 1);
				for(int i = 0; i < previous.size(); ++i) {
					previous.get(i).addBottom(rowPositions.get(i));
					rowPositions.get(i).addTop(previous.get(i));
				}
			}
			positions.add(rowPositions);
		}
	    // Add our panels
		add(_gamePanel);
		add(_actionPanel);
	}	
	
	/**
	 * Represents a board position in our game panel
	 * 
	 * @author Daniel Ricci <thedanny09@gmail.com>
	 * 
	 */
	public class BoardPosition extends JPanel {

	    private Image _image = null;
	    private int _pID = 0;	    
	    private boolean _isLocked = false;
	    private BoardPosition _left = null;
	    private BoardPosition _top = null;
	    private BoardPosition _right = null;
	    private BoardPosition _bottom = null;	    
	    
	    public BoardPosition(String coordinate) {	
	    	
	    	addMouseListener(new MouseAdapter() { 
	    		
	    		@Override public void mouseEntered(MouseEvent e) {
	    			Object source = e.getSource();
	    			if(source instanceof BoardPosition) {
	    				BoardPosition position = (BoardPosition)source;
	    				position.setBackground(Color.LIGHT_GRAY);	
	    			}
	    		}
	    		
	    		
	    		@Override public void mouseExited(MouseEvent e) {
	    			Object source = e.getSource();
	    			if(source instanceof BoardPosition) {
	    				BoardPosition position = (BoardPosition)source;
	    				position.setBackground(UIManager.getColor("Panel.background"));	
	    			}
	    		}
	    		
	    		@Override
				public void mouseClicked(MouseEvent e) {
					
					// Get the board that we selected to give our event handler some context
					BoardPosition position = (BoardPosition)e.getSource();
					if(position._isLocked) {
						return;
					}

					// If the player can play and there is no selection yet
					// then take ownership of the position and put our token
					if(_image == null) {
						try {
							System.out.println(System.getProperty("java.class.path"));
							System.out.println(_controller.getPlayerToken());
							_image = new ImageIcon(position.getClass().getResource(_controller.getPlayerToken())).getImage();
							_controller.performMove(_gamePanel);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					position.repaint();
				}
			});
		}
	    
	    /**
	     * Adds the left reference neighbor
	     * 
	     * @param position The position to add
	     */
	    public void addLeft(BoardPosition position) {
	    	_left = position;
	    }
	    
	    /**
	     * Adds the top reference neighbor
	     * 
	     * @param position The position to add
	     */
	    public void addTop(BoardPosition position) {
	    	_top = position;
	    }
	    
	    /**
	     * Adds the right reference neighbor
	     * 
	     * @param position The position to add
	     */
	    public void addRight(BoardPosition position) {
	    	_right = position;
	    }
	    
	    /**
	     * Adds the bottom reference neighbor
	     * 
	     * @param position The position to add
	     */
	    public void addBottom(BoardPosition position) {
	    	_bottom = position;
	    }
	    	    
	    /**
	     * Gets the top neighbor
	     * 
	     * @return The top neighbor
	     */
	    public BoardPosition getNeighbourTop() {
	    	return _top;
	    }
	    
	    /**
	     * Gets the bottom neighbor
	     * 
	     * @return The bottom neighbor
	     */
	    public BoardPosition getNeighbourBottom() {
	    	return _bottom;
	    }
	    
	    /**
	     * Gets the left neighbor
	     * 
	     * @return The left neighbor
	     */
	    public BoardPosition getNeighbourLeft() {
	    	return _left;
	    }
	    
	    /**
	     * Gets the right neighbor
	     * 
	     * @return The right neighbor
	     */
	    public BoardPosition getNeighbourRight() {
	    	return _right;
	    }
	    
	    /**
	     * Helper to reset this position
	     */
		private void reset() {
			_image = null;
			_pID = 0;
		}
		
		/**
		 * Gets the owner of this position
		 * 
		 * @return The player associated to this position
		 */
		public int getOwner() {
			return _pID;
		}
		
		/**
		 * Finalizes this position  
		 */
		public void finalize() {
			_isLocked = true;
		}
		
		/**
		 * Gets the locked status of this position
		 * 
		 * @return if the position is locked
		 */
		public boolean isLocked() {
			return _isLocked;
		}
		
		@Override
		protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2d = (Graphics2D)g;
	        
	        Map<RenderingHints.Key, Object> hints = new HashMap<RenderingHints.Key, Object>();
	        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2d.addRenderingHints(hints);
	        //g2d.drawString(_coordinate, 4, 12);
	        g2d.drawImage(_image, 16, 16, 32, 32, null, null);       
		}
		
		@Override
		public Dimension getPreferredSize() {
		    return new Dimension(64, 64);
		}		
	}
	
	@Override
	public void update(Observable obs, Object object) 
	{ /*
		// Update the action button if the player has spent all tokens
		if(object instanceof Integer)
		{
			int pID = (Integer)object;
			if(pID > 0)
			{
				if(!_controller.getPlayerHasAI(pID)) {
					_playMove.setEnabled(true);
				} else {
					_playMove.setEnabled(_controller.isPlayerDone(pID));	
				}
			}
		}*/
	}		
		
	/**
	 * Adds a controller to this view
	 * 
	 * @param controller The controller to add
	 */
	public void addController(BoardGameController controller) {
		if(_controller == null) {
			_controller = controller;
		}
	}
	
	/**
	 * Gets the controller associated to this view
	 * 
	 * @return The controller associated to this view
	 */
	public BoardGameController getController() {
		return _controller;
	}
	
	/**
	 * Renders this view
	 */
	public void render() {
		populateData();
	}
}