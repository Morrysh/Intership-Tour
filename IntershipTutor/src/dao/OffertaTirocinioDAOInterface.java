package dao;

import java.util.List;
import java.util.Map;

import data.model.Azienda;
import data.model.OffertaTirocinio;
import data.model.enumeration.CampoRicercaTirocinio;
import framework.data.DataLayerException;

public interface OffertaTirocinioDAOInterface {
	
	int insert(OffertaTirocinio offertaTirocinio) throws DataLayerException;
	
	int update(OffertaTirocinio offertaTirocinio) throws DataLayerException;
	
	int delete(OffertaTirocinio offertaTirocinio) throws DataLayerException;
	
	int setVisibilita(OffertaTirocinio offertaTirocinio, boolean visibilita) throws DataLayerException;
	
	int getCountAccordingToConvention(boolean visbile) throws DataLayerException;
	
	int getCountAccordingToRicerca(Map<CampoRicercaTirocinio, String> campoRicerca) throws DataLayerException;
	
	int getCountAccordingToAzienda(Azienda azienda) throws DataLayerException;
	
	OffertaTirocinio getOffertaByID(int id) throws DataLayerException;
	
	List<OffertaTirocinio> allOfferteInRange(int pagina) throws DataLayerException;
	
	List<OffertaTirocinio> allOfferteTirocinio() throws DataLayerException;
	
	List<OffertaTirocinio> allOfferteTirocinioAccordingToVisibilita(boolean visibile) throws DataLayerException;
	
	List<OffertaTirocinio> filtraPerCampo(Map<CampoRicercaTirocinio, String> campoRicerca, int paginaCorrente) throws DataLayerException;
	
	List<OffertaTirocinio> offerteTirocinioByAzienda(Azienda azienda,int paginaCorrente) throws DataLayerException;
}
