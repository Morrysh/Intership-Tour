package dao;

import java.util.List;

import data.model.OffertaTirocinio;
import data.model.Studente;
import data.model.TirocinioStudente;
import framework.data.DataLayerException;

public interface TirocinioStudenteDAOInterface {
	
	int insert(TirocinioStudente tirocinioStudente) throws DataLayerException;
	
	int update(TirocinioStudente tirocinioStudente) throws DataLayerException;
	
	int delete(TirocinioStudente tirocinioStudente) throws DataLayerException;
	
	List<Studente> getStudentiPerTirocinio(OffertaTirocinio offertaTirocinio) throws DataLayerException;

}
