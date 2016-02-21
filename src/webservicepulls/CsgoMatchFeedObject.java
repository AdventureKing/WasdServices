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




	@Override
	public String toString() {
		return "Feed [matchtitle=" + matchtitle + ", matchlink=" + matchlink + ", matchdescription=" + matchdescription
				+ ", matchpubDate=" + matchpubDate + ", matchGameType=" + matchGameType + "]";
	}

  
} 
