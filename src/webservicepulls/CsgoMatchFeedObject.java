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
  private int statusOfGame;
  

  public CsgoMatchFeedObject(String title, String link, String description, String pubDate, String matchGameType) {
   this.matchtitle = title;
   this.matchlink = link;
   this.matchdescription = description;
   this.matchpubDate = pubDate;
   this.matchGameType = matchGameType;
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




	public int getStatusOfGame() {
		return statusOfGame;
	}




	public void setStatusOfGame(int statusOfGame) {
		this.statusOfGame = statusOfGame;
	}




	@Override
	public String toString() {
		return "CsgoMatchFeedObject [matchtitle=" + matchtitle + ", matchlink=" + matchlink + ", matchdescription="
				+ matchdescription + ", matchpubDate=" + matchpubDate + ", matchGameType=" + matchGameType + ", teamA="
				+ teamA + ", teamB=" + teamB + ", statusOfGame=" + statusOfGame + "]\n";
	}






	@Override
	

  
} 
