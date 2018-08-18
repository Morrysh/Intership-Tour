package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.AziendaDAO;
import dao.impl.StudenteDAO;
import dao.impl.TirocinioStudenteDAO;
import data.model.Azienda;
import data.model.Studente;
import framework.data.DataLayerException;
import framework.result.FailureResult;
import framework.result.TemplateManagerException;

@SuppressWarnings("serial")
public class Downloader extends IntershipTutorBaseController{
	
	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
	
	private void action_download_training_project(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException{
		try {			
			Studente studente = new StudenteDAO().getStudenteByCF(request.getParameter(Studente.CODICE_FISCALE));
			
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
			if(request.getParameter(Studente.CODICE_FISCALE) != null) {
				action_download_training_project(request, response);
			}
			else if(request.getParameter("azienda") != null) {
				action_download_company_convention(request, response);
			}
			else {
				request.setAttribute("message", "Errore nella richiesta");
	            action_error(request, response);
			}
        }
    	catch (TemplateManagerException | IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        }
	}

}
