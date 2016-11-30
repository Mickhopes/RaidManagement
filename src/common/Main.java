package common;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
	public static void main(String[] args) {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Parser("Parser RCLootCouncil").setVisible(true);
			}
		});
	}
}
