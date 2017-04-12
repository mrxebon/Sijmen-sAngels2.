package controller;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import server.Conversation;
import server.Handler;

public class WachtwoordWijzigenController implements Handler {
	private PrIS informatieSysteem; //
	
	public WachtwoordWijzigenController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}

	public void handle(Conversation conversation) {
	  if(conversation.getRequestedURI().startsWith("/wachtwoordwijzigen")) {
	  	wijzigWachtwoord(conversation);
	  }
	}
  
	private void wijzigWachtwoord(Conversation conversation) {
  	JsonObject JsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
  	
  	String rol = JsonObjectIn.getString("rol");
  	String naam = JsonObjectIn.getString("username");
  	String wachtwoordHuidig = JsonObjectIn.getString("huidig");
  	String wachtwoordNieuw = JsonObjectIn.getString("nieuw");
		
		JsonObjectBuilder JsonObjectBuilder = Json.createObjectBuilder();
		if(rol.equals("docent")) {
			if(informatieSysteem.getDocent(naam).komtWachtwoordOvereen(wachtwoordHuidig)) {
				informatieSysteem.getDocent(naam).setWachtwoord(wachtwoordNieuw);
				JsonObjectBuilder.add("result", "succes");
			}
			else {
				JsonObjectBuilder.add("result", "wachtwoord_incorrect");
			}			
		}
		else if(rol.equals("student")) {
			if(informatieSysteem.getStudent(naam).komtWachtwoordOvereen(wachtwoordHuidig)) {
				informatieSysteem.getStudent(naam).setWachtwoord(wachtwoordNieuw);
				JsonObjectBuilder.add("result", "succes");
			}
			else {
				JsonObjectBuilder.add("result", "wachtwoord_incorrect");
			}
		}
		else {
			JsonObjectBuilder.add("result", "rol_incorrect");
		}		
		String lJsonOut = JsonObjectBuilder.build().toString();		
		conversation.sendJSONMessage(lJsonOut);
  }
}
