import Gateway.csgoSQLGateway;
import webservicepulls.csgoWebservice;

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
		//test print
		//System.out.println(csgoCall.getFeedResults().toString() + "\n");
		System.out.println("Store in database");
		csgoSQLGateway csgoInsert = new csgoSQLGateway();
		csgoInsert.insertMatch(csgoCall.getFeedResults());
		
	}

}
