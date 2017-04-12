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
  	
  	String rol = JsonObjectIn.getString("rol");																					//Rol (docent/student) van gebruiker
  	String naam = JsonObjectIn.getString("username");																		//Gebruikersnaam (= E-mail adres) van gebruiker
  	String wachtwoordHuidig = JsonObjectIn.getString("huidig");													//Ingevoerde huidige wachtwoord van de gebruiker
  	String wachtwoordNieuw = JsonObjectIn.getString("nieuw");														//Gewenste nieuwe wachtwoord van de gebruiker
		
		JsonObjectBuilder JsonObjectBuilder = Json.createObjectBuilder();
		if(rol.equals("docent")) {																													//Is de gebruiker in kwestie een docent of een student?
			if(informatieSysteem.getDocent(naam).komtWachtwoordOvereen(wachtwoordHuidig)) {		//Komt het ingevoerde wachtwoord overeen met het opgeslagen wachtwoord?
				informatieSysteem.getDocent(naam).setWachtwoord(wachtwoordNieuw);								//Zo ja, verander het wachtwoord van de persoon in kwestie naar het gewenste nieuwe wachtwoord
				JsonObjectBuilder.add("result", "succes");
			}
			else {
				JsonObjectBuilder.add("result", "wachtwoord_incorrect");												//Ingevoerde huidige wachtwoord komt niet overeen; stuur foutmelding terug
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
			JsonObjectBuilder.add("result", "rol_incorrect");																	//Gebruiker is een docent noch student; stuur foutmelding terug.
		}		
		String lJsonOut = JsonObjectBuilder.build().toString();		
		conversation.sendJSONMessage(lJsonOut);
  }
}
