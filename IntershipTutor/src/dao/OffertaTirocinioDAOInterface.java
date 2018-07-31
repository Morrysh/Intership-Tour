package dao;

import java.util.List;
import java.util.Map;

import model.Azienda;
import model.OffertaTirocinio;
import model.enumeration.CampoRicercaTirocinio;

public interface OffertaTirocinioDAOInterface {
	
	int insert(OffertaTirocinio offertaTirocinio);
	
	int update(OffertaTirocinio offertaTirocinio);
	
	int delete(OffertaTirocinio offertaTirocinio);
	
	int setVisibilita(OffertaTirocinio offertaTirocinio, boolean visibilita);
	
	OffertaTirocinio getOffertaByID(int id);
	
	Azienda getAziendaByIDTirocinio(int id);
	
	List<OffertaTirocinio> allOfferteTirocinio();
	
	List<OffertaTirocinio> allOfferteTirocinioAccordingToVisibilita(boolean visibile);
	
	List<OffertaTirocinio> filtraPerCampo(Map<CampoRicercaTirocinio, String> campoRicerca);
	
	List<OffertaTirocinio> offerteTirocinioByIDAzienda(String utente);
}
