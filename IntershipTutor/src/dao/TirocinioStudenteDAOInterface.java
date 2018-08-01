package dao;

import java.sql.SQLException;
import java.util.List;

import data.model.OffertaTirocinio;
import data.model.Studente;
import data.model.TirocinioStudente;

public interface TirocinioStudenteDAOInterface {
	
	int insert(TirocinioStudente tirocinioStudente) throws SQLException;
	
	int update(TirocinioStudente tirocinioStudente) throws SQLException;
	
	int delete(TirocinioStudente tirocinioStudente) throws SQLException;
	
	List<Studente> getStudentiPerTirocinio(OffertaTirocinio offertaTirocinio) throws SQLException;

}
