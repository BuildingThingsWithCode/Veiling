package veiling.domein;

import java.util.*;

import veiling.data.*;

/**
 * Klasse die de veiling beheert.
 * Deze klasse regelt het hele veilingproces en is verantwoordelijk voor het
 * afdwingen van de regels van de veiling.
 * Er is slechts één instantie van deze klasse.
 * @author Open Universiteit
 *
 */
public class VeilingBeheer {
	private static VeilingBeheer instance = null;

	// constanten voor afdwingen veilingregels
	private final static int RUBRIEK_NAAM_MIN = 6;
	private final static int RUBRIEK_NAAM_MAX = 32;

	private final static int AANBOD_NAAM_MIN = 4;
	private final static int AANBOD_NAAM_MAX = 32;
	private final static int AANBOD_OMSCHRIJVING_MAX = 256; 
	private final static double AANBOD_MINBOD_MIN = 0.50;
	private final static double AANBOD_MINBOD_MAX = 200.00; 
	private final static double BOD_VERHOGING_MIN = 0.50;

	// private constructor om te voorkomen dat instantie wordt gemaakt
	private VeilingBeheer() { }

	/**
	 * Creeert een instantie van VeilingBeheer.
	 * Er is slechts één instantie van deze klasse (singleton patroon).
	 * @return instantie van VeilingBeheer
	 */
	public static synchronized VeilingBeheer getInstance() {
		if (instance == null) {
			instance = new VeilingBeheer();
		}
		return instance;
	}

	/**
	 * Registreert een nieuwe rubriek voor de veiling.
	 * @param rubriek de nieuwe rubriek
	 * @throws VeilingException wanneer de ingevoerde rubriek niet aan de veilingregels voldoet.
	 * @throws SysteemException wanneer operatie door systeem niet uitgevoerd kan worden.
	 */
	public void registreerRubriek(Rubriek rubriek) throws VeilingException, SysteemException {
		// kijk of rubrieksgegevens voldoen aan veilingeisen
		String naam = rubriek.getNaam();
		if (naam.length() < RUBRIEK_NAAM_MIN ||
				naam.length() > RUBRIEK_NAAM_MAX) {
			throw new VeilingException("Naam rubriek voldoet niet aan eisen "+
					"(minimaal " + RUBRIEK_NAAM_MIN +
					", maximaal "+ RUBRIEK_NAAM_MAX +
					" karakters");
		}
		// kijk of rubrieknaam niet al voorkomt
		if (VeilingIO.getInstance().getRubriek(naam) != null) {
			throw new VeilingException("Rubriek " + naam + " bestaat al");
		}
		// sla rubriek op in database
		VeilingIO.getInstance().addRubriek(rubriek);
	}

	/**
	 * Geeft lijst van alle rubrieken van veiling.
	 * @return lijst van alle rubrieken.
	 * @throws SysteemException wanneer operatie niet uitgevoerd kan worden.
	 */
	public ArrayList<Rubriek> getAlleRubrieken() throws SysteemException {
		return VeilingIO.getInstance().getAlleRubrieken();
	}

	/**
	 * Registreert een nieuw aanbod (artikel) dat geveild kan worden.
	 * @param aanbod het nieuwe aanbod.
	 * @throws VeilingException wanneer aanbod niet voldoet aan veilingregels.
	 * @throws SysteemException wanneer operatie niet kan worden uitgevoerd.
	 */
	public void registreerAanbod(Aanbod aanbod) throws VeilingException, SysteemException {
		// kijk of aanbodgegevens voldoen aan veilingeisen
		if (aanbod.getNaam().length() < AANBOD_NAAM_MIN ||
				aanbod.getNaam().length() > AANBOD_NAAM_MAX) {
			throw new VeilingException("Naam voldoet niet aan eisen "+
					"(minimaal " + AANBOD_NAAM_MIN +
					", maximaal "+ AANBOD_NAAM_MAX +
					" karakters");
		}
		if (aanbod.getOmschrijving().length() > AANBOD_OMSCHRIJVING_MAX) {
			throw new VeilingException("Omschrijving voldoet niet aan eisen "+
					"(maximaal "+ AANBOD_OMSCHRIJVING_MAX +
					" karakters");
		}
		if (aanbod.getMinbod() < AANBOD_MINBOD_MIN ||
				aanbod.getMinbod() > AANBOD_MINBOD_MAX) {
			throw new VeilingException("Minimaal bod voldoet niet aan eisen "+
					"(minimaal " + Conversie.formatGeld(AANBOD_MINBOD_MIN) +
					", maximaal "+ Conversie.formatGeld(AANBOD_MINBOD_MAX) +
					" euro");
		}
		// sla aanbod op in database
		VeilingIO.getInstance().addAandbod(aanbod);
	}

	/**
	 * Geeft huidig aanbod behorende bij rubriek.
	 * Voordat aanbod wordt opgezocht wordt eerst de veiling geupdate.
	 * @param rubriekid identificatie van rubriek
	 * @return lijst met huidig aanbod
	 * @throws SysteemException wanneer operatie door systeem niet kan worden uitgevoerd.
	 */
	public ArrayList<Aanbod> getHuidigAanbod(int rubriekid) throws SysteemException {
		// update eerst de database
		updateVeiling();
		// dan aanbod opvragen
		ArrayList<Aanbod> huidigAanbod = VeilingIO.getInstance().getHuidigAanbod(rubriekid);
		return huidigAanbod;
	}

	//deze methode is toegevoegd en vraagt het verkocht aanbod op
	public ArrayList<Aanbod> getVerkocht(Date startdatum, Date einddatum) throws SysteemException{
		// update eerst de database
		updateVeiling();
		//dan verkochte artikelen opvragen
		ArrayList<Aanbod> verkocht = VeilingIO.getInstance().getVerkochtAanbod(startdatum, einddatum);
		return verkocht;
	}

	/**
	 * Doet een bod
	 * @param bod het bod dat wordt gedaan
	 * @throws VeilingException wanneer bod niet voldoet aan veilingregels
	 * @throws SysteemException wanneer systeem operatie niet kan uitvoeren
	 */
	public void doeBod(Bod bod) throws VeilingException, SysteemException {
		// test of bedrag voldoet
		double minimaal = minimaalBod(bod.getAanbod());
		if (bod.getBedrag() < minimaal) {
			throw new VeilingException("Bod te laag.<br>U dient minimaal " +
					Conversie.formatGeld(minimaal) + " te bieden");
		}
		VeilingIO.getInstance().addBod(bod);
		//alles hieronder is toegevoegd aan de methode doeBod
		//we vragen alle gebruikers op die een automatisch bod hebben geplaatst
		VeilingIO.getInstance().getObservers(bod.getAanbod());
		if (bod.getAanbod().countObservers() > 0){
			ArrayList autoBodInfo = VeilingIO.getInstance().getAutoBodInfo(bod.getAanbod());
			//we zorgen ervoor dan van die gebruikers de update() methode wordt aanroepen
			bod.getAanbod().doeChanged(autoBodInfo);
			bod.getAanbod().doeClearChanged();
		}
	}

	/**
	 * Berekent minimaal biedbedrag bij aanbod.
	 * @param aanbod het aanbod
	 * @return het minimaal te bieden bedrag
	 * @throws SysteemException wanneer operatie niet kan worden uitgevoerd
	 */
	public double minimaalBod(Aanbod aanbod) throws SysteemException {
		double minimaalBod = aanbod.getMinbod();
		if (aanbod.getMaxbod() > 0.0) {
			minimaalBod = aanbod.getMaxbod() + BOD_VERHOGING_MIN;
		}
		return minimaalBod;
	}

	/**
	 * Updates het aanbod van de veiling.
	 * Het kijkt van ieder aanbod in de veiling of de status moet worden
	 * veranderd, en zo ja, wijzigt de status, voert eventuele daarbij horende
	 * acties uit en slaat de nieuwe gegevens op in de database.
	 * @throws SysteemException als fout opgetreden is bij gebruik van database.
	 */
	private void updateVeiling() throws SysteemException {
		Date vandaag = DatumUtil.vandaag();
		ArrayList<Aanbod> aanbodlijst = VeilingIO.getInstance().getOnverwerktAanbod();
		for (Aanbod aanbod : aanbodlijst) {
			if (!aanbod.getStartdatum().after(vandaag)) {
				// als veiling nog niet geopend, open deze dan
				if (aanbod.getStatus() == Aanbod.STATUS_WACHT) {
					aanbod.setStatus(Aanbod.STATUS_INVEILING);
					VeilingIO.getInstance().updateAanbod(aanbod);
				}
			}
			if (aanbod.getEinddatum().before(vandaag) &&
					(aanbod.getStatus() == Aanbod.STATUS_WACHT ||
					aanbod.getStatus() == Aanbod.STATUS_INVEILING)) {
				// kijk of er bod is geweest
				Bod bod = VeilingIO.getInstance().getHoogsteBod(aanbod);
				if (bod == null) {
					// geen bod geweest; veiling wordt onverkocht afgesloten
					aanbod.setStatus(Aanbod.STATUS_NIET_VERKOCHT);
					VeilingIO.getInstance().updateAanbod(aanbod);
				} else {
					// bod geweest; regel verkoop
					// toekomst: stuur mail naar bieder
					aanbod.setStatus(Aanbod.STATUS_VERKOCHT);
					VeilingIO.getInstance().updateAanbod(aanbod);
				}
			}
		}
	}
}

