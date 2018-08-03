package controller;

import java.io.IOException;

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
import framework.result.TemplateManagerException;
import framework.result.TemplateResult;
import framework.security.SecurityLayer;

@SuppressWarnings("serial")
public class Login extends IntershipTutorBaseController {

	private void action_error(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        TemplateResult result = new TemplateResult(getServletContext());
        result.activate("errore.ftl.html", request, response);
    }

    private void action_login(HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateManagerException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (!username.isEmpty() && !password.isEmpty()) {
        	// Get user from database if exists;
            Utente utente = new UtenteDAO().getLogged(username, password);
            Object utenteLoggato = null;
            
            if(utente != null) {
	            switch(utente.getTipoUtente()) {
		        	case studente:
		        		utenteLoggato = (Studente) new StudenteDAO().getStudenteByCF(utente.getCodiceFiscale());
		        		break;
		        	case azienda:
		        		utenteLoggato = (Azienda) new AziendaDAO().getAziendaByCF(utente.getCodiceFiscale());
		        		break;
		        	case amministratore:
		        		utenteLoggato = (Amministratore) new AmministratoreDAO().getAmministratoreByCF(utente.getCodiceFiscale());
	            }
            }
            
            //request.setAttribute("utenteLoggato", utenteLoggato);
            if(utente != null) {
            	SecurityLayer.createSession(request, utenteLoggato);
            }
            //se è stato trasmesso un URL di origine, torniamo a quell'indirizzo
            //if an origin URL has been transmitted, return to it
            if (request.getParameter("referrer") != null) {
            	// REMEMBER TO USE THIS!
                response.sendRedirect(request.getParameter("referrer"));
            } else {
            	if(utente == null)
            		response.sendRedirect(".#login-modal");
            	else
            		response.sendRedirect(".");
            }
        } else {
            request.setAttribute("exception", new Exception("Login failed"));
            action_error(request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
        	action_login(request, response);
        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            try {
				action_error(request, response);
			} catch (TemplateManagerException e) {
				e.printStackTrace();
			}
        } catch (TemplateManagerException e) {
			e.printStackTrace();
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
