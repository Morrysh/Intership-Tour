package model;

import java.sql.Date;

public class Studente {

	// DB fields
	public static final String UTENTE = "utente";
	public static final String NOME = "nome";
	public static final String COGNOME = "cognome";
	public static final String DATA_NASCITA = "data_nascita";
	public static final String LUOGO_NASCITA = "luogo_nascita";
	public static final String PROVINCIA_NASCITA = "provincia_nascita";
	public static final String RESIDENZA = "residenza";
	public static final String PROVINCIA_RESIDENZA = "provincia_residenza";
	public static final String TIPO_LAUREA = "tipo_laurea";
	public static final String CORSO_LAUREA = "corso_laurea";
	public static final String HANDICAP = "handicap";
	
	private String utente;
	private String nome;
	private String cognome;
	private Date dataNascita;
	private String luogoNascita;
	private String provinciaNascita;
	private String residenza;
	private String provinciaResidenza;
	private String tipoLaurea;
	private String corsoLaurea;
	private boolean handicap;
	
	public Studente() {
		this.utente = "";
		this.nome = "";
		this.cognome = "";
		this.dataNascita = null;
		this.luogoNascita = "";
		this.provinciaNascita = "";
		this.residenza = "";
		this.provinciaResidenza = "";
		this.tipoLaurea = "";
		this.corsoLaurea = "";
		this.handicap = false;
	}

	public Studente(String utente, String nome, String cognome, Date dataNascita, String luogoNascita,
			String provinciaNascita, String residenza, String provinciaResidenza, String tipoLaurea,
			String corsoLaurea, boolean handicap) {
		this.utente = utente;
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
		this.luogoNascita = luogoNascita;
		this.provinciaNascita = provinciaNascita;
		this.residenza = residenza;
		this.provinciaResidenza = provinciaResidenza;
		this.tipoLaurea = tipoLaurea;
		this.corsoLaurea = corsoLaurea;
		this.handicap = handicap;
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

	public Date getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public String getProvinciaNascita() {
		return provinciaNascita;
	}

	public void setProvinciaNascita(String provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
	}

	public String getResidenza() {
		return residenza;
	}

	public void setResidenza(String residenza) {
		this.residenza = residenza;
	}

	public String getProvinciaResidenza() {
		return provinciaResidenza;
	}

	public void setProvinciaResidenza(String provinciaResidenza) {
		this.provinciaResidenza = provinciaResidenza;
	}

	public String getTipoLaurea() {
		return tipoLaurea;
	}

	public void setTipoLaurea(String tipoLaurea) {
		this.tipoLaurea = tipoLaurea;
	}

	public String getCorsoLaurea() {
		return corsoLaurea;
	}

	public void setCorsoLaurea(String corsoLaurea) {
		this.corsoLaurea = corsoLaurea;
	}

	public boolean isHandicap() {
		return handicap;
	}

	public void setHandicap(boolean handicap) {
		this.handicap = handicap;
	}
	
	

}
