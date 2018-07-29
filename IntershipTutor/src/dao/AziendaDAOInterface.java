package dao;

import java.util.List;

import model.Azienda;
import model.Utente;

public interface AziendaDAOInterface {
	
	int insert(Azienda azienda);
	
	int update(Azienda azienda);
	
	boolean setConvenzione(Azienda azienda, boolean convezione);
	
	Azienda getAziendaByCF(String codiceFiscale);

	List<Azienda> allAziendeAccordingToConvention(boolean convenzione);
	
	List<Azienda> allAziende();
	
	Azienda getAziendaByUtente(Utente utente);
	
}
