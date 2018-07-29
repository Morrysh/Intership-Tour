package dao;

import model.Utente;

public interface UtenteDAOInterface {

	int insert(Utente utente);
	
	int update(Utente utente);
	
	int delete(Utente utente);
	
	boolean checkEmailDisponibile(String email);
	
	boolean checkUsernameDisponibile(String username);
	
	boolean checkTelefonoDisponibile(String telefono);
	
	Utente getUtenteByUsernameAndPassword(String username, String password);
	
	Utente getUtenteByCF(String codiceFiscale);
	
	Object getLogged(String username, String password);
	
}
