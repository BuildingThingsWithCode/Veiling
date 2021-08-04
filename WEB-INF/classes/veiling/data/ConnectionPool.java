package veiling.data;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import veiling.domein.*;

/**
 * Singletonklasse die verbinding maakt met de database en
 * verbindingen opvraagt en vrij geeft.
 * @author Open Universiteit
 */
public class ConnectionPool {
  // constante voor de JNDI databasenaam
  private static final String DBNAAM = "java:/comp/env/jdbc/Verkocht";
  // de enige instantie van deze klasse
  private static ConnectionPool pool = null;
  // de datasource
  private DataSource dataSource = null; 

  /**
   * De (private) constructor maakt verbinding met de database.
   * @throws SysteemException wanneer systeem operatie niet kan uitvoeren
   */
  private ConnectionPool() throws SysteemException {
    try {
      Context initCtx = new InitialContext() ;
      dataSource = (DataSource) initCtx.lookup(DBNAAM);
    }
    catch (NamingException e) {
      e.printStackTrace();
      throw new SysteemException(
          "Door een systeemfout bij het openen van de database " +
          "kan niet voldaan worden aan dit verzoek");
    }
  }
  
  /**
   * Geeft de enige instantie terug.
   * @return  de enige instantie van ConnectionPool
   * @throws SysteemException wanneer systeem operatie niet kan uitvoeren
   */
  static synchronized ConnectionPool getInstance() throws SysteemException {
    if (pool == null) {
      pool = new ConnectionPool();
    }
    return pool;
  }
  
  /**
   * Vraagt om een beschikbare connectie uit de pool die wordt
   * bijgehouden door de datasource.
   * @return  een connectie met de datasource
   * @throws SysteemException wanneer systeem operatie niet kan uitvoeren
   */
  Connection getConnection() throws SysteemException {
    try {
      return dataSource.getConnection();
    }
    catch (SQLException e) {
      e.printStackTrace();
      throw new SysteemException(
          "Door een systeemfout bij het verbinden met de database " +
          "kan niet voldaan worden aan dit verzoek");
    }
  }
  
  /**
   * Geeft een connectie terug aan de pool.
   * @param con  de connectie die vrijgegeven kan worden
   * @throws SysteemException wanneer systeem operatie niet kan uitvoeren
   */
  void freeConnection(Connection con) throws SysteemException {
    try {
      con.close();
    }
    catch (SQLException e) {
      e.printStackTrace();
      throw new SysteemException(
          "Door een systeemfout bij het sluiten van een databaseverbinding " +
          "kan niet voldaan worden aan dit verzoek");
    }
  }
    
}
