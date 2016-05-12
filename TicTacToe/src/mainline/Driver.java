package mainline;

public class Driver {
	public static void main(String[] argv) {
        try {
        	GameInstance.getInstance().setVisible(true);
        } catch (Exception exception) {
        	System.out.println(exception.getStackTrace());
        }
    }
}