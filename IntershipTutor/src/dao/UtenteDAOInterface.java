package dao;

import model.Utente;

public interface UtenteDAOInterface {

	int insert(Utente utente);
	
	int update(Utente utente);
	
	int delete(Utente utente);
	
	Utente getUtenteByUsernameAndPassword(String username, String password);
	
	Utente getUtenteByCF(String codiceFiscale);
	
}
