
//import Gateway.csgoSQLGateway;
import webservicepulls.CsgoMatchFeedObject;
import webservicepulls.csgoFinishResults;
import webservicepulls.csgoWebservice;

import java.util.ArrayList;

import org.json.JSONException;

import Gateway.csgoSQLGateway;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Start webservice pull");
		csgoWebservice csgoCallGenerateMatches = new csgoWebservice();
		csgoFinishResults csgoCallFinishMatches = new csgoFinishResults();
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
		//System.out.println("going into database: " + csgoCallGenerateMatches.getHLTVGOFeedResults());
		//System.out.print("\n\n\n\n\n\n\n\n\n\n");
		//System.out.println("getting closed in database: " + csgoCallFinishMatches.getFinishResults().toString());

		//csgoSQLGateway csgoInsert = new csgoSQLGateway();
		//csgoInsert.insertMatch(csgoCallGenerateMatches.getHLTVGOFeedResults());

		//csgoInsert.updateCsGoMatchTable(csgoCallFinishMatches.getFinishResults());
	}

}
