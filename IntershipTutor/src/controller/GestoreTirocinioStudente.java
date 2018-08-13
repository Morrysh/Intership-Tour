package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.OffertaTirocinioDAO;
import dao.impl.TirocinioStudenteDAO;
import data.model.Azienda;
import data.model.OffertaTirocinio;
import data.model.Studente;
import data.model.TirocinioStudente;
import data.model.enumeration.StatoRichiestaTirocinio;
import framework.data.DataLayerException;
import framework.result.FailureResult;
import framework.result.TemplateManagerException;
import framework.result.TemplateResult;
import framework.security.SecurityLayer;

@SuppressWarnings("serial")
public class GestoreTirocinioStudente extends IntershipTutorBaseController {

	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
	
	private void action_rimuovi(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException{
		try {
			Studente studente = (Studente) request.getAttribute("utente");
			
			new TirocinioStudenteDAO().delete(studente);
			
			response.sendRedirect(request.getContextPath());
			
		}
		catch (DataLayerException | IOException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
	}
	
	private void action_show_candidates(HttpServletRequest request, HttpServletResponse response, int idTirocinio) throws IOException, ServletException, TemplateManagerException{
		try {
			TemplateResult res = new TemplateResult(getServletContext());
			
			Azienda azienda = (Azienda) request.getAttribute("utente");
	        
			OffertaTirocinio offertaTirocinio = new OffertaTirocinioDAO().getOffertaByID(idTirocinio);
			Map<Studente, TirocinioStudente> candidatoTirocinio = new HashMap<>();
			List<Studente> candidati = new TirocinioStudenteDAO().getStudentiByOffertaTirocinio(offertaTirocinio);
			
			for(Studente studente : candidati) {
				candidatoTirocinio.put(studente, new TirocinioStudenteDAO().getTirocinioStudenteByStudente(studente));
			}
			
			request.setAttribute("azienda", azienda);
			request.setAttribute("page_css", "candidati");
			request.setAttribute("candidatoTirocinio", candidatoTirocinio);
			request.setAttribute("offertaTirocinio", offertaTirocinio);
			
			res.activate("candidati.ftl.html", request, response);
			
		}
		catch (DataLayerException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
	}
	
	private void action_update_state(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException{
		try {	    
			String codiceFiscale = request.getParameter(Studente.CODICE_FISCALE);
			StatoRichiestaTirocinio statoRichiestaTirocinio = StatoRichiestaTirocinio.valueOf(request.getParameter(TirocinioStudente.STATO));
			new TirocinioStudenteDAO().updateStato(codiceFiscale, statoRichiestaTirocinio);		
			
			if(request.getParameter("referrer") != null) {
				response.sendRedirect(request.getParameter("referrer"));
			}
			else {
				response.sendRedirect(request.getContextPath());
			}
		}
		catch (DataLayerException | IOException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
	}
	
	@Override
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		try {
			if(request.getParameter("rimuovi") != null) {
				action_rimuovi(request, response);
			}
			else if(request.getParameter(OffertaTirocinio.ID_TIROCINIO) != null) {
					int idTirocinio = SecurityLayer.checkNumeric(request.getParameter(OffertaTirocinio.ID_TIROCINIO));
					action_show_candidates(request, response, idTirocinio);
			}
			else if(request.getParameter(Studente.CODICE_FISCALE) != null &&
			   request.getParameter(TirocinioStudente.STATO) != null) {
				action_update_state(request, response);
			}
			else {
				request.setAttribute("message", "Non è stata specificato uno studente, un tirocinio o un nuovo stato per la candidatura");
			    action_error(request, response);
			}
		} catch (IOException | TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } 
		
	}

}
