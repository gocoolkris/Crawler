package edu.upenn.cis455.servlet;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.upenn.cis455.dbservice.DbStoreSetup;
import edu.upenn.cis455.dbservice.UserService;

/**
 * The login servlet used to login to the database.
 * @author gokul
 *
 */
public class LoginServlet extends HttpServlet{

	public void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession(false);
			if(session != null) session.invalidate();
			PrintWriter writer = response.getWriter();
			writer.print("<!DOCTYPE html><html><head><title>Login</title>");
			writer.print("</head><body><div id=\"header\" align=\"center\"");
			writer.print("<h2>Welcome</h2></div><div align=\"center\">");
			writer.print("<form action=\"login\"method=\"POST\">");
			writer.print("Username :<input type=\"text\" name=\"username\"><br>");
			writer.print("Password :<input type=\"password\" name=\"password\"><br>");
			writer.print("<input type=\"submit\" value=\"Login\">");
			writer.print("</form><form action=\"register\" method=\"GET\">");
			writer.print("<input type=\"submit\" value=\"New User\">");
			writer.print("</form></div></body></html>");
			writer.flush();
		}catch(Exception e){}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession(false);
			if(session != null) session.invalidate();
			String username_value = request.getParameter("username");
			String password_value = request.getParameter("password");
			String dbLocation = this.getServletContext().getInitParameter("BDBstore");
			DbStoreSetup setup = new DbStoreSetup(dbLocation,false);
			UserService userService = new UserService(setup.getEntityStore(),setup.getEnvironment());
			boolean isValid = userService.validateLogin(username_value, password_value);
			if(isValid){
				session = request.getSession(true);
				session.setAttribute("username", username_value);
				session.setMaxInactiveInterval(-1);
				response.sendRedirect("list");
			}
			else{
				PrintWriter writer = response.getWriter();
				writer.print("<!DOCTYPE html><html><head><title>Login</title>");
				writer.print("</head><body><div id=\"header\" align=\"center\"");
				writer.print("<h2>Welcome</h2></div><div align=\"center\">");
				writer.print("<b><i>Username/Password Invalid</i></b>");
				writer.print("<form action=\"login\"method=\"POST\">");
				writer.print("Username :<input type=\"text\" name=\"username\"><br>");
				writer.print("Password :<input type=\"password\" name=\"password\"><br>");
				writer.print("<input type=\"submit\" value=\"Login\">");
				writer.print("</form><form action=\"register\" method=\"GET\">");
				writer.print("<input type=\"submit\" value=\"New User\">");
				writer.print("</form></div></body></html>");
				writer.flush();
			}
			setup.close();
		}catch(Exception e){}
	}
}
