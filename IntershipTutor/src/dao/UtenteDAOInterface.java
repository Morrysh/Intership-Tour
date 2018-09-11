package dao;

import data.model.Utente;
import data.model.enumeration.TipoUtente;
import framework.data.DataLayerException;

public interface UtenteDAOInterface {

	int insert(Utente utente) throws DataLayerException;
	
	int update(Utente utente) throws DataLayerException;
	
	int delete(Utente utente) throws DataLayerException;
	
	int delete(String codiceFiscale) throws DataLayerException;
	
	int getCountAccordingToUserType(TipoUtente tipoUtente) throws DataLayerException;
	
	boolean checkCodiceFiscaleDisponibile(String email) throws DataLayerException;
	
	boolean checkEmailDisponibile(String email) throws DataLayerException;
	
	boolean checkUsernameDisponibile(String username) throws DataLayerException;
	
	boolean checkTelefonoDisponibile(String telefono) throws DataLayerException;
	
	Utente getUtenteByEmail(String email) throws DataLayerException;
	
	Utente getUtenteByCF(String codiceFiscale) throws DataLayerException;
	
	Utente getLogged(String username, String password) throws DataLayerException;
	
}
