package veiling.domein;

import java.text.*;
import java.util.Date;

/**
 * Klasse met methoden voor formatterer van geldbedragen en data.
 * @author Open Universiteit Nederland
 */
public final class Conversie {
  
  /**
   * Private constructor om instantiatie te voorkomen.
   */
  private Conversie() {
  }
  
  /**
   * Zet geldbedrag om van string-representatie naar double.
   * Zowel , als . worden geaccepteerd als scheiding tussen euro en cent.
   * @param bedrag  string-representatie van geldbedrag 
   * @return double representatie voor geldbedrag
   * @throws ParseException wanneer string niet omgezet kan worden naar double.
   */
  public static double parseGeld(String bedrag) throws ParseException {
    return NumberFormat.getInstance().parse(bedrag.replace(".", ",")).doubleValue();
  }
  
  /**
   * Zet geldbedrag om van double-representatie naar string.
   * Twee cijfers achter de komma.
   * @param bedrag geldbedrag als double
   * @return string-representatie van geldbedrag.
   */
  public static String formatGeld(double bedrag) {
    NumberFormat fmt = NumberFormat.getInstance();
    fmt.setMinimumFractionDigits(2);
    fmt.setMaximumFractionDigits(2);
    fmt.setMinimumIntegerDigits(1);
    return fmt.format(bedrag);
  }
  
  /**
   * Zet string-representatie van datum om naar Date-representatie.
   * Het gebruikte formaat is dd-MM-yyy.
   * @param datum stringrepresentatie voor datum
   * @return datum als Date-object.
   * @throws ParseException Wanneer stringrepresentatie niet voldoet aan formaat.
   */
  public static Date parseDatum(String datum) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    sdf.setLenient(false);
    return sdf.parse(datum);
  }
}
