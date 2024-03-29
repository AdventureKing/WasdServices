package com.wasdplay.services.csgo;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.CharacterData;

public class CsgoWebservice {

	public SSLContext getContext() {
		return context;
	}

	private SSLContext context = null;
	private ArrayList<CSGOMatch> feedResults;
	private ArrayList<CSGOMatch> FinishResults;

	public CsgoWebservice() {
	}

	// get newest matchs from hltv
	public void hltvMatchFeedCall() {

		feedResults = new ArrayList<CSGOMatch>();
		String response;
		URL wsURL;
		try {

			wsURL = new URL("http://www.hltv.org/hltv.rss.php");
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

		// test print the reesponse
		// System.out.println(response);
		// build a xml document
		DocumentBuilder db = null;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// pipe data from
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(response));

		Document doc = null;
		// build doc for parsing
		try {
			doc = db.parse(is);
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// name of segment i need to look at in case of hltv all things are
		// under the item tag
		NodeList nodes = doc.getElementsByTagName("item");

		for (int i = 0; i < nodes.getLength(); i++) {
			// pull fields and store in objects and put in array list
			Element element = (Element) nodes.item(i);

			NodeList name = element.getElementsByTagName("title");

			Element line = (Element) name.item(0);
			String tempName = getCharacterDataFromElement(line);

			NodeList link = element.getElementsByTagName("link");
			line = (Element) link.item(0);
			String templink = getCharacterDataFromElement(line);

			NodeList des = element.getElementsByTagName("description");
			line = (Element) des.item(0);
			String tempdes = getCharacterDataFromElement(line);

			NodeList pubDate = element.getElementsByTagName("pubDate");
			line = (Element) pubDate.item(0);
			String tempPubDate = getCharacterDataFromElement(line);

			NodeList gametype = element.getElementsByTagName("gametype");
			line = (Element) gametype.item(0);
			String tempgameType = getCharacterDataFromElement(line);

			CSGOMatch tempObject = new CSGOMatch(tempName, templink, tempdes, tempPubDate,
					tempgameType, "", "", 0);
			feedResults.add(tempObject);
		}

	}

	// scrape match page info for stream and for matchEvent
	// need to get link to stream
	// need to get odds
	// need to get matchType = best of
	// need to get match id from provider
	public void getHltvgoMatchPageinFo() {

		String response;
		URL wsURL;
		int i = 0;
		for (CSGOMatch feedObject : feedResults) {
			try {

				wsURL = new URL(feedResults.get(i).matchlink);
				i++;
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
				// store all bytes read to the response buffer for document
				// builder
				response = new String(out.toByteArray());

			} catch (FileNotFoundException e) {
				throw new RuntimeException("Web resource not found!");
			} catch (Exception e) {
				// chain the causing exception to a new RuntimeException
				throw new RuntimeException(e);
			}
			// System.out.println(response);
			org.jsoup.nodes.Document doc = Jsoup.parse(response);
			// get the second iframe which is not the special one but the one i
			// can use due to legal issues with the first one
			
			/*
			 * Get match id
			 */
			String TempmatchId = StringUtils.substringBetween(feedObject.matchlink, "http://www.hltv.org/match/", "-");
			Long matchId = Long.valueOf(TempmatchId);
			
			//System.out.println("Match id: " + matchId);

			/*
			 * test print the match link
			 */
		//	System.out.println("MatchLink:" + feedObject.matchlink);
			/*
			 * Get match type best of need to trim and check but working so far
			 */
			org.jsoup.nodes.Element matchGameType = doc.getElementById("mapformatbox");
			
			String matchGameType_value;
			if(matchGameType.text().isEmpty()){
				matchGameType_value = "not yet determined";
			}else{
				String[] matchGameType_temp = matchGameType.text().split(" ");
				 matchGameType_value = matchGameType_temp[0] + " " + matchGameType_temp[1] + " "+ matchGameType_temp[2];
			}
			
			//System.out.println("MatchGameType: " + matchGameType_value);
			/*
			 * Get Stream Link THIS IS NOT WORKING
			 */
			org.jsoup.select.Elements streamLinkForParse = doc.getElementsByClass("hotmatchroundbox");
			//System.out.println(streamLinkForParse.size());
			org.jsoup.select.Elements streamLinkTemp = null;
			streamLinkTemp = streamLinkForParse.get(0).getElementsByTag("a");
			String streamLinkTemp1 = null;
			String streamLinkTemp2 = null;
			org.jsoup.select.Elements otherStream = matchGameType.getElementsByTag("a");
			String otherStreamChoice = null;
			if(!otherStream.text().isEmpty()){

			otherStreamChoice = otherStream.get(0).attr("href");
			//System.out.println(otherStreamChoice);
			}
			if (!streamLinkTemp.isEmpty() ) {
				streamLinkTemp1 = streamLinkTemp.get(0).attr("href");
				if (streamLinkTemp1 == null) {

				} else {
					//System.out.println("At this point is streamLinkTemp1 is : " + streamLinkTemp1);
					streamLinkTemp2 = null;
					try{
					streamLinkTemp2 = getStreamFromEmbeded("http://www.hltv.org" + streamLinkTemp1);
					}catch (Exception e){
						System.out.println("trouble pulling stream for matchId" + matchId);
						
					}
					if (streamLinkTemp2 == null && otherStreamChoice == null) {
						streamLinkTemp2 = "No Stream as of yet please check back later";
					}else if(otherStreamChoice != null){
						streamLinkTemp2 = otherStreamChoice;
					}
				}
			} else {
				streamLinkTemp2 = "No Stream as of yet please check back later";
			}

			//System.out.println("Stream Link" + streamLinkTemp2);

			/*
			 * Get Team 1 and Team 2 odds THIS IS WORKING
			 */
			org.jsoup.nodes.Element teamOdds1temp = doc.getElementById("voteteam1results");
			org.jsoup.nodes.Element teamOdds2temp = doc.getElementById("voteteam2results");
			String team1Odds = null;
			String team2Odds = null;

			if (teamOdds2temp == null || teamOdds1temp == null || teamOdds2temp.equals("-")
					|| teamOdds1temp.equals("-")) {
				team1Odds = "50";
				team2Odds = "50";
			} else {
				team1Odds = teamOdds1temp.text().replaceAll("%", "");
				team2Odds = teamOdds2temp.text().replaceAll("%", "");
			}

			//System.out.println("team 1 odds: " + team1Odds);
			//System.out.println("team 2 odds: " + team2Odds);
			//System.out.println("\n");
			
			//set all the data
			feedObject.setMatchIdFromSource(matchId);
			feedObject.setMatchGameType(matchGameType_value);
			//feedObject.setMatchEvent(matchEvent);
			feedObject.setTeam1Odds(Float.parseFloat(team1Odds));
			feedObject.setTeam2Odds(Float.parseFloat(team2Odds));
			feedObject.setStreamLink(streamLinkTemp2);
		}
	}

	// getting the stream from the hltv page because it was embeded as a hlgo
	// match very frustrating
	private String getStreamFromEmbeded(String watchCatagory) {
		// TODO Auto-generated method stub
		// System.out.println("HIt Here");
		String response;

		URL tempURL = null;
		try {
			tempURL = new URL(watchCatagory);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
		// System.out.println(response);
		String streamLink = null;
		org.jsoup.nodes.Document doc = Jsoup.parse(response);
		// get the second iframe which is not the special one but the one i can
		// use due to legal issues with the first one
		org.jsoup.select.Elements divs = doc.getElementsByTag("iframe");
		int ntmAmount = divs.size();
		org.jsoup.nodes.Element matchDivs = null;
		// System.out.println("Number of iframes: " + ntmAmount);
		if (ntmAmount == 1) {
			matchDivs = doc.select("iframe").get(0);
		} else if (ntmAmount == 2) {
			org.jsoup.nodes.Element srcCheck = null;
			srcCheck = matchDivs.getElementById("google_video_inner_static_iframe");
			if(!srcCheck.equals(null)){
				matchDivs = doc.select("iframe").get(0);
			}else{
				matchDivs = doc.select("iframe").get(1);
			}
		} else {
			org.jsoup.select.Elements lastCheck = doc.getElementsByClass("player-channel-status ng-binding");
			String finalCheck = lastCheck.get(0).attr("href");
			if(finalCheck.isEmpty()){
			System.out.println(
					"didnt find anything your in trouble line 390:searching iframes csgoWebservice.java for watchCatagory: "
							+ watchCatagory);
			}
			else{
				System.out.println("FINAL CHECK : " + finalCheck);
				return finalCheck;
			}
		}
		String tempStr = matchDivs.attr("src");
		if (tempStr.length() == 0) {
			org.jsoup.select.Elements lastCheck = doc.getElementsByClass("player-channel-status ng-binding");
			String finalCheck = lastCheck.get(0).attr("href");
			if(finalCheck.isEmpty()){
			System.err.println(
					"Cannot find match Stream on the watchCatagory page we dun goofed line 384 csgoWebservice.java");
			}else{
				System.out.println("FINAL CHECK:" + finalCheck);
				return finalCheck;
			}
		}
		// System.out.println(tempStr);
		streamLink = tempStr;
		return streamLink;
	}

	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}

	public void setContext(SSLContext context) {
		this.context = context;
	}

	public ArrayList<CSGOMatch> getHLTVGOFeedResults() {
		return feedResults;
	}

	public void setFeedResults(ArrayList<CSGOMatch> feedResults) {
		this.feedResults = feedResults;
	}

	public ArrayList<CSGOMatch> getFinishResults() {
		return FinishResults;
	}

	public void setsFinishResults(ArrayList<CSGOMatch> loungeFinishResults) {
		this.FinishResults = loungeFinishResults;
	}
}
