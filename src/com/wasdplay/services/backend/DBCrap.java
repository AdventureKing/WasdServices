package com.wasdplay.services.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DBCrap {
	private static final SimpleDateFormat DB_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
	
	private DataSource ds;
	private Connection conn = null;

	private Random roller;
	
	public DBCrap() {
		roller = new Random();
		try {
			ds = this.setDsUrl();
			
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	public void getMatchSchema() throws SQLException {
		//PreparedStatement st = conn.prepareStatement("describe csgoMatchData");
		//PreparedStatement st = conn.prepareStatement("describe csgoBetTable");
		PreparedStatement st = conn.prepareStatement("describe users");
		ResultSet rs = st.executeQuery();
		//add each to list of people to return
		System.out.println("Field\tType");
		while(rs.next()) {
			System.out.print(rs.getString("Field"));
			System.out.println("\t" + rs.getString("Type"));
		}
		

		//st = conn.prepareStatement("update csgoMatchData set matchWinner = null where id = 435");
		//st.executeUpdate();
		st.close();
	}
	
	public void exploreBetTable() throws SQLException {
		PreparedStatement st = conn.prepareStatement("select * "
				+ " from csgoBetTable order by id");
		ResultSet rs = st.executeQuery();
		//add each to list of people to return
		System.out.println("id\temail\tbetAmount\tmatchTitle\tteamPicked"
				+ "\tmatchId\tmatchOdds\tPaidOut");
		while(rs.next()) {
			System.out.print(rs.getInt("id") + "\t");
			System.out.print(rs.getString("email") + "\t");
			System.out.print(rs.getString("betAmount") + "\t");
			System.out.print(rs.getString("matchTitle") + "\t");

			System.out.print(rs.getString("teamPicked") + "\t");
			System.out.print(rs.getString("matchId") + "\t");
			System.out.print(rs.getString("matchOdds") + "\t");
			System.out.print(rs.getString("PaidOut") + "\t");
			
			System.out.println();
		}
		st.close();
	}
	
	public void exploreMatchTable() throws SQLException {
		PreparedStatement st = conn.prepareStatement("select * "
				+ " from csgoMatchData order by id");
		ResultSet rs = st.executeQuery();
		//add each to list of people to return
		System.out.println("id\tsourceId\tmatchEvent\tmatchType\tgameTitle"
				+ "\tteam1\tteam2\tmatchWinner\tgameTimeStart\tbetCutoff"
				+ "\tbetOpen\tteam1Odds\tteam2Odds\tisVisOnSite\tfakeMatch"
				+ "\tstreamLink");
		while(rs.next()) {
			System.out.print(rs.getInt("id") + "\t");
			System.out.print(rs.getString("sourceId") + "\t");
			System.out.print(rs.getString("matchEvent") + "\t");
			System.out.print(rs.getString("matchType") + "\t");

			System.out.print(rs.getString("gameTitle") + "\t");
			System.out.print(rs.getString("team1") + "\t");
			System.out.print(rs.getString("team2") + "\t");
			System.out.print(rs.getString("matchWinner") + "\t");
			System.out.print(rs.getString("gameTimeStart") + "\t");
			System.out.print(rs.getString("betCutoff") + "\t");
			
			System.out.print(rs.getString("betOpen") + "\t");
			System.out.print(rs.getString("team1Odds") + "\t");
			System.out.print(rs.getString("team2Odds") + "\t");
			System.out.print(rs.getString("isVisOnSite") + "\t");
			System.out.print(rs.getString("fakeMatch") + "\t");
			
			System.out.print(rs.getString("streamLink") + "\t");
			System.out.println();
		}
		st.close();
	}
	
	/**
	 * insert a new random match
	 */
	private void testInsert() {
		String q = "";
		try {
			conn.setAutoCommit(false);

			q = "select max(CAST(sourceId AS UNSIGNED)) as max_test_id "
					+ " from csgoMatchData where matchEvent = 'TEST' ";
			PreparedStatement ps = conn.prepareStatement(q);
			ResultSet rs = ps.executeQuery();
			int nextTestId = 0;
			if(rs.first())
				nextTestId = rs.getInt("max_test_id") + 1;
			rs.close();
			
			//System.out.println(nextTestId);
			//if(true)
				//return;
			
			int winner = roller.nextInt(3);//0 = team 1, 1 = team 2, 2 = draw
			String winLbl = "DRAW";//set to '(w)' for winner, else 0
			//String team2WinLbl = "";
			int team1Odds = roller.nextInt(20) + 1;//range between 1 and 20
			int team2Odds = roller.nextInt(20) + 1;//range between 1 and 20
			if(winner == 0) {
				winLbl = "RED wins";
			} else if(winner == 1) {
				winLbl = "BLUE wins";
			}
			
			//calc gameTimeStart to client now() + 5 minutes 
			//and betCutoff to now() + 3 minutes
			
			Calendar date = Calendar.getInstance();
			long t= date.getTimeInMillis();
			
			//Date gameStart = new Date(t + (375  * ONE_MINUTE_IN_MILLIS));
			//Date betEnd = new Date(t + (360 * ONE_MINUTE_IN_MILLIS));
			Date gameStart = new Date(t + (15 * ONE_MINUTE_IN_MILLIS));
			Date betEnd = new Date(t + (10 * ONE_MINUTE_IN_MILLIS));
			
			//use odds to indicate who the winner will be for the test closing process
			q = "insert into csgoMatchData (matchEvent, matchType "
					+ " , gameTitle, gameTimeStart, betCutoff, team1, team2 "
					+ " , team1Odds, team2Odds, betOpen, streamLink, isVisOnSite "
					+ " , matchWinner, sourceId, fakeMatch) "
					+ " values('TEST', '" + winLbl + "' "
					+ " , 'TEST " + nextTestId + " : RED vs. BLUE' "
					+ " , '" + DB_DATETIME_FORMAT.format(gameStart) + "' "
					+ " , '" + DB_DATETIME_FORMAT.format(betEnd) + "' "
					+ " , 'RED', 'BLUE'"
					+ " , " + team1Odds + ", " + team2Odds + ", 'Open'"
					+ " , '', 1, 'RED', " + nextTestId + ", 1)";
			System.out.println(q);
			
			ps = conn.prepareStatement(q);
			ps.executeUpdate();
			
			ps.close();
			
			conn.commit();
			
			conn.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	private DataSource setDsUrl() { 
    	//ds.setURL(props.getProperty("MYSQL_DB_URL"));
		//ds.setUser(props.getProperty("MYSQL_DB_USERNAME"));
		//ds.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
		MysqlDataSource mysqlDS = new MysqlDataSource();
		mysqlDS.setURL(
				"jdbc:mysql://wasdplay.cm7k6xsx1khr.us-west-2.rds.amazonaws.com:3306/wasdplaySchool?user=Rod_S_B&password=Grap3R0d929!");
		return mysqlDS;
	}

	/**
	 * close test matches that are:
	 * 	matchEvent TEST
	 *  gameTimeStart < server time now() 
	 *  betOpen = "Open"
	 *  
	 *  first close matches where team1 wins and do payouts for team1 bets
	 *  next close matches where team2 wins and do payouts for team2 bets
	 *  lastly, close matches where no one wins 
	 *  
	 *  closeType: 0 close  
	 */
	public void testClose() {
		String q = "";
		try {
			Calendar date = Calendar.getInstance();
			long t= date.getTimeInMillis();
			
			Date now = new Date(t);

			//get matches that need to close
			q = "select id, team1, team2, team1Odds, team2Odds, matchType "
					+ " from csgoMatchData "
					+ " where matchEvent = 'TEST' "
					+ " and betOpen = 'Open' "
					+ " and gameTimeStart < '" + DB_DATETIME_FORMAT.format(now) + "' ";
			
			System.out.println(q);
			
			PreparedStatement ps = conn.prepareStatement(q);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {

				conn.setAutoCommit(false);

				int matchId = rs.getInt("id");
				String team1 = rs.getString("team1");
				String team2 = rs.getString("team2");
				String winningTeam = "";
				
				//if team 1 wins then payout bets that picked team 1
				PreparedStatement ps2 = null;
				PreparedStatement ps3 = null;
				ResultSet rs2 = null;
				
				double winOdds = 1d;
				double loseOdds = 1d;
				
				if(rs.getString("matchType").equals("RED wins")) {
					winningTeam = team1;
					winOdds = rs.getDouble("team1Odds");
					loseOdds = rs.getDouble("team2Odds");
				} else if(rs.getString("matchType").equals("BLUE wins")) {
					//else if team 2 wins then payout bets that picked team 2
					winningTeam = team2;
					winOdds = rs.getDouble("team2Odds");
					loseOdds = rs.getDouble("team1Odds");
				} else {
					//else draw so return bets to betters
					winOdds = 0;
					loseOdds = 1;
				}

				//get bets that picked the winner
				//note: draw returns everyone's money
				if(winOdds == 0) {
					q = "select * from csgoBetTable "
							+ " where matchId = " + matchId;
				} else {
					q = "select * from csgoBetTable "
						+ " where teamPicked = '" + winningTeam + "' "
						+ " and matchId = " + matchId;
				}
				ps3 = conn.prepareStatement(q);
				rs2 = ps3.executeQuery();
				while(rs2.next()) {
					//FIX THIS
					String userEmail = rs2.getString("email");
					double betAmount = 0d;
					try {
						betAmount = rs2.getDouble("betAmount");
					} catch(java.sql.SQLException e1) {
						//bet amount was invalid so output error and skip it
						System.err.println("Invalid bet amount: bet id " + rs2.getInt("id") + " bet amount: " + rs2.getString("betAmount"));
						continue;
					}
					
					//figure out how much to pay the user who won
					/*
					 * This calculator will convert "odds of winning" an event into a percentage chance of winning.  If odds are stated as an A to B chance of success then the probability of success is given as P = B / (A + B).  The percent chance of winning is calculated as P x 100.
					 * 
					 * I do amount bet x (1+percentage chance of winning) + total ammount of spacebucks = new total amount of spacebucks
					 * 
					 * 6-5 means you will get $6 profit for every $5 wagered. 20-1 means you get $20 profit for every $1 wagered (i.e. bet $2 and get $42 back).
					 * 
					 */
					Money payout = Money.dollars(betAmount * (1 + (winOdds / loseOdds))); 
					
					String qPayout = "update users "
							+ " set totalSpaceBucks = totalSpaceBucks + " + payout.amount().doubleValue()
							+ " where email = '" + userEmail + "' ";
					PreparedStatement psPayout = conn.prepareStatement(qPayout);
					psPayout.executeUpdate();
					psPayout.close();
				}
				
				//close out the bet
				q = "update csgoMatchData "
						+ " set betOpen = 'Closed' "
						+ " where id = " + matchId;
				
				System.out.println(q);
				
				ps3 = conn.prepareStatement(q);
				ps3.executeUpdate();
				
				conn.commit();

				conn.setAutoCommit(true);
			}
			rs.close();
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
				conn.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	public void close() {
		try {
			if(conn != null)
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	public void temp() throws SQLException {
		//PreparedStatement st = conn.prepareStatement("describe csgoMatchData");
		//PreparedStatement st = conn.prepareStatement("describe csgoBetTable");
		PreparedStatement st = conn.prepareStatement("delete from csgoMatchData "
				+ " where sourceId = 100 ");
		st.executeUpdate();
		st.close();
	}
	
	public static void main(String [] args) throws SQLException {
		DBCrap app = new DBCrap();
		
		//BEWARE!!!
		//app.temp();
		
		//app.getMatchSchema();
		
		//UNCOMMENT THIS IF YOU WANT TO INSERT NEW TEST MATCHES
		//app.testInsert();
		
		//UNCOMMENT THIS IF YOU WANT TO CLOSE FINISHED MATCHES
		//app.testClose();
		
		//app.exploreMatchTable();
		//app.exploreBetTable();
		
		app.close();
	}

}
