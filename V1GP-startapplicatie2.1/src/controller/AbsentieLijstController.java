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

public class AbsentieLijstController implements Handler {
	private PrIS informatieSysteem;
	
	public AbsentieLijstController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}

	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/student/absentielijst/ophalen")) {
			ophalen(conversation);
		}
	}
	
	private void ophalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String klasnaam = lJsonObjectIn.getString("klas");
		if(klasnaam == null){ klasnaam = "V1A"; }
		
		Klas lKlas = informatieSysteem.getKlas(klasnaam);		// klas van de student opzoeken

    ArrayList<Student> lStudentenVanKlas = lKlas.getStudenten();	// studenten opzoeken
		
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();						// Uiteindelijk gaat er een array...
		
		for (Student lMedeStudent : lStudentenVanKlas) {									        // met daarin voor elke medestudent een JSON-object... 
				
				JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); // maak het JsonObject voor een student
				String lLastName = lMedeStudent.getVolledigeAchternaam();
				lJsonObjectBuilderVoorStudent
					.add("id", lMedeStudent.getStudentNummer())																	//vul het JsonObject		     
					.add("firstName", lMedeStudent.getVoornaam())	
					.add("lastName", lLastName)				 
				  .add("presence", lMedeStudent.getAanwezigheid());					     
			  
			  lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent);													//voeg het JsonObject aan het array toe				     
			
		}
    String lJsonOutStr = lJsonArrayBuilder.build().toString();												// maak er een string van
		conversation.sendJSONMessage(lJsonOutStr);																				// string gaat terug naar de Polymer-GUI!
	}
}
