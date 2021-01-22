package hr.fer.oprpp1.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Class PrimDemo represents a frame that prints out prime numbers in a row on click of a button.
 * @author leokiparje
 *
 */

public class PrimDemo extends JFrame {

	private static final long serialVersionUID = 1L;

	/*
	 * Basic constructor
	 */
	
	public PrimDemo() {
		setLocation(20, 50);
		setSize(300, 200);
		setTitle("PrimList");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		initGUI();
	}
	
	/*
	 * Method initGUI initializes the GUI used to project the prime number frame
	 */
	
	public void initGUI() {
		
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		PrimListModel<Integer> model = new PrimListModel<>();
		
		JList<Integer> list1 = new JList<>(model);
		JList<Integer> list2 = new JList<>(model);
		
		JPanel bottomPanel = new JPanel(new GridLayout(1, 0));

		JButton dodaj = new JButton("Dodaj");
		bottomPanel.add(dodaj);
		
		JButton obrisi = new JButton("Obriši");
		bottomPanel.add(obrisi);
		
		
		
		
		dodaj.addActionListener(e -> {
			model.add(model.next());
		});
		obrisi.addActionListener(e -> {
			int index = list1.getSelectedIndex();
			if(index != -1) {
				model.remove(index);
			}
		});
		
		JPanel central = new JPanel(new GridLayout(1, 0));
		central.add(new JScrollPane(list1));
		central.add(new JScrollPane(list2));
		
		cp.add(central, BorderLayout.CENTER);
		cp.add(bottomPanel, BorderLayout.PAGE_END);
	}
	
	/*
	 * Method main
	 */

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new PrimDemo();
			frame.pack();
			frame.setVisible(true);
		});
	}
}
