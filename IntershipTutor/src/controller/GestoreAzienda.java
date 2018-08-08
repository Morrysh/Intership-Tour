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
	
	private void action_aggiorna(HttpServletRequest request, HttpServletResponse response, String codiceFiscale) {
		
	}
	
	private void action_offerte_proposte(HttpServletRequest request, HttpServletResponse response, String codiceFiscale) {
		
	}

    private void action_default(HttpServletRequest request, HttpServletResponse response, String codiceFiscale) throws IOException, ServletException, TemplateManagerException {
        try {
	    	TemplateResult res = new TemplateResult(getServletContext());
	        
	        Azienda azienda = new AziendaDAO().getAziendaByCF(codiceFiscale);
	        Map<String, String> pareriAzienda = new ParereAziendaDAO().getPareriAzienda(azienda);
	        int voto = new ParereAziendaDAO().getMediaVoto(azienda);
	        
	        int numeroPagine = (int) Math.ceil(new OffertaTirocinioDAO().getCountAccordingToAzienda(azienda) / 5.0);
	        
	        // Verifica della pagina, impostata sulla prima di default
	        int paginaCorrente = 0;
	        if(request.getParameter("pagina") != null) {
	        	paginaCorrente = (SecurityLayer.checkNumeric(request.getParameter("pagina")));
	        }
	        
	        List<OffertaTirocinio> offerte = new OffertaTirocinioDAO().offerteTirocinioByAzienda(azienda, paginaCorrente);

	        request.setAttribute("azienda", azienda);
	        request.setAttribute("tirocini", offerte);
	        request.setAttribute("voto", voto);
	        request.setAttribute("pareriAzienda", pareriAzienda);
	        request.setAttribute("paginaCorrente", paginaCorrente);
	        request.setAttribute("numeroPagine", numeroPagine);
	        request.setAttribute("queryString", "&" + request.getQueryString());
	        
			res.activate("gestore-azienda.ftl.html", request, response);
        }
        catch (DataLayerException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

    	try {
    		request.setAttribute("page_css", "gestore-azienda");
    		if(request.getParameter("azienda") != null) { 
    			String codiceFiscale = request.getParameter("azienda");
	    		if(request.getParameter("aggiorna") != null) {
	    			action_aggiorna(request, response, codiceFiscale);
	    		}
	    		else if(request.getParameter("offerte") != null) {
	    			action_offerte_proposte(request, response, codiceFiscale);
	    		}
	    		else {
	    			action_default(request, response, codiceFiscale);
	    		}
    		}
    		else {
    			request.setAttribute("message", "Non è stata specificata un'azienda");
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
        return "Company details";
    }

}
