package veiling.domein;

import java.util.*;

/**
 * Bod van de veiling: informatie op welk artikel, door wie
 * en met welk bedrag geboden is.
 * @author Open Universiteit Nederland
 * @version 2.0
 */
public class Bod {

	private int id; // identificatie van bod
	private Aanbod aanbod; // aanbod waarop wordt geboden
	private Gebruiker lid; // lid dat bod doet
	private double bedrag; // biedbedrag
	private Date datum; // datum van bod
	private double autoBod;//maximale bedrag dat automatisch mag geboden worden

	/**
	 * Creeert een (leeg) bod.
	 */
	public Bod() {
		datum = new Date();
	}

	/**
	 * Geeft identificatie van bod.
	 * @return identificatie van bod
	 */
	public int getId() {
		return id;
	}

	/**
	 * Zet identificatie van bod
	 * @param id identificatie van bod
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Geeft aanbod waarop bod is gedaan.
	 * @return aanbod waarop bod is gedaan.
	 */
	public Aanbod getAanbod() {
		return aanbod;
	}

	/**
	 * Zet het aanbod waarop bod is gedaan.
	 * @param aanbod aanbod waarop bod is gedaan
	 */
	public void setAanbod(Aanbod aanbod) {
		this.aanbod = aanbod;
	}

	/**
	 * Geeft het lid dat het aanbod heeft gedaan.
	 * @return het lid dat het aanbod heeft gedaan.
	 */
	public Gebruiker getLid() {
		return lid;
	}

	/**
	 * Zet het lid dat het aanbod heeft gedaan.
	 * @param lid het lid dat het aanbod heeft gedaan
	 */
	public void setLid(Gebruiker lid) {
		this.lid = lid;
	}

	/**
	 * Geeft het bied-bedrag.
	 * @return het bied-bedrag.
	 */
	public double getBedrag() {
		return bedrag;
	}

	/**
	 * Zet het bied-bedrag.
	 * @param bedrag het bied-bedrag.
	 */
	public void setBedrag(double bedrag) {
		this.bedrag = bedrag;
	}

	/**
	 * Geeft de datum van bieding.
	 * @return de datum van bieding.
	 */
	public Date getDatum() {
		return datum;
	}

	/**
	 * Zet de datum van bieding.
	 * @param datum de datum van bieding.
	 */
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	
	//de 2 methoden hieronder zijn toegevoegd
	//getter voor het automatisch bod
	public Double getAutoBod(){
		return autoBod;
	}

	//setter voor het automatisch bod
	public void setAutoBod (double autoBod){
		this.autoBod = autoBod;
	}

}
