package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import data.model.Azienda;
import data.model.OffertaTirocinio;
import data.model.enumeration.CampoRicercaTirocinio;

public interface OffertaTirocinioDAOInterface {
	
	int insert(OffertaTirocinio offertaTirocinio) throws SQLException;
	
	int update(OffertaTirocinio offertaTirocinio) throws SQLException;
	
	int delete(OffertaTirocinio offertaTirocinio) throws SQLException;
	
	int setVisibilita(OffertaTirocinio offertaTirocinio, boolean visibilita) throws SQLException;
	
	OffertaTirocinio getOffertaByID(int id) throws SQLException;
	
	Azienda getAziendaByIDTirocinio(int id) throws SQLException;
	
	List<OffertaTirocinio> allOfferteTirocinio() throws SQLException;
	
	List<OffertaTirocinio> allOfferteTirocinioAccordingToVisibilita(boolean visibile) throws SQLException;
	
	List<OffertaTirocinio> filtraPerCampo(Map<CampoRicercaTirocinio, String> campoRicerca) throws SQLException;
	
	List<OffertaTirocinio> offerteTirocinioByIDAzienda(String utente) throws SQLException;
}
