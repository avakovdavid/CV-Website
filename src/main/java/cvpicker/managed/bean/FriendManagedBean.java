/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.managed.bean;

import cvpicker.hibernate.Friend;
import cvpicker.hibernate.HibernateUtil;
import cvpicker.hibernate.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author DAVID
 */
public class FriendManagedBean  implements Serializable{
    
    private LoginManagedBean loginBean;
    private UserManagedBean userBean;
    
    public void acceptRequest(int id){
	User userToAccepte = getUserBean().getUserById(id);
	
	Session session = HibernateUtil.getSessionFactory().openSession();
	
	Criteria criteria = session.createCriteria(Friend.class);
	criteria.add(Restrictions.or(
		Restrictions.eq("userA", getLoginBean().getCurrentUser()),
		Restrictions.eq("userB", getLoginBean().getCurrentUser())));
	criteria.add(Restrictions.or(
		Restrictions.eq("userA", userToAccepte),
		Restrictions.eq("userB", userToAccepte)));
	criteria.add(Restrictions.eq("accepted", false));
	
	Friend friend = (Friend)criteria.uniqueResult();
	
	Transaction tx = null;
		
	try {	
	    friend.setAccepted(true);
	    
	    tx = session.beginTransaction();
	    session.persist(friend);
	    tx.commit();
	    
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userToAccepte.getFirstName() + " " + userToAccepte.getLastName() + " a été ajouté dans votre liste.", ""));
	    
	} catch (Exception e) {
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Un problème est survenu sur le serveur. Veuillez réessayer ultérieurement.", ""));

	    if (tx != null) {
		tx.rollback();
	    }
	} finally {
	    session.close();
	}
    }
    
    public void sendRequest(int id){
	User userA = getLoginBean().getCurrentUser();
	User userB = getUserBean().getUserById(id);
	
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
		
	try {	
	    Friend friend = new Friend();
	    friend.setUserA(userA);
	    friend.setUserB(userB);
	    friend.setAccepted(false);
	    
	    tx = session.beginTransaction();
	    session.save(friend);
	    tx.commit();
	    
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "La demande a été envoyée à " + userB.getFirstName() + " " + userB.getLastName() + ".", ""));
	    
	} catch (Exception e) {
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Un problème est survenu sur le serveur. Veuillez réessayer ultérieurement.", ""));

	    if (tx != null) {
		tx.rollback();
	    }
	} finally {
	    session.close();
	}
    }
    
    public void removeFriend(int id){
	User friendToRemove = getUserBean().getUserById(id);
	
	Session session = HibernateUtil.getSessionFactory().openSession();
	
	Criteria criteria = session.createCriteria(Friend.class);
	criteria.add(Restrictions.or(
		Restrictions.eq("userA", getLoginBean().getCurrentUser()),
		Restrictions.eq("userB", getLoginBean().getCurrentUser())));
	criteria.add(Restrictions.or(
		Restrictions.eq("userA", friendToRemove),
		Restrictions.eq("userB", friendToRemove)));
	
	Friend friend = (Friend)criteria.uniqueResult();
	
	Transaction tx = null;
	
	try {
	    tx = session.beginTransaction();
	    session.delete(friend);
	    tx.commit();
	    
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, friendToRemove.getFirstName() + " " + friendToRemove.getLastName() + " a bien été retiré de la liste.", ""));
	} catch (Exception e) {
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Une erreur s'est produite. " + friendToRemove.getFirstName() + " " + friendToRemove.getLastName() + " n'a pas pu être retiré de la liste d'amis.", ""));
	    if (tx != null) {
		tx.rollback();
	    }
	} finally {
	    session.close();
	}
    }
    
    public List<User> getRequestList(){
	List<User> result = new ArrayList<User>();
	
	Session session = HibernateUtil.getSessionFactory().openSession();
	Criteria criteria = session.createCriteria(Friend.class);
	criteria.add(Restrictions.eq("userB", getLoginBean().getCurrentUser()));
	criteria.add(Restrictions.eq("accepted", false));
	List<Friend>  friendList = criteria.list();

	for(Friend f : friendList){
	    result.add(f.getUserA());
	}
	
	session.close();
	
	return result;
    }
    
    public List<User> getList(){
	List<User> result = new ArrayList<User>();
	
	Session session = HibernateUtil.getSessionFactory().openSession();
	Criteria criteria = session.createCriteria(Friend.class);
	criteria.add(Restrictions.or(
		Restrictions.eq("userA", getLoginBean().getCurrentUser()),
		Restrictions.eq("userB", getLoginBean().getCurrentUser())));
	criteria.add(Restrictions.eq("accepted", true));
	List<Friend>  friendList = criteria.list();
	
	for(Friend f : friendList){
	    if(getLoginBean().getCurrentUser().getId().equals(f.getUserA().getId())){
		result.add(f.getUserB());
	    }else{
		 result.add(f.getUserA());
	    }
	}
	
	session.close();
	
	return result;
    }

    /**
     * @return the loginBean
     */
    public LoginManagedBean getLoginBean() {
	return loginBean;
    }

    /**
     * @param loginBean the loginBean to set
     */
    public void setLoginBean(LoginManagedBean loginBean) {
	this.loginBean = loginBean;
    }

    /**
     * @return the userBean
     */
    public UserManagedBean getUserBean() {
	return userBean;
    }

    /**
     * @param userBean the userBean to set
     */
    public void setUserBean(UserManagedBean userBean) {
	this.userBean = userBean;
    }
}
