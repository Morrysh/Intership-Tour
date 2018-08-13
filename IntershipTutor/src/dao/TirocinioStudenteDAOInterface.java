package dao;

import java.util.List;

import data.model.OffertaTirocinio;
import data.model.Studente;
import data.model.TirocinioStudente;
import data.model.enumeration.StatoRichiestaTirocinio;
import framework.data.DataLayerException;

public interface TirocinioStudenteDAOInterface {
	
	int insert(TirocinioStudente tirocinioStudente) throws DataLayerException;
	
	int update(TirocinioStudente tirocinioStudente) throws DataLayerException;
	
	int delete(Studente studente) throws DataLayerException;
	
	int updateStato(String codiceFiscale, StatoRichiestaTirocinio statoRichiestaTirocinio) throws DataLayerException;
	
	TirocinioStudente getTirocinioStudenteByStudente(Studente studente) throws DataLayerException;
	
	// Per gli studenti
	OffertaTirocinio getOffertaTirocinioByStudente(Studente studente) throws DataLayerException;
	
	// Per le aziende
	List<Studente> getStudentiByOffertaTirocinio(OffertaTirocinio offertaTirocinio) throws DataLayerException;

}
