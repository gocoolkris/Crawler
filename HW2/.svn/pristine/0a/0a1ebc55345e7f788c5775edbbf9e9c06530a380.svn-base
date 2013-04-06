package edu.upenn.cis455.dbobjects;

import java.util.Date;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class Document {

	public Document(){}
	
	public Document(String documenturl, String content, Date crawleddate){
		this.documentUrl = documenturl;
		this.documentContent = content;
		this.lastCrawled = crawleddate;
	}
	
	
	@PrimaryKey
	private String documentUrl;
	
	private String documentContent;
	
	private Date lastCrawled;

	public String getDocumentUrl() {
		return documentUrl;
	}

	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	public String getDocumentContent() {
		return documentContent;
	}

	public void setDocumentContent(String documentContent) {
		this.documentContent = documentContent;
	}

	public Date getLastCrawled() {
		return lastCrawled;
	}

	public void setLastCrawled(Date lastCrawled) {
		this.lastCrawled = lastCrawled;
	}
	
	public void setNowAsLastCrawled(){
		this.lastCrawled = new Date();
	}
}
