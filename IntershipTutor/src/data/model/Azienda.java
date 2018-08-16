package data.model;

import data.model.enumeration.TipoUtente;

public class Azienda extends Utente {
	
	// DB fields
	public static final String UTENTE = "utente";
	public static final String NOME = "nome";
	public static final String REGIONE = "regione";
	public static final String INDIRIZZO_SEDE_LEGALE = "indirizzo_sede_legale";
	public static final String FORO_COMPETENTE = "foro_competente";
	public static final String NOME_RAPPRESENTANTE = "nome_rappresentante";
	public static final String COGNOME_RAPPRESENTANTE = "cognome_rappresentante";
	public static final String NOME_RESPONSABILE = "nome_responsabile";
	public static final String COGNOME_RESPONSABILE = "cognome_responsabile";
	public static final String CONVENZIONATA = "convenzionata";
	public static final String CONVENZIONE_DOC = "convenzione_doc";
	
	private String utente;
	private String nome;
	private String regione;
	private String indirizzoSedeLegale;
	private String foroCompetente;
	private String nomeRappresentante;
	private String cognomeRappresentante;
	private String nomeResponsabile;
	private String cognomeResponsabile;
	private boolean convenzionata;
	
	public Azienda() {
		super();
		this.utente = "";
		this.nome = "";
		this.regione = "";
		this.indirizzoSedeLegale = "";
		this.foroCompetente = "";
		this.nomeRappresentante = "";
		this.cognomeRappresentante = "";
		this.nomeResponsabile = "";
		this.cognomeResponsabile = "";
		this.convenzionata = false;
	}
	
	public Azienda(String codiceFiscale, String email, String username, String password, 
			String telefono, TipoUtente tipoUtente, String utente, String nome, String regione, 
			String indirizzoSedeLegale, String foroCompetente, String nomeRappresentante, 
			String cognomeRappresentante, String nomeResponsabile, String cognomeResponsabile,
			boolean convenzionata) {
		super(codiceFiscale, username, email, password, telefono, tipoUtente);
		this.utente = utente;
		this.nome = nome;
		this.regione = regione;
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
	public String getRegione() {
		return regione;
	}
	public void setRegione(String regione) {
		this.regione = regione;
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
