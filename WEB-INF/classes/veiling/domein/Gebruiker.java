package veiling.domein;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import veiling.data.VeilingIO;


/**
 * Gebruiker van de veiling.
 * Er zijn twee soorten gebruikers: leden en beheerders.
 * @author Open Universiteit
 */
public class Gebruiker implements Observer {
	private int id; // id van lid
	private String naam;
	private String adres;
	private String postcode;
	private String plaats;
	private String email;
	private String wachtwoord;
	private boolean beheerder;

	/**
	 * Creeert (lege) gebruiker.
	 */
	public Gebruiker()  {  
	}

	/**
	 * Geeft identificatie van gebruiker.
	 * @return identificatie van gebruiker
	 */
	public int getId() {
		return id;
	}

	/**
	 * Zet identificatie van gebruiker.
	 * @param id identificatie van gebruiker.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Geeft naam van gebruiker.
	 * @return naam van gebruiker
	 */
	public String getNaam() {
		return naam;
	}

	/**
	 * Zet naam van gebruiker.
	 * @param naam naam van gebruiker
	 */
	public void setNaam(String naam) {
		this.naam = naam;
	}

	/**
	 * Geeft adres van gebruiker.
	 * @return adres van gebruiker
	 */
	public String getAdres() {
		return adres;
	}

	/**
	 * Zet adres van gebruiker.
	 * @param adres adres van gebruiker
	 */
	public void setAdres(String adres) {
		this.adres = adres;
	}

	/**
	 * Geeft postcode van gebruiker.
	 * @return postcode van gebruiker
	 */
	public String getPostcode() {
		return postcode;
	}

	/**
	 * Zet postcode van gebruiker.
	 * @param postcode postcode van gebruiker
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	/**
	 * Geeft woonplaats van gebruiker.
	 * @return woonplaats van gebruiker
	 */
	public String getPlaats() {
		return plaats;
	}

	/**
	 * Zet woonplaats van gebruiker.
	 * @param plaats woonplaats van gebruiker
	 */
	public void setPlaats(String plaats) {
		this.plaats = plaats;
	}

	/**
	 * Geeft email-adres van gebruiker.
	 * @return email-adres van gebruiker
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Zet email-adres van gebruiker.
	 * @param email email-adres van gebruiker
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Geeft wachtwoord van gebruiker.
	 * @return wachtwoord van gebruiker
	 */
	public String getWachtwoord() {
		return wachtwoord;
	}

	/**
	 * Zet wachtwoord van gebruiker.
	 * @param wachtwoord wachtwoord van gebruiker
	 */
	public void setWachtwoord(String wachtwoord) {
		this.wachtwoord = wachtwoord;
	}

	/**
	 * Geef aan of gebruiker beheerder is
	 * @param beheerder zet true voor beheerder, false voor lid
	 */
	public void setBeheerder(boolean beheerder) {
		this.beheerder = beheerder;
	}

	/**
	 * Is gebruiker beheerder?
	 * @return true als gebruiker beheerder is, anders false.
	 */
	public boolean isBeheerder() {
		return beheerder;
	}

	//de 3 methoden hieronder zijn toegevoegd
	/*
	 * Deze methode zorgt ervoor dat de gebruiker
	 * met het hoogste automatisch bod, het correcte
	 * bedrag bied wanneer er een bod wordt gedaan op het 
	 * aanbod waarop deze gebruiker een automatisch bod heeft gedaan.
	 */
	public void update (Observable object, Object arg) throws RuntimeException {
		/*
		 * ArrayList bevat:
		 * (0) lid met hoogste autobod
		 * (1) bedrag hoogste autobod
		 * (2) tweede hoogste autobod
		 * (3) huidige hoogste bod
		 * (4) aanbod
		 */
		ArrayList autoBodInfo = (ArrayList)arg;
		Bod bod = (Bod)autoBodInfo.get(3);
		Bod autobod = new Bod();
		/*
		 * er moet geboden worden indien het huidige hoogste bod+0.5 <= dan het hoogste autoBod
		 * en het huidige lid het lid is met het hoogste autobod en het huidige lid niet diegene
		 * is die zelf het hoogste bod heeft uitgebracht
		 */
		if (bod.getBedrag()+0.5 <= (Double)autoBodInfo.get(1) && autoBodInfo.get(0).equals(this) && (!bod.getLid().equals(this))) {
			//System.out.println("Huidige hoogste bod is: "+bod.getBedrag()+" en is gedaan door "+bod.getLid().getNaam());
			//tweede hoogste autoBod is kleiner dan huidige hoogste bod + 0.5
			try {
				if ((Double)autoBodInfo.get(2) < bod.getBedrag()+0.5){
					//System.out.println("1 Mijn naam is "+this.getNaam()+" en ik ga "+(bod.getBedrag()+0.5)+" bieden."); 
					autobod.setAanbod((Aanbod)autoBodInfo.get(4));
					autobod.setLid(this);
					autobod.setBedrag(bod.getBedrag()+0.5);
					autobod.setAutoBod((Double)autoBodInfo.get(1));
					VeilingIO.getInstance().addBod(autobod);
				}
				//tweede hoogste autoBod is groter dan of gelijk aan huidige hoogste bod+0.5
				else {
					//en tweede hoogste autoBod+0.5 > hoogste autoBod
					if ((Double)autoBodInfo.get(2)+0.5 > (Double)autoBodInfo.get(1)) {
						//System.out.println("2 Mijn naam is "+this.getNaam()+" en ik ga "+(Double)autoBodInfo.get(1)+" bieden.");
						autobod.setAanbod((Aanbod)autoBodInfo.get(4));
						autobod.setLid(this);
						autobod.setBedrag((Double)autoBodInfo.get(1));
						autobod.setAutoBod((Double)autoBodInfo.get(1));
						VeilingIO.getInstance().addBod(autobod);
					}
					else {
						//en tweede hoogste autoBod+0.5 <= hoogste autoBod
						//System.out.println("3 Mijn naam is "+this.getNaam()+" en ik ga "+((Double)autoBodInfo.get(2)+0.5)+" bieden."); 
						autobod.setAanbod((Aanbod)autoBodInfo.get(4));
						autobod.setLid(this);
						autobod.setBedrag((Double)autoBodInfo.get(2)+0.5);
						autobod.setAutoBod((Double)autoBodInfo.get(1));
						VeilingIO.getInstance().addBod(autobod);
					}
				}
			} catch (SysteemException e) {
				throw new RuntimeException(e);
			}
		}
	}


	/*
	 * De twee methoden hieronder zorgen ervoor dat twee gebruikers
	 * verschillend zijn indien hun e-mailadres verschillend is.
	 * Aangezien het verplicht is om bij de registratie een nog niet
	 * bestaand e-mail in te vullen, zijn er geen twee gebruikers met
	 * hetzelfde e-mailadres.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Gebruiker))
			return false;
		Gebruiker other = (Gebruiker) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}
}
