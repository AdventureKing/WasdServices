//import Gateway.csgoSQLGateway;
import webservicepulls.CsgoMatchFeedObject;
import webservicepulls.csgoWebservice;

import java.util.ArrayList;

import org.json.JSONException;

import Gateway.csgoSQLGateway;
public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Start webservice pull");
		csgoWebservice csgoCall = new csgoWebservice();
		try {
			csgoCall.hltvMatchFeedCall();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			csgoCall.csgoLoungeMatchFeedCall();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		csgoCall.getHltvgoMatchPageinFo();
		System.out.println("going into database: " + csgoCall.getHLTVGOFeedResults());
		System.out.print("\n\n\n\n\n\n\n\n\n\n");
		System.out.println("getting closed in database: " + csgoCall.getLoungeFinishResults().toString());
	
		csgoSQLGateway csgoInsert = new csgoSQLGateway();
        csgoInsert.insertMatch(csgoCall.getHLTVGOFeedResults());
        
        csgoInsert.updateCsGoMatchTable(csgoCall.getLoungeFinishResults());
	}

}
