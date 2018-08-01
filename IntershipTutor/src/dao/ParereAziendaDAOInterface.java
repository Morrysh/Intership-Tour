package dao;

import java.sql.SQLException;
import java.util.List;

import data.model.Azienda;
import data.model.ParereAzienda;

public interface ParereAziendaDAOInterface {
	
	int insert(ParereAzienda parereAzienda) throws SQLException;
	
	int update(ParereAzienda parereAzienda) throws SQLException;
	
	int delete(ParereAzienda parereAzienda) throws SQLException;

	List<String> getPareriAzienda(Azienda azienda) throws SQLException;
	
	int getMediaVoto(Azienda azienda) throws SQLException;
}
