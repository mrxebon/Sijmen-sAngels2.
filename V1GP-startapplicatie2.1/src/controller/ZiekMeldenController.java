package controller;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import server.Conversation;
import server.Handler;

public class ZiekMeldenController implements Handler {
	private boolean isZiek = false;
	
	public ZiekMeldenController(PrIS infoSys) {
		
	}

	public void handle(Conversation conversation) {
	  if(conversation.getRequestedURI().startsWith("/student/ziekmelden/ziekteupdated")) {
	  	verzendZiekte(conversation);
	  }
	}
  
  private void verzendZiekte(Conversation conversation) {
  	JsonObject JsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String studentGebruikersnaam = JsonObjectIn.getString("username");
		//isZiek = student.get(studentGebruikersnaam).getZiek()		- Haal van de student met de username op of deze ziek (true) of niet (false) is. (methode moet nog geaakt worden)
		
  	JsonObjectBuilder JsonObjectBuilder = Json.createObjectBuilder();
  	if(isZiek)
  		JsonObjectBuilder.add("is_ziek", "wel");
  	else
  		JsonObjectBuilder.add("is_ziek",  "niet");
		String lJsonOut = JsonObjectBuilder.build().toString();
		
		conversation.sendJSONMessage(lJsonOut);
  }
}
