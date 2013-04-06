package edu.upenn.cis455.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import edu.upenn.cis455.validator.*;
import edu.upenn.cis455.xpathengine.XPathEngineImpl;

import javax.servlet.http.*;

import org.w3c.dom.Document;

@SuppressWarnings("serial")
public class XPathServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response){
		try {
			PrintWriter out = response.getWriter();
			out.print("<html><head><title>XPath Form</title></head><body>");
			out.print("<div><h2> Name : </i>Gokul Krishnan Ragunathan</i><br>");
			out.print("<h3> SEAS Name : <i>grag</i></h3></div>");
			out.print("<div><form action=\"xpathservlet\" method=\"POST\">");
			out.print("URL : <input type=\"text\" name=\"documenturl\"><br>");
			out.print("XPath Condition: <input type=\"text\" name=\"xpathcondn\"><br>");
			out.print("<input type=\"submit\" name=\"submit\" value=\"Submit\" />");
			out.print("</form></div></body></html>");
			out.flush();
		} catch (IOException e) {}
	}


	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response){

		String url = request.getParameter("documenturl");
		String xpathcondition = request.getParameter("xpathcondn");
		Parser parser = new Parser(url);
		DoMTreeBuilder builder = new DoMTreeBuilder(parser.fileContents(),url);
		Document rootNode = builder.getDocumentNode();
		String[] conditions = new String[]{xpathcondition};
		XPathEngineImpl engine = new XPathEngineImpl();
		engine.setXPaths(conditions);
		boolean isValid = engine.evaluate(rootNode)[0];
		try{
			PrintWriter writer = response.getWriter();
			if(isValid){
			writer.print("<html><head><title>Success</title></head>");
			writer.print("<body><h3> Hurray!! The XPath Condition passed the filter</h3><br>");
			writer.print("<form name = \"GET\" action=\"xpathservlet\">");
			writer.print("<input type=\"submit\" name=\"submit\" value=\"Back\"/>");
			writer.print("</form></body></html>");
			}
			else{
				writer.print("<html><head><title>Not Valid</title></head>");
				writer.print("<body><h3> The XPath condition did not pass the filter</h3><br>");
				writer.print("<form name = \"GET\" action=\"xpathservlet\">");
				writer.print("<input type=\"submit\" name=\"submit\" value=\"Back\"/>");
				writer.print("</form></body></html>");
			}
			writer.flush();
		}
		catch(Exception e){}
	}

}









