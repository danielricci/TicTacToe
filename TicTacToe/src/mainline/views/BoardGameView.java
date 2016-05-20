/**
* Daniel Ricci <thedanny09@gmail.com>
*
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without restriction,
* including without limitation the rights to use, copy, modify, merge,
* publish, distribute, sublicense, and/or sell copies of the Software,
* and to permit persons to whom the Software is furnished to do so, subject
* to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
* THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
* IN THE SOFTWARE.
*/

package mainline.views;

import java.awt.Color;
import java.awt.Component;
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

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import mainline.controllers.BoardGameController;

@SuppressWarnings("serial")
public final class BoardGameView extends JPanel {
	
	private BoardGameController _controller = null;
	//private JPanel _actionPanel = new JPanel();
	private final ScoreboardView _scoreboardView = new ScoreboardView();
	private final JPanel _gamePanel = new JPanel(new GridBagLayout());	
		
	public BoardGameView() {	
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		_gamePanel.setBackground(Color.GRAY);
	}
	
	public class BoardPosition extends JPanel {

	    private Image _image = null;
	    private boolean _locked = false;
	    
	    private BoardPosition _left = null;
	    private BoardPosition _top = null;
	    private BoardPosition _right = null;
	    private BoardPosition _bottom = null;	    
	    
	    public BoardPosition() {	
	    	addMouseListener(new MouseAdapter() { 
	    		
	    		@Override public void mouseEntered(MouseEvent e) {
	    			
	    			if(_controller.isGameOver())
	    			{
	    				return;
	    			}
	    			
	    			Object source = e.getSource();
	    			if(source instanceof BoardPosition) {
	    				BoardPosition position = (BoardPosition)source;
	    				
	    				if(!_locked) {
	    					position.setBackground(Color.LIGHT_GRAY);
	    					try {
								_image = new ImageIcon(position.getClass().getResource(_controller.getPlayerToken())).getImage();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
	    				}
    				
						position.repaint();
	    			}
	    		}
		
	    		@Override public void mouseExited(MouseEvent e) {
	    			
	    			if(_controller.isGameOver())
	    			{
	    				return;
	    			}
    			
    				if(!_locked)
    				{
    					_image = null;	
    				}
    				setBackground(UIManager.getColor("Panel.background"));
	    		}
	    		
	    		@Override public void mouseClicked(MouseEvent e) {
					
	    			if(_controller.isGameOver())
	    			{
	    				return;
	    			}
	    			
					// Get the board that we selected to give our event handler some context
					BoardPosition position = (BoardPosition)e.getSource();
				
					// If the player can play and there is no selection yet
					// then take ownership of the position and put our token
					if(!_locked) {
						try {
							_image = new ImageIcon(position.getClass().getResource(_controller.getPlayerToken())).getImage();
							_controller.performMove(e);
							position._locked = true;
							
							if(_controller.isGameOver())
							{
								_controller.updateScore();
							}
							
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					position.repaint();
				}
			});
		}
	    
	    public void addLeft(BoardPosition position) { _left = position; }
	    public void addTop(BoardPosition position) { _top = position; }
	    public void addRight(BoardPosition position) { _right = position; }
	    public void addBottom(BoardPosition position) { _bottom = position; }
	    	    
	    public BoardPosition getNeighbourTop() { return _top; }
	    public BoardPosition getNeighbourBottom() { return _bottom; }
	    public BoardPosition getNeighbourLeft() { return _left; }
	    public BoardPosition getNeighbourRight() { return _right; }

	    @Override protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2d = (Graphics2D)g;
	        
	        Map<RenderingHints.Key, Object> hints = new HashMap<RenderingHints.Key, Object>();
	        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2d.addRenderingHints(hints);
	        g2d.drawImage(_image, 16, 16, 32, 32, null, null);       
		}
	
		@Override public Dimension getPreferredSize() {
		    return new Dimension(64, 64);
		}

		public boolean equals(BoardPosition bp) 
		{
			if(bp == null || bp == this)
			{
				return false;
			}
			
			return bp._image == _image;
		}

		public void reset() {
			_image = null;
			_locked = false;
			setBackground(UIManager.getColor("Panel.background"));
			repaint();
		}
	}
	
	public void addController(BoardGameController controller) {
		if(_controller == null) {
			_controller = controller;
		}
	}
	
	public void reload() {
		
		for(Component component : _gamePanel.getComponents())
		{
			if(component instanceof BoardPosition)
			{
				((BoardPosition)component).reset();
			}
		}
	}
	
	public void render() {
		// get the grid selection of our user control
		int gridSize = _controller.getGridSize();
		
		// Create a list of panels based on the n-by-n grid selection
		GridBagConstraints gbc = new GridBagConstraints();
		ArrayList<ArrayList<BoardPosition>> positions = new ArrayList<ArrayList<BoardPosition>>();
		for (int row = 0; row < gridSize; ++row) {
			
			ArrayList<BoardPosition> rowPositions = new ArrayList<BoardPosition>();
			for (int col = 0; col < gridSize; ++col) {
				gbc.gridx = col;
				gbc.gridy = row;
		
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
				BoardPosition position = new BoardPosition();
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
		
		_controller.updateScore();
		
		// Add our panels
		add(_scoreboardView);
		add(_gamePanel);
	}

	public ScoreboardView getScoreboard() {
		return _scoreboardView;
	}
}