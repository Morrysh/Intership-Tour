package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.AziendaDAO;
import dao.impl.OffertaTirocinioDAO;
import data.model.Azienda;
import data.model.OffertaTirocinio;
import data.model.enumeration.CampoRicercaTirocinio;
import framework.data.DataLayerException;
import framework.result.FailureResult;
import framework.result.TemplateManagerException;
import framework.result.TemplateResult;
import framework.security.SecurityLayer;

@SuppressWarnings("serial")
public class HomePage extends IntershipTutorBaseController {

	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
	    	TemplateResult res = new TemplateResult(getServletContext());
	        
	        // Lista aziende convenzionate
	        List<Azienda> aziende = new AziendaDAO().allAziendeAccordingToConvention(true);
	        
	        // Verifica della pagina, impostata sulla prima(pagina 0) di default
	        int paginaCorrente = 0;
	        if(request.getParameter("pagina") != null) {
	        	paginaCorrente = (SecurityLayer.checkNumeric(request.getParameter("pagina")));
	        }
	        
	        Map<CampoRicercaTirocinio, String> campiDaCercare = new HashMap<>();
	        
	        for(CampoRicercaTirocinio campoRicerca : CampoRicercaTirocinio.values()) {
	        	if(request.getParameter(campoRicerca.name()) != "" &&
	        	   request.getParameter(campoRicerca.name()) != null) {
	        		campiDaCercare.put(campoRicerca, (String) request.getParameter(campoRicerca.name()));
	        	}
	        }
	        
	        // La lista verrà riempita con tutte le offerte se l'utente non ha filtrato la ricerca;
	        // con le offerte risultanti dalla ricerca in caso contrario
	        List<OffertaTirocinio> offerte;
	        
	        // Totale delle offerte visibili all'utente / 5 (5 offerte per pagina) = numero delle pagine
	        int numeroPagine;
	        
	        // Filtraggio avvenuto da parte dell'utente
	        if(campiDaCercare.size() != 0) {
	        	// Tutte le offerte in base alla ricerca
	        	offerte = new OffertaTirocinioDAO().filtraPerCampo(campiDaCercare, paginaCorrente);
	        	// Massimo 5 offerte per pagina
	            // il .0 è necessario per il cast di Java (1.1 deve essere arrotondato a 2 per esempio)
	        	numeroPagine = (int) Math.ceil(new OffertaTirocinioDAO().getCountAccordingToRicerca(campiDaCercare) / 5.0);
	        }
	        // Nessun filtraggio
	        else {
	        	// Tutte le offerte senza filtraggio
	            offerte = new OffertaTirocinioDAO().allOfferteInRange(paginaCorrente);
	         // Massimo 5 offerte per pagina
	            // il .0 è necessario per il cast di Java (1.1 deve essere arrotondato a 2 per esempio)
	            numeroPagine = (int) Math.ceil(new OffertaTirocinioDAO().getCountAccordingToConvention(true) / 5.0);
	        }
	        
	        // Per paginare le offerte filtrate
	        String queryString = "";
	        if(request.getQueryString() != null)
	        	queryString = "&" + request.getQueryString();
	        
	        request.setAttribute("page_css", "homepage");
	        request.setAttribute("campiRicerca", CampoRicercaTirocinio.values());
			request.setAttribute("offerte", offerte);
			request.setAttribute("aziende", aziende);
			request.setAttribute("numeroPagine", numeroPagine);
			request.setAttribute("paginaCorrente", paginaCorrente);
			request.setAttribute("queryString", queryString);
			
			res.activate("homepage.ftl.html", request, response);
        }
        catch (DataLayerException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

    	try {
            action_default(request, response);

        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        } 
    }

    @Override
    public String getServletInfo() {
        return "Main IntershipTutor servlet";
    }

}
