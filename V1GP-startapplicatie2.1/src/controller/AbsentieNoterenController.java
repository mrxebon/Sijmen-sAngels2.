package controller;

import java.util.ArrayList;
import java.util.Calendar;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import model.klas.Klas;
import model.persoon.Student;
import server.Conversation;
import server.Handler;

public class AbsentieNoterenController implements Handler {
	private PrIS informatieSysteem;
	
	public AbsentieNoterenController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}

	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/docent/absentienoteren/ophalen")) {
			ophalen(conversation);
			
		}
	}
	
	private void ophalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String klasnaam = ((klasnaam = lJsonObjectIn.getString("klas")) != null) ? klasnaam : "V1B"; //als er geen klas is aangeklikt gebruik V1B
		Klas lKlas = informatieSysteem.getKlas(klasnaam);		// Zet de klas

    ArrayList<Student> lStudentenVanKlas = lKlas.getStudenten();	// Studenten van klas pakken
		
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();						// Maak array voor return
		
		for (Student lMedeStudent : lStudentenVanKlas) {									        // loop door de studenten
				
				JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); // maak het JsonObject voor een student
				String lLastName = lMedeStudent.getVolledigeAchternaam();
				lJsonObjectBuilderVoorStudent
					.add("id", lMedeStudent.getStudentNummer())																	//vul het JsonObject		     
					.add("firstName", lMedeStudent.getVoornaam())	
					.add("lastName", lLastName)				 
				  .add("presence", lMedeStudent.getAanwezigheid());					     
			  
			  lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent);													//voeg het JsonObject aan de array toe				     
			
		}
    String lJsonOutStr = lJsonArrayBuilder.build().toString();												// maak er een string van
		conversation.sendJSONMessage(lJsonOutStr);																				// string gaat terug naar de Polymer-GUI!
	}
}
