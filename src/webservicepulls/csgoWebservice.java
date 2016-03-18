package webservicepulls;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.omg.CORBA_2_3.portable.InputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.CharacterData;

public class csgoWebservice {

	public SSLContext getContext() {
		return context;
	}

	private SSLContext context = null;
	private ArrayList<CsgoMatchFeedObject> feedResults;
	private ArrayList<CsgoMatchFeedObject> loungeFinishResults;

	public csgoWebservice() {
	}

	public void csgoLoungeMatchFeedCall() throws JSONException {
		loungeFinishResults = new ArrayList<CsgoMatchFeedObject>();
		String response;
		URL wsURL;
		try {

			wsURL = new URL("http://csgolounge.com/api/matches");
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
			//set browser and request methods
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
		// JSONObject jsnobject = new JSONObject(response);
		JSONArray jsonArray = new JSONArray(response);

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject explrObject = jsonArray.getJSONObject(i);
			String delims = "-";
			String[] tokens = explrObject.get("when").toString().split(delims);
			// System.out.println("teamA: " + tokens[0] + "TeamB:" + tokens[1]);
			String year = tokens[0];
			String month = tokens[1];
			// System.out.println(month);
			int yearActual = Calendar.getInstance().get(Calendar.YEAR);
			int monthActual = Calendar.getInstance().get((Calendar.MONTH)) + 1;
			// System.out.println(monthActual);
			if (Integer.parseInt(year) == yearActual && Integer.parseInt(month) == monthActual
					&& !explrObject.get("winner").toString().equals("")) {

				// System.out.println(explrObject.get("event").toString());

				// need to convert time stamp to match time stamp that will be
				// in db
				String timeWhen = explrObject.get("when").toString();
				String matchEvent = explrObject.getString("event").toString();
				String teamA = explrObject.getString("a").toString();
				String teamB = explrObject.getString("b").toString();
				String matchTitle = teamA + " vs " + teamB;
				// System.out.println(matchTitle);
				String matchWinner = explrObject.getString("winner").toString();
				if (matchWinner.equals("a")) {
					matchWinner = teamA;
				} else if (matchWinner.equals("b")) {
					matchWinner = teamB;
				} else {
					matchWinner = "Closed";
				}
				CsgoMatchFeedObject tempObject = new CsgoMatchFeedObject(matchTitle, "", matchEvent, timeWhen, "",
						teamA, teamB, 1);
				tempObject.setMatchWinner(matchWinner);
				loungeFinishResults.add(tempObject);
			}

		}

	}

	public ArrayList<CsgoMatchFeedObject> getLoungeFinishResults() {
		return loungeFinishResults;
	}

	public void setLoungeFinishResults(ArrayList<CsgoMatchFeedObject> loungeFinishResults) {
		this.loungeFinishResults = loungeFinishResults;
	}

	public void hltvMatchFeedCall() {

		feedResults = new ArrayList<CsgoMatchFeedObject>();
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

			CsgoMatchFeedObject tempObject = new CsgoMatchFeedObject(tempName, templink, tempdes, tempPubDate,
					tempgameType, "", "", 0);
			feedResults.add(tempObject);
		}

	}

	public void getHltvgoMatchPageinFo() {

		String response;
		URL wsURL;
		int i = 0;
		for (CsgoMatchFeedObject feedObject : feedResults) {
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

			String Link = StringUtils.substringBetween(response, "<div id=\"mapformatbox\">", "</div>");
			String watchCatagory = StringUtils.substringBetween(response,
					"<div style=\"float:right;position:relative;\"><a href=\"", "<img src=");
			String team1Odds = StringUtils.substringBetween(response,
					"<td style=\"text-align: right;\" id=\"voteteam1results\">", "</td>");
			String team2Odds = StringUtils.substringBetween(response,
					"<td style=\"text-align: right;\" id=\"voteteam2results\">", "</td>");
			// if i was able to pull the match
			if (watchCatagory != null) {
				watchCatagory = watchCatagory.replaceAll("amp;", "");
				watchCatagory = watchCatagory.replaceAll("\">", "");
			}
			// add tag
			Link = "<br>" + Link;
			 //System.out.println(Link);
			String streamLink = StringUtils.substringBetween(Link, "<a href=\"", "\">English stream</a>");
			// set stream link
			//System.out.println(watchCatagory);
			
				if (watchCatagory != null) {
					watchCatagory = "http://www.hltv.org/" + watchCatagory;
					watchCatagory = getStreamFromEmbeded(watchCatagory);
					feedObject.setStreamLink(watchCatagory);
				} else {
					if(streamLink != null){
						feedObject.setStreamLink(streamLink);
					}else
					feedObject.setStreamLink("No Stream As of Yet Check Back Later");
				}
		
				
			
			// sejt best of
			String bestOf = StringUtils.substringBetween(Link, "<br>", "<br />");
			if (bestOf == null) {
				bestOf = StringUtils.substringBetween(Link, "<br>",
						"<div style=\"border-top: 1px solid darkgray;margin-bottom: 3px;margin-top:3px;\">");
			}
			bestOf = bestOf.replaceAll("[^a-zA-Z0-9]", "");
			// System.out.println("WHAT IS IT " +bestOf);
			feedObject.setMatchGameType(bestOf);

			// set odds
			if (team1Odds == null) {
				feedObject.setTeam1Odds((float) 50);
				feedObject.setTeam2Odds((float) 50);
			} else {
				team1Odds = team1Odds.replaceAll(" ", "");
				team1Odds = team1Odds.replaceAll("%", "");
				// System.out.println("Match");
				// System.out.println(team1Odds);
				team2Odds = team2Odds.replaceAll(" ", "");
				team2Odds = team2Odds.replaceAll("%", "");
				// System.out.println(team2Odds);
				if (team1Odds == "-") {
					feedObject.setTeam1Odds((float) 50);
					feedObject.setTeam2Odds((float) 50);
				} else {

					feedObject.setTeam1Odds(Float.parseFloat(team1Odds));
					feedObject.setTeam2Odds(Float.parseFloat(team2Odds));
				}
			}
		}

	}
//getting the stream from the hltv page because it was embeded as a hlgo match very frustrating
	private String getStreamFromEmbeded(String watchCatagory) {
		// TODO Auto-generated method stub
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
		String streamLink = StringUtils.substringBetween(response, "<iframe src=\"", "\" frameborder=\"0\"");
		if(streamLink == null){
			//System.out.println("fucked");
			String tempLink = StringUtils.substringBetween(response, "<iframe", ">");
			tempLink = StringUtils.substringBetween(tempLink, "src=\"", "\"");
			//System.out.println(tempLink);
			streamLink = tempLink;
		}
		// System.out.println(streamLink);
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

	public ArrayList<CsgoMatchFeedObject> getHLTVGOFeedResults() {
		return feedResults;
	}

	public void setFeedResults(ArrayList<CsgoMatchFeedObject> feedResults) {
		this.feedResults = feedResults;
	}
}
