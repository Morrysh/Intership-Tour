package model;

import java.sql.Date;
import java.sql.Time;

public class OffertaTirocinio {

	// DB fields
	public static final String ID_TIROCINIO = "id_tirocinio";
	public static final String AZIENDA = "azienda";
	public static final String TITOLO = "titolo";
	public static final String LUOGO = "luogo";
	public static final String OBIETTIVI = "obiettivi";
	public static final String MODALITA = "modalita";
	public static final String RIMBORSO = "rimborso";
	public static final String DATA_INIZIO = "data_inizio";
	public static final String DATA_FINE = "data_fine";
	public static final String ORA_INIZIO = "ora_inizio";
	public static final String ORA_FINE = "ora_fine";
	public static final String NUMERO_ORE = "numero_ore";
	public static final String VISIBILE = "visibile";

	private int idTirocinio;
	private String azienda;
	private String titolo;
	private String luogo;
	private String obiettivi;
	private String modalita;
	private String rimborso;
	private Date dataInizio;
	private Date dataFine;
	private Time oraInizio;
	private Time oraFine;
	private int numeroOre;
	private boolean visibile;
	
	public OffertaTirocinio() {
		this.idTirocinio = 0;
		this.azienda = "";
		this.titolo = "";
		this.luogo = "";
		this.obiettivi = "";
		this.modalita = "";
		this.rimborso = null;
		this.dataInizio = null;
		this.dataFine = null;
		this.oraInizio = null;
		this.oraFine = null;
		this.numeroOre = 0;
		this.visibile = false;
	}
	
	public OffertaTirocinio(int idTirocinio, String azienda, String titolo, String luogo, String obiettivi, String modalita,
			String rimborso, Date dataInizio, Date dataFine, Time oraInizio, Time oraFine, int numeroOre,
			boolean visibile) {
		this.idTirocinio = idTirocinio;
		this.azienda = azienda;
		this.titolo = titolo;
		this.luogo = luogo;
		this.obiettivi = obiettivi;
		this.modalita = modalita;
		this.rimborso = rimborso;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
		this.oraInizio = oraInizio;
		this.oraFine = oraFine;
		this.numeroOre = numeroOre;
		this.visibile = visibile;
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
	
	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getLuogo() {
		return luogo;
	}

	public void setLuogo(String luogo) {
		this.luogo = luogo;
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

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public Time getOraInizio() {
		return oraInizio;
	}

	public void setOraInizio(Time oraInizio) {
		this.oraInizio = oraInizio;
	}

	public Time getOraFine() {
		return oraFine;
	}

	public void setOraFine(Time oraFine) {
		this.oraFine = oraFine;
	}
	
	public int getNumeroOre() {
		return numeroOre;
	}

	public void setNumeroOre(int numeroOre) {
		this.numeroOre = numeroOre;
	}

	public boolean isVisibile() {
		return visibile;
	}

	public void setVisibile(boolean visibile) {
		this.visibile = visibile;
	}
	
}
