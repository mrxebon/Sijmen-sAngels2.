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
		} else if (conversation.getRequestedURI().startsWith("/student/studentabsentie/chartdataophalen")) {
			chartdataophalen(conversation);
		} 
	}
	
	private void ophalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();	//Fetch de request van JSON
		//Pak de waardes die meegegeven zijn
		String klasnaam = lJsonObjectIn.getString("klas");
		String username = lJsonObjectIn.getString("username");
		Klas lKlas = informatieSysteem.getKlas(klasnaam);  								//pak de klas aan de hand van de klascode(klasnaam)
		Student lStudentZelf = informatieSysteem.zoekStudent(username);  	//pak de student aan de hand van zijn gebruikersnaam
		//Als de klas correct is gevonden
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
	    String lJsonOutStr = lJsonArrayBuilder.build().toString();												// maak er een string van
			conversation.sendJSONMessage(lJsonOutStr);																				// string gaat terug naar de Polymer-GUI!
		}
		//Als de student correct gevonden is
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
				  .add("presence", informatieSysteem.presentiePercentageVanStudent(lMedeStudent.getStudentNummer()));					     
			  lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent);													//voeg het JsonObject aan de array toe				     
		}
    String lJsonOutStr = lJsonArrayBuilder.build().toString();												// maak er een string van
		conversation.sendJSONMessage(lJsonOutStr);																				// string gaat terug naar de Polymer-GUI!
		} else{ conversation.sendJSONMessage("LEEG");	}
																						
	}
	
	private void studentophalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String lGebruikersnaam = lJsonObjectIn.getString("username");
		Student lStudentZelf = informatieSysteem.getStudent(lGebruikersnaam.replaceAll("\\s+",""));
		String data = informatieSysteem.weergeefAlleAbsenten(lStudentZelf.getStudentNummer()); //haal alle absenties van de student op
		
		JsonArrayBuilder AbsentieArrayBuilder = Json.createArrayBuilder(); // maak array voor de absenties
		JsonObjectBuilder lJsonObjectBuilderVoorAbsentie = Json.createObjectBuilder(); // maak het JsonObject voor een absentie
		if(data.length() > 0){
  		List<String> dataList = Arrays.asList(data.split(":"));
  		for(String absentie : dataList){
  			String[] absentieData = absentie.split(",");
  			String vak = absentieData[0];
  			String datum = absentieData[1];
  			String ziek = absentieData[2];
  			lJsonObjectBuilderVoorAbsentie //vul absentie object met data
  			.add("vak", vak)     
  			.add("datum", datum)	
  			.add("ziek", ziek);				     
  			AbsentieArrayBuilder.add(lJsonObjectBuilderVoorAbsentie);			//voeg het JsonObject aan de array voor absenties toe		
  		}
		}
		
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();						// Maak array voor return
		JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); // maak het JsonObject voor een student
		String lLastName = lStudentZelf.getVolledigeAchternaam();
		lJsonObjectBuilderVoorStudent
		.add("id", lStudentZelf.getStudentNummer())																	//vul het JsonObject		     
		.add("firstName", lStudentZelf.getVoornaam())	
		.add("lastName", lLastName)	
		.add("presence", informatieSysteem.presentiePercentageVanStudent(lStudentZelf.getStudentNummer()))
		.add("absenties", AbsentieArrayBuilder);				//Voeg de absentie array toe als absenties	     
		lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent);													//voeg het JsonObject aan de array toe		
		
    String lJsonOutStr = lJsonArrayBuilder.build().toString();												// maak er een string van
		conversation.sendJSONMessage(lJsonOutStr);					// string gaat terug naar de Polymer-GUI!
	}
	
	private void chartdataophalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String klasnaam = lJsonObjectIn.getString("klas");
		Klas lKlas = informatieSysteem.getKlas(klasnaam);  		
		ArrayList<Student> lStudentenVanKlas = lKlas.getStudenten();		// Studenten van klas pakken
  	JsonArrayBuilder ChartDataArrayBuilder = Json.createArrayBuilder(); // maak array voor de chartdata
  	JsonArrayBuilder lJsonObjectBuilderVoorChartData = Json.createArrayBuilder(); // maak het JsonObject voor een chartdata
  	lJsonObjectBuilderVoorChartData.add("Naam").add("Absentie").add("Gemiddelde");
  	ChartDataArrayBuilder.add(lJsonObjectBuilderVoorChartData);
  	for (Student lMedeStudent : lStudentenVanKlas) {
  		lJsonObjectBuilderVoorChartData
  		.add(lMedeStudent.getVoornaam())     
  		.add(informatieSysteem.presentiePercentageVanStudent(lMedeStudent.getStudentNummer()))	
  		.add(informatieSysteem.presentiePercentageVanKlas(klasnaam));				     
  		ChartDataArrayBuilder.add(lJsonObjectBuilderVoorChartData);
  	}
  	
  	String lJsonOutStr = ChartDataArrayBuilder.build().toString();												// maak er een string van
		conversation.sendJSONMessage(lJsonOutStr);
	}
}
