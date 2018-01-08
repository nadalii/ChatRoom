package client;

import java.awt.*;

/**
 * Created by luke shen on 1/8/18.
 */
public class ClientLogin {
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientLoginView window = new ClientLoginView();
                    window.getFrame().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
