package model.absentie;

import model.persoon.*;
import model.rooster.*;

public class Absentie {
	private Les deLes;
	private Student leerling;
	private boolean isAbsent, isZiek;

	public Absentie(boolean absent, boolean ziek, Student stu, Les ls) {
		isAbsent = absent;
		ziek = isZiek;
		leerling = stu;
		isZiek=stu.getZiek();
		deLes = ls;
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
	
}
