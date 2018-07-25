package dao;

import java.util.List;

import model.OffertaTirocinio;

public interface OffertaTirocinioDAOInterface {
	
	int insert(OffertaTirocinio offertaTirocinio);
	
	int update(OffertaTirocinio offertaTirocinio);
	
	int delete(OffertaTirocinio offertaTirocinio);
	
	int setVisibilita(OffertaTirocinio offertaTirocinio, boolean visibilita);
	
	List<OffertaTirocinio> allOfferteTirocinio();
	
}
