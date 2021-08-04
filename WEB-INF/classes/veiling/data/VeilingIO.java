package veiling.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import veiling.domein.*;

/**
 * Mapperklasse voor operaties op de database met betrekking tot rubrieken,
 * artikelen (aanbod) en biedingen van de veiling.
 * Singletonklasse die verbinding maakt met de database. Er zijn methoden om
 * <ul>
 * <li>rubrieken toe te voegen</li>
 * <li>rubrieken op te vragen</li>
 * <li>aanbod toe te voegen</li>
 * <li>aanbod op te vragen</li>
 * <li>bod toe te voegen</li>
 * </ul>
 * 
 * @author Open Universiteit
 */
public class VeilingIO {

	// Enige instantie.
	private static VeilingIO instance = null;

	// constanten
	private static final String ALLES = "Alles";

	// stringconstanten met gemeenschappelijke delen van sql-statements
	private static final String SUB_COND_INVEILING = 
			" (status = '" + Aanbod.STATUS_INVEILING + "') ";
	private static final String SUB_COND_ONVERWERKT = 
			" (status <= '" + Aanbod.STATUS_INVEILING + "') ";
	private static final String SUB_SELECTJOINMAXBOD =
			"SELECT aanbod.*, bod.bedrag FROM aanbod LEFT JOIN bod ON aanbod.id = bod.aanbod ";
	private static final String SUB_COND_JOINMAXBOD =
			" ((bod.bedrag IN (SELECT MAX(bedrag) FROM bod WHERE aanbod.id =bod.aanbod)) " +
					"OR (NOT EXISTS(SELECT * FROM bod WHERE aanbod.id=bod.aanbod))) ";
	private static final String SUB_ORDER = 
			" ORDER BY einddatum ASC, naam";

	// constanten voor de gebruikte SQL-opdrachten
	private static final String SQLGETRUBRIEKID =
			"SELECT * FROM rubriek WHERE id = ?";
	private static final String SQLGETRUBRIEKNAAM =
			"SELECT * FROM rubriek WHERE naam = ?";
	private static final String SQLGETALLERUBRIEKEN =
			"SELECT * FROM rubriek ORDER BY naam ASC";
	private static final String SQLADDRUBRIEK =
			"INSERT INTO rubriek (naam) values (?)";
	private static final String SQLGETAANBOD =
			SUB_SELECTJOINMAXBOD + "WHERE aanbod.id = ? AND " + SUB_COND_JOINMAXBOD;
	private static final String SQLADDAANBOD =
			"INSERT INTO aanbod (naam, omschrijving, lid, rubriek, minbod, startdatum, einddatum, status) values (?,?,?,?,?,?,?,?)";
	private static final String SQLUPDATEAANBOD =
			"UPDATE aanbod SET status= ? WHERE id=?";
	private static final String SQLGETHUIDIGAANBOD =
			SUB_SELECTJOINMAXBOD + "WHERE " + SUB_COND_INVEILING +
			" AND " + SUB_COND_JOINMAXBOD + SUB_ORDER;
	private static final String SQLGETHUIDIGAANBODRUBRIEK =
			SUB_SELECTJOINMAXBOD + "WHERE rubriek = ? AND " + SUB_COND_INVEILING + 
			" AND " + SUB_COND_JOINMAXBOD + SUB_ORDER;
	private static final String SQLGETONVERWERKTAANBOD =
			SUB_SELECTJOINMAXBOD + "WHERE " + SUB_COND_ONVERWERKT +
			" AND " + SUB_COND_JOINMAXBOD;
	private static final String SQLADDBOD =
			"INSERT INTO bod (aanbod, lid, bedrag, datum, autoBod) values (?,?,?,?,?)";
	private static final String SQLGETHOOGSTEBOD =
			"SELECT * FROM bod WHERE aanbod = ? ORDER BY bedrag DESC LIMIT 1"; 
	//constanten die zijn toegevoegd
	private static final String SQLGETOBSERVERS =
			"SELECT * FROM bod WHERE autobod > 0 AND aanbod = ? ORDER BY id DESC";
	private static final String SQLGETOBSERVERCONTROLE = 
			"SELECT * FROM bod WHERE autobod = 0 AND aanbod = ? AND lid = ? AND id > ?";
	private static final String SUB_COND_VERKOCHT = 
			" (status = '" + Aanbod.STATUS_VERKOCHT + "') ";
	private static final String SQLGETVERKOCHTAANBOD =
			SUB_SELECTJOINMAXBOD + "WHERE " + SUB_COND_VERKOCHT +
			" AND " + SUB_COND_JOINMAXBOD;


	/**
	 * Haalt de enige instantie op.
	 * @return de enige instantie van GebruikerIO
	 */
	public static synchronized VeilingIO getInstance() {
		if (instance == null) {
			instance = new VeilingIO();
		}
		return instance;
	}

	/**
	 * Private constructor
	 */
	private VeilingIO() {
	}

	/**
	 * Haalt de rubriek met gegeven id uit de database.
	 * @param id     identificatie van rubriek
	 * @return rubriek met gegeven identificatie of null als die er niet is.
	 * @throws SysteemException wanneer systeem operatie niet kan uitvoeren
	 */
	public Rubriek getRubriek(int id) throws SysteemException {
		if (id == 0) {
			return new Rubriek(0, ALLES);
		}
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = pool.getConnection();
		PreparedStatement prepstat = null;
		Rubriek rubriek = null;
		try {
			prepstat = con.prepareStatement(SQLGETRUBRIEKID);
			prepstat.setInt(1, id);
			ResultSet res = prepstat.executeQuery();
			if (res.next()) {
				rubriek = leesGegevensRubriek(res);
				return rubriek;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SysteemException(
					"Door een systeemfout bij het ophalen van een rubriek "
							+ "kan niet voldaan worden aan dit verzoek");
		} finally {
			closePreparedStatement(prepstat);
			pool.freeConnection(con);
		}
		return rubriek;
	}

	/**
	 * Haalt de rubriek met gegeven naam uit de database.
	 * @param naam     naam van rubriek
	 * @return rubriek met gegeven naam of null als die er niet is.
	 * @throws SysteemException wanneer systeem operatie niet kan uitvoeren
	 */
	public Rubriek getRubriek(String naam) throws SysteemException {
		if (ALLES.equals(naam)) {
			return new Rubriek(0, ALLES);
		}
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = pool.getConnection();
		PreparedStatement prepstat = null;
		Rubriek rubriek = null;
		try {
			prepstat = con.prepareStatement(SQLGETRUBRIEKNAAM);
			prepstat.setString(1, naam);
			ResultSet res = prepstat.executeQuery();
			if (res.next()) {
				rubriek = leesGegevensRubriek(res);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SysteemException(
					"Door een systeemfout bij het ophalen van een rubriek "
							+ "kan niet voldaan worden aan dit verzoek");
		} finally {
			closePreparedStatement(prepstat);
			pool.freeConnection(con);
		}
		return rubriek;
	}

	/**
	 * Geeft een lijst met alle rubrieken.
	 * @return lijst met alle rubrieken
	 * @throws SysteemException wanneer systeem operatie niet kan uitvoeren
	 */
	public ArrayList<Rubriek> getAlleRubrieken() throws SysteemException{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = pool.getConnection();
		PreparedStatement prepstat = null;
		ArrayList<Rubriek> rubrieken = new ArrayList<Rubriek>();
		rubrieken.add(new Rubriek(0, ALLES));
		try {
			prepstat = con.prepareStatement(SQLGETALLERUBRIEKEN);
			ResultSet res = prepstat.executeQuery();
			while (res.next()) {
				rubrieken.add(leesGegevensRubriek(res));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SysteemException(
					"Door een systeemfout bij het ophalen van alle rubrieken "
							+ "kan niet voldaan worden aan dit verzoek");
		} finally {
			closePreparedStatement(prepstat);
			pool.freeConnection(con);
		}
		return rubrieken;
	}

	/**
	 * Voegt rubriek toe aan de database.
	 * @param rubriek     de toe te voegen rubriek
	 * @throws SysteemException wanneer systeem operatie niet kan uitvoeren
	 */
	public void addRubriek(Rubriek rubriek) throws SysteemException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = pool.getConnection();
		PreparedStatement prepstat = null;
		try {
			prepstat = con.prepareStatement(SQLADDRUBRIEK);
			prepstat.setString(1, rubriek.getNaam());
			prepstat.executeUpdate();
			// vraag gegenereerd id op en voeg dat toe aan gebruiker
			ResultSet res = prepstat.getGeneratedKeys();
			if (res.next()) {
				rubriek.setId(res.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SysteemException(
					"Door een systeemfout bij het toevoegen van een rubriek "
							+ "kan niet voldaan worden aan dit verzoek");
		} finally {
			closePreparedStatement(prepstat);
			pool.freeConnection(con);
		}
	}

	/**
	 * Haalt het aanbod met gegeven identificatie uit de database. 
	 * @param id     identificatie van aanbod
	 * @return aanbod met gegeven id of null als die er niet is.
	 * @throws SysteemException wanneer systeem operatie niet kan uitvoeren
	 */
	public Aanbod getAanbod(int id) throws SysteemException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = pool.getConnection();
		PreparedStatement prepstat = null;
		Aanbod aanbod = null;
		try {
			prepstat = con.prepareStatement(SQLGETAANBOD);
			prepstat.setInt(1, id);
			ResultSet res = prepstat.executeQuery();
			if (res.next()) {
				aanbod = leesGegevensAanbod(res);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SysteemException(
					"Door een systeemfout bij het ophalen van een aanbod "
							+ "kan niet voldaan worden aan dit verzoek");
		} finally {
			closePreparedStatement(prepstat);
			pool.freeConnection(con);
		}
		return aanbod;
	}

	/**
	 * Voegt aanbod van artikel toe aan de database.
	 * @param aanbod     het toe te voegen aanbod/artikel
	 * @throws SysteemException  wanneer systeem operatie niet kan uitvoeren
	 */
	public void addAandbod(Aanbod aanbod) throws SysteemException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = pool.getConnection();
		PreparedStatement prepstat = null;
		try {
			prepstat = con.prepareStatement(SQLADDAANBOD);
			prepstat.setString(1, aanbod.getNaam());
			prepstat.setString(2, aanbod.getOmschrijving());
			prepstat.setInt(3, aanbod.getLid().getId());
			prepstat.setInt(4, aanbod.getRubriek().getId());
			prepstat.setDouble(5, aanbod.getMinbod());
			prepstat.setDate(6, new java.sql.Date(aanbod.getStartdatum().getTime()));
			prepstat.setDate(7, new java.sql.Date(aanbod.getEinddatum().getTime()));
			prepstat.setInt(8, aanbod.getStatus());
			prepstat.executeUpdate();
			// vraag gegenereerd id op en voeg dat toe aan lid
			ResultSet res = prepstat.getGeneratedKeys();
			if (res.next()) {
				aanbod.setId(res.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SysteemException(
					"Door een systeemfout bij het toevoegen van aanbod "
							+ "kan niet voldaan worden aan dit verzoek");
		} finally {
			closePreparedStatement(prepstat);
			pool.freeConnection(con);
		}
	}

	/**
	 * Updates het gegeven aanbod in de database.
	 * @param aanbod het nieuwe aanbod
	 * @throws SysteemException wanneer systeem operatie niet kan uitvoeren
	 */
	public void updateAanbod(Aanbod aanbod) throws SysteemException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = pool.getConnection();
		PreparedStatement prepstat = null;
		try {
			prepstat = con.prepareStatement(SQLUPDATEAANBOD);
			prepstat.setInt(1, aanbod.getStatus());
			prepstat.setInt(2, aanbod.getId());
			int ret = prepstat.executeUpdate();
			if (ret != 1) {
				throw new SysteemException(
						"Door een systeemfout bij het updaten van het aanbod "
								+ "kan niet voldaan worden aan dit verzoek");
			}
		} catch (SQLException e) {
			throw new SysteemException(
					"Door een systeemfout bij het updaten van het aanbod "
							+ "kan niet voldaan worden aan dit verzoek");
		} finally {
			closePreparedStatement(prepstat);
			pool.freeConnection(con);
		}
	}

	/**
	 * Geeft lijst met het huidig aanbod voor gegeven rubriek.
	 * @param rubriek de rubriek
	 * @return lijst met huidig aanbod
	 * @throws SysteemException wanneer systeem operatie niet kan uitvoeren
	 */
	public ArrayList<Aanbod> getHuidigAanbod(int rubriek) throws SysteemException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = pool.getConnection();
		PreparedStatement prepstat = null;
		ArrayList<Aanbod> huidigAanbod = new ArrayList<Aanbod>();
		try {
			if (rubriek == 0) {
				// alle rubrieken
				prepstat = con.prepareStatement(SQLGETHUIDIGAANBOD);
			} else {
				// alleen bepaalde rubriek
				prepstat = con.prepareStatement(SQLGETHUIDIGAANBODRUBRIEK);
				prepstat.setInt(1, rubriek);
			}
			ResultSet res = prepstat.executeQuery();
			while (res.next()) {
				Aanbod aanbod = leesGegevensAanbod(res);     
				huidigAanbod.add(aanbod);
			}  
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SysteemException(
					"Door een systeemfout bij het ophalen van het huidige aanbod "
							+ "kan niet voldaan worden aan dit verzoek");
		} finally {
			closePreparedStatement(prepstat);
			pool.freeConnection(con);
		}
		return huidigAanbod;
	}


	/**
	 * Geeft lijst met het onverwerkt aanbod.
	 * Het onverwerkte aanbod is het aanbod waarvan de einddatum
	 * nog niet overschreden is. Het bevat naast het huidig aanbod ook
	 * het aanbod waarvan de startdatum nog niet is bereikt.
	 * @return lijst met het onverwerkte aanbod
	 * @throws SysteemException wanneer systeem operatie niet kan uitvoeren
	 */
	public ArrayList<Aanbod> getOnverwerktAanbod() throws SysteemException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = pool.getConnection();
		PreparedStatement prepstat = null;
		ArrayList<Aanbod> aanbodlijst = new ArrayList<Aanbod>();
		try {
			prepstat = con.prepareStatement(SQLGETONVERWERKTAANBOD);
			ResultSet res = prepstat.executeQuery();
			while (res.next()) {
				aanbodlijst.add(leesGegevensAanbod(res));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SysteemException(
					"Door een systeemfout bij het ophalen van het onverwerkte aanbod "
							+ "kan niet voldaan worden aan dit verzoek");
		} finally {
			closePreparedStatement(prepstat);
			pool.freeConnection(con);
		}
		return aanbodlijst;
	}

	/**
	 * Voegt bod op artikel toe aan de database.
	 * @param bod     het toe te voegen bod
	 * @throws SysteemException wanneer systeem operatie niet kan uitvoeren
	 */
	public void addBod(Bod bod) throws SysteemException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = pool.getConnection();
		PreparedStatement prepstat = null;
		try {
			prepstat = con.prepareStatement(SQLADDBOD);
			prepstat.setInt(1, bod.getAanbod().getId());
			prepstat.setInt(2, bod.getLid().getId());
			prepstat.setDouble(3, bod.getBedrag());
			prepstat.setDate(4, new java.sql.Date(bod.getDatum().getTime()));
			//volgende regel is toegevoegd
			prepstat.setDouble(5, bod.getAutoBod());
			prepstat.executeUpdate();
			// vraag gegenereerd id op en voeg dat toe aan lid
			ResultSet res = prepstat.getGeneratedKeys();
			if (res.next()) {
				bod.setId(res.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SysteemException(
					"Door een systeemfout bij het toevoegen van een lid "
							+ "kan niet voldaan worden aan dit verzoek");
		} finally {
			closePreparedStatement(prepstat);
			pool.freeConnection(con);
		}
	}

	/**
	 * Geeft het hoogste bod op aanbod.
	 * @param aanbod het aanbod
	 * @return het hoogste bod bij aanbod
	 * @throws SysteemException wanneer systeem operatie niet kan uitvoeren
	 */
	public Bod getHoogsteBod(Aanbod aanbod) throws SysteemException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = pool.getConnection();
		PreparedStatement prepstat = null;
		Bod bod = null;
		try {
			prepstat = con.prepareStatement(SQLGETHOOGSTEBOD);
			prepstat.setInt(1, aanbod.getId());
			ResultSet res = prepstat.executeQuery();
			if (res.next()) {
				bod = leesGegevensBod(res);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SysteemException(
					"Door een systeemfout bij het ophalen van het hoogste bod "
							+ "kan niet voldaan worden aan dit verzoek");
		} finally {
			closePreparedStatement(prepstat);
			pool.freeConnection(con);
		}
		return bod;
	}


	/**
	 * Leest gegevens rubriek uit resultset en maakt rubriekobject daarvan.
	 * Hulpmethode.
	 * @param rs     Resultset
	 * @return rubriekobject
	 * @throws SQLException
	 */
	private Rubriek leesGegevensRubriek(ResultSet rs) throws SQLException {
		Rubriek rubriek = new Rubriek();
		rubriek.setId(rs.getInt("id"));
		rubriek.setNaam(rs.getString("naam"));
		return rubriek;
	}

	/**
	 * Leest gegevens aanbod uit resultset en maakt aanbodobject daarvan.
	 * Hulpmethode.
	 * @param rs     Resultset
	 * @return aanbodobject
	 * @throws SQLException
	 */
	private Aanbod leesGegevensAanbod(ResultSet res) throws SQLException, SysteemException {

		Aanbod aanbod = new Aanbod();
		aanbod.setId(res.getInt("id"));
		aanbod.setNaam(res.getString("naam"));
		aanbod.setOmschrijving(res.getString("omschrijving"));
		aanbod.setMinbod(res.getDouble("minbod"));
		aanbod.setStartdatum(res.getDate("startdatum"));
		aanbod.setEinddatum(res.getDate("einddatum"));
		aanbod.setStatus(res.getInt("status"));

		int lidid = res.getInt("lid");
		Gebruiker lid = GebruikerIO.getInstance().getLid(lidid);
		aanbod.setLid(lid);
		int rubriekid = res.getInt("rubriek");
		Rubriek rubriek = VeilingIO.getInstance().getRubriek(rubriekid);
		aanbod.setRubriek(rubriek);

		// leest maximale bodwaarde wanneer er een bod is gedaan
		String maxbod = res.getString("bod.bedrag");
		if (maxbod != null) {
			aanbod.setMaxbod(Double.parseDouble(maxbod));
		}
		return aanbod;
	}

	/**
	 * Leest gegevens bod uit resultset en maakt bodobject daarvan.
	 * Hulpmethode.
	 * @param rs     Resultset
	 * @return bodobject
	 * @throws SQLException
	 */
	private Bod leesGegevensBod(ResultSet rs) throws SQLException, SysteemException {
		Bod bod = new Bod();
		bod.setId(rs.getInt("id"));
		bod.setBedrag(rs.getDouble("bedrag"));
		bod.setDatum(rs.getDate("datum"));
		bod.setAanbod(VeilingIO.getInstance().getAanbod(rs.getInt("aanbod")));
		bod.setLid(GebruikerIO.getInstance().getLid(rs.getInt("lid")));
		//volgende regel is toegevoegd
		bod.setAutoBod(rs.getDouble("autoBod"));
		return bod;
	}

	/**
	 * Sluit een prepared statement af.
	 * @param ps
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

	//de 4 methoden die zich hieronder bevinden zijn toegevoegd
	/*
	 * Methode om alle gebruikers die een automatisch bod hebben gedaan op
	 * een bepaald aanbod, op te halen. Zij zullen observers worden. Indien
	 * een gebruiker na het plaatsen van een automatisch bod op hetzelfde 
	 * aanbod een gewoon bod doet, wordt het automatisch bod overschreven
	 * en geldt nog enkel het gewone bod.
	 */
	public Aanbod getObservers(Aanbod aanbod) throws SysteemException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = pool.getConnection();
		PreparedStatement prepstat = null;
		try {
			prepstat = con.prepareStatement(SQLGETOBSERVERS);
			prepstat.setInt(1, aanbod.getId());
			ResultSet res = prepstat.executeQuery();
			while (res.next()) {
				Bod bod = leesGegevensBod(res);
				//we kijken na of de gebruiker nadien het autobod niet overschrijft door een gewoon bod
				if (getObserverControle(bod)){
					aanbod.addObserver(GebruikerIO.getInstance().getLid(res.getInt("lid")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SysteemException(
					"Door een systeemfout bij het ophalen van de automatische biedingen "
							+ "kan niet voldaan worden aan dit verzoek");
		} finally {
			closePreparedStatement(prepstat);
			pool.freeConnection(con);
		}
		return aanbod;
	}


	/*
	 * hulpmethode om ervoor te zorgen dat observers die na
	 * het plaatsen van een automatisch bod, een gewoon bod
	 * doen op hetzelfde aanbod, niet worden opgenomen als 
	 * observer van dat aanbod.
	 */
	public Boolean getObserverControle(Bod bod) throws SysteemException{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = pool.getConnection();
		PreparedStatement prepstat = null;
		try {
			prepstat = con.prepareStatement(SQLGETOBSERVERCONTROLE);
			prepstat.setInt(1, bod.getAanbod().getId());
			prepstat.setInt(2, bod.getLid().getId());
			prepstat.setInt(3, bod.getId());
			ResultSet res = prepstat.executeQuery();
			if (res.next()) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SysteemException(
					"Door een systeemfout bij het ophalen van de automatische biedingen "
							+ "kan niet voldaan worden aan dit verzoek");
		} finally {
			closePreparedStatement(prepstat);
			pool.freeConnection(con);
		}
		return true;
	}

	/*
	 * Methode om hoogste automatische bod, 2de hoogste automatisch
	 * bod, huidige hoogste bod op het aanbod, het lid dat het hoogste
	 * automatisch bod heeft gedaan en het aanbod waarover het gaat in
	 * een lijst te plaatsen.
	 *
	 */
	public ArrayList getAutoBodInfo(Aanbod aanbod) throws SysteemException{
		ArrayList<Bod> biedingen = new ArrayList<Bod>();
		ArrayList autoBodInfo = new ArrayList();
		double hoogste = 0.0; 
		double tweedeHoogste = 0.0;
		Gebruiker lidHoogste = null;
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = pool.getConnection();
		PreparedStatement prepstat = null;
		try {
			prepstat = con.prepareStatement(SQLGETOBSERVERS);
			prepstat.setInt(1, aanbod.getId());
			ResultSet res = prepstat.executeQuery();
			while (res.next()) {
				Bod bod = leesGegevensBod(res);
				//we kijken na of de gebruiker nadien het autobod niet overschrijft door een gewoon bod
				if (getObserverControle(bod)){
					biedingen.add(bod);
				}
			}
			//we zorgen ervoor dat indien een gebruiker meerdere autoBiedingen heeft gedaan enkel de meest recente telt
			Iterator<Bod> iterator = biedingen.iterator();
			ArrayList<Gebruiker> recenteUitBiedingen = new ArrayList<Gebruiker>();
			while (iterator.hasNext()){
				Bod itBod = (Bod)iterator.next();
				if (recenteUitBiedingen.contains(itBod.getLid())){
					iterator.remove();
				}
				else {
					recenteUitBiedingen.add(itBod.getLid());
				}
			}
			//we zoeken hoogste autobod, tweede hoogste autobod en lid met hoogste autobod op
			for (Bod bod:biedingen) {
				if (hoogste <= bod.getAutoBod()){
					/*
					 * als er een hoger automatisch bod is dan krijgt de tweede hoogste de
					 * waarde van de vroegere hoogste en de vroegere hoogste krijgt
					 * de waarde van het hoger automatisch bod.
					 */
					tweedeHoogste = hoogste;
					lidHoogste = bod.getLid();
					hoogste = bod.getAutoBod();
				}
				else if (tweedeHoogste < bod.getAutoBod()) {
					tweedeHoogste = bod.getAutoBod();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SysteemException(
					"Door een systeemfout bij het ophalen van de automatische biedingen "
							+ "kan niet voldaan worden aan dit verzoek");
		} finally {
			closePreparedStatement(prepstat);
			pool.freeConnection(con);
		}
		/*
		 * We plaatsen de informatie die de observers nodig hebben om in hun update() methode
		 * een automatisch bod te kunnen doen, in de ArrayList autoBodInfo
		 */
		autoBodInfo.add(lidHoogste);
		autoBodInfo.add(hoogste);
		autoBodInfo.add(tweedeHoogste);
		autoBodInfo.add(getHoogsteBod(aanbod));
		autoBodInfo.add(aanbod);
		return autoBodInfo;
	}

	/*
	 * Geeft lijst met het verkochte aanbod in de periode
	 * bepaald door de gegeven startdatum en de gegeven einddatum.
	 * Het verkochte aanbod is het aanbod waarvan de einddatum
	 * overschreden is en waarvan de status = STATUS_VERKOCHT. 
	 */
	public ArrayList<Aanbod> getVerkochtAanbod(Date startdatum, Date einddatum) throws SysteemException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = pool.getConnection();
		PreparedStatement prepstat = null;
		ArrayList<Aanbod> aanbodlijst = new ArrayList<Aanbod>();
		try {
			prepstat = con.prepareStatement(SQLGETVERKOCHTAANBOD);
			ResultSet res = prepstat.executeQuery();
			while (res.next()) {
				aanbodlijst.add(leesGegevensAanbod(res));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SysteemException(
					"Door een systeemfout bij het ophalen van het onverwerkte aanbod "
							+ "kan niet voldaan worden aan dit verzoek");
		} finally {
			closePreparedStatement(prepstat);
			pool.freeConnection(con);
		}
		/*
		 * we verwijderen het verkochte aanbod dat niet binnen de gegeven
		 * start- en einddatum ligt uit de lijst.
		 */
		Iterator<Aanbod> iterator = aanbodlijst.iterator();
		while (iterator.hasNext()){
			Aanbod iteratorAanbod = (Aanbod)iterator.next();
			if (iteratorAanbod.getEinddatum().before(startdatum) ||iteratorAanbod.getEinddatum().after(einddatum)){
				iterator.remove();
			}
		}
		return aanbodlijst;
	}
}

