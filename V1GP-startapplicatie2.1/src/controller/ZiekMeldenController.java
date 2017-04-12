package controller;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import server.Conversation;
import server.Handler;

public class ZiekMeldenController implements Handler {
	private PrIS informatieSysteem;
	
	public ZiekMeldenController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}

	public void handle(Conversation conversation) {
	  if(conversation.getRequestedURI().startsWith("/student/ziekmelden/ziekteupdated")) {						//Request om de status van ziekte te veranderen in het systeem
	  	toggleZiekte(conversation);
	  }
	  else if(conversation.getRequestedURI().startsWith("/student/ziekmelden/ziekterequest")) {				//Request om de status van ziekte op te vragen bij het systeem
	  	verzendZiekte(conversation);
	  }
	}
  
	private void verzendZiekte(Conversation conversation) {
  	JsonObject JsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String studentGebruikersnaam = JsonObjectIn.getString("username");															//Gebruikersenaam (= E-mailadres) van gebruiker
		boolean isZiek = informatieSysteem.getStudent(studentGebruikersnaam).getZiek();									//Staat de gebruiker op dit moment als ziek gemeld in het systeem?..
																																																		
  	JsonObjectBuilder JsonObjectBuilder = Json.createObjectBuilder();
  	if(isZiek)																																											
  		JsonObjectBuilder.add("ziek", "wel");
  	else
  		JsonObjectBuilder.add("ziek", "niet");
		String lJsonOut = JsonObjectBuilder.build().toString();
		
		conversation.sendJSONMessage(lJsonOut);																													//.. stuur dat antwoord terug.
  }
	
  private void toggleZiekte(Conversation conversation) {
  	JsonObject JsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String studentGebruikersnaam = JsonObjectIn.getString("username");															//Gebruikersenaam (= E-mailadres) van gebruiker
		boolean isZiek = informatieSysteem.getStudent(studentGebruikersnaam).getZiek();									//Haal op of de gebruiker als ziek gemeld staat in het systeem
		isZiek =! isZiek;																																								//Flip die waarde om en..
		informatieSysteem.getStudent(studentGebruikersnaam).setZiek(isZiek);														//.. sla de nieuwe waarde weer op in het systeem.
  }
}
