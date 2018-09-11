package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.StudenteDAO;
import dao.impl.UtenteDAO;
import data.model.Studente;
import data.model.Utente;
import data.model.enumeration.TipoUtente;
import framework.data.DataLayerException;
import framework.result.FailureResult;
import framework.security.SecurityLayer;

@SuppressWarnings("serial")
public class GestoreStudente extends IntershipTutorBaseController {
	
	public static final String SERVLET_URI = "/studente";

	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
	
	// Funzione per controllare se lo studente può registrarsi o aggiornare la registrazione 
	// con i campi inseriti(verifica dei duplicati)
	private boolean check_fields(HttpServletRequest request, HttpServletResponse response, Studente studente) 
			throws DataLayerException {
		try {
			boolean registrazione_ok = true;
			
			// Registrazione
			if(request.getParameter("registrazione") != null) {
				if(!new UtenteDAO().checkCodiceFiscaleDisponibile(studente.getCodiceFiscale())) {
					request.setAttribute("CFInUso", true);
					registrazione_ok = false;
				}
				if(!new UtenteDAO().checkEmailDisponibile(studente.getEmail())) {
					
					request.setAttribute("emailInUso", true);
					registrazione_ok = false;
				}
				if(!new UtenteDAO().checkUsernameDisponibile(studente.getUsername())) {
					request.setAttribute("usernameInUso", true);
					registrazione_ok = false;
				}
				if(!new UtenteDAO().checkTelefonoDisponibile(studente.getTelefono())) {
					request.setAttribute("telefonoInUso", true);
					registrazione_ok = false;
				}
			}
			// Richiesta aggiornamento profilo
			else {
				
				Studente studenteLoggato = (Studente)request.getAttribute("utente");
				
				if(!(studenteLoggato.getEmail().equals(studente.getEmail())) &&
				   !new UtenteDAO().checkEmailDisponibile(studente.getEmail())){
					
					request.setAttribute("emailInUso", true);
					registrazione_ok = false;
				}
				if(!(studenteLoggato.getUsername().equals(studente.getUsername())) &&
                   !new UtenteDAO().checkUsernameDisponibile(studente.getUsername())) {
					
					request.setAttribute("usernameInUso", true);
					registrazione_ok = false;
				}
				if(!(studenteLoggato.getTelefono().equals(studente.getTelefono())) &&
				   !new UtenteDAO().checkTelefonoDisponibile(studente.getTelefono())) {
					
					request.setAttribute("telefonoInUso", true);
					registrazione_ok = false;
				}
			}
			
			return registrazione_ok;
		}
		catch(DataLayerException ex) {
			throw new DataLayerException("Errore nella verifica dei campi");
		}
	}
	
	private void action_aggiorna(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		try {
			Studente studenteLoggato = (Studente) request.getAttribute("utente");
			Studente studente = new Studente();
			
			studente.setNome(request.getParameter(Studente.NOME));
			studente.setCognome(request.getParameter(Studente.COGNOME));
			studente.setEmail(request.getParameter(Utente.EMAIL));
			studente.setUsername(request.getParameter(Utente.USERNAME));
			studente.setPassword(request.getParameter(Utente.PASSWORD));
			studente.setTelefono(request.getParameter(Utente.TELEFONO));
			studente.setDataNascita(SecurityLayer.checkDate(request.getParameter(Studente.DATA_NASCITA)));
			studente.setLuogoNascita(request.getParameter(Studente.LUOGO_NASCITA));
			studente.setProvinciaNascita(request.getParameter(Studente.PROVINCIA_NASCITA));
			studente.setResidenza(request.getParameter(Studente.RESIDENZA));
			studente.setProvinciaResidenza(request.getParameter(Studente.PROVINCIA_RESIDENZA));
			studente.setTipoLaurea(request.getParameter(Studente.TIPO_LAUREA));
			studente.setCorsoLaurea(request.getParameter(Studente.CORSO_LAUREA));
			studente.setHandicap(SecurityLayer.checkBoolean(request.getParameter(Studente.HANDICAP)));
			
			boolean registrazione_ok = check_fields(request, response, studente);
			
			if(registrazione_ok) {
				// Aggiorniamo i dati dello studente 
				studenteLoggato.setNome(studente.getNome());
				studenteLoggato.setCognome(studente.getCognome());
				studenteLoggato.setEmail(studente.getEmail());
				studenteLoggato.setUsername(studente.getUsername());
				studenteLoggato.setPassword(studente.getPassword());
				studenteLoggato.setTelefono(studente.getTelefono());
				studenteLoggato.setDataNascita(studente.getDataNascita());
				studenteLoggato.setLuogoNascita(studente.getLuogoNascita());
				studenteLoggato.setProvinciaNascita(studente.getProvinciaNascita());
				studenteLoggato.setResidenza(studente.getResidenza());
				studenteLoggato.setProvinciaResidenza(studente.getProvinciaResidenza());
				studenteLoggato.setTipoLaurea(studente.getTipoLaurea());
				studenteLoggato.setCorsoLaurea(studente.getCorsoLaurea());
				studenteLoggato.setHandicap(studente.isHandicap());
				new StudenteDAO().update(studente);
				// NOT USING request.getContextPath becouse it doesn't work with Heroku
				response.sendRedirect(".");
			}
			else {
				// studenteNR contiene i dati inseriti per la registrazione 
				// che non è andata a buon fine a causa di vlaori duplicati
				// verrà usato per non far reinserire tutti i valori all'utente
				// che ha tentato la registrazione
				request.setAttribute("updateFailed", true);
				request.setAttribute("studenteNR", studente);
				request.getRequestDispatcher(".").forward(request,response);
			}
			
		}
		catch(DataLayerException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
	}
	
	private void action_registra(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
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
				SecurityLayer.checkDate(request.getParameter(Studente.DATA_NASCITA)),
				request.getParameter(Studente.LUOGO_NASCITA),
				request.getParameter(Studente.PROVINCIA_NASCITA),
				request.getParameter(Studente.RESIDENZA),
				request.getParameter(Studente.PROVINCIA_RESIDENZA),
				request.getParameter(Studente.TIPO_LAUREA),
				request.getParameter(Studente.CORSO_LAUREA),
				SecurityLayer.checkBoolean(request.getParameter(Studente.HANDICAP)));
			
			boolean registrazione_ok = check_fields(request, response, studente);
			
			if(registrazione_ok) {
				// Inseriamo lo studente nel database
				new StudenteDAO().insert(studente);
				// NOT USING request.getContextPath becouse it doesn't work with Heroku
				response.sendRedirect(".");
			}
			else {
				// studenteNR contiene i dati inseriti per la registrazione 
				// che non è andata a buon fine a causa di vlaori duplicati
				// verrà usato per non far reinserire tutti i valori all'utente
				// che ha tentato la registrazione
				request.setAttribute("signUpFailed", true);
				// Riapriamo il modal della registrazione
				request.setAttribute("failed", true);
				request.setAttribute("studenteNR", studente);
				request.getRequestDispatcher(".").forward(request,response);
			}			
			
		}
		catch(DataLayerException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
	}

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

    	try {
			if(request.getParameter("aggiorna") != null) {
				// Verifichiamo che ci sia un utente loggato
				// Verifichiamo che l'utente loggato sia uno studente
				// Verifichiamo che lo studente stia effettivamente modificando il suo profilo
				if(request.getAttribute("utente") != null &&
				   request.getAttribute("utente") instanceof Studente &&
				   ((Studente)request.getAttribute("utente")).getCodiceFiscale().equals(request.getParameter("utente"))) {
					action_aggiorna(request, response);
				}
			}
			else if(request.getParameter("registrazione") != null) {
				action_registra(request, response);
			}
        }
    	catch (IllegalArgumentException | IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        }
    }

    @Override
    public String getServletInfo() {
        return "Student details";
    }

}
