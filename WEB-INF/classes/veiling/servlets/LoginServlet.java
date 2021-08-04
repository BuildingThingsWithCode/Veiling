package veiling.servlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import veiling.domein.*;


/**
 * Servlet klasse voor het login-proces.
 * @author Open Universiteit
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  /**
   * Coordineert het inlog-proces.
   */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // haal de parameters op uit de request
    String naam = request.getParameter("naam");
    String wachtwoord = request.getParameter("wachtwoord");
    HttpSession session = request.getSession();
    
    // controleer invoer
    String url = "";
    if ("".equals(naam) ||  "".equals(wachtwoord) ) {
      url = "/login.jsp";
      String foutmelding = "Niet alle velden ingevuld";
      request.setAttribute("foutmelding", foutmelding);
    } 
    else {
      // kijk of wachtwoord geldig is
      try {
        Gebruiker ingelogd = GebruikerBeheer.getInstance().login(naam, wachtwoord);
        if (ingelogd == null) {
          url = "/login.jsp";
          String foutmelding = "Niet ingelogd: Naam-wachtwoordcombinatie incorrect";
          request.setAttribute("foutmelding", foutmelding);
        } else {
          session.setAttribute("gebruiker", ingelogd);
          url = "/login_ok.jsp";
        }
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
