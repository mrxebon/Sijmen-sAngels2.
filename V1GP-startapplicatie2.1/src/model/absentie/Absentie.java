package model.absentie;

import model.klas.Klas;
import model.persoon.*;
import model.rooster.*;
/*de klasse absentie is gescrheven volgens de volgende principes.
 * 1. De Class is een verzameling van alle data van een enkele absentie
 * 2. De Classes Les en student bevatten alle data die worden gebruikt voor absentie
 * 3. De boolean absentie geeft aan of hij absent was in de les, de boolean isZiek of hij ziek was
 * 4. de absenties worden opgeslagen in een les waar mensen berekeningen kunnen uitvoeren*/
public class Absentie {
	private Les deLes;
	private Student leerling;
	private boolean isAbsent, isZiek;
	private Klas eigenKlas;

	public Absentie(boolean absent, Student stu, Les ls) {
		isAbsent = absent;
		deLes = ls;
		leerling = stu;
		eigenKlas = deLes.getObjectKlas();
		if (absent == false && leerling.getZiek() == true) {
			leerling.setZiek(false);
		}
		isZiek = leerling.getZiek();
	}
	
	/*returnt aan de ophalende functie of de leerling ziek was op het moment waarop hij ziek was*/
	public boolean getZiek() {
		return isZiek;
	}
	/*retuns de class les die aan deze absentie is gekoppelt*/
	public Les getLes() {
		return deLes;
	}
	/*returnt de boolean absentie om te laten zien of de leerling aanwezig was of niet*/
	public boolean getAbsentie() {
		return isAbsent;
	}
	/*De klasse student wordt teruggestuurd*/
	public Student getStudent() {
		return leerling;
	}
	/*een shortcut om sneller de datum terug te krijgen van de absentie*/
	public String getDatum() {
		return deLes.getDatum();
	}
	/*shortcut om het leerlingnummer terug te krijgen van de student die absent of present was*/
	public int getStudentNummer() {
		return leerling.getStudentNummer();
	}
	/*shortcut om de naam weer te geven*/
	public String getCursusnaam() {
		return deLes.getVak();
	}
	/*een link naar de klas object van de leerling die absent was*/
	public Klas getKlas() {
		return eigenKlas;
	}
	/*compare functie om te voorkomen dat dezelfde absentie vaker kan voorkomen*/
	public boolean equals(Object object){
		boolean x= false;
		if(object instanceof Absentie){
			Absentie absentie = (Absentie) object;
			if (this.deLes.equals(absentie.deLes)&& this.leerling.equals(absentie.leerling)){
				x=true;
			}
		}
		
		
		return x;
	}
	//returnt een string voor gebruik op de website
	public String toString(){
		String s="niet ziek";
		if (isZiek==true){
			s="ziek";
			//vak datum, isziek
		}
		return deLes.getVak()+","+deLes.getDatum()+","+s;
	}

}
