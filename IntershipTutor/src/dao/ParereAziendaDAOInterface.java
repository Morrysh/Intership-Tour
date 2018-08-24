package dao;


import java.util.Map;

import data.model.Azienda;
import data.model.ParereAzienda;
import data.model.Studente;
import framework.data.DataLayerException;

public interface ParereAziendaDAOInterface {
	
	int insert(ParereAzienda parereAzienda) throws DataLayerException;
	
	int update(ParereAzienda parereAzienda) throws DataLayerException;
	
	int delete(ParereAzienda parereAzienda) throws DataLayerException;

	Map<String, String> getPareriAzienda(Azienda azienda) throws DataLayerException;
	
	int getMediaVoto(Azienda azienda) throws DataLayerException;
	
	ParereAzienda getParereStudente(Studente studente, Azienda azienda) throws DataLayerException;
}
