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


 public String getGroepId() {
    return this.groepId;	
  }
 
  public void setGroepId(String pGroepId) {
    this.groepId= pGroepId;	
  }
  
  public double getAanwezigheid() {
    return this.aanwezigheid;	
  }
 
  public void setAanwezigheid(double procent) {
    this.aanwezigheid= procent;	
  }
 
	public int getStudentNummer() {
		return this.studentNummer;
	}

	private void setStudentNummer(int pStudentNummer) {
		this.studentNummer = pStudentNummer;
	}
	public boolean getZiek(){
		return ziek;}
	public void setZiek(boolean zk){
		ziek = zk;}
	
}
