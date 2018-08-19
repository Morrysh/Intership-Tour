package controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import dao.impl.AziendaDAO;
import dao.impl.OffertaTirocinioDAO;
import dao.impl.StudenteDAO;
import dao.impl.TirocinioStudenteDAO;
import data.model.Azienda;
import data.model.OffertaTirocinio;
import data.model.Studente;
import data.model.TirocinioStudente;
import data.model.enumeration.StatoRichiestaTirocinio;
import framework.data.DataLayerException;
import framework.result.FailureResult;
import framework.result.TemplateManagerException;
import framework.result.TemplateResult;
import framework.security.SecurityLayer;
import utils.email.EmailSender;

@SuppressWarnings("serial")
public class GestoreTirocinioStudente extends IntershipTutorBaseController {
	
	// Numero massimo di offerte per pagina per la paginazione,
	// questo numero deve corrispondere a quello in offertaTirocinioDAO
	final static double CANDIDATI_PER_PAGINA = TirocinioStudenteDAO.CANDIDATI_PER_PAGINA;


	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
	
	private void action_show_candidates(HttpServletRequest request, HttpServletResponse response, int idTirocinio) throws IOException, ServletException, TemplateManagerException{
		try {
			TemplateResult res = new TemplateResult(getServletContext());
			
			Azienda azienda = (Azienda) request.getAttribute("utente");
			
			OffertaTirocinio offertaTirocinio = new OffertaTirocinioDAO().getOffertaByID(idTirocinio);
			
			int paginaCorrente = 0;
	        if(request.getParameter("pagina") != null) {
	        	paginaCorrente = (SecurityLayer.checkNumeric(request.getParameter("pagina")));
	        }
			int numeroPagine = (int) Math.ceil(new TirocinioStudenteDAO().getCountAccordingToOffertaTirocinio(offertaTirocinio) / CANDIDATI_PER_PAGINA);

			// La linked hash map è usate per preservare l'ordine
			Map<Studente, TirocinioStudente> candidatoTirocinio = new LinkedHashMap<>();
			List<Studente> candidati = new TirocinioStudenteDAO().getStudentiByOffertaTirocinio(offertaTirocinio, paginaCorrente);
			
			for(Studente studente : candidati) {
				candidatoTirocinio.put(studente, new TirocinioStudenteDAO().getTirocinioStudenteByStudenteCF(studente.getCodiceFiscale()));
			}	     
			
			String queryString = "&id_tirocinio=" + offertaTirocinio.getIdTirocinio();
			
			//request.setAttribute("servlet", "tirocinioStudente");
	        //request.setAttribute("searchbartitle", "Filtra le candidature");
			//request.setAttribute("campiRicerca", CampoRicercaCandidato.values());
			request.setAttribute("page_css", "candidati");
			request.setAttribute("queryString", queryString);
	        request.setAttribute("numeroPagine", numeroPagine);
			request.setAttribute("paginaCorrente", paginaCorrente);
			request.setAttribute("azienda", azienda);
			request.setAttribute("offertaTirocinio", offertaTirocinio);
			request.setAttribute("candidatoTirocinio", candidatoTirocinio);
			
			res.activate("candidati.ftl.html", request, response);
			
		}
		catch (DataLayerException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
	}
	
	private void action_show_concluded_interships(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException{
		try {
			TemplateResult res = new TemplateResult(getServletContext());
			
			Azienda azienda = (Azienda) request.getAttribute("utente");
			
			int paginaCorrente = 0;
	        if(request.getParameter("pagina") != null) {
	        	paginaCorrente = (SecurityLayer.checkNumeric(request.getParameter("pagina")));
	        }
	        int numeroPagine = (int) Math.ceil(new TirocinioStudenteDAO().getCountTirociniStudenteTerminatiAccordingToAzienda(azienda) / CANDIDATI_PER_PAGINA);
			
			List<Studente> studenti = new TirocinioStudenteDAO().getStudentiInRangeByTirocinioConclusoAccordingToAzienda(azienda, paginaCorrente);
			
			Map<Studente, Map<TirocinioStudente, OffertaTirocinio>> candidatoTirocinioOfferta =
					new LinkedHashMap<>();
			
			for(Studente studente : studenti) {
				Map<TirocinioStudente, OffertaTirocinio> tirocinioOfferta = new LinkedHashMap<>();
				TirocinioStudente tirocinioStudente = new TirocinioStudenteDAO().getTirocinioStudenteByStudenteCF(studente.getCodiceFiscale());
				OffertaTirocinio offertaTirocinio = new OffertaTirocinioDAO().getOffertaByID(tirocinioStudente.getTirocinio());
				tirocinioOfferta.put(tirocinioStudente, offertaTirocinio);
				candidatoTirocinioOfferta.put(studente, tirocinioOfferta);
			}
			
			request.setAttribute("queryString", "&conclusi=true");
			request.setAttribute("azienda", azienda);
			request.setAttribute("candidatoTirocinioOfferta", candidatoTirocinioOfferta);
			request.setAttribute("page_css", "tirocini-conclusi");
	        request.setAttribute("numeroPagine", numeroPagine);
			request.setAttribute("paginaCorrente", paginaCorrente);
			
			res.activate("tirocini-conclusi.ftl.html", request, response);
		}
		catch (DataLayerException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
	}
	
	private void action_iscriviti(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
        	
        	Studente studente = (Studente) request.getAttribute("utente");
        	
        	Azienda azienda = new AziendaDAO().getAziendaByIDTirocinio(
        			SecurityLayer.checkNumeric(request.getParameter(TirocinioStudente.ID_TIROCINIO)));
        	
        	TirocinioStudente tirocinioStudente = new TirocinioStudente(
        			studente.getCodiceFiscale(),
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
        	
        	// INVIO EMAIL ALLO STUDENTE E AL TUTORE SCELTO
        	
        	// Titolo email
        	String messageTitle = "Candidatura Tirocinio";
        	// Corpo email
        	String messageBody = "Lo studente " + studente.getNome() + " " + studente.getCognome() +
			    				 " ha inviato la sua candidatira ad un tirocinio offerto dall'azienda " + 
			    				 azienda.getNome() + " scegliendo come tutore universitario " + 
			    				 request.getParameter(TirocinioStudente.TUTORE_UNIVERSITARIO);
        	// Destinatari email
        	String recipientsEmails[] = {
        			// azienda.getEmail(),
        			// request.getParameter(TirocinioStudente.EMAIL_TUTORE)
        			"stefano.martella96@gmail.com",
        			"stefano.martella9614@gmail.com"
        	};
        	// Invio email
        	EmailSender.sendEmail(messageTitle, messageBody, recipientsEmails);
        	
        	response.sendRedirect(request.getContextPath());
        }
        catch(DataLayerException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
        catch (MessagingException e) {
        	request.setAttribute("message", "Invio mail fallito: " + e.getMessage());
            action_error(request, response);
        }
    }
	
	private void action_rimuovi(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException{
		try {
			Studente studente = (Studente) request.getAttribute("utente");
			
			new TirocinioStudenteDAO().delete(studente);
			
			response.sendRedirect(request.getContextPath());
			
		}
		catch (DataLayerException | IOException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
	}
	
	private void action_update_state(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException{
		try {	    
			String codiceFiscale = request.getParameter(Studente.CODICE_FISCALE);
			StatoRichiestaTirocinio statoRichiestaTirocinio = StatoRichiestaTirocinio.valueOf(request.getParameter(TirocinioStudente.STATO));		
			
			// Aggiorniamo il progetto formativo se il tirocinio è stato accettato
			
			
			switch(statoRichiestaTirocinio) {
				case accettato: 
					new TirocinioStudenteDAO().updateStato(codiceFiscale, statoRichiestaTirocinio);
					update_training_project(request, response);
					break;
				
				case terminato:
					TirocinioStudente tirocinioStudente = new TirocinioStudenteDAO().getTirocinioStudenteByStudenteCF(codiceFiscale);

					tirocinioStudente.setDescrizioneDettagliata(request.getParameter(TirocinioStudente.DESCRIZIONE_DETTAGLIATA));
					tirocinioStudente.setOreSvolte(SecurityLayer.checkNumeric(request.getParameter(TirocinioStudente.ORE_SVOLTE)));
					tirocinioStudente.setGiudizioFinale(request.getParameter(TirocinioStudente.GIUDIZIO_FINALE));
					tirocinioStudente.setStato(StatoRichiestaTirocinio.terminato);
					
					new TirocinioStudenteDAO().update(tirocinioStudente);
					break;
					
				default:
					new TirocinioStudenteDAO().updateStato(codiceFiscale, statoRichiestaTirocinio);
			}
			
			
			if(request.getParameter("referrer") != null) {
				response.sendRedirect(request.getParameter("referrer"));
			}
			else {
				response.sendRedirect(request.getContextPath());
			}
		}
		catch (DataLayerException | IOException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
	}
	
	public void update_training_project(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
		try {
			TemplateResult res = new TemplateResult(getServletContext());
			
			Studente studente = new StudenteDAO().getStudenteByCF(request.getParameter(Studente.CODICE_FISCALE));
			TirocinioStudente tirocinioStudente = new TirocinioStudenteDAO().getTirocinioStudenteByStudenteCF(studente.getCodiceFiscale());
			OffertaTirocinio offertaTirocinio = new OffertaTirocinioDAO().getOffertaByID(tirocinioStudente.getTirocinio());
			Azienda azienda = new AziendaDAO().getAziendaByIDTirocinio(offertaTirocinio.getIdTirocinio());
			
			request.setAttribute("studente", studente);
			request.setAttribute("tirocinioStudente", tirocinioStudente);
			request.setAttribute("tirocinio", offertaTirocinio);
			request.setAttribute("azienda", azienda);
			request.setAttribute("data", new Date(System.currentTimeMillis()));
			
			String progettoFormativo = res.getFilledTemplate("doc/progetto-formativo.ftl.html", request);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			// Generating pdf
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			document.open();
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(progettoFormativo.getBytes(StandardCharsets.UTF_8)));
			document.close();

			ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(baos.toByteArray());
			
			new TirocinioStudenteDAO().setProgettoFormativo(pdfInputStream, studente);
		}
		catch (DataLayerException | IOException | DocumentException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
	}
	
	@Override
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		try {
			if(request.getParameter("rimuovi") != null) {
				action_rimuovi(request, response);
			}
			else if(request.getParameter("conclusi") != null) {
				action_show_concluded_interships(request, response);
			}
			else if(request.getParameter("iscriviti") != null) {
				if(request.getParameter(TirocinioStudente.ID_TIROCINIO) != null) {
					action_iscriviti(request, response);
				}
			}
			else if(request.getParameter(OffertaTirocinio.ID_TIROCINIO) != null) {
				int idTirocinio = SecurityLayer.checkNumeric(request.getParameter(OffertaTirocinio.ID_TIROCINIO));
				action_show_candidates(request, response, idTirocinio);
			}
			else if(request.getParameter(Studente.CODICE_FISCALE) != null &&
			   request.getParameter(TirocinioStudente.STATO) != null) {
				action_update_state(request, response);
			}
			else {
				request.setAttribute("message", "Non è stata specificato uno studente, un tirocinio o un nuovo stato per la candidatura");
			    action_error(request, response);
			}
		} catch (IOException | TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } 
		
	}

}
