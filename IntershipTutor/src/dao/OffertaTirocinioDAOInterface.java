package dao;

import java.util.List;

import model.OffertaTirocinio;
import model.enumeration.CampoRicercaTirocinio;

public interface OffertaTirocinioDAOInterface {
	
	int insert(OffertaTirocinio offertaTirocinio);
	
	int update(OffertaTirocinio offertaTirocinio);
	
	int delete(OffertaTirocinio offertaTirocinio);
	
	int setVisibilita(OffertaTirocinio offertaTirocinio, boolean visibilita);
	
	List<OffertaTirocinio> allOfferteTirocinio();
	
	List<OffertaTirocinio> allOfferteTirocinioAccordingToVisibilita(boolean visibile);
	
	List<OffertaTirocinio> filtraPerCampo(CampoRicercaTirocinio campo, String ricerca);
	
}
