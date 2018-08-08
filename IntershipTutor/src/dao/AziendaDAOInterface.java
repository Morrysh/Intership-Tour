package dao;

import java.util.List;

import data.model.Azienda;
import data.model.Utente;
import framework.data.DataLayerException;

public interface AziendaDAOInterface {
	
	int insert(Azienda azienda) throws DataLayerException;
	
	int update(Azienda azienda) throws DataLayerException;
	
	boolean setConvenzione(Azienda azienda, boolean convezione) throws DataLayerException;
	
	Azienda getAziendaByCF(String codiceFiscale) throws DataLayerException;
	
	Azienda getAziendaByIDTirocinio(int id) throws DataLayerException;

	List<Azienda> allAziendeAccordingToConvention(boolean convenzione) throws DataLayerException;
	
	List<Azienda> allAziende() throws DataLayerException;
	
	Azienda getAziendaByUtente(Utente utente) throws DataLayerException;
	
}
