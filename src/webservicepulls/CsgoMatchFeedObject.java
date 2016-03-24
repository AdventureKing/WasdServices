package webservicepulls;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
 * Stores an RSS feed
 */
public class CsgoMatchFeedObject {

	final String matchtitle;

	final String matchlink;
	private String matchEvent;
	private String matchpubDate;
	private String matchGameType;
	private String teamA;
	private String teamB;
	private String statusOfGame;
	private String betCuttoff;
	private Float team1Odds;

	private float team2Odds;
	private String matchWinner = "";
	private String streamLink = "";
	private long matchIdFromSource;

	public CsgoMatchFeedObject(String title, String link, String description, String pubDate, String matchGameType,
			String teamA, String teamB, int statusOfGame) {
		this.matchtitle = title;
		this.matchlink = link;
		this.matchEvent = description;
		this.matchpubDate = pubDate;
		this.matchGameType = matchGameType;
		this.teamA = teamA;
		this.teamB = teamB;
		if (teamA == "") {
			String delims = "vs";
			String[] tokens = title.split(delims);
			// System.out.println("teamA: " + tokens[0] + "TeamB:" + tokens[1]);
			this.teamA = tokens[0];
			this.teamB = tokens[1];
		}
		if (statusOfGame == 0)
			this.statusOfGame = "Open";
		else
			this.statusOfGame = "Closed";
	}

	public String getTeamA() {
		return teamA;
	}

	public void setTeamA(String teamA) {
		this.teamA = teamA;
	}

	public String getTeamB() {
		return teamB;
	}

	public void setTeamB(String teamB) {
		this.teamB = teamB;
	}

	public String getStatusOfGame() {
		return statusOfGame;
	}

	public void setStatusOfGame(String statusOfGame) {
		this.statusOfGame = statusOfGame;
	}

	public String getBetCuttoff() {
		return betCuttoff;
	}

	public void setBetCuttoff(String betCuttoff) {
		this.betCuttoff = betCuttoff;
	}

	public String getMatchWinner() {
		return matchWinner;
	}

	public void setMatchWinner(String matchWinner) {
		this.matchWinner = matchWinner;
	}

	public String getMatchtitle() {
		return matchtitle;
	}

	public String getMatchlink() {
		return matchlink;
	}

	public String getMatchEvent() {
		return matchEvent;
	}

	public Timestamp getMatchpubDateHLTVGOCONVERT() {
		String formatString = "EEE, dd MMM yyyy HH:mm:ss ZZZZ";
		DateFormat format = new SimpleDateFormat(formatString, Locale.ENGLISH);
		java.util.Date insertDate = null;

		try {
			insertDate = format.parse(matchpubDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Timestamp sqlDate = new Timestamp(insertDate.getTime());
		return sqlDate;
	}

	public String getMatchpubDate() {
		return matchpubDate;
	}

	public void setMatchEvent(String matchEvent) {
		this.matchEvent = matchEvent;
	}

	public String getMatchGameType() {
		return matchGameType;
	}

	@Override
	public String toString() {
		return "CsgoMatchFeedObject [matchtitle=" + matchtitle + "\n" + " matchlink=" + matchlink + "\n"
				+ ", matchEvent=" + matchEvent + "\n" + ", matchpubDate=" + matchpubDate + "\n" + ", matchGameType="
				+ matchGameType + "\n" + ", teamA=" + teamA + "\n" + ", teamB=" + teamB + "\n" + ", statusOfGame="
				+ statusOfGame + "\n" + ", betCuttoff=" + betCuttoff + "\n" + ", team1Odds=" + team1Odds + "\n"
				+ ", team2Odds=" + team2Odds + "\n" + ", matchWinner=" + matchWinner + "\n" + ", streamLink="
				+ streamLink + "\n" + "SourceID= " + matchIdFromSource + "\n" +"]";
	}

	public void setMatchGameType(String bestOf) {
		// TODO Auto-generated method stub
		this.matchGameType = bestOf;
	}

	public String getStreamLink() {
		return streamLink;
	}

	public void setStreamLink(String streamLink) {
		this.streamLink = streamLink;
	}

	public Float getTeam1Odds() {
		return team1Odds;
	}

	public void setTeam1Odds(float f) {
		this.team1Odds = f;
	}

	public Float getTeam2Odds() {
		return team2Odds;
	}

	public void setTeam2Odds(Float team2Odds) {
		this.team2Odds = team2Odds;
	}

	public long getMatchIdFromSource() {
		return matchIdFromSource;
	}

	public void setMatchIdFromSource(long matchIdFromSource) {
		this.matchIdFromSource = matchIdFromSource;
	}

}
