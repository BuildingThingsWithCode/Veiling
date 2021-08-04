package veiling.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import veiling.domein.*;

/**
 * Mapperklasse voor operaties op de database met betrekking tot gebruikers van
 * de veiling (leden en beheerder).
 * Singletonklasse die verbinding maakt met de database. Er zijn methoden om
 * <ul>
 * <li>leden te zoeken</li>
 * <li>leden te voegen</li>
 * <li>beheerder te zoeken</li>
 * </ul>
 * @author Open Universiteit
 */
public class GebruikerIO {

  // Enige instantie.
  private static GebruikerIO instance = null;

  // constanten voor de gebruikte SQL-opdrachten
  private static final String SQLGETLID = "SELECT * FROM lid WHERE id = ?";
  private static final String SQLGETLIDEMAIL = "SELECT * FROM lid WHERE email = ?";
  private static final String SQLGETADMIN = "SELECT * FROM beheer WHERE naam = ?";
  private static final String SQLADDLID = "INSERT INTO lid (naam, adres, postcode, plaats, email, wachtwoord) values (?,?,?,?,?,?)";

  /**
   * Haalt de enige instantie op.
   * @return de enige instantie van GebruikerIO
   */
  public static synchronized GebruikerIO getInstance() {
    if (instance == null) {
      instance = new GebruikerIO();
    }
    return instance;
  }

  /**
   * Private constructor.
   */
  private GebruikerIO() {
  }

  /**
   * Haalt de lid met gegeven id uit de database.
   * @param id  identificatie (sleutel) van lid
   * @return gevraagde lid of null als die er niet is.
   * @throws SysteemException wanneer operatie niet kan worden uitgevoerd
   */
  public Gebruiker getLid(int id) throws SysteemException {
    ConnectionPool pool = ConnectionPool.getInstance();
    Connection con = pool.getConnection();
    PreparedStatement prepGetUser = null;
    Gebruiker lid = null;
    try {
      prepGetUser = con.prepareStatement(SQLGETLID);
      prepGetUser.setInt(1, id);
      ResultSet res = prepGetUser.executeQuery();
      if (res.next()) {
        lid = leesGegevensLid(res);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SysteemException(
          "Door een systeemfout bij het ophalen van lid "
              + "kan niet voldaan worden aan dit verzoek");
    } finally {
      closePreparedStatement(prepGetUser);
      pool.freeConnection(con);
    }
    return lid;
  }

  /**
   * Haalt lid met gegeven emailadres uit de database.
   * @param email emailadres van lid
   * @return lid met gegeven emailadres of null als die er niet is.
   * @throws SysteemException wanneer operatie niet kan worden uitgevoerd
   */
  public Gebruiker getLid(String email) throws SysteemException {
    ConnectionPool pool = ConnectionPool.getInstance();
    Connection con = pool.getConnection();
    PreparedStatement prepGetUser = null;
    Gebruiker lid = null;
    try {
      prepGetUser = con.prepareStatement(SQLGETLIDEMAIL);
      prepGetUser.setString(1, email);
      ResultSet res = prepGetUser.executeQuery();
      if (res.next()) {
        lid = leesGegevensLid(res);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SysteemException(
          "Door een systeemfout bij het ophalen van lid "
              + "kan niet voldaan worden aan dit verzoek");
    } finally {
      closePreparedStatement(prepGetUser);
      pool.freeConnection(con);
    }
    return lid;
  }

  /**
   * Voegt lid toe aan de database.
   * @param lid  het toe te voegen lid
   * @throws SysteemException wanneer operatie niet kan worden uitgevoerd
   */
  public void addLid(Gebruiker lid) throws SysteemException {
    ConnectionPool pool = ConnectionPool.getInstance();
    Connection con = pool.getConnection();
    PreparedStatement prepAddUser = null;
    try {
      prepAddUser = con.prepareStatement(SQLADDLID);
      prepAddUser.setString(1, lid.getNaam());
      prepAddUser.setString(2, lid.getAdres());
      prepAddUser.setString(3, lid.getPostcode());
      prepAddUser.setString(4, lid.getPlaats());
      prepAddUser.setString(5, lid.getEmail());
      prepAddUser.setString(6, lid.getWachtwoord());
      prepAddUser.executeUpdate();
      // vraag gegenereerd id op en voeg dat toe aan gebruiker
      ResultSet res = prepAddUser.getGeneratedKeys();
      if (res.next()) {
        lid.setId(res.getInt(1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SysteemException(
          "Door een systeemfout bij het toevoegen van een gebruiker "
              + "kan niet voldaan worden aan dit verzoek");
    } finally {
      closePreparedStatement(prepAddUser);
      pool.freeConnection(con);
    }
  }

  /**
   * Haalt de beheerder op met de gegeven gebruikersnaam.
   * @param naam naam van beheerder
   * @return de gevraagde beheerder of null als die er niet is
   * @throws SysteemException wanneer operatie niet kan worden utigevoerd
   */
  public Gebruiker getBeheerder(String naam) throws SysteemException {
    ConnectionPool pool = ConnectionPool.getInstance();
    Connection con = pool.getConnection();
    PreparedStatement prepGetAdmin = null;
    Gebruiker gebruiker = null;
    try {
      prepGetAdmin = con.prepareStatement(SQLGETADMIN);
      prepGetAdmin.setString(1, naam);
      ResultSet res = prepGetAdmin.executeQuery();
      if (res.next()) {
        gebruiker = new Gebruiker();
        gebruiker.setId(0);
        gebruiker.setNaam(naam);
        gebruiker.setWachtwoord(res.getString("wachtwoord"));
        gebruiker.setBeheerder(true);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SysteemException(
          "Door een systeemfout bij het opzoeken van de beheersgegevens "
              + "kan niet voldaan worden aan dit verzoek");
    } finally {
      closePreparedStatement(prepGetAdmin);
      pool.freeConnection(con);
    }
    return gebruiker;
  }

  /**
   * Leest gegevens lid uit resultset en maakt lidobject daarvan.
   * Hulpmethode
   * @param rs  Resultset
   * @return gebruikersobject met lidgegevens
   * @throws SQLException
   */
  private Gebruiker leesGegevensLid(ResultSet res) throws SQLException {
    Gebruiker gebruiker = new Gebruiker();
    gebruiker.setId(res.getInt("id"));
    gebruiker.setNaam(res.getString("naam"));
    gebruiker.setAdres(res.getString("adres"));
    gebruiker.setPostcode(res.getString("postcode"));
    gebruiker.setPlaats(res.getString("plaats"));
    gebruiker.setEmail(res.getString("email"));
    gebruiker.setWachtwoord(res.getString("wachtwoord"));
    gebruiker.setBeheerder(false);
    return gebruiker;
  }

  /**
   * Sluit een prepared statement af.
   * @param ps het prepared statement
   * @throws SysteemException
   */
  private void closePreparedStatement(PreparedStatement ps)
      throws SysteemException {
    try {
      if (ps != null) {
        ps.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SysteemException(
          "Door een systeemfout bij het sluiten van een databaseopdracht "
              + "kan niet voldaan worden aan dit verzoek");
    }
  }

}
