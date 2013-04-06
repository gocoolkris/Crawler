package edu.upenn.cis455.dbobjects;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * 
 * This class represents the user object. It stores the
 * username and password. The username is unique
 * @author gokul
 *
 */
@Entity
public class User {
	@PrimaryKey
	String username;
	String password;
	
	public User(){}
	
	public User(String username, String passwd){
		this.username = username;
		this.password = passwd;
	}
	
	
	/**
	 * getter for the username.
	 * @return username of the user object it is referring to.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * setter of the username.
	 * @param username - sets the username to the 
	 * new username.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * setters for the password.
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * getter method for password variable.
	 * @return the password for the
	 * current user object
	 */
	public String getPassword() {
		return password;
	}

}
