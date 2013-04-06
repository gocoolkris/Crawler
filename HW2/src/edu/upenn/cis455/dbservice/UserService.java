package edu.upenn.cis455.dbservice;

import com.sleepycat.je.Environment;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;

import edu.upenn.cis455.dbobjects.User;

/**
 * this class operates on User object of the database.
 * @author gokul
 */
public class UserService {

	public UserService(){}

	private PrimaryIndex<String,User> username;
	private Environment env;

	/**
	 * constructs a userservice object.
	 * @param str - the entity store.
	 * @param e - the environment.
	 */
	public UserService(EntityStore str, Environment e){
		username = str.getPrimaryIndex(String.class, User.class);
		env = e;
	}

	/**
	 * method verifies whether a valid user name already exists in the database
	 * if so, it return true. false otherwise.
	 * @param uname - the username
	 * @return true if a username exists, false otherwise.
	 */
	public boolean userNameAlreadyExists(String uname){
		try{
			User u = username.get(uname);
			if(u != null)return true;
			else return false;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * method that authenticates the user credentials for a given set of
	 * username/password credentials.
	 * @param uname - the username.
	 * @param password - the password.
	 * @return true if a login credentials are valid, false otherwise
	 */
	public boolean validateLogin(String uname, String password){
		try{
			User u = username.get(uname);
			if(u == null) return false;
			if(u.getUsername().equals(uname) && 
					u.getPassword().equals(password)) return true;
		}
		catch(Exception e){}
		return false;
	}
	
	
	
	/**
	 * Adds a new user to the database.
	 * @param uname - the  username
	 * @param passwd - password.
	 * @return true if a user account has been created.
	 */
	public boolean addUser(String uname, String passwd){
		try{
			if(!userNameAlreadyExists(uname)){
				User u = new User(uname,passwd);
				username.put(u);
				env.sync();
				return true;
			}
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * method that deletes the user from the database.
	 * @param uname - username
	 * @return - true if the user account has been deleted, false otherwise.
	 */
	public boolean deleteUser(String uname){
		try{
			if(!userNameAlreadyExists(uname)) return false;
			username.delete(uname);
			env.sync();
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * method is used to change the password of the user
	 * @param uname - the username
	 * @param oldPasswd - old password.
	 * @param newPasswd - new password.
	 * @return true if the password has been changed.
	 */
	public boolean changePassword(String uname, String oldPasswd, String newPasswd){
		try{
			if(!userNameAlreadyExists(uname)) return false;
			else{
				User usr = username.get(uname);
				if(usr.getPassword().equals(oldPasswd)){
					usr.setPassword(newPasswd);
					username.put(usr);
					return true;
				}
				else return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * helper method for validating the entries in the table.
	 * @param uname
	 */
	public void printUser(String uname){
		try{
			User u = username.get(uname);
			if(u!= null)
				System.out.println(u.getUsername() + ":" + u.getPassword());
			else{
				System.out.println("No entry for username: " + uname );
			}
		}catch(Exception e){}
	}
	
}
