package dao.impl;

import java.util.List;

import dao.AziendaDAOInterface;
import model.Azienda;

public class AziendaDAO implements AziendaDAOInterface {
	
	@Override
	public List<Azienda> allAziende() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Azienda getAziendaByCodiceFiscale(String codiceFiscale) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean isConvenzionata(Azienda azienda) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public int insert(Azienda azienda) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Azienda azienda) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Azienda> allAziendeNonConvenzionate() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
