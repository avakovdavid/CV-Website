/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.managed.bean;

import cvpicker.hibernate.Friend;
import cvpicker.hibernate.HibernateUtil;
import cvpicker.hibernate.Skill;
import cvpicker.hibernate.User;
import cvpicker.hibernate.UserSkill;
import cvpicker.hibernate.UserSkillPK;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ReorderEvent;

/**
 *
 * @author DAVID
 */
public class UserSkillManagedBean implements Serializable{

    private String skillName;
    private int skillMark;
    private String skillDescription;
        
    private List<UserSkill> skills;
    
    private LoginManagedBean loginBean;
    
    @PostConstruct
    public void init(){
	Session session = HibernateUtil.getSessionFactory().openSession();
	
	Criteria criteria = session.createCriteria(UserSkill.class);
	criteria.add(Restrictions.eq("user", loginBean.getCurrentUser()));
	criteria.addOrder(Order.asc("position"));	
	
	setSkills(criteria.list());	
	session.close();
    }
    
    public List<UserSkill> getUserSkillsByUser(User u){
	List<UserSkill> result = new ArrayList<UserSkill>();
	
	Session session = HibernateUtil.getSessionFactory().openSession();
	
	Criteria criteria = session.createCriteria(UserSkill.class);
	criteria.add(Restrictions.eq("user", u));
	criteria.addOrder(Order.asc("position"));	
	
	result = criteria.list();
	
	session.close();
	System.out.println(u.getId());
	return result; 
    }
    
    public void addSkill(){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
		
	try {
	    tx = session.beginTransaction();
	    
	    //Vérifier si la compétence qu'on ajoute existe déjà dans la base
	    Criteria criteria = session.createCriteria(Skill.class);
	    criteria.add(Restrictions.eq("name", skillName));
	    Skill skill = (Skill)criteria.uniqueResult();
	    
	    if(skill==null){
		skill = new Skill();
		skill.setName(skillName);
		session.persist(skill);
		tx.commit();
		tx.begin();
	    }
	    
	    //Vérifier si l'utilisateur a déjà indiqué avoir cette compétence 
	    criteria = session.createCriteria(UserSkill.class);
	    criteria.add(Restrictions.eq("user", loginBean.getCurrentUser()));
	    criteria.add(Restrictions.eq("skill", skill));
	    UserSkill userSkill = (UserSkill)criteria.uniqueResult();
	    
	    if(userSkill==null){
		userSkill = new UserSkill();
		UserSkillPK uSkillPK = new UserSkillPK(loginBean.getCurrentUser().getId(), skill.getId());
		
		userSkill.setUserSkillPK(uSkillPK);
		userSkill.setUser(loginBean.getCurrentUser());
		userSkill.setSkill(skill);
		userSkill.setMark(skillMark);
		userSkill.setDescription(getSkillDescription());
		userSkill.setPosition(skills.size());
		
		session.save(userSkill);
		skills.add(userSkill);
		
		session.update(loginBean.getCurrentUser());
		tx.commit();
		
		RequestContext.getCurrentInstance().execute("PF('newSkillDialog').hide()");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Une nouvelle compétence a été ajoutée dans votre liste.", ""));
		
		resetNewSkillValues();
	    } else {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Les doublons ne sont pas autorisés.", ""));
	    }
	}  catch (Exception e) {
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Un problème est survenu sur le serveur. Veuillez réessayer ultérieurement.", ""));

	    if (tx != null) {
		tx.rollback();
	    }
	} finally {
	    session.close();
	}
    }
    
    public void removeSkill(UserSkill userSkill){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
		
	try {
	    tx = session.beginTransaction();
	    session.delete(userSkill);
	    
	    skills.remove(userSkill);
	    
	    session.update(loginBean.getCurrentUser());
	    tx.commit();
	    
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userSkill.getSkill().getName() + " a bien été supprimé de la liste.", ""));
	    
	} catch (Exception e) {
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Un problème est survenu sur le serveur. Veuillez réessayer ultérieurement.", ""));

	    if (tx != null) {
		tx.rollback();
	    }
	} finally {
	    session.close();
	}
    }
    
    public void swapSkills(ReorderEvent event){
	//When this method is called, two skills are already swaped in the skills array
	int from = event.getFromIndex();
	int to = event.getToIndex();
	
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
	
	try {
	    tx = session.beginTransaction();
	    
	    UserSkill skillFrom = skills.get(from);
	    UserSkill skillTo = skills.get(to);
	    
	    skillFrom.setPosition(from);
	    skillTo.setPosition(to);
	    
	    session.update("position", skillFrom);
	    session.update("position", skillTo);
	    
	    tx.commit();	    
	} catch (Exception e) {
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Un problème est survenu sur le serveur. Veuillez réessayer ultérieurement.", ""));

	    if (tx != null) {
		tx.rollback();
	    }
	} finally {
	    session.close();
	}
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
     * @return the skillName
     */
    public String getSkillName() {
	return skillName;
    }

    /**
     * @param skillName the skillName to set
     */
    public void setSkillName(String skillName) {
	this.skillName = skillName;
    }

    /**
     * @return the skillMark
     */
    public int getSkillMark() {
	return skillMark;
    }

    /**
     * @param skillMark the skillMark to set
     */
    public void setSkillMark(int skillMark) {
	this.skillMark = skillMark;
    }

    /**
     * @return the skills
     */
    public List<UserSkill> getSkills() {
	return skills;
    }

    /**
     * @param skills the skills to set
     */
    public void setSkills(List<UserSkill> skills) {
	this.skills = skills;
    }

    /**
     * @return the skillDescription
     */
    public String getSkillDescription() {
	return skillDescription;
    }

    /**
     * @param skillDescription the skillDescription to set
     */
    public void setSkillDescription(String skillDescription) {
	this.skillDescription = skillDescription;
    }

    private void resetNewSkillValues() {
	skillName = "";
	skillDescription = "";
	skillMark = 0;
    }
}
