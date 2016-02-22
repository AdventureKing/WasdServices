package webservicepulls;


import java.util.ArrayList;
import java.util.List;

/*
 * Stores an RSS feed
 */
public class CsgoMatchFeedObject {

 

final String matchtitle;
  final String matchlink;
  final String matchdescription;
  final String matchpubDate;
  final String matchGameType;
  private String teamA;
  private String teamB;
  private String statusOfGame;
  private String betCuttoff;
  private String matchWinner = "";
  

public CsgoMatchFeedObject(String title, String link, String description, String pubDate, String matchGameType,String teamA,String teamB, int statusOfGame) {
   this.matchtitle = title;
   this.matchlink = link;
   this.matchdescription = description;
   this.matchpubDate = pubDate;
   this.matchGameType = matchGameType;
   this.teamA = teamA;
   this.teamB = teamB;
   if(teamA == ""){
   String delims = "vs";
   String[] tokens = title.split(delims);
   //System.out.println("teamA: " + tokens[0] + "TeamB:" + tokens[1]);
   this.teamA = tokens[0];
   this.teamB = tokens[1];
   }
   if(statusOfGame == 0)
   this.statusOfGame = "Open";
   else
   this.statusOfGame = "Closed";  
  }



 
  public String getMatchtitle() {
		return matchtitle;
	}


	public String getMatchlink() {
		return matchlink;
	}


	public String getMatchdescription() {
		return matchdescription;
	}


	public String getMatchpubDate() {
		return matchpubDate;
	}


	public String getMatchGameType() {
		return matchGameType;
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

	  @Override
	public String toString() {
		return "CsgoMatchFeedObject [matchtitle=" + matchtitle + ", matchlink=" + matchlink + ", matchdescription="
				+ matchdescription + ", matchpubDate=" + matchpubDate + ", matchGameType=" + matchGameType + ", teamA="
				+ teamA + ", teamB=" + teamB + ", statusOfGame=" + statusOfGame + ", betCuttoff=" + betCuttoff + "]\n";
	}


} 
