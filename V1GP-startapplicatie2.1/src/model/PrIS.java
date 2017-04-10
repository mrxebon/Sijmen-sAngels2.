package model;

import java.util.ArrayList;

import model.klas.Klas;
import model.persoon.Docent;
import model.persoon.Student;
import model.rooster.Les;
import model.absentie.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PrIS {
	private ArrayList<Docent> deDocenten;
	private ArrayList<Student> deStudenten;
	private ArrayList<Klas> deKlassen;
	private ArrayList<Les> deLessen;
	private ArrayList<Les> deLessenVanVandaag = new ArrayList<Les>();
	private ArrayList<Absentie> deAbsenties= new ArrayList<Absentie>();
	
	/**
	 * De constructor maakt een set met standaard-data aan. Deze data
	 * moet nog uitgebreidt worden met rooster gegevens die uit een bestand worden
	 * ingelezen, maar dat is geen onderdeel van deze demo-applicatie!
	 * 
	 * De klasse PrIS (PresentieInformatieSysteem) heeft nu een meervoudige
	 * associatie met de klassen Docent, Student, Vakken en Klassen Uiteraard kan dit nog veel
	 * verder uitgebreid en aangepast worden! 
	 * 
	 * De klasse fungeert min of meer als ingangspunt voor het domeinmodel. Op
	 * dit moment zijn de volgende methoden aanroepbaar:
	 * 
	 * String login(String gebruikersnaam, String wachtwoord)
	 * Docent getDocent(String gebruikersnaam)
	 * Student getStudent(String gebruikersnaam)
	 * ArrayList<Student> getStudentenVanKlas(String klasCode)
	 * 
	 * Methode login geeft de rol van de gebruiker die probeert in te loggen,
	 * dat kan 'student', 'docent' of 'undefined' zijn! Die informatie kan gebruikt 
	 * worden om in de Polymer-GUI te bepalen wat het volgende scherm is dat getoond 
	 * moet worden.
	 * 
	 */
	public PrIS() {
		deDocenten = new ArrayList<Docent>();
		deStudenten = new ArrayList<Student>();
		deKlassen = new ArrayList<Klas>();
		deLessen = new ArrayList<Les>();

		// Inladen klassen
		vulKlassen(deKlassen);

		// Inladen studenten in klassen
		vulStudenten(deStudenten, deKlassen);

		// Inladen docenten
		vulDocenten(deDocenten);
		
		vulLessen(deLessen);
	
	} //Einde Pris constructor
	
	//deze method is static onderdeel van PrIS omdat hij als hulp methode 
	//in veel controllers gebruikt wordt
	//een standaardDatumString heeft formaat YYYY-MM-DD
	public static Calendar standaardDatumStringToCal(String pStadaardDatumString) {
		Calendar lCal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			lCal.setTime(sdf.parse(pStadaardDatumString));
		}  catch (ParseException e) {
			e.printStackTrace();
			lCal=null;
		}
		return lCal;
	}
	//deze method is static onderdeel van PrIS omdat hij als hulp methode 
	//in veel controllers gebruikt wordt
	//een standaardDatumString heeft formaat YYYY-MM-DD
	public static String calToStandaardDatumString (Calendar pCalendar) {
		int lJaar = pCalendar.get(Calendar.YEAR);
		int lMaand= pCalendar.get(Calendar.MONTH) + 1;
		int lDag  = pCalendar.get(Calendar.DAY_OF_MONTH);

		String lMaandStr = Integer.toString(lMaand);
		if (lMaandStr.length() == 1) {
			lMaandStr = "0"+ lMaandStr;
		}
		String lDagStr = Integer.toString(lDag);
		if (lDagStr.length() == 1) {
			lDagStr = "0"+ lDagStr;
		}
		String lString = 
				Integer.toString(lJaar) + "-" +
				lMaandStr + "-" +
				lDagStr;
		return lString;
	}

	public Docent getDocent(String gebruikersnaam) {
		Docent resultaat = null;
		
		for (Docent d : deDocenten) {
			if (d.getGebruikersnaam().equals(gebruikersnaam)) {
				resultaat = d;
				break;
			}
		}
		
		return resultaat;
	}
	
	public ArrayList<Les> getLessenVanDatum(String datum) {
		deLessenVanVandaag.clear();
		for (Les les : deLessen) {
			if (datum.equals(les.getDatum())) {
				deLessenVanVandaag.add(les);
			}
		}
		return deLessenVanVandaag;
	}
	
	public Klas getKlasVanStudent(Student pStudent) {
	  //used
		for (Klas lKlas : deKlassen) {
			if (lKlas.bevatStudent(pStudent)){
				return (lKlas);
			}
		}
		return null;
	}
	
	public Klas getKlas(String klasNaam) {
		// used
		Klas lGevondenKlas = null;
		for (Klas klas : deKlassen) {
			if (klas.getNaam().equals(klasNaam)) {
				lGevondenKlas = klas;
				break;
			}
		}
		return lGevondenKlas;
	}
	
	public Student zoekStudent(String nm) {
		// used
		Student lGevondenStudent = null;
		for (Student lStudent : deStudenten) {
			if (lStudent.getGebruikersnaam().contains(nm)) {
				lGevondenStudent = lStudent;
				break;
			}
		}
		return lGevondenStudent;
	}
	
	public ArrayList<Student> zoekStudenten(String nm) {
		// used
		ArrayList<Student> gevondenStudenten = new ArrayList<Student>();	
		for (Student lStudent : deStudenten) {
			if (lStudent.getGebruikersnaam().contains(nm)) {
				gevondenStudenten.add(lStudent);
			}
		}
		return gevondenStudenten;
	}
	
	public Student getStudent(String pGebruikersnaam) {
		// used
		Student lGevondenStudent = null;
		
		for (Student lStudent : deStudenten) {
			if (lStudent.getGebruikersnaam().equals(pGebruikersnaam)) {
				lGevondenStudent = lStudent;
				break;
			}
		}
		
		return lGevondenStudent;
	}
	
	public Student getStudent(int pStudentNummer) {
		// used
		Student lGevondenStudent = null;
		
		for (Student lStudent : deStudenten) {
			if (lStudent.getStudentNummer()==(pStudentNummer)) {
				lGevondenStudent = lStudent;
				break;
			}
		}
		
		return lGevondenStudent;
	}

	public String login(String gebruikersnaam, String wachtwoord) {
		for (Docent d : deDocenten) {
			if (d.getGebruikersnaam().equals(gebruikersnaam)) {
				if (d.komtWachtwoordOvereen(wachtwoord)) {
					return "docent";
				}
			}
		}
		
		for (Student s : deStudenten) {
			if (s.getGebruikersnaam().equals(gebruikersnaam)) {
				if (s.komtWachtwoordOvereen(wachtwoord)) {
					return "student";
				}
			}
		}
		
		return "undefined";
	}
	
	//Hier komen de absentie functies
	public int presentiePercentageVanStudent(int studentNummer){
		int absent=0;
		int totaal=0;
		for (Absentie absentie : deAbsenties) {
			if (absentie.getStudentNummer()==studentNummer){
				if(absentie.getAbsentie()==true){
					absent=absent+1;
				}
				totaal=totaal+1;
			}

		}
		if(absent == 0){
			return 100;
		}
		else{
			int presentie = 100-(absent/totaal*100);
			return presentie;
		}
	}
	public int presentiePercentageVanStudentperVak(int studentNummer, String vakCode){
		int absent=0;
		int totaal=0;
		for (Absentie absentie : deAbsenties) {
			if (absentie.getStudentNummer()==studentNummer && absentie.getLes().getVak().equals(vakCode)){
				if(absentie.getAbsentie()==true){
					absent=absent+1;
				}
				totaal=totaal+1;
			}

		}
		if(absent == 0){
			return 100;
		}
		else{
			int presentie = 100-(absent/totaal*100);
			return presentie;
		}
	}
	public int presentiePercentageVanKlas(String klasCode){
		int absent=0;
		int totaal=0;
		for (Absentie absentie : deAbsenties) {
			if (absentie.getKlas().getKlasCode().equals(klasCode)){
				if(absentie.getAbsentie()==true){
					absent=absent+1;
				}
				totaal=totaal+1;
			}

		}
		if(absent == 0){
			return 100;
		}
		else{
			int presentie = 100-(absent/totaal*100);
			return presentie;
		}
	}
	public int percentageVanKlasPerVak(String klasCode, String vakCode){
		int absent=0;
		int totaal=0;
		for (Absentie absentie : deAbsenties) {
			if (absentie.getKlas().getKlasCode().equals(klasCode) && absentie.getLes().getVak().equals(vakCode)){
				if(absentie.getAbsentie()==true){
					absent=absent+1;
				}
				totaal=totaal+1;
			}

		}
		if(absent == 0){
			return 100;
		}
		else{
			int presentie = 100-(absent/totaal*100);
			return presentie;
		}
	}
	//hier komt de setter
	public void addabsentie(String datum, int studentNummer, String vakNaam,String klasCode, boolean absent){
		Les les1=null;
		Student student1=null;
		for (Les les : deLessen) {
			if(les.getDatum()==datum && les.getKlas()==klasCode && les.getVak()==vakNaam){
				les1=les;
				System.out.println("student1 is"+ les1);
			}
		for (Student student: deStudenten){
				if(student.getStudentNummer()==studentNummer){
					student1=student;
					System.out.println("blahvlha");
				}
		
		
		
		}
		Absentie a1= new Absentie(absent,student1,les1);
		deAbsenties.add(a1);
		}
	}
	//hier komen de vul de lijst functies
	private void vulDocenten(ArrayList<Docent> pDocenten) {
		String csvFile = "././CSV/docenten.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
			
		try {
	
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	
			        // use comma as separator
				String[] element = line.split(cvsSplitBy);
				String gebruikersnaam = element[0].toLowerCase();
				String voornaam = element[1];
				String tussenvoegsel = element[2];
				String achternaam = element[3];
				pDocenten.add(new Docent(voornaam, tussenvoegsel, achternaam, "geheim", gebruikersnaam, 1));
				
				//System.out.println(gebruikersnaam);
		
			}
	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void vulLessen(ArrayList<Les> pLes){
		String csvFile = "././CSV/rooster.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		
		
		try {
			
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	
			        // use comma as separator
				String[] element = line.split(cvsSplitBy);
				String datum = element[0];
				String begintijd = element[1];
				String eindtijd = element[2];
				String vak = element[3];
				String docent = element[4];
				String lokaal = element[5];
				String klas = element[6];
				Les l1 = new Les(datum, begintijd, eindtijd, vak, docent, lokaal, klas);
				pLes.add(l1);
				for(Klas k: deKlassen){
					if (k.getKlasCode().equals(klas)){
						l1.setObjectKlas(k);
					}
				}
			}
	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void vulKlassen(ArrayList<Klas> pKlassen) {
		//TICT-SIE-VIA is de klascode die ook in de rooster file voorkomt
		//V1A is de naam van de klas die ook als file naam voor de studenten van die klas wordt gebruikt
		Klas k1 = new Klas("TICT-SIE-V1A", "V1A");
		Klas k2 = new Klas("TICT-SIE-V1B", "V1B");
		Klas k3 = new Klas("TICT-SIE-V1C", "V1C");
		Klas k4 = new Klas("TICT-SIE-V1D", "V1D");
		Klas k5 = new Klas("TICT-SIE-V1E", "V1E");
		Klas k6 = new Klas("TICT-SIE-V1F", "V1F");
		
		pKlassen.add(k1);
		pKlassen.add(k2);
		pKlassen.add(k3);
		pKlassen.add(k4);
		pKlassen.add(k5);
		pKlassen.add(k6);
	}	
	private void vulStudenten(
			ArrayList<Student> pStudenten,
			ArrayList<Klas> pKlassen) {
		Student lStudent;
		for (Klas k : pKlassen) {			
			String csvFile = "././CSV/" + k.getNaam() + ".csv";
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ",";

			try {

				br = new BufferedReader(new FileReader(csvFile));
				
				while ((line = br.readLine()) != null) {

				    // use comma as separator
					String[] element = line.split(cvsSplitBy);
					String gebruikersnaam = (element[3] + "." + element[2] + element[1] + "@student.hu.nl").toLowerCase();
					// verwijder spaties tussen  dubbele voornamen en tussen bv van der 
					gebruikersnaam = gebruikersnaam.replace(" ","");
					String lStudentNrString  = element[0];
					String lStudentAanwezigheid = element[4];
					double lAanwezigheid = Double.parseDouble(lStudentAanwezigheid);
					int lStudentNr = Integer.parseInt(lStudentNrString);
					lStudent = new Student(element[3], element[2], element[1], "geheim", gebruikersnaam, lStudentNr); //Volgorde 3-2-1 = voornaam, tussenvoegsel en achternaam
					lStudent.setAanwezigheid(lAanwezigheid); //zet meteen de aanwezigheidspercentage
					pStudenten.add(lStudent);
					k.voegStudentToe(lStudent);
					
					//System.out.println(gebruikersnaam);
			
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}	
			
		}
	}	


}
