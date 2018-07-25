package model;

public class Azienda {
	
	// DB fields
	public static final String UTENTE = "utente";
	public static final String NOME = "nome";
	public static final String REGIONE = "regione";
	public static final String INDIRIZZO_SEDE_LEGALE = "indirizzo_sede_legale";
	public static final String FORO_COMPETENTE = "foro_competente";
	public static final String CONVENZIONATA = "convenzionata";
	public static final String NOME_RAPPRESENTANTE = "nome_rappresentante";
	public static final String COGNOME_RAPPRESENTANTE = "cognome_rappresentante";
	public static final String NOME_RESPONSABILE = "nome_responsabile";
	public static final String COGNOME_RESPONSABILE = "cognome_responsabile";
	
	private String utente;
	private String nome;
	private String cognome;
	private String indirizzoSedeLegale;
	private String foroCompetente;
	private String nomeRappresentante;
	private String cognomeRappresentante;
	private String nomeResponsabile;
	private String cognomeResponsabile;
	private boolean convenzionata;
	
	public Azienda() {
		this.utente = "";
		this.nome = "";
		this.cognome = "";
		this.indirizzoSedeLegale = "";
		this.foroCompetente = "";
		this.nomeRappresentante = "";
		this.cognomeRappresentante = "";
		this.nomeResponsabile = "";
		this.cognomeResponsabile = "";
		this.convenzionata = false;
	}
	
	public Azienda(String utente, String nome, String cognome, String indirizzoSedeLegale, String foroCompetente,
			String nomeRappresentante, String cognomeRappresentante, String nomeResponsabile,
			String cognomeResponsabile, boolean convenzionata) {
		this.utente = utente;
		this.nome = nome;
		this.cognome = cognome;
		this.indirizzoSedeLegale = indirizzoSedeLegale;
		this.foroCompetente = foroCompetente;
		this.nomeRappresentante = nomeRappresentante;
		this.cognomeRappresentante = cognomeRappresentante;
		this.nomeResponsabile = nomeResponsabile;
		this.cognomeResponsabile = cognomeResponsabile;
		this.convenzionata = convenzionata;
	}
	
	public String getUtente() {
		return utente;
	}
	public void setUtente(String utente) {
		this.utente = utente;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getIndirizzoSedeLegale() {
		return indirizzoSedeLegale;
	}
	public void setIndirizzoSedeLegale(String indirizzoSedeLegale) {
		this.indirizzoSedeLegale = indirizzoSedeLegale;
	}
	public String getForoCompetente() {
		return foroCompetente;
	}
	public void setForoCompetente(String foroCompetente) {
		this.foroCompetente = foroCompetente;
	}
	public String getNomeRappresentante() {
		return nomeRappresentante;
	}
	public void setNomeRappresentante(String nomeRappresentante) {
		this.nomeRappresentante = nomeRappresentante;
	}
	public String getCognomeRappresentante() {
		return cognomeRappresentante;
	}
	public void setCognomeRappresentante(String cognomeRappresentante) {
		this.cognomeRappresentante = cognomeRappresentante;
	}
	public String getNomeResponsabile() {
		return nomeResponsabile;
	}
	public void setNomeResponsabile(String nomeResponsabile) {
		this.nomeResponsabile = nomeResponsabile;
	}
	public String getCognomeResponsabile() {
		return cognomeResponsabile;
	}
	public void setCognomeResponsabile(String cognomeResponsabile) {
		this.cognomeResponsabile = cognomeResponsabile;
	}
	public boolean isConvenzionata() {
		return convenzionata;
	}
	public void setConvenzionata(boolean convenzionata) {
		this.convenzionata = convenzionata;
	}	

}
