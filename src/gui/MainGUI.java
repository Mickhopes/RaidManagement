package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

public class MainGUI extends JFrame {
	static final int HEIGHT = 600;
	static final int WIDTH = 800;

	public MainGUI(String title) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// MenuBar
		JMenuBar menuBar = new JMenuBar();
		
		// JMenu Fichier
		JMenu menuFichier = new JMenu("Fichier");
		JMenuItem menuItemOpen = new JMenuItem("Ouvrir");
		menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));

		menuFichier.add(menuItemOpen);
		menuBar.add(menuFichier);
		
		// JMenu Aide
		JMenu menuAide = new JMenu("Aide");
		JMenuItem menuItemAPropos = new JMenuItem("À propos...");

		menuAide.add(menuItemAPropos);
		menuBar.add(menuAide);
		
		setJMenuBar(menuBar);

		// Création des onglets
		JTabbedPane onglets = new JTabbedPane(SwingConstants.TOP);

		LootPanel lootPanel = new LootPanel(this);

		onglets.add("Loot", lootPanel);
		onglets.add("Composition", new JPanel());
		add(onglets);

		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		pack();
	}
}
