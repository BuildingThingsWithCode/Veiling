package veiling.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import veiling.domein.*;


/**
 * Servlet klasse voor het beheren van rubrieken.
 * @author Open Universiteit
 */
public class RubriekServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  /**
   * Leest gegevens rubriek en slaat rubriek op.
   */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // haal de parameters op uit de request
    String naam = request.getParameter("naam");
    Rubriek rubriek = new Rubriek();
    rubriek.setNaam(naam);
    
    // controleer invoer
    String url = "";
    if ("".equals(naam)) {
      url = "/rubriek_registratie.jsp";
      String foutmelding = "Niet alle velden ingevuld";
      request.setAttribute("rubriek", rubriek);
      request.setAttribute("foutmelding", foutmelding);
    } 
    else {
      // voeg de nieuwe rubriek toe aan de database
      try {
        VeilingBeheer.getInstance().registreerRubriek(rubriek);
        url = "/algemeen_ok.jsp";
        String bericht = "Registreren rubriek " + rubriek.getNaam() + " geslaagd";
        request.setAttribute("bericht", bericht);
        
        // vul application scope met alle rubrieken omdat nieuwe rubriek is toegevoegd
        ArrayList<Rubriek> rubrieken = VeilingBeheer.getInstance().getAlleRubrieken();
        getServletContext().setAttribute("rubrieken", rubrieken);
      }
      catch (VeilingException e) {
        url = "/rubriek_registratie.jsp";
        request.setAttribute("rubriek", rubriek);
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
