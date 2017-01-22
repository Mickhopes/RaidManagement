package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import business.Loot;

public class LootPanel extends JPanel {
	static final String[] upgrades = { "Upgrade", "2 Part", "4 Part", "Mini-upgrade", "Spé2", "Transmo", "Passer", "Hors ligne" };

	private JFrame parent;

	private JPanel pLoot;
	private JTextField lNomFichier;
	private JCheckBox cTri;

	private File fichier;
	private ArrayList<Loot> lLoot;
	private ArrayList<JTextField> lIlvls;

	public LootPanel(JFrame parent) {
		this.parent = parent;

		// Initialisation des attributs
		lLoot = new ArrayList<>();
		lIlvls = new ArrayList<>();
		fichier = null;

		this.setLayout(new BorderLayout());

		// Création du panel de sélection de fichier à parser
		JPanel pSelection = new JPanel();
		pSelection.setLayout(new BoxLayout(pSelection, BoxLayout.X_AXIS));

		// Création du label d'indication du nom de fichier
		lNomFichier = new JTextField();
		lNomFichier.setEnabled(false);

		// Création du bouton de sélection de fichier à parser
		JButton bSelection = new JButton("Ouvrir...");
		bSelection.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ouvrirFichier();
			}

		});

		// Création du bouton pour parser le fichier
		JButton bParse = new JButton("Parser");
		bParse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (fichier != null) {
					parse();
				} else {
					JOptionPane.showMessageDialog(parent, "Vous devez sélectionner un fichier à parser", "Erreur",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		// Ajout des composants dans le panel de sélection de fichier
		pSelection.add(lNomFichier);
		pSelection.add(Box.createRigidArea(new Dimension(10, 0)));
		pSelection.add(bSelection);
		pSelection.add(Box.createRigidArea(new Dimension(10, 0)));
		pSelection.add(bParse);

		pSelection.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		this.add(pSelection, BorderLayout.NORTH);

		// Création du panel d'affichage des loots
		pLoot = new JPanel();
		pLoot.setLayout(new BoxLayout(pLoot, BoxLayout.Y_AXIS));

		// Création du panel pour scroller
		JScrollPane pScroll = new JScrollPane(pLoot);
		pScroll.setPreferredSize(new Dimension(pScroll.getWidth(), 400));
		pScroll.getVerticalScrollBar().setUnitIncrement(16);

		this.add(pScroll, BorderLayout.CENTER);

		// Création du panel d'ajout de loot et de génération des résultats
		JPanel pBas = new JPanel(new BorderLayout());

		JPanel pAjout = new JPanel();

		JButton bAjout = new JButton("Ajouter");
		bAjout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				afficherFenetreAjout();
			}

		});

		pAjout.add(bAjout);

		pBas.add(pAjout, BorderLayout.WEST);

		// Création du panel contenant les boutons de résultat
		JPanel pGenerer = new JPanel();

		cTri = new JCheckBox("Upgrade/2P/4P uniquement");
		cTri.setSelected(true);

		// Création du jbutton de génération du récap
		JButton bRecap = new JButton("Récap");
		bRecap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				genererRecap();
			}

		});

		// Création du jbutton de génération des points ninjacup
		JButton bPoints = new JButton("Points");
		bPoints.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				genererPoints();
			}

		});

		pGenerer.add(cTri);
		pGenerer.add(Box.createRigidArea(new Dimension(10, 0)));
		pGenerer.add(bRecap);
		pGenerer.add(Box.createRigidArea(new Dimension(10, 0)));
		pGenerer.add(bPoints);

		pBas.add(pGenerer, BorderLayout.EAST);

		pBas.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		this.add(pBas, BorderLayout.SOUTH);
	}

	private void afficherFenetreAjout() {
		// Création du panel de d'ajout
		JPanel pAjout = new JPanel();
		pAjout.setLayout(new BoxLayout(pAjout, BoxLayout.Y_AXIS));

		// Création du label de nom de joueur
		JPanel pNomJoueur = new JPanel(new BorderLayout());
		JLabel lNomJoueur = new JLabel("Pseudo * :");
		JTextField tNomJoueur = new JTextField("", 15);
		pNomJoueur.add(lNomJoueur, BorderLayout.WEST);
		pNomJoueur.add(tNomJoueur, BorderLayout.EAST);
		pNomJoueur.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		// Création du label de nom de boss
		JPanel pNomBoss = new JPanel(new BorderLayout());
		JLabel lNomBoss = new JLabel("Boss * :");
		JTextField tNomBoss = new JTextField("", 15);
		pNomBoss.add(lNomBoss, BorderLayout.WEST);
		pNomBoss.add(tNomBoss, BorderLayout.EAST);
		pNomBoss.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		// Création du label de nom d'item
		JPanel pNomItem = new JPanel(new BorderLayout());
		JLabel lNomItem = new JLabel("Item * :");
		JTextField tNomItem = new JTextField("", 15);
		pNomItem.add(lNomItem, BorderLayout.WEST);
		pNomItem.add(tNomItem, BorderLayout.EAST);
		pNomItem.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		// Création du label d'id d'item
		JPanel pItemId = new JPanel(new BorderLayout());
		JLabel lItemId = new JLabel("ID de l'item :");
		JTextField tItemId = new JTextField("", 15);
		pItemId.add(lItemId, BorderLayout.WEST);
		pItemId.add(tItemId, BorderLayout.EAST);
		pItemId.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		// Création du label du bonus de l'item
		JPanel pItemBonus = new JPanel(new BorderLayout());
		JLabel lItemBonus = new JLabel("Bonus de l'item :");
		JTextField tItemBonus = new JTextField("", 15);
		pItemBonus.add(lItemBonus, BorderLayout.WEST);
		pItemBonus.add(tItemBonus, BorderLayout.EAST);
		pItemBonus.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		// Création de la combobox pour le type d'upgrade
		JPanel pUp = new JPanel(new BorderLayout());
		JLabel lUp = new JLabel("Raison * :");
		JComboBox<String> cbUp = new JComboBox<>(upgrades);
		pUp.add(lUp, BorderLayout.WEST);
		pUp.add(cbUp, BorderLayout.EAST);
		pUp.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		// Création de la combobox pour la difficulte
		JPanel pDiff = new JPanel(new BorderLayout());
		JLabel lDiff = new JLabel("Difficulté * :");
		String[] diff = { "Normal", "Héroïque", "Mythique" };
		JComboBox<String> cbDiff = new JComboBox<>(diff);
		cbDiff.setPreferredSize(cbUp.getPreferredSize());
		pDiff.add(lDiff, BorderLayout.WEST);
		pDiff.add(cbDiff, BorderLayout.EAST);
		pDiff.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		
		// Création du textfield pour l'ilvl
		JPanel pIlvl = new JPanel(new BorderLayout());
		JLabel lIlvl = new JLabel("Ilvl de l'item *:");
		JTextField tIlvl = new JTextField("", 15);
		tIlvl.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
					e.consume();
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {

			}
			
			@Override
			public void keyPressed(KeyEvent e) {

			}
		});
		pItemId.add(lIlvl, BorderLayout.WEST);
		pItemId.add(tIlvl, BorderLayout.EAST);
		pItemId.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		// Création du bouton ajouter
		JButton bAjout = new JButton("Ajouter");
		bAjout.setEnabled(false);
		bAjout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane pane = getOptionPane((JComponent) e.getSource());
				pane.setValue(bAjout);
			}

		});

		// Création du bouton annuler
		JButton bAnnuler = new JButton("Annuler");
		bAnnuler.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane pane = getOptionPane((JComponent) e.getSource());
				pane.setValue(bAnnuler);
			}

		});

		// Création du KeyListener pour les labels
		KeyListener kl = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (tNomJoueur.getText().equals("") || tNomItem.getText().equals("") || tNomBoss.getText().equals("")) {
					bAjout.setEnabled(false);
				} else {
					bAjout.setEnabled(true);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

		};
		tNomJoueur.addKeyListener(kl);
		tNomItem.addKeyListener(kl);
		tNomBoss.addKeyListener(kl);

		// Ajout des composants dans le panel ajout
		pAjout.add(pNomJoueur);
		pAjout.add(pNomItem);
		pAjout.add(pItemId);
		pAjout.add(pItemBonus);
		pAjout.add(pNomBoss);
		pAjout.add(pDiff);
		pAjout.add(pUp);

		int r = JOptionPane.showOptionDialog(parent, pAjout, "Ajouter un loot", JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] { bAnnuler, bAjout }, bAjout);

		if (r == JOptionPane.NO_OPTION) {
			int itemId = -1;
			String bonus = "";

			if (!tItemId.getText().equals("")) {
				try {
					itemId = Integer.parseInt(tItemId.getText());
				} catch (NumberFormatException e) {
					itemId = -1;
				}
			}

			if (!tItemBonus.getText().equals("")) {
				bonus = tItemBonus.getText();
			}

			Loot l = new Loot(tNomJoueur.getText(), new Date(), tNomItem.getText(), itemId, bonus, tNomBoss.getText(),
					(String) cbDiff.getSelectedItem(), (String) cbUp.getSelectedItem(), false, Integer.parseInt(tIlvl.getText()));
			ajoutLoot(l);
		}
	}

	private void ajoutLoot(Loot l) {
		lLoot.add(l);
		Collections.sort(lLoot);

		refreshListe();
	}

	private void supprimerLoot(Loot l, JPanel panel) {
		lLoot.remove(l);

		refreshListe();
	}

	private void refreshListe() {
		// On enlève tout ce qu'on avait déjà
		pLoot.removeAll();
		pLoot.revalidate();
		pLoot.repaint();
		
		lIlvls.clear();

		for (Loot l : lLoot) {
			JPanel pl = new JPanel(new BorderLayout());
			pl.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			JPanel left = new JPanel();
			JPanel right = new JPanel();

			JTextField tNom = new JTextField(l.getJoueur(), 10);
			tNom.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					if (!tNom.getText().equals("")) {
						l.setJoueur(tNom.getText());
					}
				}

				@Override
				public void keyPressed(KeyEvent e) {
				}

			});

			JLabel lItem = new JLabel(l.getItem());
			lItem.setForeground(new Color(163, 48, 201));
			JLabel date = new JLabel(new SimpleDateFormat("dd/MM/yy", Locale.FRENCH).format(l.getDateLoot()));
			JLabel lBoss = new JLabel(l.getBoss() + "-" + l.getDifficulte());
			JComboBox<String> cbRaison = new JComboBox<>(upgrades);
			cbRaison.setSelectedItem(l.getRaison());
			cbRaison.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					l.setRaison((String) cbRaison.getSelectedItem());
				}

			});
			
			JTextField jIlvl = new JTextField(3);
			jIlvl.addKeyListener(new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent e) {
					if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
						e.consume();
					}
				}
				
				@Override
				public void keyReleased(KeyEvent e) {
					if (!jIlvl.getText().equals("")) {
						l.setIlvl(Integer.parseInt(jIlvl.getText()));
					}
				}
				
				@Override
				public void keyPressed(KeyEvent e) {

				}
			});
			if (l.getIlvl() != -1) {
				jIlvl.setText(String.valueOf(l.getIlvl()));
			}

			JButton bSup = new JButton("X");
			bSup.setForeground(Color.RED);

			bSup.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					supprimerLoot(l, pl);
				}

			});

			left.add(tNom);
			left.add(Box.createRigidArea(new Dimension(10, 0)));
			left.add(cbRaison);
			left.add(Box.createRigidArea(new Dimension(10, 0)));
			left.add(date);
			left.add(Box.createRigidArea(new Dimension(10, 0)));
			left.add(lBoss);
			if (l.isSet()) {
				left.add(Box.createRigidArea(new Dimension(10, 0)));
				left.add(jIlvl);
				lIlvls.add(jIlvl);
			} else {
				left.add(Box.createRigidArea(new Dimension(10, 0)));
				left.add(new JLabel(String.valueOf(l.getIlvl())));
			}
			left.add(Box.createRigidArea(new Dimension(10, 0)));
			left.add(lItem);

			right.add(bSup);

			pl.add(left, BorderLayout.WEST);
			pl.add(right, BorderLayout.EAST);

			pLoot.add(pl);
		}

		parent.pack();
	}

	private void parse() {
		try (
			FileInputStream fin = new FileInputStream(fichier);
			InputStreamReader inr = new InputStreamReader(fin);
			BufferedReader bufRead = new BufferedReader(inr);
		) {
			lLoot.clear();

			String line = null;
			String regex;
			int refNumber, refIlvl, ninjacupBase;
			
			// On récupère le fichier de propriétés
			try(
				FileInputStream fins = new FileInputStream(SettingsGUI.PROPERTY_FILE);
			) {
				Properties props = new Properties();
				props.load(fins);
				
				// On récupère les configurations
				regex = props.getProperty("loot.regex");
				refNumber = Integer.parseInt(props.getProperty("loot.refNumber"));
				refIlvl = Integer.parseInt(props.getProperty("loot.refIlvl"));
			} catch (FileNotFoundException ex) {
				// Si le fichier n'existe pas alors on récupère les infos dans notre fichier
				ResourceBundle bundle = ResourceBundle.getBundle("config");
				regex = bundle.getString("loot.regex");
				refNumber = Integer.parseInt(bundle.getString("loot.refNumber"));
				refIlvl = Integer.parseInt(bundle.getString("loot.refIlvl"));
			}
			
			Pattern pattern = Pattern.compile(regex);

			// On lit le fichier ligne par ligne
			while ((line = bufRead.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					// On récupère la date
					Date date = new SimpleDateFormat("dd/MM/yy hh:mm:ss", Locale.FRENCH).parse(matcher.group(2) + " " + matcher.group(3));

					// On check si on est dans le mode "Passer automatiquement"
					String raison = matcher.group(8);
					if (raison.startsWith("Passer")) {
						raison = "Passer";
					} else if (raison.startsWith("Hors ligne")) {
						raison = "Hors ligne";
					}
					
					boolean set;
					int ilvl;
					String bonus = "";
					if (matcher.group(5) == null || matcher.group(5).equals("")) {
						set = true;
						ilvl = -1;
					} else {
						set = false;
						ilvl = Integer.parseInt(matcher.group(5)) - refNumber + refIlvl;
						bonus = matcher.group(5);
					}
					
					
					// On récupère l'éventuel bonus de l'item
					if (matcher.group(4) != null && !matcher.group(4).equals("")) {
						bonus += ":" + matcher.group(4);
					}

					Loot loot = new Loot(matcher.group(1), date, matcher.group(6), Integer.parseInt(matcher.group(7)), bonus, matcher.group(10), matcher.group(9), raison, set, ilvl);

					lLoot.add(loot);
					Collections.sort(lLoot);
				}
			}

			refreshListe();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Erreur lors du parsage du fichier :\n" + e.getMessage(),
					"Erreur de parsage", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void genererRecap() {
		if (lLoot != null && !lLoot.isEmpty()) {
			boolean rempli = true;
			for(JTextField j : lIlvls) {
				if (j.getText().equals("")) {
					rempli = false;
					break;
				}
			}
			
			if (rempli) {
				int ninjacupBase;
				// On récupère le fichier de propriétés
				try(
					FileInputStream fins = new FileInputStream(SettingsGUI.PROPERTY_FILE);
				) {
					Properties props = new Properties();
					props.load(fins);
					
					// On récupère les configurations
					ninjacupBase = Integer.parseInt(props.getProperty("loot.ninjacupBase"));
				} catch (IOException ex) {
					// Si le fichier n'existe pas alors on récupère les infos dans notre fichier
					ResourceBundle bundle = ResourceBundle.getBundle("config");
					ninjacupBase = Integer.parseInt(bundle.getString("loot.ninjacupBase"));
				}
				
				JFrame jf = new JFrame("Génération Récap de raid");
	
				JTextArea text = new JTextArea();
				text.setEditable(false);
				DefaultCaret caret = (DefaultCaret) text.getCaret();
				caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
	
				JScrollPane pScroll = new JScrollPane(text);
				pScroll.setPreferredSize(new Dimension(MainGUI.WIDTH, MainGUI.HEIGHT));
	
				jf.add(pScroll);
	
				text.append("[align=center]- RAID DU ");
	
				Date date = lLoot.get(0).getDateLoot();
				text.append(new SimpleDateFormat("EEEE dd MMMM", Locale.FRENCH).format(date).toUpperCase());
				text.append(" -[/align]\n\n");
	
				text.append("[u][b]- Compo et logs de la soirée :[/b][/u]\n\n");
				text.append(
						"[url=https://www.warcraftlogs.com/guilds/5208]https://www.warcraftlogs.com/guilds/5208[/url]\n\n\n");
	
				text.append("[u][b]- Boss down :[/b][/u]\n\n");
	
				String boss = "";
				for (Loot l : lLoot) {
					if (!boss.equals(l.getBoss())) {
						boss = l.getBoss();
						text.append("- " + boss + " " + l.getDifficulte() + "\n");
					}
				}
	
				text.append("\n\n[u][b]- Loots :[/b][/u]\n");
	
				String r = "";
				boolean last;
				boss = "";
				for (Loot l : lLoot) {
					if (l.getRaison().equals("Upgrade")
							|| l.getRaison().equals("2 Part")
							|| l.getRaison().equals("4 Part")
							|| !cTri.isSelected()) {
						last = boss.equals(l.getBoss());
	
						if (!last) {
							text.append(r + "\n");
							r = "- " + l.getBoss() + " " + l.getDifficulte() + " : ";
						} else {
							r += " / ";
						}
						r += l.getJoueur() + " ";
	
						if (l.getItemID() != -1) {
							r += "[url=http://fr.wowhead.com/item=" + l.getItemID()
									+ (!l.getBonus().equals("") ? "&bonus=" + l.getBonus() : "") + "][" + l.getItem()
									+ "][/url] ";
						} else {
							r += "[" + l.getItem() + "] ";
						}
						r += String.valueOf(l.getIlvl() - ninjacupBase) + " ";
	
						/*switch (l.getRaison()) {
							case "Upgrade":
								r += "Spé1";
								break;
							default:
								r += l.getRaison();
								break;
						}*/
	
						boss = l.getBoss();
					}
				}
				text.append(r);
	
				jf.setMinimumSize(new Dimension(MainGUI.WIDTH, MainGUI.HEIGHT));
				jf.pack();
				jf.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, "Des Ilvls sont encore inconnus.\nIl faut les renseigner pour continuer.",
						"Champs non remplis", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Erreur lors de la génération :\nAucun loot détecté",
					"Erreur de parsage", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void genererPoints() {
		if (lLoot != null && !lLoot.isEmpty()) {
			boolean rempli = true;
			for(JTextField j : lIlvls) {
				if (j.getText().equals("")) {
					rempli = false;
					break;
				}
			}
			
			if (rempli) {
				int ninjacupBase;
				// On récupère le fichier de propriétés
				try(
					FileInputStream fins = new FileInputStream(SettingsGUI.PROPERTY_FILE);
				) {
					Properties props = new Properties();
					props.load(fins);
					
					// On récupère les configurations
					ninjacupBase = Integer.parseInt(props.getProperty("loot.ninjacupBase"));
				} catch (IOException ex) {
					// Si le fichier n'existe pas alors on récupère les infos dans notre fichier
					ResourceBundle bundle = ResourceBundle.getBundle("config");
					ninjacupBase = Integer.parseInt(bundle.getString("loot.ninjacupBase"));
				}
				
				JFrame jf = new JFrame("Génération Points Ninjacup");
	
				JTextArea text = new JTextArea();
				text.setEditable(false);
				DefaultCaret caret = (DefaultCaret) text.getCaret();
				caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
	
				JScrollPane pScroll = new JScrollPane(text);
				pScroll.setPreferredSize(new Dimension(MainGUI.WIDTH, MainGUI.HEIGHT));
	
				jf.add(pScroll);
	
				SortedMap<String, Integer> mapPoints = new TreeMap<>();
				for (Loot l : lLoot) {
					if (l.getRaison().equals("Upgrade") || l.getRaison().equals("2 Part") || l.getRaison().equals("4 Part")) {
						int nbPoints = 0;
						if (mapPoints.containsKey(l.getJoueur())) {
							nbPoints = mapPoints.get(l.getJoueur());
						}
	
						nbPoints += l.getIlvl() - ninjacupBase;
	
						mapPoints.put(l.getJoueur(), nbPoints);
					}
				}
	
				for (String joueur : mapPoints.keySet()) {
					text.append(joueur + " :\n");
					text.append("    Points : +" + mapPoints.get(joueur) + "\n");
					text.append("    Loots:\n");
	
					int nbNm = 0;
					int nbHm = 0;
					int nbMm = 0;
					String nm = "";
					String hm = "";
					String mm = "";
					for (Loot l : lLoot) {
						if (joueur.equals(l.getJoueur())) {
							if (l.getRaison().equals("Upgrade")) {
								switch (l.getDifficulte()) {
								case "Normal":
									nbNm++;
	
									if (l.getItemID() != -1) {
										nm += "[url=http://fr.wowhead.com/item=" + l.getItemID()
												+ (!l.getBonus().equals("") ? "&bonus=" + l.getBonus() : "") + "][" + l.getItem()
												+ "][/url] ";
									} else {
										nm += "[" + l.getItem() + "] ";
									}
	
									break;
								case "Héroïque":
									nbHm++;
	
									if (l.getItemID() != -1) {
										hm += "[url=http://fr.wowhead.com/item=" + l.getItemID()
												+ (!l.getBonus().equals("") ? "&bonus=" + l.getBonus() : "") + "][" + l.getItem()
												+ "][/url] ";
									} else {
										hm += "[" + l.getItem() + "] ";
									}
	
									break;
								case "Mythique":
									nbMm++;
	
									if (l.getItemID() != -1) {
										mm += "[url=http://fr.wowhead.com/item=" + l.getItemID()
												+ (!l.getBonus().equals("") ? "&bonus=" + l.getBonus() : "") + "][" + l.getItem()
												+ "][/url] ";
									} else {
										mm += "[" + l.getItem() + "] ";
									}
	
									break;
								}
							}
						}
					}
	
					text.append("        " + nbNm + " NM : " + nm + "\n");
					text.append("        " + nbHm + " HM : " + hm + "\n");
					text.append("        " + nbMm + " MM : " + mm + "\n");
					text.append("\n");
				}
	
				jf.setMinimumSize(new Dimension(MainGUI.WIDTH, MainGUI.HEIGHT));
				jf.pack();
				jf.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, "Des Ilvls sont encore inconnus.\nIl faut les renseigner pour continuer.",
						"Champs non remplis", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Erreur lors de la génération :\nAucun loot détecté",
					"Erreur de parsage", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected JOptionPane getOptionPane(JComponent parent) {
		JOptionPane pane = null;
		if (!(parent instanceof JOptionPane)) {
			pane = getOptionPane((JComponent) parent.getParent());
		} else {
			pane = (JOptionPane) parent;
		}
		return pane;
	}
	
	public void ouvrirFichier() {
		final JFileChooser fc = new JFileChooser();
		int r = fc.showOpenDialog(parent);

		if (r == JFileChooser.APPROVE_OPTION) {
			fichier = fc.getSelectedFile();
			lNomFichier.setText(fichier.getName());
		}
	}

	public ArrayList<Loot> getlLoot() {
		return lLoot;
	}

	public void setlLoot(ArrayList<Loot> lLoot) {
		this.lLoot = lLoot;
		this.refreshListe();
	}
	
	public void showList() {
		for(Loot l : lLoot) {
			System.out.println(l);
		}
	}
}
