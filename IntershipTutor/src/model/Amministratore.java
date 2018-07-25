package model;

public class Amministratore {
	
	// DB fields
	public static final String UTENTE = "utente";
	public static final String NOME = "nome";
	public static final String COGNOME = "cognome";
	
	private String utente;
	private String nome;
	private String cognome;
	
	public Amministratore() {
		this.utente = "";
		this.nome = "";
		this.cognome = "";
	}
	
	public Amministratore(String utente, String nome, String cognome) {
		this.utente = utente;
		this.nome = nome;
		this.cognome = cognome;
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

}
