package controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import dao.impl.AziendaDAO;
import dao.impl.OffertaTirocinioDAO;
import dao.impl.ParereAziendaDAO;
import dao.impl.TirocinioStudenteDAO;
import dao.impl.UtenteDAO;
import data.model.Azienda;
import data.model.OffertaTirocinio;
import data.model.ParereAzienda;
import data.model.Studente;
import data.model.TirocinioStudente;
import data.model.Utente;
import data.model.enumeration.StatoRichiestaTirocinio;
import data.model.enumeration.TipoUtente;
import framework.data.DataLayerException;
import framework.result.FailureResult;
import framework.result.TemplateManagerException;
import framework.result.TemplateResult;
import framework.security.SecurityLayer;

@SuppressWarnings("serial")
public class GestoreAzienda extends IntershipTutorBaseController {
	
	public static final String SERVLET_URI = "/azienda";
	// Numero massimo di offerte per pagina per la paginazione,
	// questo numero deve corrispondere a quello in offertaTirocinioDAO
	final static double OFFERTE_PER_PAGINA = OffertaTirocinioDAO.OFFERTE_PER_PAGINA;
	
	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
	
	// Funzione per controllare se l'azienda può registrarsi o aggiornare la registrazione 
	// con i campi inseriti(verifica dei duplicati)
	private boolean check_fields(HttpServletRequest request, HttpServletResponse response, Azienda azienda) 
			throws DataLayerException {
		try {
			boolean registrazione_ok = true;
			
			// Registazione
			if(request.getParameter("registrazione") != null) {
				if(!new UtenteDAO().checkCodiceFiscaleDisponibile(azienda.getCodiceFiscale())) {
					request.setAttribute("PIInUso", true);
					registrazione_ok = false;
				}
				if(!new UtenteDAO().checkEmailDisponibile(azienda.getEmail())) {
					request.setAttribute("emailAInUso", true);
					registrazione_ok = false;
				}
				if(!new UtenteDAO().checkUsernameDisponibile(azienda.getUsername())) {
					request.setAttribute("usernameAInUso", true);
					registrazione_ok = false;
				}
				if(!new UtenteDAO().checkTelefonoDisponibile(azienda.getTelefono())) {
					request.setAttribute("telefonoAInUso", true);
					registrazione_ok = false;
				}
			}
			// Richiesta aggiornamento profilo
			else {
				
				Azienda aziendaLoggata = (Azienda)request.getAttribute("utente");
				
				if(!(aziendaLoggata.getEmail().equals(azienda.getEmail())) &&
				   !new UtenteDAO().checkEmailDisponibile(azienda.getEmail())) {
					
					request.setAttribute("emailAInUso", true);
					registrazione_ok = false;
				}
				if(!(aziendaLoggata.getUsername().equals(azienda.getUsername())) &&
				   !new UtenteDAO().checkUsernameDisponibile(azienda.getUsername())) {
					
					request.setAttribute("usernameAInUso", true);
					registrazione_ok = false;
				}
				if(!(aziendaLoggata.getTelefono().equals(azienda.getTelefono())) &&
				   !new UtenteDAO().checkTelefonoDisponibile(azienda.getTelefono())) {
					
					request.setAttribute("telefonoAInUso", true);
					registrazione_ok = false;
				}
			}
			
			return registrazione_ok;
		}
		catch(DataLayerException ex) {
			throw new DataLayerException("Errore nella verifica dei campi");
		}
	}
	
	private void setConvezioneAzienda(HttpServletRequest request, HttpServletResponse response, Azienda azienda) 
			throws IOException, ServletException, DocumentException, TemplateManagerException{
		try {
			TemplateResult res = new TemplateResult(getServletContext());
			
			request.setAttribute("azienda", azienda);
			
			String schemaConvenzione = res.getFilledTemplate("doc/schema-convenzione.ftl.html", request);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			// Generating pdf
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			document.open();
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(schemaConvenzione.getBytes(StandardCharsets.UTF_8)));
			document.close();

			ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(baos.toByteArray());
			
			new AziendaDAO().setConvenzioneDoc(pdfInputStream, azienda);
		} catch (DataLayerException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
	        action_error(request, response);
		} 
		
	}
	
	private void action_aggiorna(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException{
		try {
			Azienda aziendaLoggata = (Azienda) request.getAttribute("utente");
			Azienda azienda = new Azienda();
			
			azienda.setNome(request.getParameter(Azienda.NOME));
			azienda.setEmail(request.getParameter(Azienda.EMAIL));
			azienda.setUsername(request.getParameter(Azienda.USERNAME));
			azienda.setPassword(request.getParameter(Azienda.PASSWORD));
			azienda.setTelefono(request.getParameter(Studente.TELEFONO));
			azienda.setRegione(request.getParameter(Azienda.REGIONE));
			azienda.setIndirizzoSedeLegale(request.getParameter(Azienda.INDIRIZZO_SEDE_LEGALE));
			azienda.setForoCompetente(request.getParameter(Azienda.FORO_COMPETENTE));
			azienda.setNomeRappresentante(request.getParameter(Azienda.NOME_RAPPRESENTANTE));
			azienda.setCognomeRappresentante(request.getParameter(Azienda.COGNOME_RAPPRESENTANTE));
			azienda.setNomeResponsabile(request.getParameter(Azienda.NOME_RESPONSABILE));
			azienda.setCognomeResponsabile(request.getParameter(Azienda.COGNOME_RESPONSABILE));
			
			boolean registrazione_ok = check_fields(request, response, azienda);
			
			if(registrazione_ok) {
				aziendaLoggata.setNome(azienda.getNome());
				aziendaLoggata.setEmail(azienda.getEmail());
				aziendaLoggata.setUsername(azienda.getUsername());
				aziendaLoggata.setTelefono(azienda.getTelefono());
				aziendaLoggata.setRegione(azienda.getRegione());
				aziendaLoggata.setIndirizzoSedeLegale(azienda.getIndirizzoSedeLegale());
				aziendaLoggata.setForoCompetente(azienda.getForoCompetente());
				aziendaLoggata.setNomeRappresentante(azienda.getNomeRappresentante());
				aziendaLoggata.setCognomeRappresentante(azienda.getCognomeRappresentante());
				aziendaLoggata.setNomeResponsabile(azienda.getNomeResponsabile());
				aziendaLoggata.setCognomeResponsabile(azienda.getCognomeResponsabile());
				// Aggiorniamo i dati dell'azienda
				new AziendaDAO().update(azienda);
				// NOT USING request.getContextPath because it doesn't work with Heroku
				response.sendRedirect(".");
			}
			else {
				// aziendaNR contiene i dati inseriti per la registrazione 
				// che non è andata a buon fine a causa di vlaori duplicati
				// verrà usato per non far reinserire tutti i valori all'utente
				// che ha tentato la registrazione
				request.setAttribute("updateAFailed", true);
				request.setAttribute("aziendaNR", azienda);
				request.getRequestDispatcher(".").forward(request,response);
			}
			
		} catch (DataLayerException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
	}
	
	private void action_registra(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException, DocumentException, TemplateManagerException {
		try {
			Azienda azienda = new Azienda(
					request.getParameter(Utente.CODICE_FISCALE),
					request.getParameter(Utente.EMAIL),
					request.getParameter(Utente.USERNAME),
					request.getParameter(Utente.PASSWORD),
					request.getParameter(Utente.TELEFONO),
					TipoUtente.azienda,
					request.getParameter(Azienda.CODICE_FISCALE),
					request.getParameter(Azienda.NOME),
					request.getParameter(Azienda.REGIONE),
					request.getParameter(Azienda.INDIRIZZO_SEDE_LEGALE),
					request.getParameter(Azienda.FORO_COMPETENTE),
					request.getParameter(Azienda.NOME_RAPPRESENTANTE),
					request.getParameter(Azienda.COGNOME_RAPPRESENTANTE),
					request.getParameter(Azienda.NOME_RESPONSABILE),
					request.getParameter(Azienda.COGNOME_RESPONSABILE),
					false);
			
			boolean registrazione_ok = check_fields(request, response, azienda);
			
			if(registrazione_ok) {
				new AziendaDAO().insert(azienda);
				// Generiamo il pdf schema convenzione
				setConvezioneAzienda(request, response, azienda);
				// NOT USING request.getContextPath becouse it doesn't work with Heroku
				response.sendRedirect(".");
			}
			else {
				// aziendaNR contiene i dati inseriti per la registrazione 
				// che non è andata a buon fine a causa di vlaori duplicati
				// verrà usato per non far reinserire tutti i valori all'utente
				// che ha tentato la registrazione
				request.setAttribute("signUpAFailed", true);
				// Riapriamo il modal della registrazione
				request.setAttribute("failed", true);
				request.setAttribute("aziendaNR", azienda);
				request.getRequestDispatcher(".").forward(request,response);
			}
			
		} catch (DataLayerException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		} 
	}

	// Show details
    private void action_default(HttpServletRequest request, HttpServletResponse response) 
    		throws IOException, ServletException, TemplateManagerException {
        try {
	    	TemplateResult res = new TemplateResult(getServletContext());
	    	
	    	Azienda azienda = new AziendaDAO().getAziendaByCF(request.getParameter("azienda"));   	
	        Map<String, String> pareriAzienda = new ParereAziendaDAO().getPareriAzienda(azienda);
	        int voto = new ParereAziendaDAO().getMediaVoto(azienda);
	        
	        int numeroPagine = (int) Math.ceil(new OffertaTirocinioDAO().getCountAccordingToAzienda(azienda) / OFFERTE_PER_PAGINA);
	        
	        int paginaCorrente = 0;
	        if(request.getParameter("pagina") != null) {
	        	paginaCorrente = SecurityLayer.checkNumeric(request.getParameter("pagina"));
	        }
	        
	        List<OffertaTirocinio> offerte = 
	        		new OffertaTirocinioDAO().allOfferteInRangeAccordingToAziendaAndVisibilita(
	        				azienda, true, paginaCorrente);

	        // Verifichiamo che l'utente loggato sia uno studente
	        // in modo da dargli la possibilità di recensire l'azienda(se ha effettuato un tirocinio
	        // tramite essa) o di aggiornare la sua recensione
	        if(request.getAttribute("utente") instanceof Studente) {
	    		Studente studente = (Studente) request.getAttribute("utente");
	    		// Eventuale parere già espresso sull'azienda dallo studente loggato.
		    	ParereAzienda parere = null;
		    	// Indica se lo studente ha effettuato e terminato il tirocinio con l'azienda di cui sta visualizzando il dettaglio,
		    	// Questo ci permetterà di dare la possibilità allo studente di recensire l'azienda.
		    	boolean hasIntership = false; 
	    		// Recuperiamo il tirocinio dello studente loggato se ne ha uno 
	    		TirocinioStudente tirocinioStudente = 
	    				new TirocinioStudenteDAO().getTirocinioStudenteByStudenteCF(studente.getCodiceFiscale());
	    		
	    		// Verifichiamo che lo studente abbia un tirocinio e che sia terminato
	    		if(tirocinioStudente != null && tirocinioStudente.getStato() == StatoRichiestaTirocinio.terminato) {
		    		int idTirocinio = tirocinioStudente.getTirocinio();
		    		Azienda aziendaStudente = new AziendaDAO().getAziendaByIDTirocinio(idTirocinio);
		    		// Verifichiamo che il tirocinio terminato dello studente sia dell'azienda che si sta guardando
		    		if(aziendaStudente != null && aziendaStudente.getCodiceFiscale().equals(azienda.getCodiceFiscale())) {
		    			parere = new ParereAziendaDAO().getParereStudente(studente, azienda);
		    			hasIntership = true;
		    			request.setAttribute("studente", studente);
		    			request.setAttribute("parere", parere);
		    	        request.setAttribute("hasIntership", hasIntership);
		    		}
		    	}
	    	}
	        
	        request.setAttribute("azienda", azienda);
	        request.setAttribute("tirocini", offerte);
	        request.setAttribute("voto", voto);
	        request.setAttribute("pareriAzienda", pareriAzienda);
	        request.setAttribute("paginaCorrente", paginaCorrente);
	        request.setAttribute("numeroPagine", numeroPagine);
	        request.setAttribute("queryString", "&" + request.getQueryString());
	        
			res.activate("dettaglio-azienda.ftl.html", request, response);
        }
        catch (DataLayerException e) {
            request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
        }
    }
    
    private void action_recensisci(HttpServletRequest request, HttpServletResponse response) 
    		throws IOException, ServletException {
		try {
			
			String codiceFiscale = ((Studente)request.getAttribute("utente")).getCodiceFiscale();
			
			ParereAzienda parereAzienda = new ParereAzienda(
					codiceFiscale,
					request.getParameter("azienda"),
					request.getParameter("parere"),
					SecurityLayer.checkNumeric(request.getParameter("rating")));
			
			if(request.getParameter("update") != null) {
				new ParereAziendaDAO().update(parereAzienda);
			}
			else {
				new ParereAziendaDAO().insert(parereAzienda);
			}
			
			if(request.getParameter("referer") != null) {
				response.sendRedirect(request.getHeader("referer"));
			}
			else {
				// NOT USING request.getContextPath because it doesn't work with Heroku
    			response.sendRedirect(".");
			}
			
		}
		 catch (DataLayerException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		 } 
	}

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

    	try {
    		request.setAttribute("page_css", "gestore-azienda");
  
    		if(request.getParameter("aggiorna") != null || request.getParameter("recensisci") != null) {
    			HttpSession session = SecurityLayer.checkSession(request);
    			
    			if(session != null) {
    				// Richiesta di aggiornamento del profilo di un'azienda
    				if(request.getParameter("aggiorna") != null) {
    					// Verifichiamo che l'utente loggato sia un'azineda    					
    					if(request.getAttribute("utente") instanceof Azienda &&
    						((Azienda)request.getAttribute("utente")).getCodiceFiscale()
    							.equals(request.getParameter("utente"))) {
    						action_aggiorna(request, response);
    					}
    					else {
    						request.setAttribute("message", "Azione non autorizzata:<br />" + 
    														"Si sta cercando di modificare il profilo di un'altra azienda");
    	                    action_error(request, response);
    					}
    				}
    				// Lo studente vuole recensire l'azienda
    				else {
    					// Verifichiamo che l'utente loggato sia uno studente
    					if(request.getAttribute("utente") instanceof Studente) {
    						action_recensisci(request, response);
    					}
    					else {
    						request.setAttribute("message", "Studente non autenticato:<br />" + 
    														"È necessario l'accesso per recensire l'azienda");
    	                    action_error(request, response);
    					}
    				}
    			}
    			else {
    				request.setAttribute("message", "Azione non autorizzata:<br />" + 
    												"È necessario l'accesso per completare l'operazione");
                    action_error(request, response);
    			}
    		}
    		else if(request.getParameter("azienda") != null) {
    			action_default(request, response);
    		}
    		else if(request.getParameter("registrazione") != null) {
    			action_registra(request, response);
    		}
    		else {
    			request.setAttribute("message", "Errore:<br />Nessuna opzione valida specificata");
                action_error(request, response);
    		}
    		
    		
		} catch (IOException | TemplateManagerException | IllegalArgumentException | DocumentException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } 
    }

    @Override
    public String getServletInfo() {
        return "Company details";
    }

}
