package veiling.servlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import veiling.domein.*;

/**
 * Servlet klasse die dient als controller. Deze vangt alle requests
 * op (van menu's en formulieren), kijkt of gebruiker deze actie mag uitvoeren
 * en delegeert werk aan jsp of servlet.
 * @author Open Universiteit
 */
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
  /**
   * Verwerkt http requests voor get.
   * Controleert of gebruiker toegestaan is operatie uit te voeren.
   */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String forward = "";
    String bron = request.getParameter("bron");
    HttpSession session = request.getSession();
    Gebruiker gebruiker = (Gebruiker)session.getAttribute("gebruiker");
    boolean isLid = false;
    boolean isBeheerder = false;
    if (gebruiker != null) {
      isBeheerder = gebruiker.isBeheerder();
      isLid = !isBeheerder;
    }
    
    String foutmelding = "";

    // vul applicatie met alle rubrieken (indien nog niet gedaan)
    ServletContext sc = this.getServletContext();
    if (sc.getAttribute("rubrieken") == null) {
      try {
        ArrayList<Rubriek> rubrieken = VeilingBeheer.getInstance().getAlleRubrieken();
        sc.setAttribute("rubrieken", rubrieken);
      } catch (SysteemException e) {
        foutmelding = "Fout bij ophalen rubrieken ";
        request.setAttribute("foutmelding", foutmelding);
        forward = "/algemeen_fout.jsp";
      }
    }
    
    if (bron.equals("home")) { 
      forward = "/home.jsp";
    }
    else if (bron.equals("registratie")) {
      if (isLid) {
        foutmelding = "Log eerst uit voordat u zich registreert ";
        request.setAttribute("foutmelding", foutmelding);
        forward = "/algemeen_fout.jsp";
      } else {
        forward = "/registratie.jsp";
      }
    }
    
    else if (bron.equals("aanbod_tonen")) {
       forward = "/AanbodServlet";
    }
    
    else if (bron.equals("login")) {
      if (isBeheerder || isLid) {
        foutmelding = "Log eerst uit voordat u weer inlogt ";
        request.setAttribute("foutmelding", foutmelding);
        forward = "/algemeen_fout.jsp";
      } else {
        forward = "/login.jsp";
      }
    }
    
    else if (bron.equals("logout")) {
      forward = "/LogoutServlet";
    }
    
    else if (bron.equals("aanbod_registratie")) {
      if (!isLid) {
        foutmelding = "U dient ingelogd te zijn om een artikel te verkopen ";
        request.setAttribute("foutmelding", foutmelding);
        forward = "/algemeen_fout.jsp";
      } else {
        forward = "/aanbod_registratie.jsp";
      }
    }
    
    else if (bron.equals("rubriek_registratie")) {
      if (!isBeheerder) {
        foutmelding = "Alleen de beheerder kan rubrieken toevoegen ";
        request.setAttribute("foutmelding", foutmelding);
        forward = "/algemeen_fout.jsp";
      } else {
        forward = "/rubriek_registratie.jsp";
      }
    }
    
    //de "else if" hieronder is toegevoegd
    else if (bron.equals("financieel_overzicht")) {
        if (!isBeheerder) {
          foutmelding = "Alleen de beheerder kan het financieel overzicht bekijken ";
          request.setAttribute("foutmelding", foutmelding);
          forward = "/algemeen_fout.jsp";
        } else {
          forward = "/financieel_overzicht.jsp";
        }
      }

    else {
      request.setAttribute("method", "ControllerServlet.doPost");
      forward = "/unknown_bron.jsp";
    } 
    
    RequestDispatcher dispatcher = 
      getServletContext().getRequestDispatcher(forward);
    dispatcher.forward(request, response);
	}

  /**
   * Verwerkt http requests voor post.
   * Controleert daarbij of gebruiker toegestaan is operatie uit te voeren.
   */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String forward = "";
    String bron = request.getParameter("bron");
    HttpSession session = request.getSession();

    Gebruiker gebruiker = (Gebruiker)session.getAttribute("gebruiker");
    boolean isLid = false;
    boolean isBeheerder = false;
    if (gebruiker != null) {
      isBeheerder = gebruiker.isBeheerder();
      isLid = !isBeheerder;
    }
    
    String foutmelding = "";
    
    if (bron.equals("registratie")) {
      if (isLid) {
        foutmelding = "Log eerst uit voordat u zich registreert ";
        request.setAttribute("foutmelding", foutmelding);
        forward = "/algemeen_fout.jsp";
      } else {
        forward = "/RegistratieServlet";
      }
    }
    
    else if (bron.equals("login")) {
      if (isBeheerder || isLid) {
        foutmelding = "Log eerst uit voordat u weer inlogt ";
        request.setAttribute("foutmelding", foutmelding);
        forward = "/algemeen_fout.jsp";
      } else {
        forward = "/LoginServlet";
      }
    }
      
    else if (bron.equals("aanbod_registratie")) {
      if (isLid) {
        forward = "/ArtikelServlet";
      } else {
        foutmelding = "U dient ingelogd te zijn om een artikel te verkopen ";
        request.setAttribute("foutmelding", foutmelding);
        forward = "/algemeen_fout.jsp";
      }
    }
    
    else if (bron.equals("aanbod_bieden")) {
      if (isLid) {
        forward = "/AanbodServlet";
      } else {
        foutmelding = "U dient ingelogd te zijn om te kunnen bieden ";
        request.setAttribute("foutmelding", foutmelding);
        forward = "/algemeen_fout.jsp";
      }
    }
    
    else if (bron.equals("rubriek_registratie")) {
      if (isBeheerder) {
        forward = "/RubriekServlet";
      } else {
        foutmelding = "Alleen de beheerder kan rubrieken toevoegen ";
        request.setAttribute("foutmelding", foutmelding);
        forward = "/algemeen_fout.jsp";
      }
    }
    
  //de "else if" hieronder is toegevoegd
    else if (bron.equals("financieel_overzicht")) {
        if (isBeheerder) {
          forward = "/FinancieelServlet";
        } else {
          foutmelding = "Alleen de beheerder kan het financieel overzicht bekijken ";
          request.setAttribute("foutmelding", foutmelding);
          forward = "/algemeen_fout.jsp";
        }
      }
    
    else {
      forward = "/unknown_bron.jsp";
    }
    
    RequestDispatcher dispatcher = 
      getServletContext().getRequestDispatcher(forward);
    dispatcher.forward(request, response);
	}
	
}
