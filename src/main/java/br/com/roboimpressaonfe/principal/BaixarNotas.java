package br.com.roboimpressaonfe.principal;

import javax.swing.JFrame;

public class BaixarNotas {

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public BaixarNotas() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
