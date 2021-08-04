package veiling.domein;

import java.util.*;

/**
 * Klasse met hulpfuncties voor het omgaan met datum.
 * @author Open Universiteit
 */
public final class DatumUtil {

  /**
   * Private constructor om creatie instanties te voorkomen.
   */
  private DatumUtil() {
  }
  
  /**
   * Geeft Date-object met de datum van vandaag.
   * @return Date-object met de datum van vandaag.
   */
  public static Date vandaag() {
    return alleenDatum(new Date());
  }
  
  /**
   * Haalt tijdscomponent van Date-object af.
   * @param datum Date-object
   * @return Date object zonder tijdscomponent.
   */
  public static Date alleenDatum(Date datum) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(datum);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }
}
