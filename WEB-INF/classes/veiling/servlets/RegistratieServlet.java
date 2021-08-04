package veiling.servlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import veiling.domein.*;


/**
 * Servlet klasse voor de registratie van een nieuwe gebruiker.
 * @author Open Universiteit
 */
public class RegistratieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * Leest de ingevulde gegevens en maakt een nieuwe gebruiker aan.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // haal de parameters op uit de request
    String naam = request.getParameter("naam");
    String adres = request.getParameter("adres");
    String postcode = request.getParameter("postcode");
    String plaats = request.getParameter("plaats");
    String email = request.getParameter("email");
    String wachtwoord1 = request.getParameter("wachtwoord1");
    String wachtwoord2 = request.getParameter("wachtwoord2");

    Gebruiker lid = new Gebruiker();
    lid.setNaam(naam);
    lid.setAdres(adres);
    lid.setPostcode(postcode);
    lid.setPlaats(plaats);
    lid.setEmail(email);
    lid.setWachtwoord(wachtwoord1);
    lid.setBeheerder(false);
    
    // controleer invoer
    String url = "";
    request.setAttribute("geregistreerd", lid);
    if ("".equals(naam) || "".equals(adres) || "".equals(plaats) ||
        "".equals(email) || "".equals(wachtwoord1) || "".equals(wachtwoord2)) {
      String foutmelding = "Niet alle velden ingevuld";
      url = "/registratie.jsp";
      // geef de foutboodschap door
      request.setAttribute("foutmelding", foutmelding);
    } 
    else if (wachtwoord1.equals(wachtwoord2)){
      // voeg de nieuwe gebruiker toe aan de database
      try {
        GebruikerBeheer.getInstance().registreerLid(lid);
        url = "/registratie_ok.jsp";
      }
      catch (VeilingException e) {
        url = "/registratie.jsp";
        lid.setWachtwoord("");
        request.setAttribute("foutmelding", e.getMessage());
      }
      catch (SysteemException e) {
        url = "/algemeen_fout.jsp";
        request.setAttribute("foutmelding", e.toString());
      }
    } else {
      String foutmelding = "Wachtwoorden ongelijk";
      url = "/registratie.jsp";
      lid.setWachtwoord("");
      request.setAttribute("foutmelding", foutmelding);
    }
       
    // stuur request and response door naar het antwoord
    RequestDispatcher dispatcher =
         getServletContext().getRequestDispatcher(url);
    dispatcher.forward(request, response);   
	}

}
