package controller;

import java.io.IOException;
import java.util.Map;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.AziendaDAO;
import dao.impl.OffertaTirocinioDAO;
import dao.impl.ParereAziendaDAO;
import data.model.Azienda;
import data.model.OffertaTirocinio;
import data.model.Studente;
import data.model.Utente;
import data.model.enumeration.TipoUtente;
import framework.data.DataLayerException;
import framework.result.FailureResult;
import framework.result.TemplateManagerException;
import framework.result.TemplateResult;
import framework.security.SecurityLayer;

@SuppressWarnings("serial")
public class GestoreAzienda extends IntershipTutorBaseController {
	
	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
	
	private void action_aggiorna(HttpServletRequest request, HttpServletResponse response) {
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
			
			response.sendRedirect(request.getContextPath());
			
		} catch (DataLayerException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		} catch (IOException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
	}
	
	private void action_registra(HttpServletRequest request, HttpServletResponse response) {
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
			
			response.sendRedirect(request.getContextPath());
			
		} catch (DataLayerException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		} catch (IOException e) {
			request.setAttribute("message", "Data access exception: " + e.getMessage());
            action_error(request, response);
		}
	}
	
	private void action_offerte_proposte(HttpServletRequest request, HttpServletResponse response) {
		
	}

    private void action_default(HttpServletRequest request, HttpServletResponse response, String codiceFiscale) throws IOException, ServletException, TemplateManagerException {
        try {
	    	TemplateResult res = new TemplateResult(getServletContext());
	        
	        Azienda azienda = new AziendaDAO().getAziendaByCF(codiceFiscale);
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
    
    private void action_dettaglio(HttpServletRequest request, HttpServletResponse response, String codiceFiscale) {
		
	}

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

    	try {
    		request.setAttribute("page_css", "gestore-azienda");
    		if(request.getParameter("aggiorna") != null) {
    			action_aggiorna(request, response);
    		}
    		else if(request.getParameter("offerte") != null) {
    			action_offerte_proposte(request, response);
    		}
    		else if(request.getParameter("registrazione") != null) {
    			action_registra(request, response);
    		}
    		else {
    			if(request.getParameter("azienda") != null) {
    				String codiceFiscale = request.getParameter("azienda");
					if(request.getParameter("dettaglio") != null) {
	        			action_dettaglio(request, response, codiceFiscale);
	        		}
					else {
						action_default(request, response, codiceFiscale);
					}
    			}
    			else {
    				request.setAttribute("message", "Non è stata specificata un'azienda");
    			    action_error(request, response);
    			}
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
        return "Company details";
    }

}
