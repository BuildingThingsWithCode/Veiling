package veiling.servlets;

import java.io.*;
import java.util.*;
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
 * Servlet klasse voor het tonen van het aanbod (doGet)
 * en het bieden op een aanbod (doPost).
 * @author Open Universiteit
 */
public class AanbodServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Toont het huidige aanbod voor een bepaalde rubriek.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url;
		int rubriek = 0;
		try {      
			String param = request.getParameter("rubriek");
			if (param == null) {
				rubriek = 0; // betekent : alles
			} else {
				rubriek = Integer.parseInt(request.getParameter("rubriek"));
			}
			ArrayList<Aanbod> inveiling = VeilingBeheer.getInstance().getHuidigAanbod(rubriek);
			request.setAttribute("inveiling", inveiling);
			url = "/aanbod_tonen.jsp";
		}
		catch (SysteemException e) {
			url = "/algemeen_fout.jsp";
			request.setAttribute("foutmelding", e.getMessage());
		} 
		catch (Exception e) {
			url = "/algemeen_fout.jsp";
			request.setAttribute("foutmelding", "Fout bij opvragen aanbod");
		}
		// stuur request and response door naar het antwoord
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
		dispatcher.forward(request, response); 
	}

	/**
	 * Verwerkt het bieden op het aanbod af.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url;
		HttpSession session = request.getSession();
		Gebruiker lid = (Gebruiker)session.getAttribute("gebruiker");
		double biedbedrag = 0.0;
		try {
			try {
				biedbedrag = Conversie.parseGeld(request.getParameter("biedbedrag"));
			}
			catch (ParseException e) {
				throw new VeilingException("Incorrect biedbedrag ");
			}
			int aanbodid = Integer.parseInt(request.getParameter("aanbodid"));
			Aanbod aanbod = VeilingIO.getInstance().getAanbod(aanbodid);
			Bod bod = new Bod();
			//regel 74 t.e.m. 102 is toegevoegd
			/*opm autoBod kan als mogelijke waarden hebben aangevinkt of null
			 * Indien AutoBod als waarde "aangevinkt" heeft dan moet
			 * bod.SetBedrag(huidige hoogste bod + 0,5) worden en 
			 *  en moet bod.setAutoBod(biedbedrag) worden. in het andere geval
			 *  moet er niets gebeuren.
			 */
			String autoBod = null;
			autoBod = request.getParameter("autobod");
			if (autoBod != null){
				double huidigeMinimumbod = VeilingBeheer.getInstance().minimaalBod(aanbod);
				/*
				 * we controleren of het bedrag dat men als automatisch bod invoert
				 * groter is dan het minimumbod. 
				 */
				if (huidigeMinimumbod >= biedbedrag){
					throw new VeilingException("Als u gebruik wil maken van het automatisch bod, moet het bedrag dat u invult groter zijn dan "+huidigeMinimumbod);
				}
				bod.setAutoBod(biedbedrag);
				bod.setBedrag(huidigeMinimumbod);
				bod.setAanbod(aanbod);
				bod.setLid(lid);
				VeilingBeheer.getInstance().doeBod(bod);
				request.setAttribute("bericht", "Uw bod van " +
						(huidigeMinimumbod) + " op " +
						aanbod.getNaam() + " is geslaagd. Er zal automatisch worden geboden voor u tot een maximum van "+biedbedrag);
				url = "/algemeen_ok.jsp";
			}
			else{  
				bod.setBedrag(biedbedrag);
				bod.setAanbod(aanbod);
				bod.setLid(lid);
				VeilingBeheer.getInstance().doeBod(bod);
				request.setAttribute("bericht", "Uw bod van " +
						Conversie.formatGeld(biedbedrag) + " op " +
						aanbod.getNaam() + " is geslaagd");
				url = "/algemeen_ok.jsp";
			}
		}
		catch (VeilingException e) {
			url = "/algemeen_fout.jsp";
			request.setAttribute("foutmelding", e.getMessage());
		}
		catch (SysteemException e) {
			url = "/algemeen_fout.jsp";
			request.setAttribute("foutmelding", e.getMessage());
		} 

		// stuur request and response door naar het antwoord
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

}
