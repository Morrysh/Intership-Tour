package dao;

import java.sql.SQLException;

import data.model.Amministratore;
import data.model.Utente;

public interface AmministratoreDAOInterface {
	
	Amministratore getAmministratoreByUtente(Utente utente) throws SQLException;
	
	Amministratore getAmministratoreByCF(String codiceFiscale) throws SQLException;

}
