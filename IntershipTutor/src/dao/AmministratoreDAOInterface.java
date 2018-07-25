package dao;

import model.Amministratore;
import model.Utente;

public interface AmministratoreDAOInterface {
	
	Amministratore getAmministratoreByUtente(Utente utente);

}
