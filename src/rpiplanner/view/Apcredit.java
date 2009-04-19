package rpiplanner.view;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Feb 24, 2009
 * Time: 6:42:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class Apcredit {
    private JPanel tests;
    private JPanel buttons;
    private JComboBox Score;
    private JComboBox Test;
    public JButton AddApButton;
    private JButton CancelButton;
    private JButton OkButton;
    private JPanel panel1;


    public Apcredit() {
        //AddApButton.addActionListener(new ActionListener() {
        //  public void actionPerformed(final ActionEvent e) {
        //   System.out.println("This does something.");
        //   }
        // });
        CancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.out.println("this also does something.");
            }
        });
        OkButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
            }
        });
        AddApButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 2;
                c.gridheight = 1;
                c.gridwidth = 1;
                tests.add(new JButton("foo"), c);
            }

        });
    }

    public static void main(String[] args) {
        final JFrame window = new JFrame("Title");
        window.setBounds(100, 100, 500, 400);
        window.getContentPane().setLayout(new BorderLayout());
        window.getContentPane().add(new Apcredit().panel1, BorderLayout.CENTER);
        window.setVisible(true);
    }

}

