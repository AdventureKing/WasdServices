package com.wasdplay.services;

//import Gateway.csgoSQLGateway;
import java.util.ArrayList;

import org.json.JSONException;

import com.wasdplay.services.csgo.CsgoFinishResults;
import com.wasdplay.services.csgo.CsgoSQLGateway;
import com.wasdplay.services.csgo.CsgoWebservice;

public class WasdServices {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Start webservice pull");
		CsgoWebservice csgoCallGenerateMatches = new CsgoWebservice();
		CsgoFinishResults csgoCallFinishMatches = new CsgoFinishResults();
		try {
			csgoCallGenerateMatches.hltvMatchFeedCall();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			csgoCallFinishMatches.csgoMatchFinishFeedCall();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		csgoCallGenerateMatches.getHltvgoMatchPageinFo();
		System.out.println("going into database: " + csgoCallGenerateMatches.getHLTVGOFeedResults());
		//System.out.print("\n\n\n\n\n\n\n\n\n\n");
		System.out.println("getting closed in database: " + csgoCallFinishMatches.getFinishResults().toString());

		CsgoSQLGateway csgoInsert = new CsgoSQLGateway();
		csgoInsert.insertMatch(csgoCallGenerateMatches.getHLTVGOFeedResults());

		csgoInsert.updateCsGoMatchTable(csgoCallFinishMatches.getFinishResults());
		csgoInsert.payoutCsgoMatches(csgoCallFinishMatches.getFinishResults());
	}

}
