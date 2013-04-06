package edu.upenn.cis455.servlet;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.upenn.cis455.components.DocumentCollector;

/**
 * This channel is used to display the document content of the channel.
 * Only, owner and the subscribed users can view the channel.
 * @author gokul
 *
 */
public class DisplayChannelServlet extends HttpServlet{

	public void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession();
			if(session == null || session.getAttribute("username") == null)
				response.sendRedirect("/login");
			else{
				String username = (String)session.getAttribute("username");
				String channelname = request.getParameter("channelname");
				String owner = request.getParameter("owner");
				if(channelname != null && owner != null){
					PrintWriter writer = response.getWriter();
					String dbLocation = this.getServletContext().getInitParameter("BDBstore");
					DocumentCollector collector = new DocumentCollector(dbLocation, channelname, owner);
					writer.print(collector.getDocumentCollection());
					writer.flush();
				}
				else{
					PrintWriter writer = response.getWriter();
					writer.print("<html><head><title>No Content</title><body>");
					writer.print("The Database contains no matching channelcontent");
					writer.print("</body></html>");
					writer.flush();
				}
			}
		}catch(Exception e){}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response){

	}
}
