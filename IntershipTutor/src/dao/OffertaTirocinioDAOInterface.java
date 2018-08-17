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
	
	//int delete(OffertaTirocinio offertaTirocinio) throws DataLayerException;
	int delete(int idTirocinio) throws DataLayerException;
	
	//int setVisibilita(OffertaTirocinio offertaTirocinio, boolean visibilita) throws DataLayerException;
	int setVisibilita(int idOffertaTirocinio, boolean visibilita) throws DataLayerException;
	
	int getCountAccordingToVisibilita(boolean visbile) throws DataLayerException;
	
	int getCountAccordingToRicerca(Map<CampoRicercaTirocinio, String> campoRicerca) throws DataLayerException;
	
	int getCountAccordingToAzienda(Azienda azienda) throws DataLayerException;
	
	int getCountAccordingToAziendaAndVisibilita(Azienda azienda, boolean visibile) throws DataLayerException;
	
	OffertaTirocinio getOffertaByID(int idTirocinio) throws DataLayerException;
	
	OffertaTirocinio getBestOfferta() throws DataLayerException;
	
	List<OffertaTirocinio> allOfferte() throws DataLayerException;
	
	List<OffertaTirocinio> allOfferteInRangeAccordingToVisibilita(boolean visibile, int pagina) throws DataLayerException;
	
	List<OffertaTirocinio> allOfferteInRangePerCampo(Map<CampoRicercaTirocinio, String> campoRicerca, int paginaCorrente) throws DataLayerException;
	
	List<OffertaTirocinio> allOfferteInRangeAccordingToAzienda(Azienda azienda, int paginaCorrente) throws DataLayerException;
	
	List<OffertaTirocinio> allOfferteInRangeAccordingToAziendaAndVisibilita(Azienda azienda, boolean visibile, int paginaCorrente) throws DataLayerException;
}
