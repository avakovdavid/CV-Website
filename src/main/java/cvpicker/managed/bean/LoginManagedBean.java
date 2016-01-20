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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


public class LoginManagedBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String email;
    private String password;
    private User currentUser = null;
    
    
    public String getEmail() {
	return email;
    }
    
    public void setEmail(String email) {
	this.email = email;
    }
 
    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }
    
    public User getCurrentUser(){
	return currentUser;
    }
    
    /**
     * @param currentUser the currentUser to set
     */
    public void setCurrentUser(User currentUser) {
	this.currentUser = currentUser;
    }
    
    public void login(){
	Session session = HibernateUtil.getSessionFactory().openSession();

	User user = (User) session.createCriteria(User.class).add(Restrictions.eq("email", email)).uniqueResult();
	
	MessageDigest messageDigest;
	try {
	    messageDigest = MessageDigest.getInstance("MD5");
	    messageDigest.update(password.getBytes());
	    password = new String(messageDigest.digest());
	} catch (NoSuchAlgorithmException ex) {
	    
	}
	
	if(user == null){
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Utilisateur avec l'adresse '" + email + "' n'existe pas.", ""));
	} else if (!user.getPassword().equals(password)){
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mot de passe incorrect.", ""));
	} else {
	    setCurrentUser(user);
	}
	
	session.close();
    }
     
    public String logout() {
         setCurrentUser(null);
         email = "";
         password = "";
          
         FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
         return "/authentication.xhtml?faces-redirect=true";
    }
    
    public void checkLoggedIn(ComponentSystemEvent cse) {
	FacesContext context = FacesContext.getCurrentInstance();
	if( currentUser == null){
	    context.getApplication().getNavigationHandler().handleNavigation(context, null, "/authentication.xhtml?faces-redirect=true");
	}
    }
    
    public void checkLoggedOut(ComponentSystemEvent cse) {
	FacesContext context = FacesContext.getCurrentInstance();
	if( currentUser != null){
	    context.getApplication().getNavigationHandler().handleNavigation(context, null, "/index.xhtml?faces-redirect=true");
	}
    }
}
