package veiling.domein;

/**
 * Exception klasse voor gebruikersfouten.
 * Gebruikersfouten zijn fouten van de gebruiker tegen de veilingregels.
 * Excepties van deze klasse worden gebruikt om meldingen door te geven 
 * naar de interface.
 * @author Open Universiteit
 */
public class VeilingException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor.
   */
  public VeilingException() {
    super();
  }

  /**
   * Constructor.
   * @param s Foutmelding.
   */
  public VeilingException(String s) {
    super(s);
  }
}
