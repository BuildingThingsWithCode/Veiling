package veiling.domein;

/**
 * Exception klasse voor systeemfouten.
 * Systeemfouten zijn fouten waar de gebruiker niets aan kan doen.
 * Excepties van deze klasse worden gebruikt om meldingen door te geven van
 * applicatie naar interface.
 * @author Open Universiteit
 */
public class SysteemException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor.
   */
  public SysteemException() {
    super();
  }

  /**
   * Constructor.
   * @param s Foutmelding.
   */
  public SysteemException(String s) {
    super(s);
  }
}
