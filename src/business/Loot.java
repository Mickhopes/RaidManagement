package business;

import java.io.Serializable;
import java.util.Date;

public class Loot implements Comparable<Loot>, Serializable {
	private String joueur;
	private Date dateLoot;
	private String item;
	private int itemID;
	private String bonus;
	private String boss;
	private String difficulte;
	private String raison;
	private boolean set;
	private int ilvl;

	public Loot(String joueur, Date dateLoot, String item, int itemID, String bonus, String boss, String difficulte, String raison, boolean set, int ilvl) {
		this.joueur = joueur;
		this.dateLoot = dateLoot;
		this.item = item;
		this.itemID = itemID;
		this.bonus = bonus;
		this.boss = boss;
		this.difficulte = difficulte;
		this.raison = raison;
		this.set = set;
		this.ilvl = ilvl;
	}

	public String getJoueur() {
		return joueur;
	}

	public void setJoueur(String joueur) {
		this.joueur = joueur;
	}

	public Date getDateLoot() {
		return dateLoot;
	}

	public void setDateLoot(Date dateLoot) {
		this.dateLoot = dateLoot;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public String getBonus() {
		return bonus;
	}

	public void setBonus(String bonus) {
		this.bonus = bonus;
	}

	public String getBoss() {
		return boss;
	}

	public void setBoss(String boss) {
		this.boss = boss;
	}

	public String getDifficulte() {
		return difficulte;
	}

	public void setDifficulte(String difficulte) {
		this.difficulte = difficulte;
	}

	public String getRaison() {
		return raison;
	}

	public void setRaison(String raison) {
		this.raison = raison;
	}
	
	public boolean isSet() {
		return this.set;
	}
	
	public void setSet(boolean set) {
		this.set = set;
	}

	public int getIlvl() {
		return ilvl;
	}

	public void setIlvl(int ilvl) {
		this.ilvl = ilvl;
	}

	@Override
	public String toString() {
		return this.joueur + ", " + 
				this.dateLoot + ", " + 
				this.item + ", " + 
				this.itemID + ", " + 
				this.bonus + ", " + 
				this.boss + ", " + 
				this.difficulte + ", " + 
				this.raison + ", " +
				this.set + ", " +
				this.ilvl;
	}

	@Override
	public int compareTo(Loot o) {
		return this.dateLoot.compareTo(o.getDateLoot());
	}
}
