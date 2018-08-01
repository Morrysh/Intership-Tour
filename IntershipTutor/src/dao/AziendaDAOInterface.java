package dao;

import java.sql.SQLException;
import java.util.List;

import data.model.Azienda;
import data.model.Utente;

public interface AziendaDAOInterface {
	
	int insert(Azienda azienda) throws SQLException;
	
	int update(Azienda azienda) throws SQLException;
	
	boolean setConvenzione(Azienda azienda, boolean convezione) throws SQLException;
	
	Azienda getAziendaByCF(String codiceFiscale) throws SQLException;

	List<Azienda> allAziendeAccordingToConvention(boolean convenzione) throws SQLException;
	
	List<Azienda> allAziende() throws SQLException;
	
	Azienda getAziendaByUtente(Utente utente) throws SQLException;
	
}
