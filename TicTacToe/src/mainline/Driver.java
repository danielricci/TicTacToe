package mainline;

/**
 * This is the driver program that will instantiate a process
 * of our application
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * 
 */
public class Driver {

        /**
         * This function is the main function of execution for
         * this application
         * 
         * @param argv The arguments that are to be interacted upon
         */
        public static void main(String[] argv) {

                // Create an instance of our game and set the layout
                GameInstance game = GameInstance.getInstance();
                try {
                	game.setVisible(true);
                } catch (Exception exception) {
                	System.out.println(exception.getStackTrace());
                }
        }
}