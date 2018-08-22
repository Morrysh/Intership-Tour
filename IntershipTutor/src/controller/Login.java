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
import framework.data.DataLayerException;
import framework.result.FailureResult;
import framework.result.TemplateManagerException;
import framework.security.SecurityLayer;

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
			        		break;
			        	case azienda:
			        		utenteLoggato = (Azienda) new AziendaDAO().getAziendaByCF(utente.getCodiceFiscale());
			        		break;
			        	case amministratore:
			        		utenteLoggato = (Amministratore) new AmministratoreDAO().getAmministratoreByCF(utente.getCodiceFiscale());
		            }
		            
		            SecurityLayer.createSession(request, utenteLoggato);
	            
	        		/*if(request.getParameter("referrer") != null)
		                response.sendRedirect(request.getParameter("referrer"));
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
            action_login(request, response);

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
