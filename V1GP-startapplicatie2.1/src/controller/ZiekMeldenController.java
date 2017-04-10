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
	  if(conversation.getRequestedURI().startsWith("/student/ziekmelden/ziekteupdated")) {
	  	toggleZiekte(conversation);
	  }
	  else if(conversation.getRequestedURI().startsWith("/student/ziekmelden/ziekterequest")) {
	  	verzendZiekte(conversation);
	  }
	}
  
	private void verzendZiekte(Conversation conversation) {
  	JsonObject JsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String studentGebruikersnaam = JsonObjectIn.getString("username");
		boolean isZiek = informatieSysteem.getStudent(studentGebruikersnaam).getZiek();
		
  	JsonObjectBuilder JsonObjectBuilder = Json.createObjectBuilder();
  	if(isZiek)
  		JsonObjectBuilder.add("ziek", "wel");
  	else
  		JsonObjectBuilder.add("ziek", "niet");
		String lJsonOut = JsonObjectBuilder.build().toString();
		
		conversation.sendJSONMessage(lJsonOut);
  }
	
  private void toggleZiekte(Conversation conversation) {
  	JsonObject JsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String studentGebruikersnaam = JsonObjectIn.getString("username");
		boolean isZiek = informatieSysteem.getStudent(studentGebruikersnaam).getZiek();
		isZiek =! isZiek;		
		informatieSysteem.getStudent(studentGebruikersnaam).setZiek(isZiek);
  }
}
