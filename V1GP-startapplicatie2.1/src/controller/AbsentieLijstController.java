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
	
	private void ophalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String klasnaam = lJsonObjectIn.getString("klas");
		String username = lJsonObjectIn.getString("username");
		Klas lKlas = informatieSysteem.getKlas(klasnaam);
		Student lStudentZelf = informatieSysteem.zoekStudent(username);
		if(lKlas != null){
	    ArrayList<Student> lStudentenVanKlas = lKlas.getStudenten();	// Studenten van klas pakken
			JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();						// Maak array voor return
			for (Student lMedeStudent : lStudentenVanKlas) {									        // loop door de studenten
					JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); // maak het JsonObject voor een student
					String lLastName = lMedeStudent.getVolledigeAchternaam();
					lJsonObjectBuilderVoorStudent
						.add("id", lMedeStudent.getStudentNummer())																	//vul het JsonObject		     
						.add("firstName", lMedeStudent.getVoornaam())	
						.add("lastName", lLastName)				 
					  .add("presence", informatieSysteem.presentiePercentageVanStudent(lMedeStudent.getStudentNummer()));					     
				  lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent);													//voeg het JsonObject aan de array toe				     
			}
	    String lJsonOutStr = lJsonArrayBuilder.build().toString();												// maak er een string van
			conversation.sendJSONMessage(lJsonOutStr);																				// string gaat terug naar de Polymer-GUI!
		}
		else if (lStudentZelf != null){
			ArrayList<Student> lStudentenGevonden = informatieSysteem.zoekStudenten(username);
			JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();						// Maak array voor return
			for (Student lMedeStudent : lStudentenGevonden) {									        // loop door de studenten
				JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); // maak het JsonObject voor een student
				String lLastName = lMedeStudent.getVolledigeAchternaam();
				lJsonObjectBuilderVoorStudent
					.add("id", lMedeStudent.getStudentNummer())																	//vul het JsonObject		     
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
		Student lStudentZelf = informatieSysteem.getStudent(lGebruikersnaam);
		String data = informatieSysteem.weergeefAlleAbsenten(lStudentZelf.getStudentNummer());
		
		JsonArrayBuilder TestArrayBuilder = Json.createArrayBuilder();
		JsonObjectBuilder lJsonObjectBuilderVoorAbsentie = Json.createObjectBuilder(); // maak het JsonObject voor een absentie
		List<String> dataList = Arrays.asList(data.split(":"));
		for(String absentie : dataList){
			String[] absentieData = absentie.split(",");
			String vak = absentieData[0];
			String datum = absentieData[1];
			String ziek = absentieData[2];
			
			lJsonObjectBuilderVoorAbsentie
			.add("vak", vak)     
			.add("datum", datum)	
			.add("ziek", ziek);				     
			TestArrayBuilder.add(lJsonObjectBuilderVoorAbsentie);													//voeg het JsonObject aan de array toe		
		}
		
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();						// Maak array voor return
		JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); // maak het JsonObject voor een student
		String lLastName = lStudentZelf.getVolledigeAchternaam();
		lJsonObjectBuilderVoorStudent
		.add("id", lStudentZelf.getStudentNummer())																	//vul het JsonObject		     
		.add("firstName", lStudentZelf.getVoornaam())	
		.add("lastName", lLastName)	
		.add("presence", informatieSysteem.presentiePercentageVanStudent(lStudentZelf.getStudentNummer()))
		.add("absenties", TestArrayBuilder);					     
		lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent);													//voeg het JsonObject aan de array toe		
		
    String lJsonOutStr = lJsonArrayBuilder.build().toString();												// maak er een string van
    System.out.println(lJsonOutStr);
		conversation.sendJSONMessage(lJsonOutStr);					// string gaat terug naar de Polymer-GUI!
	}
}
