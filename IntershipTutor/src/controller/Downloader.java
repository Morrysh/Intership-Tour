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
import data.model.TirocinioStudente;
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
					// Richiesta di download da un'azienda
					// Verifichiamo che la richiesta sia fatta da un utente loggato come azienda
					// e che la convenzione richiesta sia legata effettivamente a quell'azienda
					if(session.getAttribute("utente") instanceof Azienda &&
					   ((Azienda)session.getAttribute("utente")).getCodiceFiscale()
					   		.equals(request.getParameter("azienda"))) {
						action_download_company_convention(request, response);
					}
					// Richiesta di download da un'amministratore
					else if(session.getAttribute("utente") instanceof Amministratore) {
						action_download_company_convention(request, response);
					}
					else {
						request.setAttribute("message", "Azienda non autorizzata:<br />" + 
														"La convenzione che si vuole scaricare non è legata a quest'azienda");
						action_error(request, response);
					}
				}
				// Richiesta di download di un progetto formativo
				else {
					// Richiesta proveniente da uno studente
					if(session.getAttribute("utente") instanceof Studente) {
						// Verifichiamo che il codice fiscale dello studente che ha fatto richiesta(utente loggato)
						// corrisponda a quello del progetto formativo che si è richiesto
						
						if(((Studente)request.getAttribute("utente")).getCodiceFiscale()
								.equals(request.getParameter("candidato"))){
							action_download_training_project(request, response);
						}
						else {
							request.setAttribute("message", "Studente non autorizzato:<br />" +
											     			"Stai cercando di scaricare un progetto formativo " +
															"di un'altro studente");
							action_error(request, response);
						}
					}
					// Richiesta proveniente da un'azienda
					else if (request.getAttribute("utente") instanceof Azienda) {
						// Verifichiamo che lo studente abbia effettivamente un tirocinio in corso
						// con l'azienda che vuole scaricare il progetto formativo
						TirocinioStudente tirocinioStudente = 
								new TirocinioStudenteDAO().getTirocinioStudenteByStudenteCF(request.getParameter("candidato"));
						if(tirocinioStudente != null) {
							Azienda azienda = new AziendaDAO().getAziendaByIDTirocinio(tirocinioStudente.getTirocinio());
							if(((Azienda)request.getAttribute("utente")).getCodiceFiscale().equals(azienda.getCodiceFiscale())){
								action_download_training_project(request, response);
							}
							else {
								request.setAttribute("message", "Azienda non autorizzata:<br />" + 
																"Il progetto formativo che si sta cercando di scaricare " +
																"non è legato a quest'azienda");
								action_error(request, response);
							}
						}
						else {
							request.setAttribute("message", "Errore nella richiesta:<br />" + 
															"Lo studente di cui si vuole scaricare il progetto formativo " +
															"sembra non aver svolto l'attività con quest'azienda");
							action_error(request, response);
						}
					}
				}
			}
			else {
				request.setAttribute("message", "Accesso non autorizzato:<br />" +
									 			"Devi aver effettuato l'accesso per scaricare questa risorsa");
				action_error(request, response);
			}
			
        }
    	catch (TemplateManagerException | IOException | DataLayerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        }
	}

}
