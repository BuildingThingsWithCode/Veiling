package veiling.domein;

import java.util.*;

/**
 * Aanbod van de veiling: de gegevens van het artikel,
 * de voorwaarden waaronder het verkocht wordt, en de status van
 * het artikel in de veiling.
 * @author Open Universiteit 
 */
public class Aanbod extends Observable {

	/**
	 * Status: artikel nog niet in veiling.
	 */
	public static final int STATUS_WACHT = 0;
	/**
	 * Status: artikel in veiling.
	 */
	public static final int STATUS_INVEILING = 1;
	/**
	 * Status: artikel uit veiling, verkocht.
	 */
	public static final int STATUS_VERKOCHT = 2;
	/**
	 * Status: artikel uit veiling, niet verkocht.
	 */
	public static final int STATUS_NIET_VERKOCHT = 3;

	private int id; // identificatie
	private String naam; // naam van artikel
	private String omschrijving; // uitgebreide omschrijving
	private double minbod; // minimaal biedbedrag bij start veiling
	private double maxbod; // hoogste biedbedrag tot nu toe
	private Date startdatum; // startdatum veiling van artikel
	private Date einddatum; // einddatum veiling van artikel
	private int status; // status van veiling
	private Gebruiker lid; // lid dat artikel heeft ingebracht in veiling
	private Rubriek rubriek; // rubriek waartoe artikel hoort

	/**
	 * Creeert een leeg aanbod (artikel).
	 */
	public Aanbod() {
		this.maxbod = 0;
	}

	/**
	 * Zet identificatienummer voor aanbod.
	 * @param id identificatienummer voor aanbod
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Geeft identificatie van aanbod.
	 * @return Identificatie van aanbod.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Zet naam voor aanbod.
	 * @param naam naam (korte beschrijving)
	 */
	public void setNaam(String naam) {
		this.naam = naam;
	}

	/**
	 * Geeft artikelnaam.
	 * @return Artikelnaam.
	 */
	public String getNaam() {
		return naam;
	}

	/**
	 * Zet uitgebreide omschrijving voor aanbod (artikel).
	 * @param omschrijving  omschrijving)
	 */
	public void setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
	}

	/**
	 * Geeft uitgebreide omschrijving van het aanbod (artikel).
	 * @return Uitgebreide omschrijving.
	 */
	public String getOmschrijving() {
		return omschrijving;
	}

	/**
	 * Zet de rubriek waartoe aanbod (artikel) behoort.
	 * @param rubriek
	 */
	public void setRubriek(Rubriek rubriek) {
		this.rubriek = rubriek;
	}

	/**
	 * Geeft rubriek van artikel.
	 * @return Rubriek van artikel.
	 */
	public Rubriek getRubriek() {
		return rubriek;
	}

	/**
	 * Zet het minimaal vereiste bod voor het aanbod (artikel).
	 * @param minbod minimaal bod (startbedrag)
	 */
	public void setMinbod(double minbod) {
		this.minbod = minbod;
	}

	/**
	 * Geeft het minimaal vereiste bod op het aanbod (artikel).
	 * @return Minimaal bod.
	 */
	public double getMinbod() {
		return minbod;
	}

	/**
	 * Zet waarde van hoogste bod behorende bij het aanbod (artikel).
	 * @param maxbod hoogste bod
	 */
	public void setMaxbod(double maxbod) {
		this.maxbod = maxbod;
	}

	/**
	 * Geeft de waarde van het hoogste bod op het aanbod (artikel).
	 * @return hoogste bod.
	 */
	public double getMaxbod() {
		return maxbod;
	}

	/**
	 * Zet startdatum voor veiling van het aanbod (artikel).
	 * @param startdatum  startdatum van veiling
	 */
	public void setStartdatum(Date startdatum) {
		this.startdatum = startdatum;
	}

	/**
	 * Geeft datum dat veiling van aanbod (artikel) start.
	 * @return Startdatum van veiling.
	 */
	public Date getStartdatum() {
		return startdatum;
	}

	/**
	 * Zet einddatum voor veiling van het aanbod (artikel).
	 * @param einddatum  einddatum van veiling
	 */
	public void setEinddatum(Date einddatum) {
		this.einddatum = einddatum;
	}

	/**
	 * Geeft datum dat veiling van aanbod (artikel) eindigt.
	 * @return Einddatum van veiling.
	 */
	public Date getEinddatum() {
		return einddatum;
	}

	/**
	 * Zet status van aanbod (artikel) in veiling.
	 * @param status status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Geeft status van aanbod (artikel) in veiling.
	 * @return Status.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Zet lid dat artikel ter veiling aanbiedt.
	 * @param lid lid dat aanbod doet.
	 */
	public void setLid(Gebruiker lid) {
		this.lid = lid;
	}

	/**
	 * Geeft eigenaar van artikel (inbrenger artikel in veiling).
	 * @return lid, eigenaar van artikel.
	 */
	public Gebruiker getLid() {
		return lid;
	}
	
	//de twee methoden hieronder zijn toegevoegd
	public void doeChanged(ArrayList autoBodInfo){
		setChanged();
		notifyObservers(autoBodInfo);
	}
	
	public void doeClearChanged(){
		clearChanged();
	}
}
