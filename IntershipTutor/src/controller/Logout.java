package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.result.FailureResult;
import framework.security.SecurityLayer;

@SuppressWarnings("serial")
public class Logout extends IntershipTutorBaseController{

	private void action_error(HttpServletRequest request, HttpServletResponse response) {
		if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }

    private void action_logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityLayer.disposeSession(request);
        
        if (request.getParameter("referrer") != null) {
        	// REMEMBER TO USE THIS TO REDIRECT ON THE PAGE WHERE THE USER UNLOGGED
            response.sendRedirect(request.getParameter("referrer"));
        } else {
            response.sendRedirect(request.getContextPath());
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
    	try {
            action_logout(request, response);

        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    public String getServletInfo() {
        return "Short description";
    }

}
