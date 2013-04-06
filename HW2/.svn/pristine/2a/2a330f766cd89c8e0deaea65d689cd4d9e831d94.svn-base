package edu.upenn.cis455.components;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.upenn.cis455.dbobjects.Channel;
import edu.upenn.cis455.dbobjects.Document;
import edu.upenn.cis455.dbservice.ChannelContentService;
import edu.upenn.cis455.dbservice.ChannelService;
import edu.upenn.cis455.dbservice.DbStoreSetup;
import edu.upenn.cis455.dbservice.DocumentService;

public class DocumentCollector {

	private ArrayList<Document> documents;
	private String dbLocation, channelname, owner, xslturl;
	private StringBuffer documentCollector;
	/**
	 * class that collects the documents from the database
	 * and returns a string(xml) in the specified format.
	 * @param dbLocation
	 * @param channelname
	 * @param owner
	 */
	public DocumentCollector(String dbLocation, String channelname, String owner){
		this.dbLocation = dbLocation;
		this.channelname = channelname;
		this.owner = owner;
		documentCollector = new StringBuffer();
		documents = new ArrayList<Document>();
	}
	
	/**
	 * it opens a database connection, 
	 * uses the database API to connect to fetch the documents
	 * stored against a particular channel. The 
	 * retrived documents are formatted and collected as a
	 * required string and returned.
	 * @return
	 */
	public String getDocumentCollection(){
		
		
		DbStoreSetup setup = new DbStoreSetup(dbLocation, false);
		ChannelService channelService = new ChannelService(setup.getEntityStore(), setup.getEnvironment());
		ChannelContentService ccs = new ChannelContentService(setup.getEntityStore(),setup.getEnvironment());
		DocumentService docService = new DocumentService(setup.getEntityStore(), setup.getEnvironment());
		Channel c = channelService.getChannel(channelname, owner);
		String channelid = c.getChannelid();
		System.out.println(channelid + "passing from servlet to document collector");
		xslturl = c.getXslturl();
		if(channelid != null){
			HashSet<String> docUrls = ccs.getChannelUrls(channelid);
			for(String url : docUrls){
				Document d = docService.getDocument(url);
				documents.add(d);
			}
			createDocumentCollection();
			return documentCollector.toString();
		}
		return null;
	}

	/**
	 * helper method that aids in creating the document
	 * collector string. The expected api is to have a
	 * <documentcollection> tag that has various
	 * <document> tags with attributes lastCrawled
	 * and location.
	 */
	private void createDocumentCollection() {
		String xmlHeader="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
		documentCollector.append(xmlHeader);
		
		if(!xslturl.trim().isEmpty() && !xslturl.trim().equals("null")){
		String xsltLink = "<?xml-stylesheet type=\"text/xsl\" href=\""+ xslturl +"\"?>";
		documentCollector.append(xsltLink);
		}
		documentCollector.append("<documentcollection>");
		for(Document d : documents){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			String fDate = sdf.format(d.getLastCrawled());
			documentCollector.append("<document crawled=\""+fDate+"\" ");
			documentCollector.append("location=\""+d.getDocumentUrl()+"\">");
			String editedContent = editContent(d.getDocumentContent());
			documentCollector.append(editedContent);
			documentCollector.append("</document>");
		}
		documentCollector.append("</documentcollection>");
	}

	/**
	 * helper method that eliminates the document type declaration
	 * @param content - the actual content.
	 * @return content with header line removed.
	 */
	private String editContent(String content) {
		content = content.trim();
		String pattern = "<\\?.+\\?>";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(content);
		if(m.lookingAt()){
			content = content.substring(m.end());
			return content;
		}
		return null;
	}
	
	
	
}
