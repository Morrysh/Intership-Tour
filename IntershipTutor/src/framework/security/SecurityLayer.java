package framework.security;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SecurityLayer {
	
    public static HttpSession checkSession(HttpServletRequest r) {
        boolean check = true;

        HttpSession s = r.getSession(false);
        //per prima cosa vediamo se la sessione è attiva
        if (s == null) {
            return null;
        }

        //check sulla validità  della sessione
        if (s.getAttribute("utente") == null) {
            check = false;
        } else {
            //inizio sessione
            Calendar begin = (Calendar) s.getAttribute("inizio-sessione");
            //ultima azione
            Calendar last = (Calendar) s.getAttribute("ultima-azione");
            //data/ora correnti
            Calendar now = Calendar.getInstance();
            if (begin == null) {
                check = false;
            } else {
                //secondi trascorsi dall'inizio della sessione
                long secondsfrombegin = (now.getTimeInMillis() - begin.getTimeInMillis()) / 1000;
                //dopo tre ore la sessione scade
                if (secondsfrombegin > 3 * 60 * 60) {
                    check = false;
                } else if (last != null) {
                    //secondi trascorsi dall'ultima azione
                    long secondsfromlast = (now.getTimeInMillis() - last.getTimeInMillis()) / 1000;
                    //dopo trenta minuti dall'ultima operazione la sessione è invalidata                 
                    if (secondsfromlast > 30 * 60) {
                        check = false;
                    }
                }
            }
        }
        if (!check) {
            s.invalidate();
            return null;
        } else {
            //reimpostiamo la data/ora dell'ultima azione
            s.setAttribute("ultima-azione", Calendar.getInstance());
            return s;
        }
    }

    public static HttpSession createSession(HttpServletRequest request, Object utente) {
        HttpSession s = request.getSession(true);
        s.setAttribute("utente", utente);
        s.setAttribute("ip", request.getRemoteHost());
        s.setAttribute("inizio-sessione", Calendar.getInstance());
        return s;
    }

    public static void disposeSession(HttpServletRequest request) {
        HttpSession s = request.getSession(false);
        if (s != null) {
            s.invalidate();
        }
    }
    
    /*public static String addSlashes(String s) {
        return s.replaceAll("(['\"\\\\])", "\\\\$1");
    }

    public static String stripSlashes(String s) {
        return s.replaceAll("\\\\(['\"\\\\])", "$1");
    }*/

    public static int checkNumeric(String number) throws NumberFormatException {
        //convertiamo la stringa in numero, ma assicuriamoci prima che sia valida
        try {
            //se la conversione fallisce, viene generata un'eccezione
            return Integer.valueOf(number);
        } catch(NumberFormatException e) {
            throw new NumberFormatException("Il valore inserito non è un numero");
        }
    }
    
    public static Boolean checkBoolean(String bool) throws NumberFormatException {
        //convertiamo la stringa in booleano, ma assicuriamoci prima che sia valida
        try {
            //se la conversione fallisce, viene generata un'eccezione
            return Boolean.valueOf(bool);
        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("Il valore inserito non è valido");
        }
    }
    
    public static Time checkTime(String time) throws NumberFormatException {
        //convertiamo la stringa in Time, ma assicuriamoci prima che sia valida
        try {
            //se la conversione fallisce, viene generata un'eccezione
            return Time.valueOf(time);
        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("Il valore inserito non è un orario");
        }
    }
    
    public static Date checkDate(String date) throws NumberFormatException {
        //convertiamo la stringa in Date, ma assicuriamoci prima che sia valida
        try {
            //se la conversione fallisce, viene generata un'eccezione
            return Date.valueOf(date);
        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("Il valore inserito non è una data");
        }
    }

    //questa funzione verifica se il protocollo HTTPS è attivo
    public static boolean checkHttps(HttpServletRequest r) {
        return r.isSecure();
        //metodo "fatto a mano" che funziona solo se il server trasmette gli header corretti
        //the following is an "handmade" alternative, which works only if the server sends correct headers
        //String httpsheader = r.getHeader("HTTPS");
        //return (httpsheader != null && httpsheader.toLowerCase().equals("on"));
    }

    //questa funzione ridirige il browser sullo stesso indirizzo
    //attuale, ma con protocollo https
    public static void redirectToHttps(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //estraiamo le parti della request url
        String server = request.getServerName();
        //int port = request.getServerPort();
        String context = request.getContextPath();
        String path = request.getServletPath();
        String info = request.getPathInfo();
        String query = request.getQueryString();

        //ricostruiamo la url cambiando il protocollo e la porta COME SPECIFICATO NELLA CONFIGURAZIONE DI TOMCAT
        String newUrl = "https://" + server + ":8080" +  context + path + (info != null ? info : "") + (query != null ? "?" + query : "");
        try {
            //ridirigiamo il client
            response.sendRedirect(newUrl);
        } catch (IOException ex) {
            try {
                //in caso di problemi tentiamo prima di inviare un errore HTTP standard
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot redirect to HTTPS, blocking request");
            } catch (IOException ex1) {
                //altrimenti generiamo un'eccezione
                throw new ServletException("Cannot redirect to https!");
            }
        }
    }
}
