package model;

public class ParereAzienda {
	
	// DB fields
	public static final String STUDENTE = "studente";
	public static final String AZIENDA = "azienda";
	public static final String PARERE = "parere";
	
	private String studente;
	private String azienda;
	private String parere;
	
	public ParereAzienda() {
		this.studente = "";
		this.azienda = "";
		this.parere = "";
	}
	
	public ParereAzienda(String studente, String azienda, String parere) {
		this.studente = studente;
		this.azienda = azienda;
		this.parere = parere;
	}

	public String getStudente() {
		return studente;
	}

	public void setStudente(String studente) {
		this.studente = studente;
	}

	public String getAzienda() {
		return azienda;
	}

	public void setAzienda(String azienda) {
		this.azienda = azienda;
	}

	public String getParere() {
		return parere;
	}

	public void setParere(String parere) {
		this.parere = parere;
	}

}
