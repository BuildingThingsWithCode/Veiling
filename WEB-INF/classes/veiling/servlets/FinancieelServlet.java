package veiling.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import veiling.data.VeilingIO;
import veiling.domein.Aanbod;
import veiling.domein.Conversie;
import veiling.domein.DatumUtil;
import veiling.domein.Rubriek;
import veiling.domein.SysteemException;
import veiling.domein.VeilingBeheer;

/**
 * Servlet klasse om het financieel overzicht
 * te bekijken
 * @author Lars Martens
 */
public class FinancieelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//haal parameters op uit de request
		String datumbijstart = request.getParameter("startdatum");
		String datumbijeinde = request.getParameter("einddatum");
		String url = "";
		String foutmelding = "";
		boolean fout = false;
		Date startdatum = null;
		Date einddatum = null;
		Double totaleVerkoop = 0.0;
		Double winst = 0.0;

		//controleer invoer
		if ("".equals(datumbijstart) || "".equals(datumbijeinde)) {
			fout = true;
			foutmelding = foutmelding + "Niet alle velden ingevuld<br>";
		}
		else {
			try {
				startdatum = Conversie.parseDatum(datumbijstart);
			} catch (ParseException e) {
				fout = true;
				foutmelding = foutmelding + "Geen geldige startdatum<br>";
			}
			try{
				einddatum = Conversie.parseDatum(datumbijeinde);
			}catch (ParseException e) {
				fout = true;
				foutmelding = foutmelding + "Geen geldige einddatum<br>";
			}
			if ((einddatum !=null && startdatum!=null) && einddatum.before(startdatum)){
				fout = true;
				foutmelding = foutmelding + "Einddatum kan niet voor startdatum liggen<br>";
			}
		}
		if (fout) {
			url = "/financieel_overzicht.jsp";
			request.setAttribute("startdatum", datumbijstart);
			request.setAttribute("einddatum", datumbijeinde);
			request.setAttribute("foutmelding", foutmelding);
		} else {
			try {
				//we vragen de verkochte artikelen op in de gegeven periode
				ArrayList<Aanbod> verkocht = VeilingBeheer.getInstance().getVerkocht(startdatum, einddatum);
				if (verkocht.size() == 0){
					foutmelding = foutmelding + "Er zijn geen verkochte artikelen in deze periode";
					request.setAttribute("foutmelding", foutmelding);
				}
				else {
				request.setAttribute("titel", "Overzicht verkochte artikelen:");
				//we tellen alle bedragen op om de totale omzet te berekenen
				for (Aanbod aanbod:verkocht){
					totaleVerkoop += aanbod.getMaxbod();
				}
				//winst bedraagt 5% op de omzet en we ronden af tot op 2 decimalen
				winst = (double)Math.round((totaleVerkoop * 0.05)*100)/100;
				request.setAttribute("totaleVerkoop", "Totale verkoop: "+totaleVerkoop);
				request.setAttribute("winst","Winst: "+winst+" (afgerond op 2 decimalen)");
				request.setAttribute("verkocht", verkocht);
				}
				request.setAttribute("startdatum", datumbijstart);
				request.setAttribute("einddatum", datumbijeinde);
				url = "//financieel_overzicht.jsp";
			}
			 catch (SysteemException e) {
				url = "/financieel_overzicht.jsp";
			    request.setAttribute("foutmelding", e.getMessage());
			}
		}
		// stuur request and response door naar het antwoord
		RequestDispatcher dispatcher =
				getServletContext().getRequestDispatcher(url);
		dispatcher.forward(request, response);   
	}
}



