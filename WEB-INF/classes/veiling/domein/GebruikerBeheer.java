package veiling.domein;

import veiling.data.GebruikerIO;

/**
 * Klasse die gebruikers beheert.
 * Deze klassen bevat methoden om gebruikers te beheren (toevoegen, opvragen,
 * inloggen, etc).
 * @author Open Universiteit
 */
public class GebruikerBeheer {
  private static GebruikerBeheer instance = null;
  
  // private constructor om te voorkomen dat instantie wordt gemaakt
  private GebruikerBeheer() { }
  
  /**
   * Geeft de enige instantie van deze klasse.
   * Maakt gebruik van singleton patroon.
   * @return de enige instantie van GebruikersBeheer.
   */
  public static synchronized GebruikerBeheer getInstance() {
    if (instance == null) {
      instance = new GebruikerBeheer();
    }
    return instance;
  }
  
  /**
   * Registreert een lid.
   * Controleert of de gegevens van een lid voldoen aan de eisen van de
   * veiling. Zo ja, wordt het lid toegevoegd aan de database.
   * @param lid het te registreren lid.
   * @throws VeilingException Wanneer gegevens van lid niet voldoen aan eisen.
   * @throws SysteemException Wanneer systeem operatie niet kan uitvoeren.
   */
  public void registreerLid(Gebruiker lid) throws VeilingException, SysteemException {
    // kijk of gegevens van lid voldoen aan eisen veiling
    // er mag geen lid zijn met hetzelfde email-adres (uniek)
    if (GebruikerIO.getInstance().getLid(lid.getEmail()) != null) {
      throw new VeilingException("Lid met email adres " + lid.getEmail() + " bestaat al");
    }
    // sla lid op in database
    GebruikerIO.getInstance().addLid(lid);
  }
  
  /**
   * Logt lid of beheerder in.
   * @param loginid  naam/email adres van lid/beheerder
   * @param wachtwoord wachtwoord
   * @return Ingelogde gebruiker wanneer inloggen gelukt, anders null
   * @throws SysteemException Wanneer systeem operatie niet kan uitvoeren
   */
  public Gebruiker login(String loginid, String wachtwoord) throws SysteemException {
    // is naam van beheerder?
    Gebruiker gebruiker = GebruikerIO.getInstance().getBeheerder(loginid);
    if (gebruiker == null) {
      // dan misschien van lid?
      gebruiker = GebruikerIO.getInstance().getLid(loginid);
    }
    // wanneer gebruiker gevonden, test of wachtwoord klopt
    if (gebruiker != null && !wachtwoord.equals(gebruiker.getWachtwoord())) {
      gebruiker = null;
    }
    // geen geldige naam/wachtwoord combinatie
    return gebruiker;
  }

}