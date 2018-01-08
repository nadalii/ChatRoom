package server;

import java.awt.*;

/**
 * Created by luke shen on 1/8/18.
 */
public class ServerLogin {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ServerView window = new ServerView();
                    window.getFrame().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
