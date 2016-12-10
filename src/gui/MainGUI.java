package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import business.Loot;

public class MainGUI extends JFrame {
	public enum State {LOOT, COMPO};
	static final int HEIGHT = 600;
	static final int WIDTH = 800;
	
	private State state;
	
	private File saveFile;

	public MainGUI(String title) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		LootPanel lootPanel = new LootPanel(this);
		state = State.LOOT;
		saveFile = null;

		// MenuBar
		JMenuBar menuBar = new JMenuBar();

		// JMenu Fichier
		JMenu menuFichier = new JMenu("Fichier");
		
		JMenuItem menuItemSave = new JMenuItem("Sauvegarder");
		menuItemSave.setEnabled(false);
		menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuItemSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(state) {
					case LOOT:
						if (lootPanel.getlLoot().isEmpty() || saveFile == null) {
							JOptionPane.showMessageDialog(MainGUI.this,
									"Aucun loot à sauvegarder !",
									"Attention", JOptionPane.WARNING_MESSAGE);
							return;
						}

						try (
							FileOutputStream out = new FileOutputStream(saveFile);
							ObjectOutputStream oos = new ObjectOutputStream(out);
						) {
							oos.writeObject(lootPanel.getlLoot());
						} catch (IOException ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(MainGUI.this,
									"Erreur lors de la sauvegarde du fichier :\n" + ex.getMessage(),
									"Erreur", JOptionPane.ERROR_MESSAGE);
						}
						break;
					case COMPO:
						break;
					default:
						break;
				}
			}
		});
		
		JMenuItem menuItemSaveUnder = new JMenuItem("Sauvegarder sous...");
		menuItemSaveUnder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(state) {
					case LOOT:
						if (lootPanel.getlLoot().isEmpty()) {
							JOptionPane.showMessageDialog(MainGUI.this,
									"Aucun loot à sauvegarder !",
									"Attention", JOptionPane.WARNING_MESSAGE);
							return;
						}
						
						String fileName = new SimpleDateFormat("'loots_'dd-MM-yy", Locale.FRENCH).format(lootPanel.getlLoot().get(0).getDateLoot());
						
						final JFileChooser fc = new JFileChooser();
						fc.setSelectedFile(new File(fileName));
						int r = fc.showSaveDialog(MainGUI.this);

						if (r == JFileChooser.APPROVE_OPTION) {
							File fichier = fc.getSelectedFile();
							try (
								FileOutputStream out = new FileOutputStream(fichier);
								ObjectOutputStream oos = new ObjectOutputStream(out);
							) {
								oos.writeObject(lootPanel.getlLoot());
								saveFile = fichier;
								menuItemSave.setEnabled(true);
							} catch (IOException ex) {
								ex.printStackTrace();
								JOptionPane.showMessageDialog(MainGUI.this,
										"Erreur lors de la sauvegarde du fichier :\n" + ex.getMessage(),
										"Erreur", JOptionPane.ERROR_MESSAGE);
							}
						}
						break;
					case COMPO:
						break;
					default:
						break;
				}
			}
		});
		
		JMenuItem menuItemLoad = new JMenuItem("Charger...");
		menuItemLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuItemLoad.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(state) {
					case LOOT:
						final JFileChooser fc = new JFileChooser();
						int r = fc.showOpenDialog(MainGUI.this);

						if (r == JFileChooser.APPROVE_OPTION) {
							File fichier = fc.getSelectedFile();
							try (
								FileInputStream in = new FileInputStream(fichier);
								ObjectInputStream ois = new ObjectInputStream(in);
							) {
								lootPanel.setlLoot((ArrayList<Loot>)ois.readObject());
								saveFile = fichier;
								menuItemSave.setEnabled(true);
							} catch (IOException | ClassNotFoundException ex) {
								ex.printStackTrace();
								JOptionPane.showMessageDialog(MainGUI.this,
										"Erreur lors du chargement du fichier :\n" + ex.getMessage(),
										"Erreur", JOptionPane.ERROR_MESSAGE);
							}
						}
						break;
					case COMPO:
						break;
					default:
						break;
				}
			}
		});
		
		JMenuItem menuOptions = new JMenuItem("Options...");
		menuOptions.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						new SettingsGUI("Options", MainGUI.this).setVisible(true);
					}
				});
			}
		});

		JMenuItem menuItemQuit = new JMenuItem("Quitter");
		menuItemQuit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});

		menuFichier.add(menuItemLoad);
		menuFichier.add(menuItemSave);
		menuFichier.add(menuItemSaveUnder);
		menuFichier.addSeparator();
		menuFichier.add(menuOptions);
		menuFichier.addSeparator();
		menuFichier.add(menuItemQuit);
		menuBar.add(menuFichier);

		// JMenu Aide
		JMenu menuAide = new JMenu("Aide");

		JMenuItem menuItemAPropos = new JMenuItem("À propos...");
		menuItemAPropos.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainGUI.this,
						"Application réalisée par Adragonà-Uldaman.\n\nTéléchargement: https://github.com/Mickhopes/RaidManagement/releases\nSource code: https://github.com/Mickhopes/RaidManagement",
						"À propos", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		menuAide.add(menuItemAPropos);
		menuBar.add(menuAide);

		setJMenuBar(menuBar);

		// Création des onglets
		JTabbedPane onglets = new JTabbedPane(SwingConstants.TOP);

		onglets.add("Loot", lootPanel);
		onglets.add("Composition", new JPanel());
		onglets.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if (onglets.getSelectedIndex() == 0) {
					state = State.LOOT;
				} else {
					state = State.COMPO;
				}
			}
		});

		add(onglets);

		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setLocationRelativeTo(null);
		pack();
	}
}
