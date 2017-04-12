//checked
package model.persoon;

public class Student extends Persoon {

	private int studentNummer;
	private String groepId;
	private double aanwezigheid;
	private boolean ziek =false;
	public Student(
		String pVoornaam, 
		String pTussenvoegsel, 
		String pAchternaam, 
		String pWachtwoord, 
		String pGebruikersnaam,
		int pStudentNummer) {
		super(
			pVoornaam, 
			pTussenvoegsel, 
			pAchternaam, 
			pWachtwoord, 
			pGebruikersnaam);
		this.setStudentNummer(pStudentNummer);
		this.setGroepId("");
		this.setAanwezigheid(0.0);
	}

//dit gebruikten we uiteindelijk niet, een overblijfsel van de startapplicatie
 public String getGroepId() {
    return this.groepId;	
  }
 
  public void setGroepId(String pGroepId) {
    this.groepId= pGroepId;	
  }
 //deze functies zijn uiteindelijk ook niet gebruikt, de class absentie heeft dit overgenomen 
  public double getAanwezigheid() {
    return this.aanwezigheid;	
  }
 
  public void setAanwezigheid(double procent) {
    this.aanwezigheid= procent;	
  }
 //returnt studentnummer
	public int getStudentNummer() {
		return this.studentNummer;
	}

	private void setStudentNummer(int pStudentNummer) {
		this.studentNummer = pStudentNummer;
	}
	
	//extra classes written by us, de boolean ziek is een status die op actief of inactief kan worden gezet, als een student ziek is wordt de boolean true anders is deze false.
	//we gebruiken dit in de class absentie.
	public boolean getZiek(){
		return ziek;}
	//setziek zet de status ziek op true of false.
	public void setZiek(boolean zk){
		ziek = zk;}
	
}
