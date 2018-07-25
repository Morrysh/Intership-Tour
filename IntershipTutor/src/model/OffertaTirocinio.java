package model;

public class OffertaTirocinio {
	
	// DB fields
	public static final String ID_TIROCINIO = "id_tirocinio";
	public static final String AZIENDA = "azienda";
	public static final String LUOGO = "luogo";
	public static final String ORARI = "orari";
	public static final String DURATA = "durata";
	public static final String OBIETTIVI = "obiettivi";
	public static final String MODALITA = "modalita";
	public static final String RIMBORSO = "rimborso";
	public static final String VISIBILITA = "visibilita";
	public static final String APPROVAZIONE = "approvazione"; 

	private int idTirocinio;
	private String azienda;
	private String luogo;
	private String orari;
	private String durata;
	private String obiettivi;
	private String modalita;
	private String rimborso;
	private boolean visibilita;
	private boolean approvazione;
	
	public OffertaTirocinio() {
		this.idTirocinio = 0;
		this.azienda = "";
		this.luogo = "";
		this.orari = null;
		this.durata = "";
		this.obiettivi = "";
		this.modalita = "";
		this.rimborso = null;
		this.visibilita = false;
		this.approvazione = false;
	}
	
	public OffertaTirocinio(int idTirocinio, String azienda, String luogo, String orari, String durata,
			String obiettivi, String modalita, String rimborso, boolean visibilita, boolean approvazione) {
		this.idTirocinio = idTirocinio;
		this.azienda = azienda;
		this.luogo = luogo;
		this.orari = orari;
		this.durata = durata;
		this.obiettivi = obiettivi;
		this.modalita = modalita;
		this.rimborso = rimborso;
		this.visibilita = visibilita;
		this.approvazione = approvazione;
	}
	
	public int getIdTirocinio() {
		return idTirocinio;
	}
	public void setIdTirocinio(int idTirocinio) {
		this.idTirocinio = idTirocinio;
	}
	public String getAzienda() {
		return azienda;
	}
	public void setAzienda(String azienda) {
		this.azienda = azienda;
	}
	public String getLuogo() {
		return luogo;
	}
	public void setLuogo(String luogo) {
		this.luogo = luogo;
	}
	public String getOrari() {
		return orari;
	}
	public void setOrari(String orari) {
		this.orari = orari;
	}
	public String getDurata() {
		return durata;
	}
	public void setDurata(String durata) {
		this.durata = durata;
	}
	public String getObiettivi() {
		return obiettivi;
	}
	public void setObiettivi(String obiettivi) {
		this.obiettivi = obiettivi;
	}
	public String getModalita() {
		return modalita;
	}
	public void setModalita(String modalita) {
		this.modalita = modalita;
	}
	public String getRimborso() {
		return rimborso;
	}
	public void setRimborso(String rimborso) {
		this.rimborso = rimborso;
	}
	public boolean isVisibilita() {
		return visibilita;
	}
	public void setVisibilita(boolean visibilita) {
		this.visibilita = visibilita;
	}
	public boolean isApprovazione() {
		return approvazione;
	}
	public void setApprovazione(boolean approvazione) {
		this.approvazione = approvazione;
	}
	
}
