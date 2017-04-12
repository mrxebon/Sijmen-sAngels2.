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

public class ChartArrayController implements Handler {
	private PrIS informatieSysteem;
	
	public ChartArrayController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}

	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/docent/chartsarray/klasarray")) {
			klasarray(conversation);
		} else if (conversation.getRequestedURI().startsWith("/student/chartsarray/studentarray")) {
			studentarray(conversation);
		}
	}
	
	private void klasarray(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String klasnaam = lJsonObjectIn.getString("klas");
		Klas lKlas = informatieSysteem.getKlas(klasnaam);  		
		ArrayList<Student> lStudentenVanKlas = lKlas.getStudenten();		// Studenten van klas pakken
  	JsonArrayBuilder ChartDataArrayBuilder = Json.createArrayBuilder(); // maak array voor de chartdata
  	JsonArrayBuilder lJsonObjectBuilderVoorChartData = Json.createArrayBuilder(); // maak het JsonObject voor een chartdata
  	lJsonObjectBuilderVoorChartData.add("Naam").add("Absentie").add("Gemiddelde");
  	ChartDataArrayBuilder.add(lJsonObjectBuilderVoorChartData);
  	for (Student lMedeStudent : lStudentenVanKlas) {
  		lJsonObjectBuilderVoorChartData
  		.add(lMedeStudent.getVoornaam())     
  		.add(informatieSysteem.presentiePercentageVanStudent(lMedeStudent.getStudentNummer()))	
  		.add(informatieSysteem.presentiePercentageVanKlas(klasnaam));				     
  		ChartDataArrayBuilder.add(lJsonObjectBuilderVoorChartData);
  	}
  	
  	String lJsonOutStr = ChartDataArrayBuilder.build().toString();												// maak er een string van
		conversation.sendJSONMessage(lJsonOutStr);
	}
	
	private void studentarray(Conversation conversation) { 
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String studentnaam = lJsonObjectIn.getString("username");
		Student lStudentZelf = informatieSysteem.zoekStudent(studentnaam.replaceAll("\\s+",""));
  	JsonArrayBuilder ChartDataArrayBuilder = Json.createArrayBuilder(); // maak array voor de chartdata
  	JsonArrayBuilder lJsonObjectBuilderVoorChartData = Json.createArrayBuilder(); // maak het JsonObject voor een chartdata
  	
  	String lessen = informatieSysteem.getLessenStudent(lStudentZelf.getStudentNummer());
  	List<String> lessenList = Arrays.asList(lessen.split(":"));
  	for(String les : lessenList){ 
  		lJsonObjectBuilderVoorChartData
  		.add(les)
  		.add(informatieSysteem.presentiePercentageVanStudentperVak(lStudentZelf.getStudentNummer(), les));			     
  		ChartDataArrayBuilder.add(lJsonObjectBuilderVoorChartData);
  	}
  	String lJsonOutStr = ChartDataArrayBuilder.build().toString();												// maak er een string van
		conversation.sendJSONMessage(lJsonOutStr);
	}
}
