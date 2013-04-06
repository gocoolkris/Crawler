package edu.upenn.cis455.validator;

import edu.upenn.cis455.httpclient.HttpClient;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * @author gokul.
 * The parser takes in the url passed in from the servlet
 * and returns a document String Buffer. It essentially uses the HttpClient
 * class to remotely access the document and fetch its contents.
 */
public class Parser {


	private String url;
	private boolean isLocalFile;
	private String filename;
	private StringBuffer document;

	/**
	 * @param Url - url passed via servlet.
	 */
	public Parser(String Url){
		this.url = Url;
		document = new StringBuffer();
		setIsLocalFile();
	}

	/**
	 * Helper method that Sets the boolean value isLocalFile - 
	 * If the requested Url is a local file.
	 */
	private void setIsLocalFile(){
		try {
			URL Url = new URL(url);
			if(url.startsWith("http://localhost") || url.startsWith("/")){
				isLocalFile  = true;
			}
			else isLocalFile = false;
			filename = Url.getFile();
		} catch (MalformedURLException e) {
			isLocalFile = true;
			filename = url;
		}
	}

	/**
	 *  A private that actually fetches the document.
	 *  If the file is available locally, then it 
	 *  parses that file using a reader. If it is
	 *  a remote document, then it connects to the
	 *  server using sockets and fetches the 
	 *  document.
	 */
	private void fetchDocument(){
		if(isLocalFile){
			File f = new File(filename);
			try{
				Scanner sc = new Scanner(f);
				while(sc.hasNextLine()){
					document.append(sc.nextLine() + "\n");
				}
				sc.close();
			}
			catch(Exception e){}
		}
		else{
			HttpClient httpclient = new HttpClient(url);
			document = httpclient.getDocument();
		}
	}
	
	/**
	 * Wrapper method that is used to fetch the document
	 * and return the contents.
	 * @return contents of the file, if the requested
	 * file is available locally or remotely. If the 
	 * file does not exists, it returns empty String Buffer;
	 */
	public String fileContents(){
		fetchDocument();
		return document.toString();
	}
	
}
