package dao;

import model.Studente;
import model.Utente;

public interface StudenteDAOInterface {
	
	int insert(Studente studente);
	
	int update(Studente studente);
	
	int delete(Studente studente);

	Studente getStudenteByUtente(Utente utente);
	
}
