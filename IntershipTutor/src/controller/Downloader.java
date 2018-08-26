package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.impl.AziendaDAO;
import dao.impl.StudenteDAO;
import dao.impl.TirocinioStudenteDAO;
import data.model.Amministratore;
import data.model.Azienda;
import data.model.Studente;
import framework.data.DataLayerException;
import framework.result.FailureResult;
import framework.result.TemplateManagerException;
import framework.security.SecurityLayer;

@SuppressWarnings("serial")
public class Downloader extends IntershipTutorBaseController{
	
	public static final String SERVLET_URI = "/download";
	
	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
	
	private void action_download_training_project(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException{
		try {			
			Studente studente = new StudenteDAO().getStudenteByCF(request.getParameter("candidato"));
			
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=progettoFormativo" + studente.getNome() + studente.getCognome() + ".pdf");
			InputStream fileInputStream = new TirocinioStudenteDAO().getProgettoFormativo(studente);
			OutputStream responseOutputStream = response.getOutputStream();
			int bytes;
			while ((bytes = fileInputStream.read()) != -1) {
				responseOutputStream.write(bytes);
			}
			fileInputStream.close();
		}
		catch(DataLayerException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
	}
	
	private void action_download_company_convention(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException{
		try {
			Azienda azienda = new AziendaDAO().getAziendaByCF(request.getParameter("azienda"));
	
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=convenzione" + azienda.getNome() + ".pdf");
			InputStream fileInputStream = new AziendaDAO().getConvenzioneDoc(azienda);
			OutputStream responseOutputStream = response.getOutputStream();
			int bytes;
			while ((bytes = fileInputStream.read()) != -1) {
				responseOutputStream.write(bytes);
			}
			fileInputStream.close();
		}
		catch(IOException | DataLayerException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
	}

	@Override
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			// Sessione eventualmente attiva
			HttpSession session = SecurityLayer.checkSession(request);
			
			if(session != null) {
				// Richiesta di download di una convenzione di un'azienda
				if(request.getParameter("azienda") != null) {
					// RICHIESTA DI DOWNLOAD DA UN'AZIENDA
					// Verifichiamo che la richiesta sia fatta da un utente loggato come azienda
					// e che la convenzione richiesta sia legata effettivamente a quell'azienda
					if(session.getAttribute("utente") instanceof Azienda &&
					   ((Azienda)session.getAttribute("utente")).getCodiceFiscale().equals(request.getParameter("azienda"))) {
						action_download_company_convention(request, response);
					}
					// RICHIESTA DI DOWNLOAD DA UN'AMMINISTRATORE
					else if(session.getAttribute("utente") instanceof Amministratore) {
						action_download_company_convention(request, response);
					}
					else {
						request.setAttribute("message", "Utente non autorizzata");
						action_error(request, response);
					}
				}
				// Richiesta di download di un progetto formativo
				else {
					// Richiesta proveniente da uno studente
					if(session.getAttribute("utente") instanceof Studente) {
						// Verifichiamo che il codice fiscale dello studente che ha fatto richiesta(utente loggato)
						// corrisponda a quello del progetto formativo che si è richiesto
						if(((Studente)session.getAttribute("utente")).getCodiceFiscale().equals(request.getParameter("utente"))){
							action_download_training_project(request, response);
						}
						else {
							request.setAttribute("message", "Studente non autorizzato");
							action_error(request, response);
						}
					}
					// Richiesta proveniente da un'azienda(si dovrebbe anche verificare che l'azienda
					// abbia effettivamente emesso il tirocinio per quello studente)
					else if (request.getAttribute("utente") instanceof Azienda) {
						action_download_training_project(request, response);
					}
					else {
						request.setAttribute("message", "Utente non autorizzato");
						action_error(request, response);
					}
				}
			}
			else {
				request.setAttribute("message", "Accesso non autorizzato");
				action_error(request, response);
			}
			
        }
    	catch (TemplateManagerException | IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        }
	}

}
