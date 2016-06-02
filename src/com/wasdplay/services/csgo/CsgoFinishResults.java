package com.wasdplay.services.csgo;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CsgoFinishResults {

	private SSLContext context = null;

	private ArrayList<CSGOMatch> FinishResults;

	public CsgoFinishResults() {
	}

	// get closed matches from csgoLounge
	public void csgoMatchFinishFeedCall() throws JSONException {
		setFinishResults(new ArrayList<CSGOMatch>());
		String urlRoot = "http://www.hltv.org";
		String response;
		URL wsURL;
		try {

			wsURL = new URL("http://www.hltv.org/matches/");
			String protocol = wsURL.getProtocol();
			if (!protocol.equalsIgnoreCase("https") && !protocol.equalsIgnoreCase("http"))
				throw new IllegalArgumentException("WS URL must be valid HTTP or HTTPS web resource");
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("WS URL must be valid HTTP or HTTPS web resource");
		}
		// url that has been filled
		URL tempURL = wsURL;
		try {
			// create a http request
			HttpURLConnection connection = null;

			if (tempURL.getProtocol().equalsIgnoreCase("https")) {
				// Create an SSL connection that that uses our SSL context
				connection = (HttpsURLConnection) tempURL.openConnection();
				((HttpsURLConnection) connection).setSSLSocketFactory(context.getSocketFactory());
			} else {
				connection = (HttpURLConnection) tempURL.openConnection();
			}
			// connection.setRequestMethod("POST");
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			// Send request
			connection.setDoOutput(true);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			java.io.InputStream in = connection.getInputStream();
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {

			}
			// read all bytes from open stream
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = in.read(buffer)) > 0) {
				out.write(buffer, 0, bytesRead);
			}
			out.close();
			// store all bytes read to the response buffer for document builder
			response = new String(out.toByteArray());

		} catch (FileNotFoundException e) {
			throw new RuntimeException("Web resource not found!");
		} catch (Exception e) {
			// chain the causing exception to a new RuntimeException
			throw new RuntimeException(e);
		}

		// System.out.println("Response is " + response);

		// use jsoup stuff here
		Document doc = Jsoup.parse(response);
		// iterate through all matchListBox divs (I think each one of these is a
		// match
		Elements matchDivs = doc.getElementsByClass("matchListBox");
		for (Element matchDiv : matchDivs) {
			// match id is inside a matchActionCell a now
			System.out.println(matchDiv);

			String detailsLink;
			Elements detailDivs = matchDiv.getElementsByClass("matchActionCell");
			if (detailDivs.size() != 1) {
				System.err.println("Found zero or too many Details");
				continue;
			}
			Element detailsDiv = detailDivs.first();
			Elements detailsATags = detailsDiv.getElementsByTag("a");
			if (detailsATags.size() != 1) {
				System.err.println("Found zero or too many Details A tags");
				continue;
			}
			Element detailsATag = detailsATags.first();
			detailsLink = detailsATag.attr("href");
			if(detailsLink == null)
				continue;
			
			long matchId = Long.parseLong(StringUtils.substringBetween(detailsLink, "/match/", "-"));
			// is match Finished?
			String overYN = "No";
			String winner = "";
			// look for matchTimeCell div
			// if its text is Finished then overYN is Yes
			Elements searchDivs = matchDiv.getElementsByClass("matchTimeCell");
			if (searchDivs.size() != 1) {
				System.err.println("Found zero or too many matchTimeCell entries for match id: " + matchId);
				continue;
			}
			Element searchDiv = searchDivs.first();
			if (searchDiv.text().equalsIgnoreCase("FINISHED")) {
				overYN = "Yes";
				// is team 1 the winner?
				searchDivs = matchDiv.getElementsByClass("matchScoreCell");
				if (searchDivs.size() != 1) {
					System.err.println("Found zero or too many matchScoreCell entries for match id: " + matchId);
					continue;
				}
				searchDiv = searchDivs.first();
				// now fetch its span elements (should be 2: team1 outcome is
				// 1st span element)
				searchDivs = searchDiv.getElementsByTag("span");
				if (searchDivs.size() != 2) {
					System.err.println(
							"Found too few or too many SPAN elements for matchScoreCell for match id: " + matchId);
					continue;
				}
				searchDiv = searchDivs.first();
				// if first span has resultWinner class then team1 is the winner
				if (searchDiv.hasClass("resultWinner"))
					winner = "team1";
				else if (searchDiv.hasClass("resultDraw"))
					winner = "draw";
				else
					winner = "team2";
			}
			// now find Team 1
			String team1Name = "";
			Elements teamDivs = matchDiv.getElementsByClass("matchTeam1Cell");
			if (teamDivs.size() != 1) {
				System.err.println("Found zero or too many Team 1 entries for match id: " + matchId);
				continue;
			}
			Element teamDiv = teamDivs.first();
			// get the a tag from team1Div (we want the non-span part of the
			// value)
			Elements teamATags = teamDiv.getElementsByTag("a");
			if (teamATags.size() != 1) {
				System.err.println("Found zero or too many Team 1 A tags for match id: " + matchId);
				continue;
			}
			Element teamATag = teamATags.first();
			team1Name = teamATag.text();
			String team2Name = "";
			teamDivs = matchDiv.getElementsByClass("matchTeam2Cell");
			if (teamDivs.size() != 1) {
				System.err.println("Found zero or too many Team 2 entries for match id: " + matchId);
				continue;
			}
			teamDiv = teamDivs.first();
			// get the a tag from team1Div (we want the non-span part of the
			// value)
			teamATags = teamDiv.getElementsByTag("a");
			if (teamATags.size() != 1) {
				System.err.println("Found zero or too many Team 2 A tags for match id: " + matchId);
				continue;
			}
			teamATag = teamATags.first();
			team2Name = teamATag.text();
			// get the match details link
			// got all the match info I want: matchId, team1Name, team2Name, and
			// Details link
			// so print it
			// System.out.println("Match Id: " + matchId + "\n\t" + team1Name +
			// " vs " + team2Name);
			// System.out.println("\tLink: " + urlRoot + detailsLink);
			// System.out.println("winner:" + winner);
			if (overYN.equals("Yes")) {
				if (winner.equals("team1")) {

					System.out.println("\t*** MATCH IS OVER! Winner is " +
					 team1Name);
					winner = team1Name;
					String matchTitle = team1Name + " vs " + team2Name;
					String matchLink = urlRoot + detailsLink;
					CSGOMatch tempObject = new CSGOMatch(matchTitle, matchLink, "", "", "",
							team1Name, team2Name, 1);
					// System.out.println(tempObject.getMatchWinner());
					tempObject.setMatchWinner(winner);
					//tempObject.toString();
					// System.out.println(tempObject.getMatchWinner());
					tempObject.setMatchIdFromSource(matchId);
					FinishResults.add(tempObject);
				} else if (winner.equals("team2")) {
					System.out.println("\t*** MATCH IS OVER! Winner is " +
					 team2Name);
					winner = team2Name;
					String matchTitle = team1Name + " vs " + team2Name;
					String matchLink = urlRoot + detailsLink;
					CSGOMatch tempObject = new CSGOMatch(matchTitle, matchLink, "", "", "",
							team1Name, team2Name, 1);
					// System.out.println(tempObject.getMatchWinner());
					tempObject.setMatchWinner(winner);
					//tempObject.toString();
					// System.out.println(tempObject.getMatchWinner());
					tempObject.setMatchIdFromSource(matchId);
					FinishResults.add(tempObject);
				} else if (winner.equals("draw")) {
					// System.out.println("\t*** MATCH IS OVER! No winner");
					winner = "draw";
					String matchTitle = team1Name + " vs " + team2Name;
					String matchLink = urlRoot + detailsLink;
					CSGOMatch tempObject = new CSGOMatch(matchTitle, matchLink, "", "", "",
							team1Name, team2Name, 1);
					// System.out.println(tempObject.getMatchWinner());
					tempObject.setMatchWinner("draw");
					//tempObject.toString();
					// System.out.println(tempObject.getMatchWinner());
					tempObject.setMatchIdFromSource(matchId);
					FinishResults.add(tempObject);
				} else {
					// System.out.println("\t*** MATCH IS OVER! Unknown
					// result!");
				}
			}
		}

	}

	public SSLContext getContext() {
		return context;
	}

	public ArrayList<CSGOMatch> getFinishResults() {
		return FinishResults;
	}

	public void setFinishResults(ArrayList<CSGOMatch> finishResults) {
		FinishResults = finishResults;
	}

}
