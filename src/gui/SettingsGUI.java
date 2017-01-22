package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SettingsGUI extends JFrame {
	public static final String PROPERTY_PATH = getUserHomeDir()+File.separator+"RaidManagement";
	public static final String PROPERTY_FILE = PROPERTY_PATH+File.separator+"config.properties";
	
	public SettingsGUI(String title, JFrame parent) {
		super(title);
		
		// On récupère le fichier de propriétés
		String regex;
		int refNumber, refIlvl, ninjacupBase;
		try(
			FileInputStream fins = new FileInputStream(PROPERTY_FILE);
		) {
			Properties props = new Properties();
			props.load(fins);
			
			// On récupère les configurations
			regex = props.getProperty("loot.regex");
			refNumber = Integer.parseInt(props.getProperty("loot.refNumber"));
			refIlvl = Integer.parseInt(props.getProperty("loot.refIlvl"));
			ninjacupBase = Integer.parseInt(props.getProperty("loot.ninjacupBase"));
		} catch (IOException ex) {
			// Si le fichier n'existe pas alors on récupère les infos dans notre fichier
			ResourceBundle bundle = ResourceBundle.getBundle("config");
			regex = bundle.getString("loot.regex");
			refNumber = Integer.parseInt(bundle.getString("loot.refNumber"));
			refIlvl = Integer.parseInt(bundle.getString("loot.refIlvl"));
			ninjacupBase = Integer.parseInt(bundle.getString("loot.ninjacupBase"));
		}
		
		JPanel global = new JPanel(new BorderLayout());
		
		//JPanel d'option de loot
		JPanel lootOptions = new JPanel();
		lootOptions.setLayout(new BoxLayout(lootOptions, BoxLayout.Y_AXIS));
		
		JPanel pRegex = new JPanel(new BorderLayout());
		JLabel lLootRegex = new JLabel("Loot pattern :");
		JTextField jLootRegex = new JTextField(regex, 15);
		
		pRegex.add(lLootRegex, BorderLayout.WEST);
		pRegex.add(jLootRegex, BorderLayout.EAST);
		pRegex.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JPanel pRefNumber = new JPanel(new BorderLayout());
		JLabel lRefNumber = new JLabel("Valeur de référence :");
		JTextField jRefNumber = new JTextField(String.valueOf(refNumber), 15);
		jRefNumber.addKeyListener(new KeyListener() {
			
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
		
		pRefNumber.add(lRefNumber, BorderLayout.WEST);
		pRefNumber.add(jRefNumber, BorderLayout.EAST);
		pRefNumber.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JPanel pRefIlvl = new JPanel(new BorderLayout());
		JLabel lRefIlvl = new JLabel("Ilvl de référence :");
		JTextField jRefIlvl = new JTextField(String.valueOf(refIlvl), 15);
		jRefIlvl.addKeyListener(new KeyListener() {
			
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
		
		pRefIlvl.add(lRefIlvl, BorderLayout.WEST);
		pRefIlvl.add(jRefIlvl, BorderLayout.EAST);
		pRefIlvl.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JPanel pNinjacup = new JPanel(new BorderLayout());
		JLabel lNinjacup = new JLabel("Points d'Ilvl à retirer pour la ninjacup :");
		JTextField jNinjacup = new JTextField(String.valueOf(ninjacupBase), 15);
		jNinjacup.addKeyListener(new KeyListener() {
			
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
		
		pNinjacup.add(lNinjacup, BorderLayout.WEST);
		pNinjacup.add(jNinjacup, BorderLayout.EAST);
		pNinjacup.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		lootOptions.add(pRegex);
		lootOptions.add(pRefNumber);
		lootOptions.add(pRefIlvl);
		lootOptions.add(pNinjacup);
		
		//JPanel des boutons d'action
		JPanel buttons = new JPanel();
		
		JButton bSave = new JButton("Appliquer");
		bSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Properties props = new Properties();
				
				// On récupère les propriétées
				try(
					FileInputStream fins = new FileInputStream(PROPERTY_FILE);
				) {
					props.load(fins);
				} catch (FileNotFoundException ex) {
					System.out.println("fichier de config non existant. création...");
				} catch (IOException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(SettingsGUI.this,
							"Erreur lors de l'application des paramètres :\n" + ex.getMessage(),
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
				
				if (new File(PROPERTY_PATH).mkdir()) {
					System.out.println("Création du répertoire de configuration...");
				}
				
				// Puis on les sauvegarde
				try(
					FileOutputStream out = new FileOutputStream(PROPERTY_FILE);
				) {
					props.setProperty("loot.regex", jLootRegex.getText());
					props.setProperty("loot.refNumber", jRefNumber.getText());
					props.setProperty("loot.refIlvl", jRefIlvl.getText());
					props.setProperty("loot.ninjacupBase", jNinjacup.getText());
					props.store(out, null);
				} catch (IOException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(SettingsGUI.this,
							"Erreur lors de l'application des paramètres :\n" + ex.getMessage(),
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
				
				SettingsGUI.this.setVisible(false);
			}
		});
		
		JButton bCancel = new JButton("Annuler");
		bCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SettingsGUI.this.setVisible(false);
			}
		});
		
		buttons.add(bSave);
		buttons.add(bCancel);
		
		global.add(lootOptions, BorderLayout.CENTER);
		global.add(buttons, BorderLayout.SOUTH);
		
		// Mise du bouton par défault
		getRootPane().setDefaultButton(bSave);
		bSave.requestFocus();
		
		add(global);
		pack();
		setLocationRelativeTo(parent);
	}
	
	public static String getUserHomeDir() {
		String user_home = System.getProperty("user.home");
		return user_home.replace("\\", "/");
	}
}
