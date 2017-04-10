package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import model.klas.Klas;
import model.rooster.Les;
import model.persoon.Student;
import server.Conversation;
import server.Handler;

public class AbsentieNoterenController implements Handler {
	private PrIS informatieSysteem;
	
	public AbsentieNoterenController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}

	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/docent/absentienoteren/lessen")) {
			lessenOphalen(conversation);
		} else if (conversation.getRequestedURI().startsWith("/docent/absentienoteren/ophalen")) {
			ophalen(conversation);
		}
	}
	
	private void ophalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String klasnaam = ((klasnaam = lJsonObjectIn.getString("klas")) != null) ? klasnaam : "V1B"; //als er geen klas is aangeklikt gebruik V1B
		Klas lKlas = informatieSysteem.getKlas(klasnaam);	
    ArrayList<Student> lStudentenVanKlas = lKlas.getStudenten();
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();
		for (Student lMedeStudent : lStudentenVanKlas) {
				JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder();
				String lLastName = lMedeStudent.getVolledigeAchternaam();
				lJsonObjectBuilderVoorStudent
					.add("id", lMedeStudent.getStudentNummer())	
					.add("firstName", lMedeStudent.getVoornaam())	
					.add("lastName", lLastName)				 
				  .add("presence", lMedeStudent.getAanwezigheid());					     
			  
			  lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent);		
		}
    String lJsonOutStr = lJsonArrayBuilder.build().toString();
		conversation.sendJSONMessage(lJsonOutStr);	
	}
	
	private void lessenOphalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String datum = lJsonObjectIn.getString("datum");
		SimpleDateFormat format1 = new SimpleDateFormat("d-M-yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		String newdate = null;
		try {
			Date date = format1.parse(datum);
			newdate = format2.format(date);
		} catch (ParseException e) { e.printStackTrace(); }
		ArrayList<Les> lessenVanVandaag = informatieSysteem.getLessenVanDatum(newdate);
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();
		for (Les les : lessenVanVandaag) {	
			JsonObjectBuilder lJsonObjectBuilderVoorLessen = Json.createObjectBuilder();
			lJsonObjectBuilderVoorLessen
				.add("vak", les.getVak())
				.add("begintijd", les.getBegintijd())
				.add("eindtijd", les.getEindtijd())
				.add("docent", les.getDocent())
				.add("lokaal", les.getLokaal())
				.add("klas", les.getKlas());
		  lJsonArrayBuilder.add(lJsonObjectBuilderVoorLessen);
		}
		String lJsonOutStr = lJsonArrayBuilder.build().toString();
		System.out.println(lJsonOutStr);
		conversation.sendJSONMessage(lJsonOutStr);	
	}
}
