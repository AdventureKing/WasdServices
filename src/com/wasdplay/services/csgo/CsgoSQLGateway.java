package com.wasdplay.services.csgo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.wasdplay.services.backend.Gateway;
import com.wasdplay.services.game.Match;

public class CsgoSQLGateway<T extends Match> extends Gateway<T> {
	private MysqlDataSource ds = new MysqlDataSource();
	private Connection conn = null;

	public CsgoSQLGateway(boolean production) {
		setDsUrl(production ? "wasdplay" : "wasdplaySchool");
	}

	private void setDsUrl(String table) {
		ds.setURL("jdbc:mysql://wasdplay.cm7k6xsx1khr.us-west-2.rds.amazonaws.com:3306/" + table + "?user=Rod_S_B&password=Grap3R0d929!");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertMatches(List<T> matches) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			if (conn == null) {

				conn = ds.getConnection();

				System.out.println("connection made for AWS");
			}
			// create statment to push to database

			String sql = "INSERT INTO csgoMatchData SET matchEvent=?,matchType=?,gameTitle=?,gameTimeStart=?,betCutoff=?,team1=?,team2=?,team1Odds=?,team2Odds=?,betOpen=?,streamLink=?,isVisOnSite=?,sourceId=? ON DUPLICATE KEY UPDATE matchEvent=?,matchType=?,gameTitle=?,gameTimeStart=?,betCutoff=?,team1=?,team2=?,team1Odds=?,team2Odds=?,betOpen=?,streamLink=?,isVisOnSite=?,sourceId=?";
			stmt = (PreparedStatement) conn.prepareStatement(sql);

			// set autocommit to false
			conn.setAutoCommit(false);
			// set time out so no lag

			// put data into query
			for (CSGOMatch listObject : (List<CSGOMatch>)matches) {
				if(listObject.getTeamA().contains("No team") || listObject.getTeamB().contains("No team")){
					continue;
				} else {
					Timestamp startTime = listObject.getMatchpubDateHLTVGOCONVERT();
					stmt.setString(1, listObject.getMatchEvent());
					stmt.setString(2, listObject.getMatchGameType());
					stmt.setString(3, listObject.getMatchtitle());
					// matchtime start
					// need to convert to server area code
	
					stmt.setTimestamp(4, startTime);
					// stmt.setString(4, listObject.getMatchpubDate());
	
					// match time to start but 10 mins earlier
					// need to change to like 10 mins before it starts
					Timestamp cutofftime = listObject.getMatchpubDateHLTVGOCONVERT();
	
					Timestamp newtime = new Timestamp(cutofftime.getTime() - (6000 * 300));
	
					stmt.setTimestamp(5, newtime);
	
					// need to in object split name effectivly to get team 1
					stmt.setString(6, listObject.getTeamA());
					// need to in object split name effectivly to get team 2
					stmt.setString(7, listObject.getTeamB());
					// need to in object split name effectivly to get team 1 odds
					//System.out.println("Having ISSUES_________________");
					//System.out.println(listObject.toString());
					stmt.setFloat(8, listObject.getTeam1Odds());
					// need to in object split name effectivly to get team 2 odds
					stmt.setFloat(9, listObject.getTeam2Odds());
					// set bet as open
					stmt.setString(10, "Open");
					stmt.setString(11, listObject.getStreamLink());
					stmt.setBoolean(12, true);
					stmt.setLong(13, listObject.getMatchIdFromSource());
					// update statement
					stmt.setString(14, listObject.getMatchEvent());
					stmt.setString(15, listObject.getMatchGameType());
					stmt.setString(16, listObject.getMatchtitle());
					stmt.setTimestamp(17, startTime);
					stmt.setTimestamp(18, newtime);
					stmt.setString(19, listObject.getTeamA());
					stmt.setString(20, listObject.getTeamB());
					// need to in object split name effectivly to get team 1 odds
					stmt.setFloat(21, listObject.getTeam1Odds());
					// need to in object split name effectivly to get team 2 odds
					stmt.setFloat(22, listObject.getTeam2Odds());
					stmt.setString(23, "Open");
					stmt.setString(24, listObject.getStreamLink());
					stmt.setBoolean(25, true);
					stmt.setLong(26, listObject.getMatchIdFromSource());
					stmt.addBatch();
				}
			}

			int[] count = stmt.executeBatch();
			System.out.println("Inserted " + count.length + " records for csgoMatchTable");
			conn.commit();
			conn.setAutoCommit(true);
			// stmt.close();

		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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

	@SuppressWarnings("unchecked")
	@Override
	public void updateMatches(List<T> matches) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			if (conn == null) {

				conn = ds.getConnection();

				System.out.println("connection made for AWS");
			}
			// create statment to push to database
			// TODO: fix this statement to update the visible column in the db
			String sql = "UPDATE csgoMatchData SET isVisOnSite=?, matchWinner=?, betOpen=? WHERE sourceId=?";
			stmt = (PreparedStatement) conn.prepareStatement(sql);

			// set autocommit to false
			conn.setAutoCommit(false);
			// set time out so no lag

			// put data into query
			for (CSGOMatch listObject : (List<CSGOMatch>)matches) {
				stmt.setBoolean(1, false);
				stmt.setString(2, listObject.getMatchWinner());
				stmt.setString(3, "Closed");
				stmt.setLong(4,listObject.getMatchIdFromSource());
				
				stmt.addBatch();

			}

			int[] count = stmt.executeBatch();
			System.out.println("Updated " + count.length + " records for csgoMatchTable");
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
	
	public void payoutCsgoMatches(ArrayList<CSGOMatch> matchList){

		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			if (conn == null) {

				conn = ds.getConnection();

				System.out.println("connection made for AWS");
			}
			// create statment to push to database
			// TODO: fix this statement to update the visible column in the db
			

			// set autocommit to false
			conn.setAutoCommit(false);
			// set time out so no lag

			// put data into query
			for (CSGOMatch listObject : matchList) {
			//stmt.setLong(1, listObject.getMatchIdFromSource());
			String sql = "SELECT id,email,betAmount,matchTitle,teamPicked,matchId,matchOdds,PaidOut FROM csgoBetTable WHERE matchId="+listObject.getMatchIdFromSource();
			stmt = (PreparedStatement) conn.prepareStatement(sql);
			 rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				String emailToPay = rs.getString("email");
				Long betAmount = rs.getLong("betAmount");
				String matchTitle = rs.getString("matchTitle");
				String teamPicked = rs.getString("teamPicked");
				Long matchId = rs.getLong("matchId");
				Float matchOdds= rs.getFloat("email");
				Boolean PaidOut = rs.getBoolean("PaidOut");
				System.out.println("Stuff in the db: " + emailToPay + betAmount + matchTitle + teamPicked + matchId + matchOdds + PaidOut);
			}
			}
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
