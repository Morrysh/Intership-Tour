package controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.AziendaDAO;
import dao.impl.OffertaTirocinioDAO;
import data.model.Azienda;
import data.model.OffertaTirocinio;
import framework.data.DataLayerException;
import framework.result.FailureResult;
import framework.result.TemplateManagerException;
import framework.result.TemplateResult;
import framework.security.SecurityLayer;

@SuppressWarnings("serial")
public class GestoreOffertaTirocinio extends IntershipTutorBaseController{
	
	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
	
	// Aggiungi offerta tirocinio
	private void action_aggiungi(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			Azienda azienda = (Azienda) request.getAttribute("utente");
			
			Time oraInizio = null;
			Time oraFine = null;
			
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
					Date.valueOf(request.getParameter(OffertaTirocinio.DATA_INIZIO)),
					Date.valueOf(request.getParameter(OffertaTirocinio.DATA_FINE)),
					oraInizio,
					oraFine,
					Integer.valueOf(request.getParameter(OffertaTirocinio.NUMERO_ORE)));
			
			new OffertaTirocinioDAO().insert(nuovaOfferta);
			
			response.sendRedirect(request.getContextPath());
				
		} catch (DataLayerException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
		catch(IOException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
		
	}
	
	// Aggiorna offerta tirocinio
	private void action_aggiorna(HttpServletRequest request, HttpServletResponse response, int codiceOffertaTirocinio) {
		
		try {
			Azienda azienda = (Azienda) request.getAttribute("utente");
			
			Time oraInizio = null;
			Time oraFine = null;
			
			// Controlliamo che gli orari inseriti non siano vuoti(sono opzionali)
			if(!(request.getParameter(OffertaTirocinio.ORA_INIZIO)).isEmpty()) 
				oraInizio = Time.valueOf(request.getParameter(OffertaTirocinio.ORA_INIZIO) + ":00");
			if(!(request.getParameter(OffertaTirocinio.ORA_FINE)).isEmpty()) 
				oraFine = Time.valueOf(request.getParameter(OffertaTirocinio.ORA_FINE) + ":00");
			
			OffertaTirocinio nuovaOfferta = new OffertaTirocinio(
					SecurityLayer.checkNumeric(request.getParameter(OffertaTirocinio.ID_TIROCINIO)),
					azienda.getCodiceFiscale(),
					request.getParameter(OffertaTirocinio.TITOLO),
					request.getParameter(OffertaTirocinio.LUOGO),
					request.getParameter(OffertaTirocinio.OBIETTIVI),
					request.getParameter(OffertaTirocinio.MODALITA),
					request.getParameter(OffertaTirocinio.RIMBORSO),
					Date.valueOf(request.getParameter(OffertaTirocinio.DATA_INIZIO)),
					Date.valueOf(request.getParameter(OffertaTirocinio.DATA_FINE)),
					oraInizio,
					oraFine,
					Integer.valueOf(request.getParameter(OffertaTirocinio.NUMERO_ORE)));
			
			new OffertaTirocinioDAO().update(nuovaOfferta);
			
			response.sendRedirect(request.getContextPath());
				
		} catch (DataLayerException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
		catch(IOException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
		
	}
	
	// Rimuovi offerta tirocinio
	private void action_rimuovi(HttpServletRequest request, HttpServletResponse response, int codiceOffertaTirocinio) {
		try {
			int idTirocinio = SecurityLayer.checkNumeric(request.getParameter(OffertaTirocinio.ID_TIROCINIO));
			
			new OffertaTirocinioDAO().delete(idTirocinio);
			
			response.sendRedirect(request.getContextPath());
			
		} catch (DataLayerException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
	        action_error(request, response);
		}
		catch(IOException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
	}
	
	// Proponi lo studente per il tirocinio
	private void action_iscriviti(HttpServletRequest request, HttpServletResponse response, int codiceOffertaTirocinio) {
		
	}
	
	private void action_visibilita(HttpServletRequest request, HttpServletResponse response, int codiceOffertaTirocinio) {
		try {
			new OffertaTirocinioDAO().setVisibilita(codiceOffertaTirocinio, Boolean.valueOf(request.getParameter("visibilita")));
			response.sendRedirect(request.getContextPath());
		}
		catch(DataLayerException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
		catch(IOException ex) {
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

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

    	try {
    		
			request.setAttribute("page_css", "gestore-offerta-tirocinio");
			
			if(request.getParameter("aggiungi") != null) {
				action_aggiungi(request, response);
			}
			else if (request.getParameter(OffertaTirocinio.ID_TIROCINIO) != null) {
				int codiceOffertaTirocinio = SecurityLayer.checkNumeric(request.getParameter(OffertaTirocinio.ID_TIROCINIO));
				if(request.getParameter("iscriviti") != null) {
	    			action_iscriviti(request, response, codiceOffertaTirocinio);
	    		}
				else if(request.getParameter("rimuovi") != null) {
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
				request.setAttribute("message", "Non � stato specificato un tirocinio valido");
			    action_error(request, response);
			}
    	}
		catch (IOException e) {
			request.setAttribute("exception", e);
            action_error(request, response);
		}
		catch (TemplateManagerException e) {
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
