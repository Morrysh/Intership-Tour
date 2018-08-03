package dao;

import java.sql.SQLException;

import data.model.Utente;

public interface UtenteDAOInterface {

	int insert(Utente utente) throws SQLException;
	
	int update(Utente utente) throws SQLException;
	
	int delete(Utente utente) throws SQLException;
	
	boolean checkEmailDisponibile(String email) throws SQLException;
	
	boolean checkUsernameDisponibile(String username) throws SQLException;
	
	boolean checkTelefonoDisponibile(String telefono) throws SQLException;
	
	Utente getUtenteByUsernameAndPassword(String username, String password) throws SQLException;
	
	Utente getUtenteByCF(String codiceFiscale) throws SQLException;
	
	Utente getLogged(String username, String password) throws SQLException;
	
}
