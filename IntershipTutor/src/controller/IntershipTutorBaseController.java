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
import framework.security.SecurityLayer;

@SuppressWarnings("serial")
public abstract class IntershipTutorBaseController extends HttpServlet {

	protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException;

    private void processBaseRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    	Object utente;
        
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
    	
    	processRequest(request, response);
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