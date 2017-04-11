package controller;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import server.Conversation;
import server.Handler;

public class WachtwoordWijzigenController implements Handler {
	private PrIS informatieSysteem;
	
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
  	String wachtwoordNieuw = JsonObjectIn.getString("nieuw2");
		String wachtwoordHuidig = JsonObjectIn.getString("huidig");
		
		System.out.println(rol);
		if(rol.equals("docent")) {
			informatieSysteem.getDocent(naam).setWachtwoord(wachtwoordNieuw);
		}
		else if(rol.equals("student")) {
			informatieSysteem.getStudent(naam).setWachtwoord(wachtwoordNieuw);
		}
		
  	JsonObjectBuilder JsonObjectBuilder = Json.createObjectBuilder();
  		JsonObjectBuilder.add("result", "succes");
		String lJsonOut = JsonObjectBuilder.build().toString();
		
		conversation.sendJSONMessage(lJsonOut);
  }
}
