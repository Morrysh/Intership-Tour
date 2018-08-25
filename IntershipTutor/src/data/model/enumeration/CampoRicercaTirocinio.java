package data.model.enumeration;

public enum CampoRicercaTirocinio {
	
	titolo("text", "Filtra per titolo"),
	azienda("text", "Filtra per nome dell'azienda"),
	luogo("text", "Filtra per luogo di effettuazione del tirocinio"),
	obiettivi("text", "Filtra in base agli obiettivi da raggiungere"),
	modalita("text", "Filtra in accordo alle modalità del tirocinio"),
	durata_minima("number", "Filtra per durata minima del tirocinio"),
	durata_massima("number", "Filtra per durata massima del tirocinio");

	private String input_type;
	private String placeholder;
	
	private CampoRicercaTirocinio(String input_type, String placeholder) {
		this.input_type = input_type;
		this.placeholder = placeholder;
	}
	
	public String getInputType() {
		return this.input_type;
	}
	
	public String getPlaceholder() {
		return this.placeholder;
	}
	
}
