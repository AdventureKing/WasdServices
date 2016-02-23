//import Gateway.csgoSQLGateway;
import webservicepulls.CsgoMatchFeedObject;
import webservicepulls.csgoWebservice;

import java.util.ArrayList;

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
		csgoCall.csgoLoungeMatchFeedCall();
		System.out.println(csgoCall.getHLTVGOFeedResults());
		//System.out.println(csgoCall.getLoungeFinishResults().toString());
		
		/*try {
			Thread.sleep(10000);
			System.out.println("Go to sleep");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		for(CsgoMatchFeedObject tempobject : csgoCall.getHLTVGOFeedResults()){
			
		
		System.out.println("New Times: " + tempobject.getMatchpubDateHLTVGOCONVERT());
		}
		csgoSQLGateway csgoInsert = new csgoSQLGateway();
        csgoInsert.insertMatch(csgoCall.getHLTVGOFeedResults());
	
	}

}
