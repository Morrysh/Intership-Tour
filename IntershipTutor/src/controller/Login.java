package controller;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.AmministratoreDAO;
import dao.impl.AziendaDAO;
import dao.impl.StudenteDAO;
import dao.impl.UtenteDAO;
import data.model.Amministratore;
import data.model.Azienda;
import data.model.Studente;
import data.model.Utente;
import framework.data.DataLayerException;
import framework.result.FailureResult;
import framework.result.TemplateManagerException;
import framework.security.SecurityLayer;
import utils.email.EmailSender;
import utils.password.PasswordGenerator;

@SuppressWarnings("serial")
public class Login extends IntershipTutorBaseController {

	public static final String SERVLET_URI = "/login";
	
	private void action_error(HttpServletRequest request, HttpServletResponse response) {
		if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
	
	private void action_recover(HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateManagerException {
		try {
			
			// Controlliamo che l'email esista.
			boolean emailNotExists = new UtenteDAO().checkEmailDisponibile(request.getParameter(Utente.EMAIL));
			
			if(!emailNotExists) {
				
				// Recuperiamo dal database l'utente
				Utente utente = new UtenteDAO().getUtenteByEmail(request.getParameter(Utente.EMAIL));
				
				// Generiamo una nuova password per l'utente
				String nuovaPassword = PasswordGenerator.generatePassword(8);
				
				// Aggiorniamo la password dell'utente nel database
				utente.setPassword(nuovaPassword);
				new UtenteDAO().update(utente);
				
				// Inviamo l'email con la nuova password all'utente
	        	
				// Titolo email
	        	String messageTitle = "IntershipTutor, nuova password";
	        	// Corpo email
	        	String messageBody = "Ciao " + utente.getUsername() + ",\n\n" +
				    				 "la tua nuova password per accedere al nostro servizio è:\n\n" +
				    				  nuovaPassword;
	        	// Destinatari email
	        	String recipientsEmails[] = {
	        			// request.getParameter(Utente.EMAIL)
	        			"stefano.martella9614@gmail.com"
	        	};
	        	// Invio email
	        	EmailSender.sendEmail(messageTitle, messageBody, recipientsEmails);
	        	
	        	// NOT USING request.getContextPath becouse it doesn't work with Heroku
				response.sendRedirect(".");
			}
			else {
				request.setAttribute("message", "L'email inserita non è nel sistema");
	            action_error(request, response);
			}
		}
		catch (DataLayerException | MessagingException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
		
	}

    private void action_login(HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateManagerException {
        try {
	    	String username = request.getParameter("username");
	        String password = request.getParameter("password");
	
	        if (username != null && password != null) {
	        	// Get user from database if exists;
	            Utente utente = new UtenteDAO().getLogged(username, password);
	            Object utenteLoggato = null;
	            
	            if(utente != null) {
		            switch(utente.getTipoUtente()) {
			        	case studente:
			        		utenteLoggato = (Studente) new StudenteDAO().getStudenteByCF(utente.getCodiceFiscale());
			        		// Settiamo la password non cifrata per fare in modo che possa essere visualizzata
			            	// e cambiata dalla homepage dello studente
			            	((Studente) utenteLoggato).setPassword(password);
			        		break;
			        	case azienda:
			        		utenteLoggato = (Azienda) new AziendaDAO().getAziendaByCF(utente.getCodiceFiscale());
			        		// Settiamo la password non cifrata per fare in modo che possa essere visualizzata
			            	// e cambiata dalla homepage dell'azienda
			            	((Azienda) utenteLoggato).setPassword(password);
			        		break;
			        	case amministratore:
			        		utenteLoggato = (Amministratore) new AmministratoreDAO().getAmministratoreByCF(utente.getCodiceFiscale());
		            }
		            
		            SecurityLayer.createSession(request, utenteLoggato);
	            
	        		/*if(request.getParameter("referer") != null)
		                response.sendRedirect(request.getHeader("referer"));
	        		else*/
		            
	            	// NOT USING request.getContextPath becouse it doesn't work with Heroku
        			response.sendRedirect(".");
		        }
	            else {
	        		response.sendRedirect("." + "#access-manager-modal");
	            }
	        }
	        else {
	        	response.sendRedirect("." + "#access-manager-modal");
	        }
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
    		// La precudera di recupero password per ora è in questa servlet,
    		// da valutare se tenerla qui o spostarla in una nuova servlet
    		if(request.getParameter("recupera") != null) {
    			// Procedura per recuperare la password
    			action_recover(request, response);
    		}
    		else {
    			action_login(request, response);
    		}
        } 
    	catch (TemplateManagerException | IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
