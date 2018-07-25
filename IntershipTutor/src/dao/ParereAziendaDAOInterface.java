package dao;

import java.util.List;

import model.Azienda;
import model.ParereAzienda;

public interface ParereAziendaDAOInterface {
	
	int insert(ParereAzienda parereAzienda);
	
	int update(ParereAzienda parereAzienda);
	
	int delete(ParereAzienda parereAzienda);

	List<String> getPareriAzienda(Azienda azienda);
	
}
