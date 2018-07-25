package dao;

import java.util.List;

import model.Azienda;

public interface AziendaDAOInterface {
	
	int insert(Azienda azienda);
	
	int update(Azienda azienda);
	
	boolean isConvenzionata(Azienda azienda);
	
	Azienda getAziendaByCodiceFiscale(String codiceFiscale);

	List<Azienda> allAziende();
	
	List<Azienda> allAziendeNonConvenzionate();
	
	
}
