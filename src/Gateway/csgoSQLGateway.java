package Gateway;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import webservicepulls.CsgoMatchFeedObject;

public class csgoSQLGateway {

	
	MysqlDataSource ds= new MysqlDataSource();
	Connection conn = null;
	private ArrayList<CsgoMatchFeedObject> matchList;
	
	
	
	void setDsUrl(){
		ds.setURL("jdbc:mysql://wasdplay.cm7k6xsx1khr.us-west-2.rds.amazonaws.com:3306/wasdplaySchool");
	}
	void setDsUser(){
		ds.setUser("Rod_S_B");
	}
	void setDsPassword(){
		ds.setPassword("Grap3R0d929!");
	}
	public csgoSQLGateway(){
		setDsUrl();
		setDsUser();
		setDsPassword();
		setDsName();
	}
	
	private void setDsName() {
		// TODO Auto-generated method stub
		//ds.setDatabaseName("wasdplaySchool");
	}
	public void insertMatch(ArrayList<CsgoMatchFeedObject> matchList){
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			if(conn == null)
			{
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			System.out.println("connection made for AWS");
			}
			// create statment to push to database
	
			String sql = "INSERT IGNORE INTO csgoMatchData (matchDescription,matchType,gameTitle,gameTimeStart,betCutoff,team1,team2,team1Odds,team2Odds) VALUES (?,?,?,?,?,?,?,?,?)";
			stmt = (PreparedStatement) conn.prepareStatement(sql);
			
			//set time out so no lag
			
	
			// put data into query
			for(CsgoMatchFeedObject listObject: matchList){
				stmt.setString(1, listObject.getMatchdescription());
				stmt.setString(2, listObject.getMatchGameType());
				stmt.setString(3, listObject.getMatchtitle());
				//matchtime start
				//need to convert to server area code
				stmt.setString(4, listObject.getMatchpubDate());
				
				//match time to start but 10 mins earlier
				//need to change to like 10 mins before it starts
				stmt.setString(5, listObject.getMatchpubDate());
				
				//need to in object split name effectivly to get team 1
				stmt.setString(6, listObject.getMatchtitle());
				//need to in object split name effectivly to get team 2
				stmt.setString(7, listObject.getMatchtitle());
				//need to in object split name effectivly to get team 1 odds
				stmt.setInt(8, 50);
				//need to in object split name effectivly to get team 2 odds
				stmt.setInt(9, 50);
				stmt.addBatch();
			}
			stmt.executeBatch();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			
			// close rs
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			// close stmt
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			// close conn
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
					
					conn = null;
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("Connection terminated");
		
	}
	
	
}
