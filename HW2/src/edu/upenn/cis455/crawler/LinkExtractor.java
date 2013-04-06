package edu.upenn.cis455.crawler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

/**
 * the link extractor class extracts the list of links from a
 * given html document. it then enqueues it the main queue
 * to be crawled.
 * @author gokul
 *
 */
public class LinkExtractor {

	public LinkExtractor(){}

	private URL url;
	private String contents;
	private Document doc;
	private ArrayList<String> extractedLinks;
	public LinkExtractor(String url, String contents){
		try{
			this.url = new URL(url);
			this.contents = contents;
			extractedLinks = new ArrayList<String>();
		}catch(Exception e){}
	}

	/**
	 * this is a helper method that uses JTidy to
	 * parse the document into a DOM document object.
	 */
	public void parseDocument(){
		Tidy tidy = new Tidy();
		InputStream in = new ByteArrayInputStream(contents.getBytes());
		doc = tidy.parseDOM(in, null);
	}

	/**
	 * this methods extracts the list of links in the 
	 * html file and returns them.
	 * @return the extractedLinks.
	 */
	public ArrayList<String> getLinks(){
		NodeList anchors = doc.getElementsByTagName("a");
		for(int i = 0; i < anchors.getLength(); ++i){
			Node anchor = anchors.item(i);
			extractLinks(anchor);
		}
		return extractedLinks;
	}


	/**
	 * this method extracts the links from a node
	 * that has href attribute and adds to the 
	 * list of links that would be return later.
	 * @param a - the node that has a href attribute.
	 */
	private void extractLinks(Node a){
		
		NamedNodeMap attr = a.getAttributes();
		Node n = attr.getNamedItem("href");
		String link = n.getNodeValue();
		if(link == null) return;
		if(link.startsWith("http")){
			//dont do anything
		}
		else if(link.startsWith("www.")){
			link = "http://" + link;
		}
		else{
			String path = "http://" + url.getHost() + url.getPath();
			if(path.endsWith("/")) link = path + link;
			else link = path + "/" + link;
			System.out.println(link);
		}
		extractedLinks.add(link);
	}
	
	
}
