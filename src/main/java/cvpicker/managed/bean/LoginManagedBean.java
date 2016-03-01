/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.managed.bean;

import cvpicker.hibernate.HibernateUtil;
import cvpicker.hibernate.User;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


public class LoginManagedBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String email;
    private String password;
    private User currentUser = null;
    
    
    /**
     * Getter for the email address
     * @return 
     */
    public String getEmail() {
	return email;
    }
    
    /**
     * Setter for the email address
     * @param email the email address to set
     */
    public void setEmail(String email) {
	this.email = email;
    }
 
    /**
     * Getter for the password
     * @return 
     */
    public String getPassword() {
	return password;
    }

    /**
     * Setter for the password
     * @param password the password to set
     */
    public void setPassword(String password) {
	this.password = password;
    }
    
    /**
     * Getter for the current connected user
     * @return 
     */
    public User getCurrentUser(){
	return currentUser;
    }
    
    /**
     * Setter for the current connected user
     * @param currentUser the current connected user to set
     */
    public void setCurrentUser(User currentUser) {
	this.currentUser = currentUser;
    }
    
    /**
     * Method to log in a user. 
     */
    public void login(){
	Session session = HibernateUtil.getSessionFactory().openSession();

	User user = (User) session.createCriteria(User.class).add(Restrictions.eq("email", email)).uniqueResult();
	
	MessageDigest messageDigest;
	try {
	    //try to encrypt password to compare it with encrypted password from database
	    messageDigest = MessageDigest.getInstance("MD5");
	    messageDigest.update(password.getBytes());
	    password = new String(messageDigest.digest());
	} catch (NoSuchAlgorithmException ex) {
	    
	}
	
	if(user == null){ 
	    //wrong email
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Utilisateur avec l'adresse '" + email + "' n'existe pas.", ""));
	} else if (!user.getPassword().equals(password)){ 
	    //wrong password
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mot de passe incorrect.", ""));
	} else { 
	    //user found
	    setCurrentUser(user);
	}
	
	session.close();
    }
    
    /**
     * Method to close the session (log out the user)
     * @return authentication page to redirect on after
     */
    public String logout() {
         setCurrentUser(null);
         email = "";
         password = "";
          
         FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
         return "/authentication.xhtml?faces-redirect=true";
    }
    
    /**
     * Method to check if user is logged in. If user isn't logged in, redirect to authentication page
     * @param cse ComponentSystemEvent
     */
    public void checkLoggedIn(ComponentSystemEvent cse) {
	FacesContext context = FacesContext.getCurrentInstance();
	if( currentUser == null){
	    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

	    Map<String, String> parameterMap = (Map<String, String>) externalContext.getRequestParameterMap(); 
	    String param = parameterMap.get("id");

	    if(context.getViewRoot().getViewId().equals("/cv.xhtml") && param != null && param.length() > 0){
		
	    } else {
		context.getApplication().getNavigationHandler().handleNavigation(context, null, "/authentication.xhtml?faces-redirect=true");
	    }
	}
    }
    
    /**
     * Method to check if user is logged out. If user isn't logged out, redirect to welcome page
     * @param cse ComponentSystemEvent
     */
    public void checkLoggedOut(ComponentSystemEvent cse) {
	FacesContext context = FacesContext.getCurrentInstance();
	if( currentUser != null){
	    context.getApplication().getNavigationHandler().handleNavigation(context, null, "/index.xhtml?faces-redirect=true");
	}
    }
}
