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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import dao.impl.AziendaDAO;
import dao.impl.OffertaTirocinioDAO;
import dao.impl.ParereAziendaDAO;
import dao.impl.TirocinioStudenteDAO;
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
	
	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
	
	private void setConvezioneAzienda(HttpServletRequest request, HttpServletResponse response, Azienda azienda) {
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
		} catch (DataLayerException | IOException | TemplateManagerException | DocumentException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
	        action_error(request, response);
		} 
		
	}
	
	private void action_aggiorna(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException{
		try {
			Azienda azienda = (Azienda) request.getAttribute("utente");
			
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
			
			new AziendaDAO().update(azienda);
			
			// NOT USING request.getContextPath becouse it doesn't work with Heroku
			response.sendRedirect(".");
			
		} catch (DataLayerException | IOException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
	}
	
	private void action_registra(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
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
			
			new AziendaDAO().insert(azienda);
			
			// Generiamo il pdf schema convenzione
			setConvezioneAzienda(request, response, azienda);
			
			// NOT USING request.getContextPath becouse it doesn't work with Heroku
			response.sendRedirect(".");
			
		} catch (DataLayerException | IOException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		} 
	}

	// Show details
    private void action_default(HttpServletRequest request, HttpServletResponse response, String codiceFiscale) throws IOException, ServletException, TemplateManagerException {
        try {
	    	TemplateResult res = new TemplateResult(getServletContext());
	    	
	    	Studente studente = (Studente) request.getAttribute("utente");
	    	Azienda azienda = new AziendaDAO().getAziendaByCF(codiceFiscale);
	    	// Eventuale parere già espresso sull'azienda dallo studente loggato.
	    	ParereAzienda parere = null;
	    	// Indica se lo studente ha effettuato e terminato il tirocinio con l'azienda di cui sta visualizzando il dettaglio,
	    	// Questo ci permetterà di dare la possibilità allo studente di recensire l'azienda.
	    	boolean hasIntership = false;
	    	
	    	// Verifichiamo che non sia una visita in anonimo
	    	if(studente != null) {
	    		// Recuperiamo il tirocinio dello studente loggato se ne ha uno 
	    		TirocinioStudente tirocinioStudente = 
	    				new TirocinioStudenteDAO().getTirocinioStudenteByStudenteCF(studente.getCodiceFiscale());
	    		
	    		// Verifichiamo che lo studente abbia un tirocinio e che sia terminato
	    		if(tirocinioStudente != null && tirocinioStudente.getStato() == StatoRichiestaTirocinio.terminato) {
		    		int idTirocinio = tirocinioStudente.getTirocinio();
		    		Azienda aziendaStudente = new AziendaDAO().getAziendaByIDTirocinio(idTirocinio);
		    		// Verifichiamo che il tirocinio terminato dello studente sia dell'azienda che si sta guardando
		    		if(azienda != null && aziendaStudente.getCodiceFiscale().equals(aziendaStudente.getCodiceFiscale())) {
		    			parere = new ParereAziendaDAO().getParereStudente(studente, azienda);
		    			hasIntership = true;
		    		}
		    	}
	    	}	    	
	    	
	        Map<String, String> pareriAzienda = new ParereAziendaDAO().getPareriAzienda(azienda);
	        int voto = new ParereAziendaDAO().getMediaVoto(azienda);
	        
	        int numeroPagine = (int) Math.ceil(new OffertaTirocinioDAO().getCountAccordingToAzienda(azienda) / 5.0);
	        
	        int paginaCorrente = 0;
	        if(request.getParameter("pagina") != null) {
	        	paginaCorrente = SecurityLayer.checkNumeric(request.getParameter("pagina"));
	        }
	        
	        List<OffertaTirocinio> offerte = 
	        		new OffertaTirocinioDAO().allOfferteInRangeAccordingToAziendaAndVisibilita(
	        				azienda, true, paginaCorrente);

	        request.setAttribute("parere", parere);
	        request.setAttribute("hasIntership", hasIntership);
	        request.setAttribute("studente", studente);
	        request.setAttribute("azienda", azienda);
	        request.setAttribute("tirocini", offerte);
	        request.setAttribute("voto", voto);
	        request.setAttribute("pareriAzienda", pareriAzienda);
	        request.setAttribute("paginaCorrente", paginaCorrente);
	        request.setAttribute("numeroPagine", numeroPagine);
	        request.setAttribute("queryString", "&" + request.getQueryString());
	        
			res.activate("dettaglio-azienda.ftl.html", request, response);
        }
        catch (DataLayerException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    private void action_recensisci(HttpServletRequest request, HttpServletResponse response) {
		try {
			ParereAzienda parereAzienda = new ParereAzienda(
					request.getParameter("utente"),
					request.getParameter("azienda"),
					request.getParameter("parere"),
					SecurityLayer.checkNumeric(request.getParameter("rating")));
			
			if(request.getParameter("update") != null) {
				new ParereAziendaDAO().update(parereAzienda);
			}
			else {
				new ParereAziendaDAO().insert(parereAzienda);
			}
			
			if(request.getParameter("referrer") != null) {
				response.sendRedirect(request.getParameter("referrer") + "&utente=" + request.getParameter("utente"));
			}
			else {
				// NOT USING request.getContextPath becouse it doesn't work with Heroku
    			response.sendRedirect(".");
			}
			
		}
		 catch (DataLayerException | IOException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		 } 
	}

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

    	try {
    		request.setAttribute("page_css", "gestore-azienda");
    		
    		if(request.getParameter("aggiorna") != null) {
    			action_aggiorna(request, response);
    		}
    		else if(request.getParameter("recensisci") != null) {
    			action_recensisci(request, response);
    		}
    		else if(request.getParameter("registrazione") != null) {
    			action_registra(request, response);
    		}
    		else {
    			if(request.getParameter("azienda") != null) {
    				String codiceFiscale = request.getParameter("azienda");
					action_default(request, response, codiceFiscale);
    			}
    			else {
    				request.setAttribute("message", "Non è stata specificata un'azienda");
    			    action_error(request, response);
    			}
    		}
		} catch (IOException | TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } 
    }

    @Override
    public String getServletInfo() {
        return "Company details";
    }

}
