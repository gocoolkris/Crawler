package edu.upenn.cis455.validator;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gokul. 
 * This class is used to validate a given xpath expression.
 * It only checks for semantics and construction of the XPath based on the given
 * grammar rules. It does not validate the given XPath expression against
 * document passed in.
 */
public class GrammarValidator {


	/**
	 * the Xpath expression could be of the following 
	 * form:
	 * XPath -> axis step
	 * axis  -> /
	 * step  -> nodename ([ test ])*(axis step)?
	 * test  -> step
	 * test  -> text() = "...."
	 * test  -> contains(text(), "....")
	 * test  -> @attname = "....."
	 * @param subpath - the substring to be validated for a
	 * valid xpath.
	 * @return true if the Xpath expression is valid for
	 * the passed in document, false otherwise.
	 */
	/**
	 * @param subpath
	 * @return
	 */
	public boolean isValidXpath(String subpath){
		subpath = subpath.trim();
		if(!isWellFormed(subpath)) return false;
		if(!isAxis(subpath)) return false;
		subpath = subpath.replaceFirst("/", "").trim();
		if(!isValidStep(subpath)) return false;
		return true;

	}

	/**
	 * Helper method that Checks whether the given substring starts with "/"
	 * - the axis symbol.
	 * @param xpath - the string that is to be validated.
	 * @return true if the xpath starts with "/" false
	 * otherwise.
	 */
	public boolean isAxis(String xpath) {
		if(xpath.startsWith("/")){
			return true;
		}
		return false;
	}

	/**
	 * Helper method that checks whether the given String 
	 * conforms to "step" metasymbol. A step is of the form
	 * step -> nodename([test])* (axis step)?
	 * @param subpath - string to be validated.
	 * @return - true if the passed in string is a valid
	 * step meta symbol, false otherwise.
	 */
	public boolean isValidStep(String subpath) {
		if(hasNodeName(subpath)){
			String nodeName = getNodeName(subpath);
			subpath = subpath.replaceFirst(nodeName, "").trim();
			ArrayList<String> testExpressions = getTestExpressions(subpath);
			if(testExpressions != null){
				for(String exp : testExpressions){
					if(!isValidTestExpression(exp)) return false;
					subpath = subpath.substring(getExpressionEndIndex(subpath) + 1);
				}
			}
			if(!subpath.trim().equals("")){
				if(isValidXpath(subpath)) return true;
				return false;
			}
			return true;
		}
		return false;
	}


	/**
	 * Helper method that returns the index at which
	 * the given testExpression ends in the xpath.
	 * @param subpath - the string representing the 
	 * complete remaining xpath.
	 * @return index at which the first expression
	 * ends.
	 */
	private int getExpressionEndIndex(String subpath) {
		int brace = 0, expIndex = 0;
		subpath = subpath.trim();
		while(expIndex < subpath.length()){
			if(subpath.charAt(expIndex) == '[') brace++;
			if(subpath.charAt(expIndex) == ']') brace--;
			if(brace == 0) return expIndex;
			expIndex++;
		}
		return 0;
	}

	/**
	 * Helper method that validates if the open and
	 * close brackets match. It validates whether the 
	 * given xpath is a well formed String expression.
	 * @param subpath - the Xpath to be validated.
	 * @return - true if the passed in Xpath is a valid 
	 * well formed Xpath, false otherwise.
	 */
	private boolean isWellFormed(String subpath) {
		int brace = 0, index = 0;
		while(index < subpath.length()){
			if(brace < 0) return false;
			if(subpath.charAt(index) == '[') brace++;
			if(subpath.charAt(index) == ']') brace--;
			index++;
		}
		if(brace == 0) return true;
		return false;
	}

	/**
	 * Helper method that extracts a list of expressions,
	 * if any
	 * @param subpath - the string from which a list of expression 
	 * meta symbol has to be extracted. 
	 * @return an arraylist of String, if the subpath has valid 
	 * expression meta symbol, null otherwise.
	 */
	private ArrayList<String> getTestExpressions(String subpath) {

		subpath = subpath.trim();
		if(subpath.isEmpty() || (!subpath.startsWith("["))){
			return null;
		}
		ArrayList<String> expr = new ArrayList<String>();
		while((!subpath.trim().isEmpty()) && (subpath.startsWith("["))){
			int startIndex = 0, index = 0;
			int brace = 0;
			while(index < subpath.length()){
				if(subpath.charAt(index) == '[') brace++;
				else if(subpath.charAt(index) == ']') brace--;
				if(brace == 0){
					String ex = subpath.substring(startIndex + 1, index);
					subpath = subpath.substring(index + 1).trim();
					expr.add(ex);
					break;
				}
				index++;
			}
		}
		return expr;
	}

	/**
	 * Helper method that extracts the nodename from
	 * the xpath string. It is called only if the
	 * xpath has a nodename at the beginning.
	 * @param subpath - the xpath from which the nodename
	 * has to be extracted.
	 * @return string which represents the nodename specified
	 * in the xpath.
	 */
	private String getNodeName(String subpath) {
		subpath = subpath.trim();
		String pattern = "[_a-zA-Z0-9][a-zA-Z0-9\\-\\.]*";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(subpath);
		if(m.lookingAt()){
			String nodename = subpath.substring(m.start(),m.end());
			return nodename;
		}
		return null;
	}

	/**
	 * Helper method that validates the presence of a nodename in the
	 * given substring. If there is no valid node name it returns false.
	 * @param subpath- the string that has to validated.
	 * @return true if subpath starts with a valid node name,
	 * false otherwise.
	 */
	private boolean hasNodeName(String subpath) {
		if(subpath == null || subpath.equals("")) return false;
		subpath = subpath.trim();
		String pattern = "[_a-zA-Z0-9][a-zA-Z0-9\\.\\-]*";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(subpath);
		if(m.lookingAt()){
			return true;
		}
		return false;
	}

	public boolean isValidTestExpression(String subpath){
		subpath = subpath.trim();
		if(textEqualsForm(subpath) || containsTextForm(subpath) || attributeTextForm(subpath)) return true;
		else if(isValidStep(subpath)) return true;
		return false;
	}


	/**
	 * Checks whether the initial subpart of the XPath expression 
	 * is of the form
	 * <b>contains(text(), ".....")</b>
	 * Takes the current node in context and checks whether the given
	 * text is contained in the text of the node.
	 * @return - true if the context node contains a substring that is
	 * in the Xpath condition; false otherwise.
	 */
	public boolean containsTextForm(String subpath) {
		subpath = subpath.trim();
		if(subpath.startsWith("contains")){
			subpath = subpath.replaceFirst("contains", "").trim();
			if(subpath.startsWith("(")){
				subpath = subpath.replaceFirst("\\(", "").trim();
				if(subpath.startsWith("text")){
					subpath = subpath.replaceFirst("text", "").trim();
					if(subpath.startsWith("(")){
						subpath = subpath.replaceFirst("\\(", "").trim();
						if(subpath.startsWith(")")){
							subpath = subpath.replaceFirst("\\)", "").trim();
							if(subpath.startsWith(",")){
								subpath = subpath.replaceFirst(",","").trim();
								String pattern = "\".+\"";
								Pattern p = Pattern.compile(pattern);
								Matcher m = p.matcher(subpath);
								if(m.lookingAt()){
									String textValue = subpath.substring(m.start(), m.end());
									subpath = subpath.substring(m.end()).trim();
									if(subpath.startsWith(")")){
										subpath = subpath.replaceFirst("\\)","").trim();
										return true;
									}
									else{
										subpath = "contains(text(), " + textValue + subpath;
										return false;
									}
								}
								else{
									subpath = "contains(text()," + subpath;
									return false;
								}
							}
							else{
								subpath = "contains(text()" + subpath;
								return false;
							}
						}
						else{
							subpath = "contains(text(" + subpath;
							return false;
						}
					}
					else{
						subpath = "contains(text" + subpath;
						return false;
					}
				}
				else{
					subpath = "contains(" + subpath;
					return false;
				}
			}
			else{
				subpath = "contains" + subpath;
				return false;
			}
		}
		return false;
	}
	/**
	 * Takes a node and validates whether the given XPath is of the
	 * form [text() = "...."]. If it is so, it validates the subpath
	 * for this current node. Returns false otherwise. If the root node is
	 * not of the form
	 * @param currentNode - node that is being validated against
	 * @return - true if the XPath condition is valid just for this
	 * currentNode, false otherwise.
	 */
	public boolean textEqualsForm(String subpath) {
		subpath = subpath.trim();
		String pattern = "[ ]*text[ ]*\\([ ]*\\)[ ]*";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(subpath);
		if(m.lookingAt()){
			String textString = subpath.substring(m.start(), m.end());
			subpath = subpath.replace(textString, "").trim();
			if(subpath.startsWith("=")){
				subpath = subpath.replaceFirst("=", "").trim();
				pattern = "\".*\"";
				p = Pattern.compile(pattern);
				m = p.matcher(subpath);
				if(m.lookingAt()){
					String textValue = subpath.substring(m.start(), m.end());
					subpath = subpath.replaceFirst(textValue, "").trim();
					return true;
				}
				else{
					return false;
				}
			}
			else{
				return false;
			}
		}
		return false;
	}

	/**
	 * Helper method that checks whether the given substring is of a valid 
	 * test expression that is of the form
	 * step->  @attname = "somestring"
	 * @param subpath - the string that is to be validated if the
	 * it is of the form @attname = "somestring"
	 * @return true if it is a valid testExpression containing
	 * the attribute name and value, false otherwise.
	 */
	public boolean attributeTextForm(String subpath) {
		subpath = subpath.trim();
		if(subpath.startsWith("@")){
			subpath = subpath.replaceFirst("@", "");
			String pattern = "[a-zA-Z_][_a-zA-Z0-9\\.\\-]*";
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(subpath);
			if(m.lookingAt()){
				String attributeName = subpath.substring(m.start(),m.end());
				subpath = subpath.substring(m.end()).trim();
				if(subpath.startsWith("=")){
					subpath = subpath.replaceFirst("=", "").trim();
					pattern = "\".*\"";
					p = Pattern.compile(pattern);
					m = p.matcher(subpath);
					if(m.lookingAt()){
						String attributeValue = subpath.substring(m.start(),m.end());
						subpath = subpath.substring(m.end()).trim();
						return true;
					}
					else{
						subpath = "@" + attributeName + " = " + subpath;
						return false;
					}
				}
				else{
					subpath = "@" + attributeName + subpath;
					return false;
				}
			}
			else{
				subpath = "@" + subpath;
				return false;
			}
		}
		return false;
	}

}
