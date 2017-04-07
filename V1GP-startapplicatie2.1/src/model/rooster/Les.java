package model.rooster;

import model.klas.Klas;
public class Les {
	private String datum;
	private String begintijd;
	private String eindtijd;
	private String vak;
	private String docent;
	private String lokaal;
	private String klas;
	private Klas Klas1;
	
	public Les(String datum, String begintijd, String eindtijd, String vak, String docent, String lokaal, String klas) {
		this.datum = datum;
		this.begintijd = begintijd;
		this.eindtijd = eindtijd;
		this.vak = vak;
		this.docent = docent;
		this.lokaal = lokaal;
		this.klas = klas;
	}
	
	public String getDatum() {
		return datum;
	}
	
	public String getVak() {
		return vak;
	}
	
	public String getBegintijd() {
		return begintijd;
	}
	public String getEindtijd() {
		return eindtijd;
	}
	public String getDocent() {
		return docent;
	}
	public String getLokaal() {
		return lokaal;
	}
	public String getKlas() {
		return klas;
	}
}
