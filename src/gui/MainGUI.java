package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
		
		LootPanel lootPanel = new LootPanel(this);

		// MenuBar
		JMenuBar menuBar = new JMenuBar();

		// JMenu Fichier
		JMenu menuFichier = new JMenu("Fichier");

		JMenuItem menuItemOpen = new JMenuItem("Ouvrir");
		menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuItemOpen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				lootPanel.ouvrirFichier();
			}
		});

		JMenuItem menuItemQuit = new JMenuItem("Quitter");
		menuItemQuit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});

		menuFichier.add(menuItemOpen);
		menuFichier.addSeparator();
		menuFichier.add(menuItemQuit);
		menuBar.add(menuFichier);

		// JMenu Aide
		JMenu menuAide = new JMenu("Aide");

		JMenuItem menuItemAPropos = new JMenuItem("� propos...");
		menuItemAPropos.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainGUI.this,
						"Application r�alis�e par Adragon�-Uldaman.\n\nT�l�chargement: https://github.com/Mickhopes/RaidManagement/releases\nSource code: https://github.com/Mickhopes/RaidManagement",
						"� propos", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		menuAide.add(menuItemAPropos);
		menuBar.add(menuAide);

		setJMenuBar(menuBar);

		// Cr�ation des onglets
		JTabbedPane onglets = new JTabbedPane(SwingConstants.TOP);

		onglets.add("Loot", lootPanel);
		onglets.add("Composition", new JPanel());
		add(onglets);

		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		pack();
	}
}
