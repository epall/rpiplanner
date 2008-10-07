/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpiplanner;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.jdesktop.application.Application;
import javax.swing.*;

/**
 *
 * @author epall
 */
public class Main extends Application{
    private JFrame mainFrame;

    @Override
    protected void startup() {
        mainFrame = new MainFrame();
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                mainFrame.setVisible(false);
                exit();
            }
        });
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
