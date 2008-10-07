package rpiplanner;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainFrame extends JFrame {

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame
	 */
	public MainFrame() {
		super();
		getContentPane().setLayout(new CardLayout());
		setBounds(100, 100, 566, 452);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JPanel introCard = new JPanel();
		introCard.setLayout(new BorderLayout());
		introCard.setName("introCard");
		getContentPane().add(introCard, introCard.getName());

		final JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				CardLayout l = (CardLayout)getContentPane().getLayout();
				l.next(getContentPane());
			}
		});
		button.setText("Continue");
		introCard.add(button, BorderLayout.SOUTH);

		final JLabel welcomeToRpiLabel = new JLabel();
		welcomeToRpiLabel.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeToRpiLabel.setText("Welcome to RPI Planner!");
		introCard.add(welcomeToRpiLabel);

		final JPanel planCard = new JPanel();
		planCard.setLayout(new BorderLayout());
		planCard.setName("planCard");
		getContentPane().add(planCard, planCard.getName());

		final JLabel realtimeStatusHereLabel = new JLabel();
		realtimeStatusHereLabel.setText("Real-time status here");
		planCard.add(realtimeStatusHereLabel, BorderLayout.EAST);

		final JPanel titlePanel = new JPanel();
		planCard.add(titlePanel, BorderLayout.NORTH);

		final JLabel rpiPlannerLabel = new JLabel();
		rpiPlannerLabel.setText("RPI Planner");
		titlePanel.add(rpiPlannerLabel);

		final JPanel planPanel = new JPanel();
		planCard.add(planPanel);

		final JLabel semesterPlanGoesLabel = new JLabel();
		semesterPlanGoesLabel.setText("Semester plan goes here");
		planPanel.add(semesterPlanGoesLabel);

		final JLabel courseSearchHereLabel = new JLabel();
		courseSearchHereLabel.setText("Course search here");
		planCard.add(courseSearchHereLabel, BorderLayout.WEST);

		final JLabel statusAndOtherLabel = new JLabel();
		statusAndOtherLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statusAndOtherLabel.setText("Status and other information");
		planCard.add(statusAndOtherLabel, BorderLayout.SOUTH);
		
		final JMenuBar menu = new JMenuBar();
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem fileSave = new JMenuItem("Save");
		fileSave.setEnabled(false);
		fileMenu.add(fileSave);
		menu.add(fileMenu);
		this.setJMenuBar(menu);
		//
	}

}
