package edu.upenn.cis455.xpathengine;

import org.w3c.dom.Document;

import edu.upenn.cis455.validator.GrammarValidator;
import edu.upenn.cis455.validator.XPathValidator;

public class XPathEngineImpl implements XPathEngine {

	private String[] XpathExpressions;
	private boolean[] areValidXpaths;

	public XPathEngineImpl() {
		// Do NOT add arguments to the constructor!!
	}

	public void setXPaths(String[] s) {
		XpathExpressions = s;
		areValidXpaths = new boolean[s.length];
	}

	public boolean isValid(int i) {
		if(XpathExpressions == null) return false;
		else if(i < 0 || i >= XpathExpressions.length) return false;
		else if(XpathExpressions[i] == null) return false;
		GrammarValidator validator = new GrammarValidator();
		return validator.isValidXpath(XpathExpressions[i]);
	}

	public boolean[] evaluate(Document d) { 
		if(XpathExpressions == null) return null;
		XPathValidator grammar = new XPathValidator(d);
		for(int i = 0; i < XpathExpressions.length; ++i){
			String exp = XpathExpressions[i];
			areValidXpaths[i] = isValid(i) && grammar.validateXpath(exp);
		}
		return areValidXpaths; 
	}

	public boolean atLeastOneXpathIsValid(){
		boolean result = false;
		for(boolean res : areValidXpaths){
			result = result || res;
		}
		return result;
	}
	
}
