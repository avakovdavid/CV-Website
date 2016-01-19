/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.managed.bean;

import cvpicker.hibernate.HibernateUtil;
import cvpicker.hibernate.Message;
import cvpicker.hibernate.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author DAVID
 */
public class MessageManagedBean implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private LoginManagedBean loginBean;
    
    private int id;
    private Date date;
    private String subject;
    private String content;
    private User fromU;
    private User toU;
    private String message;
    private String typeMessage;
    
    private long newMsgCounter;
    private Message selectedMsg;

    
    public List<User> completeToU(String q) {  
        List<User> results;  
	Session session = HibernateUtil.getSessionFactory().openSession();
	
	Criteria criteria = session.createCriteria(User.class);
	criteria.add(Restrictions.ne("id", loginBean.getCurrentUser().getId()));
	criteria.add(Restrictions.or(Restrictions.like("firstName", q, MatchMode.ANYWHERE), Restrictions.like("lastName", q, MatchMode.ANYWHERE)));
	results = criteria.list();
	session.close();
	
        return results;  
    } 
    
    /**
     *
     * @return
     */
    public String send(){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
	
	try {
	    Message msg = new Message();
	    msg.setDateCreation(new Date());
	    msg.setSubject(getSubject());
	    msg.setContent(getContent());
	    msg.setFromU(getLoginBean().getCurrentUser());
	    msg.setToU(getToU());

	    tx = session.beginTransaction();
	    session.save(msg);
	    tx.commit();
	    
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Votre message a bien été envoyé.", ""));

	    reset();
	} catch (Exception e) {
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Un problème est survenu sur le serveur. Veuillez réessayer ultérieurement.", ""));
	    if (tx != null) {
		tx.rollback();
	    }
	} finally {
	    session.close();
	}
	
	return "";
    }
    
    public List<Message> receivedList(){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Criteria criteria = session.createCriteria(Message.class);
	criteria.add(Restrictions.eq("toU", getLoginBean().getCurrentUser()));
	criteria.add(Restrictions.eq("deletedByToU", false));
	criteria.addOrder(Order.desc("dateCreation"));
	List<Message>  msgList = criteria.list();
	return msgList;	
    }
    
    public List<Message> sendedList(){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Criteria criteria = session.createCriteria(Message.class);
	criteria.add(Restrictions.eq("fromU", getLoginBean().getCurrentUser()));
	criteria.add(Restrictions.eq("deletedByFromU", false));
	criteria.addOrder(Order.desc("dateCreation"));
	List<Message>  msgList = criteria.list();
	return msgList;	
    }
    
    /**
     * Reset Fields
     *
     */
    public void reset() {
       this.setContent("");
       this.setDate(null);
       this.setFromU(null);
       this.setToU(null);
       this.setSubject("");
    }
    
    /**
     * @return the date
     */
    public Date getDate() {
	return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
	this.date = date;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
	return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
	this.subject = subject;
    }

    /**
     * @return the content
     */
    public String getContent() {
	return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
	this.content = content;
    }

    /**
     * @return the fromU
     */
    public User getFromU() {
	return fromU;
    }

    /**
     * @param fromU the fromU to set
     */
    public void setFromU(User fromU) {
	this.fromU = fromU;
    }

    /**
     * @return the toU
     */
    public User getToU() {
	return toU;
    }

    /**
     * @param toU the toU to set
     */
    public void setToU(User toU) {
	this.toU = toU;
    }

    /**
     * @return the loginBean
     */
    public LoginManagedBean getLoginBean() {
	return loginBean;
    }

    /**
     * @param loginBean the bean to set
     */
    public void setLoginBean(LoginManagedBean loginBean) {
	this.loginBean = loginBean;
    }

    /**
     * @return the selectedMsg
     */
    public Message getSelectedMsg() {
	return selectedMsg;
    }

    /**
     * @param selectedMsg the selectedMsg to set
     */
    public void setSelectedMsg(Message selectedMsg) {
	this.selectedMsg = selectedMsg;
	if(selectedMsg.getFromU() != loginBean.getCurrentUser() && !selectedMsg.getWasRead()){
	    selectedMsg.setWasRead(true);
	    Session session = HibernateUtil.getSessionFactory().openSession();
	    Transaction tx = session.beginTransaction();
	    session.saveOrUpdate(selectedMsg);
	    tx.commit();
	    session.close();
	}
    }

    /**
     * @return the newMsgCounter
     */
    public long getNewMsgCounter() {
	long result = 0;
	
	Session session = HibernateUtil.getSessionFactory().openSession();
	try{
	    Criteria criteria = session.createCriteria(Message.class);
	    criteria.add(Restrictions.eq("toU", getLoginBean().getCurrentUser()));
	    criteria.add(Restrictions.eq("wasRead", false));
	    criteria.add(Restrictions.eq("deletedByToU", false));
	    criteria.setProjection(Projections.rowCount());
	    result = (Long)criteria.uniqueResult();

	    setNewMsgCounter(result);
	} catch(Exception e){

	} finally{
	    session.close();
	}
	return result;
    }

    /**
     * @param newMsgCounter the newMsgCounter to set
     */
    public void setNewMsgCounter(long newMsgCounter) {
	this.newMsgCounter = newMsgCounter;
    }
    
    public void removeMsg(int id){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Criteria criteria = session.createCriteria(Message.class);
	criteria.add(Restrictions.eq("id", id));
	Message msg = (Message)criteria.uniqueResult();
	
	if(msg == null){
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Message introuvable", ""));
	    return ;
	}

	if(msg.getFromU().equals(loginBean.getCurrentUser())){
	    msg.setDeletedByFromU(true);
	}else{
	    msg.setDeletedByToU(true);
	}
	
	Transaction tx = null;
	
	try {
	    tx = session.beginTransaction();
	    if(msg.getDeletedByToU() && msg.getDeletedByFromU()){
		session.delete(msg);
	    }else{
		session.saveOrUpdate(msg);
	    }
	    
	    tx.commit();
	    
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Votre message a bien été supprimé.", ""));
	} catch (Exception e) {
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Une erreur s'est produite. Le message n'a pas pu être supprimé.", ""));
	    if (tx != null) {
		tx.rollback();
	    }
	} finally {
	    session.close();
	}
    }
    
}
