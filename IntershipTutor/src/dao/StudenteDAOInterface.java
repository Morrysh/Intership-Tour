package dao;

import java.sql.SQLException;

import data.model.Studente;

public interface StudenteDAOInterface {
	
	int insert(Studente studente) throws SQLException;
	
	int update(Studente studente) throws SQLException;
	
	int delete(Studente studente) throws SQLException;

	Studente getStudenteByCF(String codiceFiscale) throws SQLException;
	
}
