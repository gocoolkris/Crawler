package edu.upenn.cis455.httpclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import edu.upenn.cis455.components.GlobalData;

public class HttpClient {

	private String url, hostname,documenttype;
	private StringBuffer document,header,robots;
	private InetAddress ip;
	private Socket socket;
	private String file;
	private PrintWriter writer;
	private BufferedReader reader;
	private boolean bodycontent, isLocal,isdisallowed, hasCrawlDelay;
	private boolean isModified, redirected;
	private long filesize, sizelimit;
	private int crawldelay;
	private final long MB_IN_BYTES = 1024 * 1024;
	private final int TIMEOUT = 5000;

	/**
	 * @param URL - the URL of the document that is to be
	 * extracted.
	 */

	public HttpClient(String URL){
		url = URL.trim();
		sizelimit = GlobalData.fileSizeLimitInMb * MB_IN_BYTES;
		document = new StringBuffer();
		header = new StringBuffer();
		robots = new StringBuffer();
		initialize();
	}

	public boolean hasCrawlDelay(){
		return hasCrawlDelay;
	}
	public int getCrawlDelay(){
		return this.crawldelay;
	}
	
	public String getHeadRequestResponse(){
		try{
			socket = new Socket(ip, 80);
			socket.setSoTimeout(TIMEOUT);
			writer = new PrintWriter(socket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.sendHeadRequest();
			this.readHeadRequestResponse();
			this.parseParameters();
			return header.toString();
		}catch(Exception e){}
		finally{
			try{
				writer.close();
				reader.close();
				socket.close();
			}catch(Exception e){}
		}
		return null;
	}



	public void sendHeadRequest(){
		writer.print("HEAD " + file + " HTTP/1.0\r\n");
		writer.print("Host: " + hostname + "\r\n");
		writer.print("User-Agent: cis455crawler\r\n");
		writer.print("Accept: text/html,text/xml,application/xml\r\n");
		writer.print("Accept-Charset: utf-8\r\n");
		writer.print("Accept-Encoding:gzip, deflate\r\n");
		writer.print("Accept-Language:en-US\r\n");
		writer.print("Connection:close\r\n\r\n");
		writer.flush();
	}

	public boolean checkIfFileModifiedSince(Date d){
		try{
			socket = new Socket(ip, 80);
			socket.setSoTimeout(TIMEOUT);
			writer = new PrintWriter(socket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			sendIfModifiedSinceRequest(d);
			readIfModifiedSinceResponse();
			parseParameters();
			return isModified;
		}catch(Exception e){}
		finally{
			try{
				writer.close();
				reader.close();
				socket.close();
			}catch(Exception e){}
		}
		return false;
	}

	private void sendIfModifiedSinceRequest(Date d){
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String lastFetchedDate = dateFormat.format(d);
		writer.print("HEAD " + file + " HTTP/1.0\r\n");
		writer.print("Host: " + hostname + "\r\n");
		writer.print("User-Agent: cis455crawler\r\n");
		writer.print("If-Modified-Since : " + lastFetchedDate + "\r\n");
		writer.print("Accept: text/html,text/xml,application/xml\r\n");
		writer.print("Accept-Charset: utf-8\r\n");
		writer.print("Accept-Encoding:gzip, deflate\r\n");
		writer.print("Accept-Language:en-US\r\n");
		writer.print("Connection:close\r\n\r\n");
		writer.flush();
	}

	private void readIfModifiedSinceResponse(){
		try {
			String line = reader.readLine();
			if(line.toUpperCase().contains("200 OK")){
				isModified = true;
			}
			while((line = reader.readLine())!= null){
				header.append(line + "\n");
			}
		} catch (IOException e) {}
	}



	public void readHeadRequestResponse(){
		try {
			String line = reader.readLine();
			if(line.toUpperCase().contains("200 OK")){
				while((line = reader.readLine())!= null){
					header.append(line + "\n");
				}
			}
			else if(line.toUpperCase().contains("301 MOVED PERMANENTLY")){
				while((line = reader.readLine()) != null){
					if(line.trim().toLowerCase().startsWith("location")){
						url = line.substring(line.indexOf(':') + 1).trim();
						GlobalData.urlQueue.enqueueUrl(url);
						redirected = true;
					}
				}
			}
			reader.reset();
		} catch (IOException e) {}
	}

	
	/**
	 * parses the content-type, content-length 
	 * and filesize.
	 */
	private void parseParameters(){
		if(header != null){
			String[] headers = header.toString().split("\n");
			for(String h : headers){
				if(h.trim().toLowerCase().startsWith("content-type")){
					documenttype = h.substring(h.indexOf(':') + 1);
				}
				else if(h.trim().toLowerCase().startsWith("content-length")){
					try{
						String size = h.substring(h.indexOf(':') + 1).trim();
						filesize = Long.parseLong(size);
					}
					catch(Exception e){}
				}
			}
		}
	}

	public boolean isFileMimeTypeAcceptable(){

		if(documenttype == null) return false;
		String type = documenttype.toString();
		String[] types = type.split(";");
		for(String t : types){
			if(t.trim().equalsIgnoreCase("text/html") ||
					t.trim().equalsIgnoreCase("application/xml"))
				return true;
			else if(t.toLowerCase().trim().endsWith("xml")){
				return true;
			}
		}
		return false;
	}

	public boolean doesFileExceedLimit(){
		return filesize > sizelimit;
	}

	public boolean isDisallowed(){
		try{
			socket = new Socket(ip, 80);
			socket.setSoTimeout(TIMEOUT);
			writer = new PrintWriter(socket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			sendRequestForRobots();
			readRobotRequestResponse();
			checkForForbiddenAccess();
		}catch(Exception e){}
		finally{
			try{
				writer.close();
				reader.close();
				socket.close();
			}catch(Exception e){}
		}
		return isdisallowed;
	}

	private void checkForForbiddenAccess() {
		String[] lines = robots.toString().split("\n");
		boolean directives = false;
		if(robots.toString().toLowerCase().contains("user-agent: cis455crawler")){
			for(String line : lines){
				if(directives){
					if(!isBlankLine(line)){
						if(line.toLowerCase().startsWith("disallow")){
							String fpath = line.substring(line.indexOf(':') + 1).trim();
							if(fpath.contains("#")) fpath = fpath.substring(0,fpath.indexOf('#'));
							fpath = fpath.trim();
							String nUrl = url;
							if(fpath.endsWith("/"))	nUrl = url + "/";
							if(nUrl.contains(fpath)) isdisallowed = true;
						}
						else if(line.toLowerCase().startsWith("crawl-delay")){
							hasCrawlDelay = true;
							parseCrawlDelay(line);
						}
					}
					else break;
				}
				if(line.trim().toLowerCase().startsWith("user-agent: cis455crawler")){
					directives = true;
				}
			}
		}
		else{
			for(String line : lines){
				if(directives){
					if(!isBlankLine(line)){
						if(line.toLowerCase().startsWith("disallow")){
							String fpath = line.substring(line.indexOf(':') + 1).trim();
							if(fpath.contains("#")) fpath = fpath.substring(0,fpath.indexOf('#'));
							fpath = fpath.trim();
							String nUrl = url;
							if(fpath.endsWith("/"))	nUrl = url + "/";
							if(nUrl.contains(fpath)) isdisallowed = true;
						}
						else if(line.toLowerCase().startsWith("crawl-delay")){
							hasCrawlDelay = true;
							parseCrawlDelay(line);
						}
					}
					else break;
				}
				if(line.trim().toLowerCase().startsWith("user-agent: *")){
					directives = true;
				}
			}
		}
	}

	private void parseCrawlDelay(String line) {
		String delay = line.substring(line.indexOf(':') + 1).trim();
		try{
			crawldelay = Integer.parseInt(delay);
		}catch(Exception e){}

	}

	private void sendRequestForRobots(){
		writer.print("GET /robots.txt" + " HTTP/1.0\r\n");
		writer.print("Host: " + hostname + "\r\n");
		writer.print("User-Agent: cis455crawler\r\n");
		writer.print("Accept: text/html\r\n");
		writer.print("Accept-Charset: utf-8\r\n");
		writer.print("Accept-Encoding:gzip, deflate\r\n");
		writer.print("Accept-Language:en-US\r\n");
		writer.print("Connection:close\r\n\r\n");
		writer.flush();
	}

	private void readRobotRequestResponse(){
		try{

			String line = reader.readLine();
			if(line.toUpperCase().contains("200 OK")){
				while((line = reader.readLine())!= null){
					if(!bodycontent){
						if(isBlankLine(line)){
							bodycontent = true;
						}
					}
					else{
						robots.append(line + "\n");
					}
				}
			}
			bodycontent = false;
		}catch(Exception e){}
	}



	private void initialize() {
		try{
			if(url.startsWith("/")){
				isLocal = true;
				reader = new BufferedReader(new FileReader(url));
			}
			else{
				if(!url.toLowerCase().startsWith("http")){
					url = "http://" + url;
				}
				URL Url = new URL(url);
				hostname = Url.getHost();
				file = Url.getFile();
				ip = InetAddress.getByName(hostname);
			}
		}
		catch(Exception e){}
	}

	private void sendRequest(){
		try{

			writer.print("GET " + file + " HTTP/1.0\r\n");
			writer.print("Host: " + hostname + "\r\n");
			writer.print("User-Agent: cis455crawler\r\n");
			writer.print("Accept: text/html\r\n");
			writer.print("Accept-Charset: utf-8\r\n");
			writer.print("Accept-Encoding:gzip, deflate\r\n");
			writer.print("Accept-Language:en-US\r\n");
			writer.print("Connection:close\r\n\r\n");
			writer.flush();
		}
		catch(Exception e){}
	}

	private void readResponse(){
		try{
			String header = reader.readLine();
			if(header.toUpperCase().contains("200 OK")){
				String line;
				while((line = reader.readLine())!= null){
					if(!bodycontent){
						if(isBlankLine(line)){
							bodycontent = true;
						}
					}
					else{
						document.append(line + "\n");
					}
				}
			}
			else return;
		}catch(Exception e){}
	}

	private boolean isBlankLine(String line) {
		if(line.trim().equals("")) return true;
		return false;
	}

	public StringBuffer getDocument(){
		try{
			if(isLocal){
				String line = null;
				while((line = reader.readLine())!= null){
					document.append(line + "\n");
				}
			}
			else{
				socket = new Socket(ip, 80);
				socket.setSoTimeout(TIMEOUT);
				writer = new PrintWriter(socket.getOutputStream(), true);
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				sendRequest();
				readResponse();
			}
			return document;
		}
		catch(Exception e){
			return document;
		}
		finally{
			try{
				writer.close();
				reader.close();
				socket.close();
			}catch(Exception e){}
		}
	}

}
