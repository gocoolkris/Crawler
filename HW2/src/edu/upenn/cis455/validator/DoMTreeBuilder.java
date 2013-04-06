package edu.upenn.cis455.validator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xml.sax.InputSource;

/**
 * @author gokul.
 * The DoMTreeBuilder takes the content of a document
 * and returns a DoMTree as a document. 
 */
public class DoMTreeBuilder {

	
	private String filecontent;
	private Document doc;
	private boolean ishtml;

	/**
	 * Constructor that takes in the filecontents and the
	 * url that corresponds to the file. It also creates DoMTree
	 * depending on whether it is an HTML or XML.
	 * @param filecontents
	 * @param url
	 */
	public DoMTreeBuilder(String filecontents, String url){
		this.filecontent = filecontents;
		if(url.toLowerCase().endsWith("xml")){
			ishtml = false;
		}
		else ishtml = true;
		buildTree();
	}

	/**
	 * Helper method that actually builds the tree. The reader object is 
	 * initialized via constructor. This method builds the tree using DOM
	 * API for xml files and JTidy for HTML files.
	 */
	private void buildTree(){
		try {
			if(!ishtml){
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(filecontent));
				doc = builder.parse(is);
			}
			else{
				InputStream in = new ByteArrayInputStream(filecontent.getBytes());
				Tidy tidy = new Tidy();
				doc = tidy.parseDOM(in, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * the method that access the built Document
	 * object.
	 * @return the DOM Tree as a doc object.
	 */
	public Document getDocumentNode(){
		return doc;
	}

}
