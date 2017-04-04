package controller;

import java.util.Calendar;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import server.Conversation;
import server.Handler;

public class AbsentieLijstController implements Handler {
	private PrIS informatieSysteem;
	
	public AbsentieLijstController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}

	public void handle(Conversation conversation) {
		
	}
}
