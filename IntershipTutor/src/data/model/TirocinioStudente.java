package data.model;

import data.model.enumeration.StatoRichiestaTirocinio;

public class TirocinioStudente {

	// DB fields
	public static final String STUDENTE = "studente";
	public static final String TIROCINIO = "tirocinio";
	public static final String CFU = "cfu";
	public static final String TUTORE_UNIVERSITARIO = "tutore_universitario";
	public static final String TELEFONO_TUTORE = "telefono_tutore";
	public static final String EMAIL_TUTORE = "email_tutore";
	public static final String DESCRIZIONE_DETTAGLIATA = "descrizione_dettagliata";
	public static final String ORE_SVOLTE = "ore_svolte";	
	public static final String GIUDIZIO_FINALE = "giudizio_finale";
	public static final String PARERE = "parere";
	public static final String STATO = "stato";
	
	private String studente;
	private int tirocinio;
	private int cfu;
	private String tutoreUniversitario;
	private String telefonoTutoreUniversitario;
	private String emailTutoreUniversitario;
	private String descrizioneDettagliata;
	private int oreSvolte;
	private String giudizioFinale;
	private String parere;	
	private StatoRichiestaTirocinio stato;
	
	public TirocinioStudente() {
		this.studente = "";
		this.tirocinio = 0;
		this.cfu = 0;
		this.tutoreUniversitario = "";
		this.telefonoTutoreUniversitario = "";
		this.emailTutoreUniversitario = "";
		this.descrizioneDettagliata = "";
		this.oreSvolte = 0;
		this.giudizioFinale = "";
		this.parere = "";
		this.stato = StatoRichiestaTirocinio.attesa;
	}
	
	public TirocinioStudente(String studente, int tirocinio, int cfu, String tutoreUniversitario,
			String telefonoTutoreUniversitario, String emailTutoreUniversitario, String descrizioneDettagliata, 
			int oreSvolte, String giudizioFinale, String parere, StatoRichiestaTirocinio stato) {
		this.studente = studente;
		this.tirocinio = tirocinio;
		this.cfu = cfu;
		this.tutoreUniversitario = tutoreUniversitario;
		this.telefonoTutoreUniversitario = telefonoTutoreUniversitario;
		this.emailTutoreUniversitario = emailTutoreUniversitario;
		this.descrizioneDettagliata = descrizioneDettagliata;
		this.oreSvolte = oreSvolte;
		this.giudizioFinale = giudizioFinale;
		this.parere = parere;
		this.stato = stato;
	}

	public String getStudente() {
		return studente;
	}

	public void setStudente(String studente) {
		this.studente = studente;
	}

	public int getTirocinio() {
		return tirocinio;
	}

	public void setTirocinio(int tirocinio) {
		this.tirocinio = tirocinio;
	}

	public int getCfu() {
		return cfu;
	}

	public void setCfu(int cfu) {
		this.cfu = cfu;
	}

	public String getTutoreUniversitario() {
		return tutoreUniversitario;
	}

	public void setTutoreUniversitario(String tutoreUniversitario) {
		this.tutoreUniversitario = tutoreUniversitario;
	}

	public String getTelefonoTutoreUniversitario() {
		return telefonoTutoreUniversitario;
	}

	public void setTelefonoTutoreUniversitario(String telefonoTutoreUniversitario) {
		this.telefonoTutoreUniversitario = telefonoTutoreUniversitario;
	}
	
	public String getEmailTutoreUniversitario() {
		return emailTutoreUniversitario;
	}

	public void setEmailTutoreUniversitario(String emailTutoreUniversitario) {
		this.emailTutoreUniversitario = emailTutoreUniversitario;
	}

	public String getDescrizioneDettagliata() {
		return descrizioneDettagliata;
	}

	public void setDescrizioneDettagliata(String descrizioneDettagliata) {
		this.descrizioneDettagliata = descrizioneDettagliata;
	}

	public int getOreSvolte() {
		return oreSvolte;
	}

	public void setOreSvolte(int oreSvolte) {
		this.oreSvolte = oreSvolte;
	}

	public String getGiudizioFinale() {
		return giudizioFinale;
	}

	public void setGiudizioFinale(String giudizioFinale) {
		this.giudizioFinale = giudizioFinale;
	}

	public String getParere() {
		return parere;
	}

	public void setParere(String parere) {
		this.parere = parere;
	}

	public StatoRichiestaTirocinio getStato() {
		return stato;
	}

	public void setStato(StatoRichiestaTirocinio stato) {
		this.stato = stato;
	}
	
}
