package model.absentie;

import model.klas.Klas;
import model.persoon.*;
import model.rooster.*;

public class Absentie {
	private Les deLes;
	private Student leerling;
	private boolean isAbsent, isZiek;
	private Klas eigenKlas;

	public Absentie(boolean absent,Student stu, Les ls) {
		isAbsent= absent;
		deLes=ls;
		leerling=stu;
		eigenKlas= deLes.getObjectKlas();
		if(absent==false && leerling.getZiek()==true){
			leerling.setZiek(false);
		}
		isZiek=leerling.getZiek();
	}
	
	public boolean getZiek(){
		return isZiek;
	}
	
	public boolean getAbsentie(){
		return isAbsent;
	}
	
	public Student getStudent(){
		return leerling;
	}
	
	public String getDatum() {
		return deLes.getDatum();
	}
	
	public int getStudentNummer() {
		return leerling.getStudentNummer();
	}
	
	public String getCursusnaam() {
		return deLes.getVak();
	}
	
}
