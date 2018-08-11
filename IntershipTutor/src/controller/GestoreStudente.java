package controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.StudenteDAO;
import dao.impl.TirocinioStudenteDAO;
import data.model.Studente;
import data.model.TirocinioStudente;
import data.model.Utente;
import data.model.enumeration.TipoUtente;
import data.model.enumeration.StatoRichiestaTirocinio;
import framework.data.DataLayerException;
import framework.result.FailureResult;
import framework.result.TemplateManagerException;
import framework.security.SecurityLayer;

@SuppressWarnings("serial")
public class GestoreStudente extends IntershipTutorBaseController {

	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
	
	private void action_aggiorna(HttpServletRequest request, HttpServletResponse response) {
		try {
			Studente studente = (Studente) request.getAttribute("utente");
			
			studente.setNome(request.getParameter(Studente.NOME));
			studente.setCognome(request.getParameter(Studente.COGNOME));
			studente.setEmail(request.getParameter(Utente.EMAIL));
			studente.setUsername(request.getParameter(Utente.USERNAME));
			studente.setPassword(request.getParameter(Utente.PASSWORD));
			studente.setTelefono(request.getParameter(Utente.TELEFONO));
			studente.setDataNascita(Date.valueOf(request.getParameter(Studente.DATA_NASCITA)));
			studente.setLuogoNascita(request.getParameter(Studente.LUOGO_NASCITA));
			studente.setProvinciaNascita(request.getParameter(Studente.PROVINCIA_NASCITA));
			studente.setResidenza(request.getParameter(Studente.RESIDENZA));
			studente.setProvinciaResidenza(request.getParameter(Studente.PROVINCIA_RESIDENZA));
			studente.setTipoLaurea(request.getParameter(Studente.TIPO_LAUREA));
			studente.setCorsoLaurea(request.getParameter(Studente.CORSO_LAUREA));
			
			new StudenteDAO().update(studente);
			
			response.sendRedirect(request.getContextPath());
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
	
	private void action_registra(HttpServletRequest request, HttpServletResponse response) {
		try {
			Studente studente = new Studente(
				request.getParameter(Utente.CODICE_FISCALE),
				request.getParameter(Utente.EMAIL),
				request.getParameter(Utente.USERNAME),
				request.getParameter(Utente.PASSWORD),
				request.getParameter(Utente.TELEFONO),
				TipoUtente.studente,
				request.getParameter(Studente.CODICE_FISCALE),
				request.getParameter(Studente.NOME),
				request.getParameter(Studente.COGNOME),
				Date.valueOf(request.getParameter(Studente.DATA_NASCITA)),
				request.getParameter(Studente.LUOGO_NASCITA),
				request.getParameter(Studente.PROVINCIA_NASCITA),
				request.getParameter(Studente.RESIDENZA),
				request.getParameter(Studente.PROVINCIA_RESIDENZA),
				request.getParameter(Studente.TIPO_LAUREA),
				request.getParameter(Studente.CORSO_LAUREA),
				Boolean.valueOf(request.getParameter(Studente.HANDICAP)));
			
			new StudenteDAO().insert(studente);
			
			response.sendRedirect(request.getContextPath());
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
	
	private void action_iscriviti(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
        	
        	Utente utente = (Utente) request.getAttribute("utente");
        	
        	TirocinioStudente tirocinioStudente = new TirocinioStudente(
        			utente.getCodiceFiscale(),
        			SecurityLayer.checkNumeric(request.getParameter(TirocinioStudente.ID_TIROCINIO)),
        			SecurityLayer.checkNumeric(request.getParameter(TirocinioStudente.CFU)),
        			request.getParameter(TirocinioStudente.TUTORE_UNIVERSITARIO),
        			request.getParameter(TirocinioStudente.TELEFONO_TUTORE),
        			request.getParameter(TirocinioStudente.EMAIL_TUTORE),
        			null, // descrizioneDettagliata
        			0,    // oreSvolte
        			null, // giudizioFinale
        			null, // parere
        			StatoRichiestaTirocinio.attesa);
        	
        	new TirocinioStudenteDAO().insert(tirocinioStudente);
        	
        	response.sendRedirect(request.getContextPath());
        }
        catch(DataLayerException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

    	try {
			if(request.getParameter("aggiorna") != null) {
				action_aggiorna(request, response);
			}
			else if(request.getParameter("registrazione") != null) {
				action_registra(request, response);
			}
			else if(request.getParameter("iscriviti") != null) {
				if(request.getParameter(TirocinioStudente.ID_TIROCINIO) != null) {
					action_iscriviti(request, response);
				}
				else {
					request.setAttribute("message", "Non è stato specificato il tirocinio");
    			    action_error(request, response);
				}
			}
			else {
				action_default(request, response);
			}
        }
    	catch (IOException ex) {
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
