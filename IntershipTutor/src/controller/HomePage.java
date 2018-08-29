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
import dao.impl.ParereAziendaDAO;
import dao.impl.TirocinioStudenteDAO;
import dao.impl.UtenteDAO;
import data.model.Amministratore;
import data.model.Azienda;
import data.model.OffertaTirocinio;
import data.model.Studente;
import data.model.TirocinioStudente;
import data.model.enumeration.CampoRicercaTirocinio;
import data.model.enumeration.TipoUtente;
import framework.data.DataLayerException;
import framework.result.FailureResult;
import framework.result.TemplateManagerException;
import framework.result.TemplateResult;
import framework.security.SecurityLayer;

/*
 * 
 * Servlet per la gestione delle homepage. In base alla tipologia di utente
 * (studente, azienda, amministratore o anonimo) verrà mostrata l'homepage
 * 
 */
@SuppressWarnings("serial")
public class HomePage extends IntershipTutorBaseController {
	
	public static final String SERVLET_URI = "/";
	// Numero massimo di offerte per pagina per la paginazione,
	// questo numero deve corrispondere a quello in offertaTirocinioDAO
	final static double OFFERTE_PER_PAGINA = OffertaTirocinioDAO.OFFERTE_PER_PAGINA;

	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }

    private void action_student(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
        	
        	Studente studente = (Studente) request.getAttribute("utente");
        	
        	this.setCommonAttributes(request, response);
	    	TemplateResult res = new TemplateResult(getServletContext());
	    	
	    	// Lista aziende convenzionate
	        List<Azienda> aziende = new AziendaDAO().allAziendeAccordingToConvention(true);
	    	
	    	Map<CampoRicercaTirocinio, String> campiDaCercare = new HashMap<>();
            
	    	// Used to page filtered offers(without page number)
            String queryString = "";
    		
            for(CampoRicercaTirocinio campoRicerca : CampoRicercaTirocinio.values()) {
            	if(request.getParameter(campoRicerca.name()) != "" &&
            	   request.getParameter(campoRicerca.name()) != null) {
            		campiDaCercare.put(campoRicerca, (String) request.getParameter(campoRicerca.name()));
            		queryString += "&" + campoRicerca + "=" + (String) request.getParameter(campoRicerca.name());
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
            	offerte = new OffertaTirocinioDAO().allOfferteInRangePerCampo(campiDaCercare, (int) request.getAttribute("paginaCorrente"));
            	// Massimo 5 offerte per pagina
                // il .0 è necessario per il cast di Java (1.1 deve essere arrotondato a 2 per esempio)
            	numeroPagine = (int) Math.ceil(new OffertaTirocinioDAO().getCountAccordingToRicerca(campiDaCercare) / OFFERTE_PER_PAGINA);
            }
            // Nessun filtraggio
            else {
            	// Tutte le offerte senza filtraggio
                offerte = new OffertaTirocinioDAO().allOfferteInRangeAccordingToVisibilita(true, (int) request.getAttribute("paginaCorrente"));
             // Massimo 5 offerte per pagina
                // il .0 è necessario per il cast di Java (1.1 deve essere arrotondato a 2 per esempio)
                numeroPagine = (int) Math.ceil(new OffertaTirocinioDAO().getCountAccordingToVisibilita(true) / OFFERTE_PER_PAGINA);
            }
            
            TirocinioStudente tirocinioStudente = new TirocinioStudenteDAO().getTirocinioStudenteByStudenteCF(studente.getCodiceFiscale());
            OffertaTirocinio tirocinioRichiesto = new TirocinioStudenteDAO().getOffertaTirocinioByStudente(studente);
	    	
            request.setAttribute("tirocinioStudente", tirocinioStudente);
            request.setAttribute("tirocinioRichiesto", tirocinioRichiesto);
	        request.setAttribute("aziende", aziende);
	        request.setAttribute("numeroPagine", numeroPagine);
			request.setAttribute("offerte", offerte);
			request.setAttribute("queryString", queryString);
	        // Attiviamo il template per lo studente loggato
			res.activate("homepage-studente.ftl.html", request, response);
        }
        catch (DataLayerException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    private void action_azienda(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
    	try {
    		this.setCommonAttributes(request, response);
    		TemplateResult res = new TemplateResult(getServletContext());
    		
    		Azienda azienda = (Azienda) request.getAttribute("utente");
    		
    		int voto = new ParereAziendaDAO().getMediaVoto(azienda);
    		Map<String, String> pareriAzienda = new ParereAziendaDAO().getPareriAzienda(azienda);
    		
            // La lista verrà riempita con tutte le offerte se l'utente non ha filtrato la ricerca;
            // con le offerte risultanti dalla ricerca in caso contrario
            List<OffertaTirocinio> offerte;
            
            // Totale delle offerte visibili all'utente / 5 (5 offerte per pagina) = numero delle pagine
            int numeroPagine;
             offerte = new OffertaTirocinioDAO().allOfferteInRangeAccordingToAzienda(azienda, (int) request.getAttribute("paginaCorrente"));
             // Massimo 5 offerte per pagina
                // il .0 è necessario per il cast di Java (1.1 deve essere arrotondato a 2 per esempio)
             numeroPagine = (int) Math.ceil(new OffertaTirocinioDAO().getCountAccordingToAzienda(azienda) / OFFERTE_PER_PAGINA);
            //}
             
            int tirociniTerminati = new TirocinioStudenteDAO().getCountTirociniStudenteTerminatiAccordingToAzienda(azienda);
            
            request.setAttribute("tirociniTerminati", tirociniTerminati);
            request.setAttribute("azienda", azienda);
            request.setAttribute("voto", voto);
 	        request.setAttribute("pareriAzienda", pareriAzienda);
			request.setAttribute("numeroPagine", numeroPagine);
			request.setAttribute("offerte", offerte);
			// Attiviamo il template per l'utente non loggato
			res.activate("homepage-azienda.ftl.html", request, response);
    	}
    	catch (DataLayerException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    //Amministratore
    private void action_amministratore(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
    	try{this.setCommonAttributes(request, response);
    	TemplateResult res = new TemplateResult(getServletContext());
    	
    	Amministratore amministratore = (Amministratore) request.getAttribute("utente");
    	
    	Azienda bestAzienda = new AziendaDAO().getBestAzienda();
    	Azienda worstAzienda = new AziendaDAO().getWorstAzienda();
    	List<Azienda> aziendeNonConv = new AziendaDAO().allAziendeAccordingToConvention(false);
    	OffertaTirocinio bestOfferta = new OffertaTirocinioDAO().getBestOfferta();
    	
    	int numAmministratoriTotali = new UtenteDAO().getCountAccordingToUserType(TipoUtente.amministratore);
    	int numAziendeTotali = new UtenteDAO().getCountAccordingToUserType(TipoUtente.azienda);
    	int numStudentiTotali = new UtenteDAO().getCountAccordingToUserType(TipoUtente.studente);
    	int numOfferteTotali = new OffertaTirocinioDAO().getCount();
    	
    	// Removing outline template for the admin
    	request.setAttribute("outline_tpl", "");
    	request.setAttribute("numAmministratoriTotali", numAmministratoriTotali);
    	request.setAttribute("numAziendeTotali", numAziendeTotali);
    	request.setAttribute("numStudentiTotali", numStudentiTotali);
    	request.setAttribute("numOfferteTotali", numOfferteTotali);
    	request.setAttribute("bestAzienda", bestAzienda);
        request.setAttribute("worstAzienda", worstAzienda);
        request.setAttribute("aziendeNonConv", aziendeNonConv);
        request.setAttribute("numAziende", aziendeNonConv.size());
        request.setAttribute("bestOfferta", bestOfferta);
        request.setAttribute("amministratore", amministratore);
    	//Attiviamo il template dell'amministatore
    	res.activate("homepage-amministratore.ftl.html", request, response);
    	}
    	catch(DataLayerException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    // Anonimo
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
    	try {
    		this.setCommonAttributes(request, response);
    		TemplateResult res = new TemplateResult(getServletContext());
    		
    		// Lista aziende convenzionate
	        List<Azienda> aziende = new AziendaDAO().allAziendeAccordingToConvention(true);
    		
    		Map<CampoRicercaTirocinio, String> campiDaCercare = new HashMap<>();
            
    		// Used to page filtered offers(without page number)
            String queryString = "";
    		
            for(CampoRicercaTirocinio campoRicerca : CampoRicercaTirocinio.values()) {
            	if(request.getParameter(campoRicerca.name()) != "" &&
            	   request.getParameter(campoRicerca.name()) != null) {
            		campiDaCercare.put(campoRicerca, (String) request.getParameter(campoRicerca.name()));
            		queryString += "&" + campoRicerca + "=" + (String) request.getParameter(campoRicerca.name());
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
            	offerte = new OffertaTirocinioDAO().allOfferteInRangePerCampo(campiDaCercare, (int) request.getAttribute("paginaCorrente"));
            	// Massimo 5 offerte per pagina
                // il .0 è necessario per il cast di Java (1.1 deve essere arrotondato a 2 per esempio)
            	numeroPagine = (int) Math.ceil(new OffertaTirocinioDAO().getCountAccordingToRicerca(campiDaCercare) / OFFERTE_PER_PAGINA);
            }
            // Nessun filtraggio
            else {
            	// Tutte le offerte senza filtraggio
                offerte = new OffertaTirocinioDAO().allOfferteInRangeAccordingToVisibilita(true, (int) request.getAttribute("paginaCorrente"));
                // Massimo 5 offerte per pagina
                // il .0 è necessario per il cast di Java (1.1 deve essere arrotondato a 2 per esempio)
                numeroPagine = (int) Math.ceil(new OffertaTirocinioDAO().getCountAccordingToVisibilita(true) / OFFERTE_PER_PAGINA);
            }
            
			request.setAttribute("aziende", aziende);
			request.setAttribute("queryString", queryString);
			request.setAttribute("numeroPagine", numeroPagine);
			request.setAttribute("offerte", offerte);
			// Attiviamo il template per l'utente non loggato
			res.activate("homepage-anonimo.ftl.html", request, response);
    	}
    	catch (DataLayerException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    // Setting all common variables in all templates
    private void setCommonAttributes(HttpServletRequest request, HttpServletResponse response) {
    	
    	try {
			int paginaCorrente = 0;
	        if(request.getParameter("pagina") != null) {
	        	paginaCorrente = (SecurityLayer.checkNumeric(request.getParameter("pagina")));
	        }
	        
	        request.setAttribute("servlet", SERVLET_URI);
	        request.setAttribute("searchbartitle", "Filtra le proposte di tirocinio");
	        request.setAttribute("paginaCorrente", paginaCorrente);
			request.setAttribute("campiRicerca", CampoRicercaTirocinio.values());
    	}
    	catch(IllegalArgumentException ex) {
    		request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
    	}
    	
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

    	try {
    		
    		// Common css for homepages(student homepage, company homepage, admin homepage and anonymous homepage)
    		request.setAttribute("page_css", "homepage");
    		request.setAttribute("OFFERTE_PER_PAGINA", OFFERTE_PER_PAGINA);
    		
    		if(request.getAttribute("utente") instanceof Studente)
    			action_student(request, response);
    		else if(request.getAttribute("utente") instanceof Azienda)
    			action_azienda(request, response);
    		else if(request.getAttribute("utente") instanceof Amministratore)
    			action_amministratore(request, response);
    		else
    			action_default(request, response);    			

        } 
    	catch (TemplateManagerException | IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        } 
    }

    @Override
    public String getServletInfo() {
        return "Main IntershipTutor servlet";
    }

}
