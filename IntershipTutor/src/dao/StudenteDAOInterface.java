package dao;

import model.Studente;

public interface StudenteDAOInterface {
	
	int insert(Studente studente);
	
	int update(Studente studente);
	
	int delete(Studente studente);

	Studente getStudenteByCF(String codiceFiscale);
	
}
