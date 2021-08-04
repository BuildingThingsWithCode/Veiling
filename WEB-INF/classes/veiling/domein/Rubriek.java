package veiling.domein;


/**
 * Rubriek van de veiling: artikelen kunnen worden gerubriceerd middels
 * rubrieken.
 * @author Open Universiteit
 */
public class Rubriek {

  private int id; // identificatie
  private String naam; // naam van rubriek

  /**
   * Creert (lege) rubriek
   */
  public Rubriek() {
  }
  
  /**
   * Creeert een rubriek met de gegeven parameters.
   * @param id Identificatie van aanbod.
   * @param naam Naam van artikel.
   */
  public Rubriek(int id, String naam) {
    this.id = id;
    this.naam = naam;
  }

  /**
   * Zet identificatienummer voor rubriek.
   * @param id identificatienummer voor rubriek
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Geeft identificatie van rubriek.
   * @return Identificatie van rubriek.
   */
  public int getId() {
    return id;
  }

  /**
   * Zet naam voor rubriek.
   * @param naam naam voor rubriek
   */
  public void setNaam(String naam) {
    this.naam = naam;
  }
  
  /**
   * Geeft rubrieknaam.
   * @return Rubrieknaam.
   */
  public String getNaam() {
    return naam;
  }

}
