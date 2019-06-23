package game;

public class Driver {
	public static void main(String[] argv) {
    	GameInstance.getInstance().setVisible(true);
    	GameInstance.getInstance().newGame();
    }
}