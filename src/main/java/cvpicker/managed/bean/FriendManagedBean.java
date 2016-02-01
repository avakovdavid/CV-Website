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
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author DAVID
 */
public class FriendManagedBean  implements Serializable{
    
    private LoginManagedBean loginBean;
    private UserManagedBean userBean;
    
    private long friendsCounter;
    private long friendRequestsCounter;
    
    
    @PostConstruct
    public void init(){
	Session session = HibernateUtil.getSessionFactory().openSession();
	
	try{
	    Criteria criteria = session.createCriteria(Friend.class);
	    criteria.add(Restrictions.or(
		    Restrictions.eq("userA", getLoginBean().getCurrentUser()),
		    Restrictions.eq("userB", getLoginBean().getCurrentUser())));
	    criteria.add(Restrictions.eq("accepted", true));
	    criteria.setProjection(Projections.rowCount());
	    setFriendsCounter((Long)criteria.uniqueResult());
	    
	    Criteria criteria1 = session.createCriteria(Friend.class);
	    criteria1.add(Restrictions.eq("userB", getLoginBean().getCurrentUser()));
	    criteria1.add(Restrictions.eq("accepted", false));
	    criteria1.setProjection(Projections.rowCount());
	    setFriendRequestsCounter((Long)criteria1.uniqueResult());
	} catch(Exception e){

	} finally{
	    session.close();
	}
    }
    
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
	    friendRequestsCounter--;
	    friendsCounter++;
	} catch (Exception e) {
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Un problème est survenu sur le serveur. Veuillez réessayer ultérieurement.", ""));

	    if (tx != null) {
		tx.rollback();
	    }
	} finally {
	    session.close();
	}
    }
    
    public boolean alreadyFriendWith(User u){
	Session session = HibernateUtil.getSessionFactory().openSession();
	User userA = getLoginBean().getCurrentUser();
	
	Criteria criteria = session.createCriteria(Friend.class);
	criteria.add(Restrictions.or(
		Restrictions.eq("userA", userA),
		Restrictions.eq("userB", userA)));
	criteria.add(Restrictions.or(
		Restrictions.eq("userA", u),
		Restrictions.eq("userB", u)));
	
	Friend friend = (Friend)criteria.uniqueResult();
	
	return friend != null;
    }
    
    public void sendRequest(){
	int id = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
	
	User userA = getLoginBean().getCurrentUser();
	User userB = getUserBean().getUserById(id);

	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
		
	try {	
	    if(userA.equals(userB)){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Vous ne pouvez pas vous ajouter vous-mêmes.", ""));
		return ;
	    }
	    
	    Criteria criteria = session.createCriteria(Friend.class);
	    criteria.add(Restrictions.or(
		    Restrictions.eq("userA", userA),
		    Restrictions.eq("userB", userA)));
	    criteria.add(Restrictions.or(
		    Restrictions.eq("userA", userB),
		    Restrictions.eq("userB", userB)));

	    Friend friend = (Friend)criteria.uniqueResult();
	    
	    if(friend != null) {
		if(friend.getUserA().equals(userA) && !friend.getAccepted()){
		    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "La demande a déjà été envoyée mais n'a pas encore été acceptée.", ""));
		} else if(friend.getUserA().equals(userB) && !friend.getAccepted()){
		    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Répondez à la demande d'ajout.", ""));
		} else {
		    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Vous êtes déjà amis.", ""));
		}
		
		return ;
	    }
	    
	    friend = new Friend();
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
	    if(friend.getAccepted()){
		friendsCounter--;
	    } else {
		friendRequestsCounter--;
	    }
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

    /**
     * @return the friendsCounter
     */
    public long getFriendsCounter() {
	return friendsCounter;
    }

    /**
     * @param friendsCounter the friendsCounter to set
     */
    public void setFriendsCounter(long friendsCounter) {
	this.friendsCounter = friendsCounter;
    }

    /**
     * @return the friendRequestsCounter
     */
    public long getFriendRequestsCounter() {
	return friendRequestsCounter;
    }

    /**
     * @param friendRequestsCounter the friendRequestsCounter to set
     */
    public void setFriendRequestsCounter(long friendRequestsCounter) {
	this.friendRequestsCounter = friendRequestsCounter;
    }
}
