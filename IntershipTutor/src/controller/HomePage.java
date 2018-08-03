package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.impl.AziendaDAO;
import dao.impl.OffertaTirocinioDAO;
import data.model.Amministratore;
import data.model.Azienda;
import data.model.OffertaTirocinio;
import data.model.Studente;
import data.model.enumeration.CampoRicercaTirocinio;
import framework.result.FailureResult;
import framework.result.TemplateManagerException;
import framework.result.TemplateResult;
import framework.security.SecurityLayer;

@SuppressWarnings("serial")
public class HomePage extends IntershipTutorBaseController {

	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException {
        TemplateResult res = new TemplateResult(getServletContext());
        
        CampoRicercaTirocinio[] campiRicerca = CampoRicercaTirocinio.values();
        List<Azienda> aziende = new AziendaDAO().allAziendeAccordingToConvention(true);
        List<OffertaTirocinio> offerte;
        
        Map<CampoRicercaTirocinio, String> campiDaCercare = new HashMap<>();
        
        for(CampoRicercaTirocinio campoRicerca : campiRicerca) {
        	if(request.getParameter(campoRicerca.name()) != "" &&
        	   request.getParameter(campoRicerca.name()) != null) {
        		campiDaCercare.put(campoRicerca, (String) request.getParameter(campoRicerca.name()));
        	}
        }
        
        if(campiDaCercare.size() != 0) {
        	// Tutte le offerte in base alla ricerca
        	offerte = new OffertaTirocinioDAO().filtraPerCampo(campiDaCercare);
        }
        else {
        	// Tutte le offerte senza filtraggio
            offerte = new OffertaTirocinioDAO().allOfferteTirocinioAccordingToVisibilita(true);
        }
        
        
        Object utente = null;
        
        HttpSession s = SecurityLayer.checkSession(request);
        
        if(s != null) {
        	if(s.getAttribute("utente") instanceof Studente)
        		utente = (Studente) s.getAttribute("utente");
        	else if(s.getAttribute("utente") instanceof Azienda)
        		utente = (Azienda) s.getAttribute("utente");
        	else
        		utente = (Amministratore) s.getAttribute("utente");
        }
        
        request.setAttribute("page_css", "homepage");
        request.setAttribute("campiRicerca", campiRicerca);
		request.setAttribute("offerte", offerte);
		request.setAttribute("aziende", aziende);
		request.setAttribute("utente", utente);
		
		
		res.activate("homepage.ftl.html", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

    	try {
            action_default(request, response);

        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        } catch (SQLException ex) {
        	request.setAttribute("exception", ex);
            action_error(request, response);
		}
    }

    @Override
    public String getServletInfo() {
        return "Main IntershipTutor servlet";
    }// </editor-fold>

}
