package edu.upenn.cis455.servlet;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.upenn.cis455.dbservice.DbStoreSetup;
import edu.upenn.cis455.dbservice.UserService;

/**
 * This servlet is used to register a new user into the database. The username
 * is unique and prompted to user incase of a duplicate entry.
 * @author gokul
 *
 */
public class RegisterServlet extends HttpServlet{

	public void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			PrintWriter writer = response.getWriter();
			writer.print("<!DOCTYPE html><html><head><title>");
			writer.print("Register</title></head><body>");
			writer.print("<script type=\"text/javascript\">");
			writer.print("function match(){");
			writer.print("var pass = document.getElementById(\"password\").value;");
			writer.print("var cpass=document.getElementById(\"cpassword\").value;");
			writer.print("if(pass != cpass){ alert(\"Password do not match\");}}");
			writer.print("</script><body><form action=\"register\" method=\"POST\">");
			writer.print("Username<br><input type=\"text\" name=\"username\"><br>");
			writer.print("Password<br><input type=\"password\" name=\"password\" id = \"password\"><br>");
			writer.print("Confirm Password<br><input type=\"password\" id = \"cpassword\" onblur=\"match()\"><br>");
			writer.print("<input type=\"submit\" value=\"Register\">");
			writer.print("</form></body></html>");
			writer.flush();

		}catch(Exception e){

		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String dbLocation = this.getServletContext().getInitParameter("BDBstore");
			DbStoreSetup setup = new DbStoreSetup(dbLocation, false);
			UserService userService = new UserService(setup.getEntityStore(), setup.getEnvironment());
			if(userService.userNameAlreadyExists(username)){
				PrintWriter writer = response.getWriter();
				writer.print("<!DOCTYPE html><html><head><title>");
				writer.print("Register</title></head><body>");
				writer.print("<script type=\"text/javascript\">");
				writer.print("function match(){");
				writer.print("var pass = document.getElementById(\"password\").value;");
				writer.print("var cpass=document.getElementById(\"cpassword\").value;");
				writer.print("if(pass != cpass){ alert(\"Password do not match\");}}");
				writer.print("</script><body><div><b><i>Username AlreadyExists!</i></b></div>");
				writer.print("<form action=\"register\" method=\"POST\">");
				writer.print("Username<br><input type=\"text\" name=\"username\"><br>");
				writer.print("Password<br><input type=\"password\" name=\"password\" id = \"password\"><br>");
				writer.print("Confirm Password<br><input type=\"password\" id = \"cpassword\" onblur=\"match()\"><br>");
				writer.print("<input type=\"submit\" value=\"Register\">");
				writer.print("</form></body></html>");
				writer.flush();
			}
			else{
				userService.addUser(username, password);
				PrintWriter writer = response.getWriter();
				writer.print("<html><head><title>Registered</title></head>");
				writer.print("<body><h2>User Registered. Please Login again</h2><br>");
				writer.print("<form action =\"login\" method=\"GET\"> <input type=\"submit\" value=\"Login\">");
				writer.print("</form></body></html>");
			}
			setup.close();
		}catch(Exception e){
			
		}
	}



}

