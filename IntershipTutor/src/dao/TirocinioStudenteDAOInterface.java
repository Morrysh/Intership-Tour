package dao;

import java.util.List;

import model.OffertaTirocinio;
import model.Studente;
import model.TirocinioStudente;

public interface TirocinioStudenteDAOInterface {
	
	int insert(TirocinioStudente tirocinioStudente);
	
	int update(TirocinioStudente tirocinioStudente);
	
	int delete(TirocinioStudente tirocinioStudente);
	
	List<Studente> getStudentiPerTirocinio(OffertaTirocinio offertaTirocinio);

}
