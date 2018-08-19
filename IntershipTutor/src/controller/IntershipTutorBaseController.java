package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.model.Amministratore;
import data.model.Azienda;
import data.model.Studente;
import framework.result.FailureResult;
import framework.security.SecurityLayer;

@SuppressWarnings("serial")
public abstract class IntershipTutorBaseController extends HttpServlet {

	protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException;

	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
	
    private void processBaseRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    	Object utente = null;
    	// Controlliamo se esiste una sessione, in caso affermativo
    	// aggiungiamo l'informazione nella request
        HttpSession session = SecurityLayer.checkSession(request);
        
        if(session != null) {
        	if(session.getAttribute("utente") instanceof Studente)
        		utente = (Studente) session.getAttribute("utente");
        	else if(session.getAttribute("utente") instanceof Azienda)
        		utente = (Azienda) session.getAttribute("utente");
        	else
        		utente = (Amministratore) session.getAttribute("utente");
        	
        	request.setAttribute("utente", utente);
        }
        
        
        // Controlli di sicurezza
        // Per ora sono qui, poi vedremo come vanno gestiti
        String URI = request.getRequestURI();
        
        switch(URI) {
        	case Downloader.SERVLET_URI:
        		verify_user_download(session, request, response);
        		break;
        	case Uploader.SERVLET_URI:
        		verify_user_upload(session, request, response);
        		break;
        	case GestoreOffertaTirocinio.SERVLET_URI: 
        		verify_user_intership_offer(session, request, response);
        		break;
        	case GestoreTirocinioStudente.SERVLET_URI: 
        		verify_user_intership_student(session, request, response);
        		break;
        	default:
        		processRequest(request, response);
        }
    	
    	//processRequest(request, response);
    }
    
    private void verify_user_download(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException {
		if(session != null) {
			// Richiesta di download di una convenzione da parte di un'azienda
			if(request.getParameter("azienda") != null) {
				// Verifichiamo che la richiesta sia fatta da un utente loggato come azienda
				// e che la convenzione richiesta sia legata effettivamente a quell'azienda
				if(session.getAttribute("utente") instanceof Azienda && 
				   ((Azienda)session.getAttribute("utente")).getCodiceFiscale().equals(request.getParameter("azienda"))) {
					processRequest(request, response);
				}
				else if(session.getAttribute("utente") instanceof Amministratore) {
					processRequest(request, response);
				}
				else {
					request.setAttribute("message", "Azienda non autorizzata");
					action_error(request, response);
				}
			}
			// Richiesta di download di uno schema di convenzione
			else {
				// Richiesta proveniente da uno studente
				if(session.getAttribute("utente") instanceof Studente) {
					if(((Studente)session.getAttribute("utente")).getCodiceFiscale().equals(request.getParameter("utente"))){
						processRequest(request, response);
					}
					else {
						request.setAttribute("message", "Studente non autorizzato");
						action_error(request, response);
					}
				}
				// Richiesta proveniente da un'azienda(si dovrebbe anche verificare che l'azienda
				// abbia effettivamente emesso il tirocinio per quello studente)
				else {
					processRequest(request, response);
				}
			}
		}
		else {
			request.setAttribute("message", "Accesso non autorizzato");
			action_error(request, response);
		}
	}
    
    private void verify_user_upload(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException {
		if(session != null) {
			// Richiesta di upload da uno studente
			if(request.getAttribute("utente") instanceof Studente) {
				if(((Studente)request.getAttribute("utente")).getCodiceFiscale().equals(request.getParameter("utente"))){
						processRequest(request, response);
				}
				else {
					request.setAttribute("message", "Studente non autorizzato");
					action_error(request, response);
				}
			}
			// Richiesta di upload da un'azienda(Andrebbe contrallato che l'azienda
			// abbia effettivamente emesso il tirocinio per quello studente)
			else if(request.getAttribute("utente") instanceof Azienda) {
				processRequest(request, response);
			}
			// Richiesta di upload da un'amministratore(ci fidiamo)
			else {
				processRequest(request, response);
			}
		}
		else {
			request.setAttribute("message", "Accesso non autorizzato");
			action_error(request, response);
		}	
	}
    
    private void verify_user_intership_offer(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException {
		if(session != null) {
			if(request.getAttribute("utente") instanceof Azienda) {
				if(((Azienda)request.getAttribute("utente")).getCodiceFiscale().equals(request.getParameter("utente"))) {
					processRequest(request, response);
				}
				else if(request.getParameter("aggiugi") == null || 
					    request.getParameter("rimuovi") == null ||
					    request.getParameter("aggiorna") == null ||
					    request.getParameter("visibilita") == null) {
					processRequest(request, response);
				}
			    else {
			    	request.setAttribute("message", "Accesso non autorizzato");
					action_error(request, response);
			    }
			}
			else if(request.getParameter("id_tirocinio") != null &&
					request.getParameter("aggiugi") == null &&
				    request.getParameter("rimuovi") == null &&
				    request.getParameter("aggiorna") == null &&
				    request.getParameter("visibilita") == null) {
				processRequest(request, response);
			}
			else {
				request.setAttribute("message", "Accesso non autorizzato");
				action_error(request, response);
			}
		}
		// Utente non loggato
		else if(request.getParameter("id_tirocinio") != null &&
				request.getParameter("aggiugi") == null &&
			    request.getParameter("rimuovi") == null &&
			    request.getParameter("aggiorna") == null &&
			    request.getParameter("visibilita") == null) {
			processRequest(request, response);
		}
		else {
			request.setAttribute("message", "Accesso non autorizzato");
			action_error(request, response);
		}
	}

    private void verify_user_intership_student(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException {
		if(session != null) {
			// Richiesta da uno studente
			if(request.getAttribute("utente") instanceof Studente) {
				if(request.getParameter("iscriviti") != null ||
				   request.getParameter("rimuovi") != null) {
					if(((Studente)request.getAttribute("utente")).getCodiceFiscale().equals(request.getParameter("utente"))) {
						processRequest(request, response);
					}
					else {
						request.setAttribute("message", "Utente non autorizzato");
						action_error(request, response);
					}
				}
				else {
					request.setAttribute("message", "Azione non autorizzata");
					action_error(request, response);
				}
			}
			// Richiesta da un'azienda
			else {
				if(request.getAttribute("utente") instanceof Azienda) {
					if(request.getParameter("conclusi") != null ||
					   request.getParameter("candidato") != null ||
					   request.getParameter("id_tirocinio") != null ||
					   request.getParameter("rimuovi") != null) {
						if(((Azienda)request.getAttribute("utente")).getCodiceFiscale().equals(request.getParameter("utente"))) {
							processRequest(request, response);
						}
						else {
							request.setAttribute("message", "Utente non autorizzato");
							action_error(request, response);
						}
					}
					else {
						request.setAttribute("message", "Azione non autorizzata");
						action_error(request, response);
					}
				}
				else {
					request.setAttribute("message", "Azione non autorizzata");
					action_error(request, response);
				}
			}
		}
		else {
			request.setAttribute("message", "Accesso non autorizzato");
			action_error(request, response);
		}
	}


	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }
}