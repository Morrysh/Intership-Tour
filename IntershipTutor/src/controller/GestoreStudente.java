package controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.StudenteDAO;
import data.model.Studente;
import framework.data.DataLayerException;
import framework.result.FailureResult;
import framework.result.TemplateManagerException;

@SuppressWarnings("serial")
public class GestoreStudente extends IntershipTutorBaseController {

	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
	
	private void action_aggiorna(HttpServletRequest request, HttpServletResponse response, String codiceFiscale) throws IOException {
		try {
			//Studente studente = (Studente) request.getAttribute("utente");
			Studente studente = new StudenteDAO().getStudenteByCF(codiceFiscale);
			
			studente.setNome(request.getParameter(Studente.NOME));
			studente.setCognome(request.getParameter(Studente.COGNOME));
			studente.setEmail(request.getParameter(Studente.EMAIL));
			studente.setUsername(request.getParameter(Studente.USERNAME));
			studente.setPassword(request.getParameter(Studente.PASSWORD));
			studente.setTelefono(request.getParameter(Studente.TELEFONO));
			studente.setDataNascita(new Date(System.currentTimeMillis())); // DA MODIFICARE
			studente.setLuogoNascita(request.getParameter(Studente.LUOGO_NASCITA));
			studente.setProvinciaNascita(request.getParameter(Studente.PROVINCIA_NASCITA));
			studente.setResidenza(request.getParameter(Studente.RESIDENZA));
			studente.setProvinciaResidenza(request.getParameter(Studente.PROVINCIA_RESIDENZA));
			studente.setTipoLaurea(request.getParameter(Studente.TIPO_LAUREA));
			studente.setCorsoLaurea(request.getParameter(Studente.CORSO_LAUREA));
			
			new StudenteDAO().update(studente);
			
			response.sendRedirect(".");
		}
		catch(DataLayerException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
		catch(IOException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
	}

    private void action_default(HttpServletRequest request, HttpServletResponse response, String codiceFiscale) throws IOException, ServletException, TemplateManagerException {
        
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

    	try {
    		if(request.getParameter(Studente.UTENTE) != null) { 
    			String codiceFiscale = request.getParameter(Studente.UTENTE);
	    		if(request.getParameter("aggiorna") != null) {
	    			action_aggiorna(request, response, codiceFiscale);
	    		}
	    		else {
	    			action_default(request, response, codiceFiscale);
	    		}
    		}
    		else {
    			request.setAttribute("message", "Non è stata specificato uno studente");
                action_error(request, response);
    		}

        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        }
    }

    @Override
    public String getServletInfo() {
        return "Student details";
    }

}
