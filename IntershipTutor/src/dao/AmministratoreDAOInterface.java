package dao;

import data.model.Amministratore;
import data.model.Utente;
import data.model.Azienda;
import framework.data.DataLayerException;

public interface AmministratoreDAOInterface {
	
	Amministratore getAmministratoreByUtente(Utente utente) throws DataLayerException;
	
	Amministratore getAmministratoreByCF(String codiceFiscale) throws DataLayerException;

}
