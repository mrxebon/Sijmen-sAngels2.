package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import model.klas.Klas;
import model.persoon.Student;
import server.Conversation;
import server.Handler;

public class AbsentieLijstController implements Handler {
	private PrIS informatieSysteem;
	
	public AbsentieLijstController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}

	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/docent/absentielijst/ophalen")) {
			ophalen(conversation);
		} else if (conversation.getRequestedURI().startsWith("/student/studentabsentie/ophalen")) {
			studentophalen(conversation);
		}
	}
	
	//Deze methode pakt de klas en username uit de request en stuurt een array van alle studenten in een klas terug in de response.
	private void ophalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();	//Fetch de request van JSON
		//Pak de waardes die meegegeven zijn
		String klasnaam = lJsonObjectIn.getString("klas");
		String username = lJsonObjectIn.getString("username");
		Klas lKlas = informatieSysteem.getKlas(klasnaam);  								//pak de klas aan de hand van de klascode(klasnaam)
		Student lStudentZelf = informatieSysteem.zoekStudent(username);  	//pak de student aan de hand van zijn gebruikersnaam
		//Deze IF wordt uitgevoerd wanneer de gebruiker op een klas klikt.
		if(lKlas != null){
	    ArrayList<Student> lStudentenVanKlas = lKlas.getStudenten();		// Studenten van klas pakken
			JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();						// Maak array voor return
			for (Student lMedeStudent : lStudentenVanKlas) {									        // loop door de studenten
					JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); // maak het JsonObject voor een student
					String lLastName = lMedeStudent.getVolledigeAchternaam();
					lJsonObjectBuilderVoorStudent							//vul het JsonObject
						.add("id", lMedeStudent.getStudentNummer())																			     
						.add("firstName", lMedeStudent.getVoornaam())	
						.add("lastName", lLastName)				 
					  .add("presence", informatieSysteem.presentiePercentageVanStudent(lMedeStudent.getStudentNummer()));					     
				  lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent);					//voeg het JsonObject aan de array voor return toe				     
			}
	    String lJsonOutStr = lJsonArrayBuilder.build().toString();		// maak er een string van
			conversation.sendJSONMessage(lJsonOutStr);						// string gaat terug naar de Polymer-GUI!
		}
		//Deze IF wordt uitgevoerd wanneer er door de gebruiker in het zoekveld iets is opgezocht.
		else if (lStudentZelf != null){
			ArrayList<Student> lStudentenGevonden = informatieSysteem.zoekStudenten(username);
			JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();						// Maak array voor return
			for (Student lMedeStudent : lStudentenGevonden) {									        // loop door de studenten
				JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); // maak het JsonObject voor een student
				String lLastName = lMedeStudent.getVolledigeAchternaam();
				lJsonObjectBuilderVoorStudent				//vul het JsonObject
					.add("id", lMedeStudent.getStudentNummer())																			     
					.add("firstName", lMedeStudent.getVoornaam())	
					.add("lastName", lLastName)				 
				  .add("presence", informatieSysteem.presentiePercentageVanStudent(lMedeStudent.getStudentNummer()))
				  .add("presencegem", informatieSysteem.gemiddeldeAbsentie());					     
			  lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent);													//voeg het JsonObject aan de array toe				     
		}
    String lJsonOutStr = lJsonArrayBuilder.build().toString();												// maak er een string van
		conversation.sendJSONMessage(lJsonOutStr);																				// string gaat terug naar de Polymer-GUI!
		} else{ conversation.sendJSONMessage("LEEG");	}
																						
	}
	
//Deze methode pakt de username uit de request en stuurt een array terug met daarin de gegevens van de student, en alle absenties.
	private void studentophalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String lGebruikersnaam = lJsonObjectIn.getString("username");
		Student lStudentZelf = informatieSysteem.getStudent(lGebruikersnaam.replaceAll("\\s+",""));
		//Maak hieronder een array met alle absenties
		JsonArrayBuilder AbsentieArrayBuilder = Json.createArrayBuilder(); // maak array voor de absenties
		JsonObjectBuilder lJsonObjectBuilderVoorAbsentie = Json.createObjectBuilder(); // maak het JsonObject voor een absentie
		String data = informatieSysteem.weergeefAlleAbsenten(lStudentZelf.getStudentNummer()); //haal alle absenties van de student op
		if(data.length() > 0){ //controleer of er wel absenties zijn, zo niet blijft de array leeg.
  		List<String> dataList = Arrays.asList(data.split(":"));
  		for(String absentie : dataList){ //Pak alle data van de absentie en zet deze in eigen variabelen
  			String[] absentieData = absentie.split(",");
  			String vak = absentieData[0];
  			String datum = absentieData[1];
  			String ziek = absentieData[2];
  			lJsonObjectBuilderVoorAbsentie 
  			.add("vak", vak)     
  			.add("datum", datum)	
  			.add("ziek", ziek);				     
  			AbsentieArrayBuilder.add(lJsonObjectBuilderVoorAbsentie);		
  		}
		}
		//Maak hieronder een array met de gegevens van de student en stop de array met absenties er ook bij.
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();						
		JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); 
		String lLastName = lStudentZelf.getVolledigeAchternaam();
		lJsonObjectBuilderVoorStudent
		.add("id", lStudentZelf.getStudentNummer())																	 
		.add("firstName", lStudentZelf.getVoornaam())	
		.add("lastName", lLastName)	
		.add("presence", informatieSysteem.presentiePercentageVanStudent(lStudentZelf.getStudentNummer()))
		.add("presencegem", informatieSysteem.gemiddeldeAbsentie())
		.add("absenties", AbsentieArrayBuilder);				//Voeg de absentie array toe als absenties	     
		lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent);												
		
    String lJsonOutStr = lJsonArrayBuilder.build().toString();												
		conversation.sendJSONMessage(lJsonOutStr);			
	}
}
