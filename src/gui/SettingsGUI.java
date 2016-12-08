package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SettingsGUI extends JFrame {
	
	public SettingsGUI(String title, JFrame parent) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel global = new JPanel(new BorderLayout());
		
		//JPanel d'option de loot
		JPanel lootOptions = new JPanel();
		
		JLabel lLootRegex = new JLabel("Loot pattern :");
		
		ResourceBundle bundle = ResourceBundle.getBundle("config");
		JTextField jLootRegex = new JTextField(bundle.getString("loot.regex"), 20);
		
		lootOptions.add(lLootRegex);
		lootOptions.add(jLootRegex);
		
		//JPanel des boutons d'action
		JPanel buttons = new JPanel();
		
		JButton bSave = new JButton("Appliquer");
		bSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				Properties props = new Properties();
//				
//				// On récupère les propriétées
//				try(
//					FileInputStream fins = new FileInputStream("resources/config.properties");
//				) {
//					props.load(fins);
//				} catch (IOException ex) {
//					ex.printStackTrace();
//					JOptionPane.showMessageDialog(SettingsGUI.this,
//							"Erreur lors de l'application des paramètres :\n" + ex.getMessage(),
//							"Erreur", JOptionPane.ERROR_MESSAGE);
//				}
//				
//				// Puis on les sauvegarde
//				try(
//						FileOutputStream out = new FileOutputStream("resources/config.properties");
//				) {
//					props.setProperty("loot.regex", jLootRegex.getText());
//					props.store(out, null);
//				} catch (IOException ex) {
//					ex.printStackTrace();
//					JOptionPane.showMessageDialog(SettingsGUI.this,
//							"Erreur lors de l'application des paramètres :\n" + ex.getMessage(),
//							"Erreur", JOptionPane.ERROR_MESSAGE);
//				}
//				
//				SettingsGUI.this.dispose();
			}
		});
		
		JButton bCancel = new JButton("Cancel");
		bCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SettingsGUI.this.dispose();
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
		setLocationRelativeTo(parent);
		pack();
	}
}
