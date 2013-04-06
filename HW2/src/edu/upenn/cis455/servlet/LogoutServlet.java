package edu.upenn.cis455.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This servlet invalidates any current sessions and redirects to the login page.
 * @author gokul
 *
 */
public class LogoutServlet extends HttpServlet{

	
	public void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession();
			if(session != null){
				session.invalidate();
			}
			response.sendRedirect("login");
		}catch(Exception e){
		}
	}
	
}
