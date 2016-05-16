package mainline.controllers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JPanel;

import mainline.GameInstance;
import mainline.models.PlayerModel;
import mainline.models.PlayerModel.Team;
import mainline.views.BoardGameView;
import mainline.views.BoardGameView.BoardPosition;

public class BoardGameController {

	private BoardGameView _view = null;
	private boolean _isGameOver = false;
	private int _gridSize = 0;
	
	private final ArrayList<PlayerModel> _players = new ArrayList<PlayerModel>();	
	private final Queue<PlayerModel> _turns = new LinkedList<PlayerModel>();
			
	// TODO - This should be removed
	private BoardGameController() {
		registerController();
		addView(new BoardGameView());
		
		_players.add(new PlayerModel("", Team.PlayerX));
		_players.add(new PlayerModel("", Team.PlayerY));
		
		for(PlayerModel player : _players) {
			_turns.add(player);
		}

		_gridSize = 3;	
	}
	
	public BoardGameController(JPanel source) {
		this();
		source.add(_view);
	}
	
	private void registerController() {
		GameInstance.getInstance().registerController(this);
	}
		
	/**
	 * Determines if we have a winner based on the currently loaded player and the passed in position
	 * and if we do, those positions will be returned
	 * 
	 * @param position The current position that has been played
	 * 
	 * @return The list of winning positions
	 */
	public boolean isWinningPosition(BoardPosition position) {
		return isWinningRow(position) || isWinningColumn(position) || isWinningDiagonal(position);
	}
	
	public boolean isGameOver() { return _isGameOver; }
	
	private boolean isWinningRow(BoardPosition position)
	{
		int rowSize = _gridSize - 1;
		
		BoardPosition tempLeft = position;
		while((tempLeft = tempLeft.getNeighbourLeft()) != null)
		{
			if(tempLeft.equals(position))
			{
				--rowSize;
			}
		}
		
		BoardPosition tempRight = position;
		while((tempRight = tempRight.getNeighbourRight()) != null)
		{
			if(tempRight.equals(position))
			{
				--rowSize;
			}
		}
		
		assert rowSize > -1 : "Winning position invalid, something went wrong with the grid size";
		return rowSize == 0;
	}
	
	private boolean isWinningColumn(BoardPosition position)
	{
		int colSize = _gridSize - 1;
		
		BoardPosition tempTop = position;
		while((tempTop = tempTop.getNeighbourTop()) != null)
		{
			if(tempTop.equals(position))
			{
				--colSize;
			}
		}
		
		BoardPosition tempBottom = position;
		while((tempBottom = tempBottom.getNeighbourBottom()) != null)
		{
			if(tempBottom.equals(position))
			{
				--colSize;
			}
		}
		
		assert colSize > -1 : "Winning position invalid, something went wrong with the grid size";
		return colSize == 0;
	}
	
	private boolean isWinningDiagonal(BoardPosition position)
	{
		return false;
	}
	
	public String getPlayerToken() { return _turns.peek()._tokenPath; }
	
	private void nextPlayer() {
		_turns.add(_turns.poll());
	}
	
	/**
	 * Initiates the dialog component for setting up the players
	 *
	 * @return The result of the dialog box
	 */
	public void execute() {	
		_view.render();		
	}
		
	public int getGridSize() { return _gridSize; }
	
	/**
	 * Adds a view to this controller
	 * 
	 * @param view The view to add
	 */
	private void addView(BoardGameView view) {
		if(_view == null) {
			_view = view;
			_view.addController(this);
		}
	}

	/**
	 * Performs a move based on the tokens that the player has entered
	 * 
	 * @param root The game panel
	 */
	public void performMove(java.awt.event.InputEvent event) {
		if(!(_isGameOver || isWinningPosition((BoardPosition)event.getSource()))) {
			nextPlayer();
		} else {
			System.out.println("We have a winner!");
			_isGameOver = true;
		}	
	}
}