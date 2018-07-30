package servlet;

import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import model.Azienda;
import model.OffertaTirocinio;
import model.enumeration.CampoRicercaTirocinio;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.AziendaDAO;
import dao.impl.OffertaTirocinioDAO;

public class HomePage extends HttpServlet {

	private static final long serialVersionUID = 1L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=ISO-8859-1");

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setOutputEncoding("ISO-8859-1");
        cfg.setDefaultEncoding("ISO-8859-1");
        cfg.setServletContextForTemplateLoading(getServletContext(), "templates");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        DefaultObjectWrapperBuilder owb = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_28);
        owb.setForceLegacyNonListCollections(false);
        owb.setDefaultDateType(TemplateDateModel.DATETIME);
        cfg.setObjectWrapper(owb.build());
        cfg.setOutputFormat(HTMLOutputFormat.INSTANCE);
        
        Map<String, Object> templateData = new HashMap<>();

        List<Azienda> aziende = new AziendaDAO().allAziendeAccordingToConvention(true);
        List<OffertaTirocinio> offerte = new OffertaTirocinioDAO().allOfferteTirocinioAccordingToVisibilita(true);
        CampoRicercaTirocinio[] campiRicerca = CampoRicercaTirocinio.values();
        
        templateData.put("offerte", offerte);
        templateData.put("aziende", aziende);
        templateData.put("campiRicerca", campiRicerca);
        templateData.put("w3_css", "w3");
        templateData.put("page_css", "homepage");
        
        // Impostiamo il nome del template che verrà incluso tramite la direttiva include
        templateData.put("template_to_include", "homepage.ftl.html");
        
        Template t = cfg.getTemplate("container.ftl.html");
        try {
            t.process(templateData, response.getWriter());
        } catch (TemplateException ex) {
            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}