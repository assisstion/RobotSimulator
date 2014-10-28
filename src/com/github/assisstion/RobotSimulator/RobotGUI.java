package com.github.assisstion.RobotSimulator;

import java.awt.EventQueue;

import javax.swing.JFrame;
//import javax.swing.JPanel;

public class RobotGUI extends JFrame{

	public static final int width = 640;
	public static final int height = 640;

	private static final long serialVersionUID = -7326816031863491944L;
	//private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run(){
				try{
					RobotGUI frame = new RobotGUI();
					frame.setVisible(true);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RobotGUI(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, width, height);
		//contentPane = new JPanel();
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		//contentPane.setLayout(new BorderLayout(0, 0));
		//setContentPane(contentPane);

		RobotCanvas rc = new RobotCanvas();
		setContentPane(rc);
		rc.repaint();
	}

}
