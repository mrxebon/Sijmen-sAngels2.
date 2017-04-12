package model.rooster;
//een class met als doel om de lessen vast te leggen. de list deLessen in prIs bevat het hele rooster.
import model.klas.Klas;
public class Les {
	private String datum;
	private String begintijd;
	private String eindtijd;
	private String vak;
	//de String docent wordt hier uit het csv gehaald, niet uit de class docent.
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
	//returnt datum, een van de unieke identifiers van de les
	public String getDatum() {
		return datum;
	}
	//returns vakdatum, ook een unieke identifier
	public String getVak() {
		return vak;
	}
	//begintijd en eindtijd werden uiteindelijk niet echt gebruikt, maar we achtte ze aan het begin belangrijk
	public String getBegintijd() {
		return begintijd;
	}
	public String getEindtijd() {
		return eindtijd;
	
	}
	//returnt de docent, die uiteindelijk niet echt is gebruikt
	public String getDocent() {
		return docent;
	}
	//lokaalnummer, absentie per lokaal leek ons overbodig, maar we kunnen het nog steeds implementeren
	public String getLokaal() {
		return lokaal;
	}
	//returned de naam van de klas
	public String getKlas() {
		return klas;
	}
	//returned het object klas, dat wordt gebruikt in absentie
	public Klas getObjectKlas() {
		return Klas1;
	}
	//het obect Klas wordt bij elke les toegekend, maar door een constructor probleem hebben we de klas apart toegevoegd in de fucntie in de pris.
	public void setObjectKlas(Klas k){
		Klas1=k;
	}
}
