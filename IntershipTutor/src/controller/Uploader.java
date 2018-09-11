package controller;

import java.io.IOException;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import dao.impl.AziendaDAO;
import dao.impl.StudenteDAO;
import dao.impl.TirocinioStudenteDAO;
import data.model.Amministratore;
import data.model.Azienda;
import data.model.Studente;
import data.model.TirocinioStudente;
import framework.data.DataLayerException;
import framework.result.FailureResult;
import framework.security.SecurityLayer;

@MultipartConfig
@SuppressWarnings("serial")
public class Uploader extends IntershipTutorBaseController{
	
	public static final String SERVLET_URI = "/upload";
	
	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
	
	private void action_update_training_project(HttpServletRequest request, HttpServletResponse response)  
			throws IOException, ServletException {
		try {
			
			Part filePart = request.getPart("file");
			String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
			
			// Verifichiamo che sia stato inserito un pdf
			if(filePart.getSize() > 0 && fileName.split("\\.")[1].equals("pdf")) {
				Studente studente = new StudenteDAO().getStudenteByCF(request.getParameter("candidato"));
				new TirocinioStudenteDAO().setProgettoFormativo(filePart.getInputStream(), studente);
				
				if(request.getParameter("referer") != null) {
					response.sendRedirect(request.getHeader("referer"));
				}
				else {
					// NOT USING request.getContextPath becouse it doesn't work with Heroku
        			response.sendRedirect(".");
				}
				
			}
			else {
				request.setAttribute("message", "File vuoto o formato non supportato, si prega di caricare un pdf");
			    action_error(request, response);
			}
			
		} catch (DataLayerException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
	}
	
	private void action_set_convention(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			
			Part filePart = request.getPart("file");
			String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
			
			// Verifichiamo che sia stato inserito un pdf
			if(filePart.getSize() > 0 && fileName.split("\\.")[1].equals("pdf")) {
				Azienda azienda = new AziendaDAO().getAziendaByCF(request.getParameter("azienda"));
				new AziendaDAO().setConvenzioneDoc(filePart.getInputStream(), azienda);
				
				// Convenziona azienda
				new AziendaDAO().setConvenzione(azienda, true);
				
				if(request.getParameter("referer") != null) {
					response.sendRedirect(request.getHeader("referer"));
				}
				else {
					// NOT USING request.getContextPath becouse it doesn't work with Heroku
        			response.sendRedirect(".");
				}
				
			}
			else {
				request.setAttribute("message", "File vuoto o formato non supportato, si prega di caricare un pdf");
			    action_error(request, response);
			}
			
		} catch (DataLayerException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
	}

	@Override
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException {
		try {
			// Sessione eventualmente attiva
			HttpSession session = SecurityLayer.checkSession(request);
			
			if(session != null) {
				// Richiesta di upload da uno studente
				if(request.getAttribute("utente") instanceof Studente) {
					// Verifichiamo che lo studente loggato stia effettivamente caricando un
					// progetto formativo in accordo al suo codice fiscale
					if(((Studente)request.getAttribute("utente")).getCodiceFiscale()
							.equals(request.getParameter("candidato"))){
						action_update_training_project(request, response);
					}
					else {
						request.setAttribute("message", "Studente non autorizzato:<br />" + 
														"Si sta cercando aggiornare il documento di un'altro studente");
						action_error(request, response);
					}
				}
				// Richiesta di upload da un'azienda(Andrebbe contrallato che l'azienda
				// abbia effettivamente emesso il tirocinio per quello studente)
				else if(request.getAttribute("utente") instanceof Azienda) {
					// Verifichiamo che lo studente abbia effettivamente un tirocinio in corso
					// con l'azienda che vuole caricare il progetto formativo
					TirocinioStudente tirocinioStudente = 
							new TirocinioStudenteDAO().getTirocinioStudenteByStudenteCF(request.getParameter("candidato"));
					if(tirocinioStudente != null) {
						Azienda azienda = new AziendaDAO().getAziendaByIDTirocinio(tirocinioStudente.getTirocinio());
						if(((Azienda)request.getAttribute("utente")).getCodiceFiscale().equals(azienda.getCodiceFiscale())){
							action_update_training_project(request, response);
						}
						else {
							request.setAttribute("message", "Azienda non autorizzata:<br />" +
															"Si sta cercando di caricare una risorsa per un'altra azienda");
							action_error(request, response);
						}
					}
					else {
						request.setAttribute("message", "Errore nella richiesta:<br />" + 
														"Lo studente non ha svolto alcuna attività con quest'azienda");
						action_error(request, response);
					}
				}
				// Richiesta di upload da un'amministratore(ci fidiamo)
				// Upload del documento di convenzione per quell'azienda
				else if(request.getAttribute("utente") instanceof Amministratore) {
					action_set_convention(request, response);
				}
			}
			else {
				request.setAttribute("message", "Accesso non autorizzato:<br />" + 
												"È richiesto l'accesso per caricare questa risorsa");
				action_error(request, response);
			}		
		}
		catch(DataLayerException | IOException ex) {
			request.setAttribute("exception", ex);
            action_error(request, response);
		}
	}
}
