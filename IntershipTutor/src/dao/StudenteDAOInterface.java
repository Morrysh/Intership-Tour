package dao;

import data.model.Studente;
import framework.data.DataLayerException;

public interface StudenteDAOInterface {
	
	int insert(Studente studente) throws DataLayerException;
	
	int update(Studente studente) throws DataLayerException;
	
	int delete(Studente studente) throws DataLayerException;

	Studente getStudenteByCF(String codiceFiscale) throws DataLayerException;
	
}
