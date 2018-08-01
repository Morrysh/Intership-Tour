package data.model;

import data.model.enumeration.VotoAzienda;

public class ParereAzienda {
	
	// DB fields
	public static final String STUDENTE = "studente";
	public static final String AZIENDA = "azienda";
	public static final String PARERE = "parere";
	public static final String VOTO = "voto";
	
	private String studente;
	private String azienda;
	private String parere;
	private VotoAzienda voto;
	
	public ParereAzienda() {
		this.studente = "";
		this.azienda = "";
		this.parere = "";
		this.voto = null;
	}
	
	public ParereAzienda(String studente, String azienda, String parere, VotoAzienda voto) {
		this.studente = studente;
		this.azienda = azienda;
		this.parere = parere;
		this.voto = voto;
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
	
	public VotoAzienda getVoto() {
		return voto;
	}

	public void setvoto(VotoAzienda voto) {
		this.voto = voto;
	}

}
