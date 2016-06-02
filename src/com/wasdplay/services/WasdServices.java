package com.wasdplay.services;

import org.json.JSONException;

import com.wasdplay.services.csgo.CSGOMatch;
import com.wasdplay.services.csgo.CsgoFinishResults;
import com.wasdplay.services.csgo.CsgoSQLGateway;
import com.wasdplay.services.csgo.CsgoWebservice;

public class WasdServices {

	public static void main(String[] args) {
		System.out.println("Initializing WasdServices...");
		
		CsgoWebservice csgoCallGenerateMatches = new CsgoWebservice();
		CsgoFinishResults csgoCallFinishMatches = new CsgoFinishResults();
		try {
			csgoCallGenerateMatches.hltvMatchFeedCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			csgoCallFinishMatches.csgoMatchFinishFeedCall();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		csgoCallGenerateMatches.getHltvgoMatchPageinFo();
		System.out.println("going into database: " + csgoCallGenerateMatches.getHLTVGOFeedResults());
		System.out.println("getting closed in database: " + csgoCallFinishMatches.getFinishResults().toString());

		CsgoSQLGateway<CSGOMatch> prod = new CsgoSQLGateway<CSGOMatch>(true);
		prod.insertMatches(csgoCallGenerateMatches.getHLTVGOFeedResults());
		prod.updateMatches(csgoCallFinishMatches.getFinishResults());
		prod.payoutCsgoMatches(csgoCallFinishMatches.getFinishResults());

		CsgoSQLGateway<CSGOMatch> dev = new CsgoSQLGateway<CSGOMatch>(false);
		dev.insertMatches(csgoCallGenerateMatches.getHLTVGOFeedResults());
		dev.updateMatches(csgoCallFinishMatches.getFinishResults());
		dev.payoutCsgoMatches(csgoCallFinishMatches.getFinishResults());
	}

}
