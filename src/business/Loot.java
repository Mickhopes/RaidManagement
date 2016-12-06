package business;

import java.io.Serializable;
import java.util.Date;

public class Loot implements Comparable<Loot>, Serializable {
	private String joueur;
	private Date dateLoot;
	private String item;
	private int itemID;
	private int bonus;
	private String boss;
	private String difficulte;
	private String raison;

	public Loot(String joueur, Date dateLoot, String item, int itemID, int bonus, String boss, String difficulte, String raison) {
		this.joueur = joueur;
		this.dateLoot = dateLoot;
		this.item = item;
		this.itemID = itemID;
		this.bonus = bonus;
		this.boss = boss;
		this.difficulte = difficulte;
		this.raison = raison;
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

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
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

	@Override
	public String toString() {
		return this.joueur + ", " + this.dateLoot + ", " + this.item + ", " + this.itemID + ", " + this.bonus + ", " + this.boss + ", " + this.difficulte + ", " + this.raison;
	}

	@Override
	public int compareTo(Loot o) {
		return this.dateLoot.compareTo(o.getDateLoot());
	}
}
