package veiling.servlets;

import java.io.IOException;
import java.text.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import veiling.data.VeilingIO;
import veiling.domein.*;

/**
 * Servlet klasse voor het verwerken van aangeboden artikel (doPost).
 * @author Open Universiteit
 */
public class ArtikelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * Verwerkt de ingevulde gegevens voor een aanbod (artikel).
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // haal de parameters op uit de request
	  String naam = request.getParameter("naam");
	  String omschrijving = request.getParameter("omschrijving");
	  String minbod = request.getParameter("minbod");
	  String startdatum = request.getParameter("startdatum");
	  String einddatum = request.getParameter("einddatum");
	  String rubrieknaam = request.getParameter("rubriek");
	  HttpSession session = request.getSession();
	  Gebruiker lid = (Gebruiker)session.getAttribute("gebruiker");

	  Aanbod aanbod = new Aanbod();
	  aanbod.setNaam(naam);
	  aanbod.setOmschrijving(omschrijving);

	  String url = "";
	  String foutmelding = "";
	  boolean fout = false;

	  // controleer invoer
	  if ("".equals(naam) || "".equals(omschrijving) || "".equals(minbod)) {
	    fout = true;
	    foutmelding = foutmelding + "Niet alle velden ingevuld<br>";
	  } 
	  try {
	    aanbod.setMinbod(Conversie.parseGeld(minbod));
	  } catch (ParseException e) {
	    fout = true;
	    foutmelding = foutmelding + "Geen geldig startbedrag<br>";
	  }

	  if ("".equals(startdatum)) {
	    aanbod.setStartdatum(DatumUtil.vandaag());
	  } else {
	    try {
	      aanbod.setStartdatum(Conversie.parseDatum(startdatum));
	    } catch (ParseException e) {
	      fout = true;
	      foutmelding = foutmelding + "Geen geldige startdatum<br>";
	    }
	  }
	  if ("".equals(einddatum)) {
	    aanbod.setEinddatum(DatumUtil.vandaag());
	  } else {
	    try {
	      aanbod.setEinddatum(Conversie.parseDatum(einddatum));
	    } catch (ParseException e) {
	      fout = true;
	      foutmelding = foutmelding + "Geen geldige einddatum<br>";
	    }
	  }
	  if (aanbod.getEinddatum().before(aanbod.getStartdatum())) {
      fout = true;
      foutmelding = foutmelding + "Einddatum kan niet voor startdatum liggen<br>";
	  }
    if ("".equals(rubrieknaam)) {
      fout = true;
      foutmelding = foutmelding + "Kies een rubriek<br>";
    } 

	  if (fout) {
	    url = "/aanbod_registratie.jsp";
	    request.setAttribute("aanbod", aanbod);
	    request.setAttribute("foutmelding", foutmelding);
	  } else {

	    try {
	      // voeg het nieuwe aanbod toe aan de database 
	      Rubriek rubriek = VeilingIO.getInstance().getRubriek(rubrieknaam);
	      aanbod.setLid(lid);
	      aanbod.setRubriek(rubriek);
	      VeilingBeheer.getInstance().registreerAanbod(aanbod);
	      request.setAttribute("bericht", "Registreren artikel " + 
	          aanbod.getNaam() + " is geslaagd");
	      url = "/algemeen_ok.jsp";
	    }

	    catch (VeilingException e) {
	      url = "/aanbod_registratie.jsp";
	      request.setAttribute("aanbod", aanbod);
	      request.setAttribute("foutmelding", e.getMessage());
	    }
	    catch (SysteemException e) {
	      url = "/algemeen_fout.jsp";
	      request.setAttribute("foutmelding", e.toString());
	    }
	  }
	  
	  // stuur request and response door naar het antwoord
	  RequestDispatcher dispatcher =
	    getServletContext().getRequestDispatcher(url);
	  dispatcher.forward(request, response);   
	}

}
