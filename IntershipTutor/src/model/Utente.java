package model;

import model.enumeration.TipoUtente;

public class Utente {
	
	// DB fields
	public static final String CODICE_FISCALE = "codice_fiscale";
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";
	public static final String TELEFONO = "telefono";
	public static final String TIPO_UTENTE = "tipo";
	
	private String codiceFiscale;
	private String email;
	private String password;
	private String telefono; 
	private TipoUtente tipoUtente;
	
	public Utente() {
		this.codiceFiscale = "";
		this.email = "";
		this.password = "";
		this.telefono = "";
		this.tipoUtente = null;
	}
	
	public Utente(String codiceFiscale, String email, String password, String telefono, TipoUtente tipoUtente) {
		this.codiceFiscale = codiceFiscale;
		this.email = email;
		this.password = password;
		this.telefono = telefono;
		this.tipoUtente = tipoUtente;
	}
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public TipoUtente getTipoUtente() {
		return tipoUtente;
	}
	
	public void setTipoUtente(TipoUtente tipoUtente) {
		this.tipoUtente = tipoUtente;
	}

}
