package controller;

import java.io.IOException;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import dao.impl.StudenteDAO;
import dao.impl.TirocinioStudenteDAO;
import data.model.Studente;
import framework.data.DataLayerException;
import framework.result.FailureResult;

@MultipartConfig
@SuppressWarnings("serial")
public class Uploader extends IntershipTutorBaseController{
	
	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
	
	private void action_update_training_project(HttpServletRequest request, HttpServletResponse response){
		try {
			
			Part filePart = request.getPart("file");
			String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
			
			// Verifichiamo che sia stato inserito un pdf
			if(filePart.getSize() > 0 && fileName.split("\\.")[1].equals("pdf")) {
				Studente studente = new StudenteDAO().getStudenteByCF(request.getParameter(Studente.UTENTE));
				new TirocinioStudenteDAO().setProgettoFormativo(filePart.getInputStream(), studente);
				
				if(request.getParameter("referrer") != null) {
					response.sendRedirect(request.getParameter("referrer"));
				}
				else {
					response.sendRedirect(request.getContextPath());
				}
				
			}
			else {
				request.setAttribute("message", "File vuoto o formato non supportato, si prega di caricare un pdf");
			    action_error(request, response);
			}
			
		} catch (IOException | ServletException | DataLayerException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
	}

	@Override
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		//try {
			if(request.getParameter(Studente.UTENTE) != null) {
				action_update_training_project(request, response);
			}
			else {
				// Aggiorna convenzione azienda(SOLO ADMIN)
			}
		//}
		
	}

}
