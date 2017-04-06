package model.absentie;

import model.persoon.*;
import model.rooster.*;

public class Absentie {
	private Les deLes;
	private Student leerling;
	private boolean isAbsent, isZiek;

	public Absentie(boolean absent, boolean ziek, Student stu, les ls) {
		isAbsent = absent;
		ziek = isZiek;
		leerling = stu;
		isZiek=stu.getZiek();
		les = ls;
	}
}
