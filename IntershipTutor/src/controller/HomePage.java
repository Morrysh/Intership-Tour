package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.AziendaDAO;
import dao.impl.OffertaTirocinioDAO;
import data.model.Azienda;
import data.model.OffertaTirocinio;
import data.model.enumeration.CampoRicercaTirocinio;
import framework.result.FailureResult;
import framework.result.TemplateManagerException;
import framework.result.TemplateResult;

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
        List<OffertaTirocinio> offerte = new OffertaTirocinioDAO().allOfferteTirocinioAccordingToVisibilita(true);
        
        request.setAttribute("page_css", "homepage");
        request.setAttribute("campiRicerca", campiRicerca);
		request.setAttribute("offerte", offerte);
		request.setAttribute("aziende", aziende);
		
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
