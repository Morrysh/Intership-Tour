package controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.AziendaDAO;
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
public class GestoreOffertaTirocinio extends IntershipTutorBaseController{
	
	public static final String SERVLET_URI = "/offertaTirocinio";
	
	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
	
	// Aggiungi offerta tirocinio
	private void action_aggiungi(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException{
		
		try {
			Azienda azienda = (Azienda) request.getAttribute("utente");
			
			if(azienda.isConvenzionata()) {
				Date dataInizio = null, dataFine = null;
				Time oraInizio = null, oraFine = null;
				
				if(!(request.getParameter(OffertaTirocinio.DATA_INIZIO)).isEmpty()) 
					dataInizio = Date.valueOf(request.getParameter(OffertaTirocinio.DATA_INIZIO));
				if(!(request.getParameter(OffertaTirocinio.DATA_FINE)).isEmpty()) 
					dataFine = Date.valueOf(request.getParameter(OffertaTirocinio.DATA_FINE));
				
				// Controlliamo che gli orari inseriti non siano vuoti(sono opzionali)
				if(!(request.getParameter(OffertaTirocinio.ORA_INIZIO)).isEmpty()) 
					oraInizio = Time.valueOf(request.getParameter(OffertaTirocinio.ORA_INIZIO) + ":00");
				if(!(request.getParameter(OffertaTirocinio.ORA_FINE)).isEmpty()) 
					oraFine = Time.valueOf(request.getParameter(OffertaTirocinio.ORA_FINE) + ":00");
				
				OffertaTirocinio nuovaOfferta = new OffertaTirocinio(
						azienda.getCodiceFiscale(),
						request.getParameter(OffertaTirocinio.TITOLO),
						request.getParameter(OffertaTirocinio.LUOGO),
						request.getParameter(OffertaTirocinio.OBIETTIVI),
						request.getParameter(OffertaTirocinio.MODALITA),
						request.getParameter(OffertaTirocinio.RIMBORSO),
						dataInizio,
						dataFine,
						oraInizio,
						oraFine,
						SecurityLayer.checkNumeric((request.getParameter(OffertaTirocinio.NUMERO_ORE))));
				
				new OffertaTirocinioDAO().insert(nuovaOfferta);
				// NOT USING request.getContextPath becouse it doesn't work with Heroku
    			response.sendRedirect(".");
			}
			else {
				request.setAttribute("message", "Azienda non convenzionata, permesso negato");
	            action_error(request, response);
			}
		} catch (DataLayerException | IOException ex) {
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
	        action_error(request, response);
		}
		
	}
	
	// Aggiorna offerta tirocinio
	private void action_aggiorna(HttpServletRequest request, HttpServletResponse response, int codiceOffertaTirocinio) throws IOException, ServletException, TemplateManagerException{
		
		try {
			Azienda azienda = (Azienda) request.getAttribute("utente");
			
			Date dataInizio = null, dataFine = null;
			Time oraInizio = null, oraFine = null;
			
			if(!(request.getParameter(OffertaTirocinio.DATA_INIZIO)).isEmpty()) 
				dataInizio = Date.valueOf(request.getParameter(OffertaTirocinio.DATA_INIZIO));
			if(!(request.getParameter(OffertaTirocinio.DATA_FINE)).isEmpty()) 
				dataFine = Date.valueOf(request.getParameter(OffertaTirocinio.DATA_FINE));
			
			// Controlliamo che gli orari inseriti non siano vuoti(sono opzionali)
			if(!(request.getParameter(OffertaTirocinio.ORA_INIZIO)).isEmpty()) 
				oraInizio = Time.valueOf(request.getParameter(OffertaTirocinio.ORA_INIZIO) + ":00");
			if(!(request.getParameter(OffertaTirocinio.ORA_FINE)).isEmpty()) 
				oraFine = Time.valueOf(request.getParameter(OffertaTirocinio.ORA_FINE) + ":00");
			
			OffertaTirocinio nuovaOfferta = new OffertaTirocinio(
					Integer.valueOf(request.getParameter(OffertaTirocinio.ID_TIROCINIO)),
					azienda.getCodiceFiscale(),
					request.getParameter(OffertaTirocinio.TITOLO),
					request.getParameter(OffertaTirocinio.LUOGO),
					request.getParameter(OffertaTirocinio.OBIETTIVI),
					request.getParameter(OffertaTirocinio.MODALITA),
					request.getParameter(OffertaTirocinio.RIMBORSO),
					dataInizio,
					dataFine,
					oraInizio,
					oraFine,
					SecurityLayer.checkNumeric(request.getParameter(OffertaTirocinio.NUMERO_ORE)));
			
			new OffertaTirocinioDAO().update(nuovaOfferta);
			
			if(request.getParameter("referrer") != null) {
				response.sendRedirect(request.getParameter("referrer"));
			}
			else {
				// NOT USING request.getContextPath becouse it doesn't work with Heroku
    			response.sendRedirect(".");
			}
				
		} catch (DataLayerException | IOException ex) {
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
		}
		
	}
	
	// Rimuovi offerta tirocinio
	private void action_rimuovi(HttpServletRequest request, HttpServletResponse response, int codiceOffertaTirocinio) throws IOException, ServletException, TemplateManagerException{
		try {
			int idTirocinio = SecurityLayer.checkNumeric(request.getParameter(OffertaTirocinio.ID_TIROCINIO));
			
			new OffertaTirocinioDAO().delete(idTirocinio);
			
			// NOT USING request.getContextPath becouse it doesn't work with Heroku
			response.sendRedirect(".");
			
		} catch (DataLayerException | IOException ex) {
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
	        action_error(request, response);
		}
	}
	
	private void action_visibilita(HttpServletRequest request, HttpServletResponse response, int codiceOffertaTirocinio) throws IOException, ServletException, TemplateManagerException{
		try {
			new OffertaTirocinioDAO().setVisibilita(codiceOffertaTirocinio, Boolean.valueOf(request.getParameter("visibilita")));
			// NOT USING request.getContextPath becouse it doesn't work with Heroku
			response.sendRedirect(".");
		}
		catch(DataLayerException | IOException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
	}
	
	// Mostra dettagli dell'offerta
    private void action_default(HttpServletRequest request, HttpServletResponse response, int codiceOffertaTirocinio) throws IOException, ServletException, TemplateManagerException {
    	try {
	    	TemplateResult res = new TemplateResult(getServletContext());
	    	
	    	OffertaTirocinio offertaTirocinio = new OffertaTirocinioDAO().getOffertaByID(codiceOffertaTirocinio);
	        Azienda azienda = new AziendaDAO().getAziendaByIDTirocinio(codiceOffertaTirocinio);
	    	Studente studente = (Studente) request.getAttribute("utente");
	    	// Eventuale tirocinio effettuato dallo studente loggato
	    	TirocinioStudente tirocinioStudente = null;
	    	// Indica se lo studente ha effettuato e terminato il tirocinio di cui si sta visualizzando il dettaglio,
	    	// Questo ci permetterà di dare la possibilità allo studente di recensire il tirocinio.
	    	boolean hasIntership = false;
	    	
	    	// Verifichiamo che non sia una visita in anonimo
	    	if(studente != null) {
	    		// Recuperiamo dal database il tirocinio che lo studente ha effettuato(se ne ha effettuato uno)
	    		tirocinioStudente = new TirocinioStudenteDAO().getTirocinioStudenteByStudenteCF(studente.getCodiceFiscale());
	    		// Confronto l'id del tirocinio che lo studente ha effettuato (e terminato)
	    		// con quello dell'offerta di cui si sta visualizzando il dettaglio
	    		// se corrispondono lo studente può valutare il tirocinio
	    		if(tirocinioStudente != null && 
	    		   tirocinioStudente.getTirocinio() == offertaTirocinio.getIdTirocinio() &&
	    		   tirocinioStudente.getStato() == StatoRichiestaTirocinio.terminato) {
	    				hasIntership = true;
	    		}
	    	}
	    	
	    	// Pareri sul tirocinio che di cui si stanno visualizzando i dettagli
	    	Map<String, String> pareriTirocinio = new OffertaTirocinioDAO().getPareriTirocinio(offertaTirocinio);
	        
	    	request.setAttribute("pareriTirocinio", pareriTirocinio);
	    	request.setAttribute("tirocinioStudente", tirocinioStudente);
	    	request.setAttribute("hasIntership", hasIntership);
	    	request.setAttribute("studente", studente);
	        request.setAttribute("offertaTirocinio", offertaTirocinio);
	        request.setAttribute("azienda", azienda);	
	        
			if(azienda != null)
				res.activate("dettaglio-offerta-tirocinio.ftl.html", request, response);
			else {
				request.setAttribute("message", "Nessun'azienda trovata");
	            action_error(request, response);
			}
    	}
    	catch(DataLayerException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    private void action_recensisci(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException{
		try {
			new TirocinioStudenteDAO().updateParere(request.getParameter("utente"),
													request.getParameter("parere"));
			
			if(request.getParameter("referrer") != null) {
				response.sendRedirect(request.getParameter("referrer") + "&utente=" + request.getParameter("utente"));
			}
			else {
				// NOT USING request.getContextPath becouse it doesn't work with Heroku
    			response.sendRedirect(".");
			}
		}
		catch(DataLayerException | IOException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
	}

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

    	try {
			request.setAttribute("page_css", "gestore-offerta-tirocinio");
			
			if(request.getParameter("aggiungi") != null) {
				action_aggiungi(request, response);
			}
			else if(request.getParameter("recensisci") != null) {
				action_recensisci(request, response);
			}
			else if (request.getParameter(OffertaTirocinio.ID_TIROCINIO) != null) {
				int codiceOffertaTirocinio = SecurityLayer.checkNumeric(request.getParameter(OffertaTirocinio.ID_TIROCINIO));
				if(request.getParameter("rimuovi") != null) {
					action_rimuovi(request, response, codiceOffertaTirocinio);
				}
				else if(request.getParameter("aggiorna") != null) {
					action_aggiorna(request, response, codiceOffertaTirocinio);
				}
				else if(request.getParameter("visibilita") != null) {
					action_visibilita(request, response, codiceOffertaTirocinio);
				}
				else {
		    		action_default(request, response, codiceOffertaTirocinio);
				}
			}
			else {
				request.setAttribute("message", "Non è stato specificato un tirocinio valido");
			    action_error(request, response);
			}
    	}
		catch (TemplateManagerException | IOException e) {
			request.setAttribute("exception", e);
            action_error(request, response);
		} 
    	catch (NumberFormatException ex) {
            request.setAttribute("message", "Invalid number submitted");
            action_error(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Intership details";
    }

}
