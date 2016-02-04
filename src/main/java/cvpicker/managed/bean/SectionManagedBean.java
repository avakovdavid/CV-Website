/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.managed.bean;

import cvpicker.hibernate.HibernateUtil;
import cvpicker.hibernate.Section;
import cvpicker.hibernate.UserSkill;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.Criteria;
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
public class SectionManagedBean implements Serializable{

    private String sectionTitle;
    
    private List<Section> sections;
    
    private LoginManagedBean loginBean;
    
    @PostConstruct
    public void init(){
	Session session = HibernateUtil.getSessionFactory().openSession();
	
	Criteria criteria = session.createCriteria(Section.class);
	criteria.add(Restrictions.eq("user", getLoginBean().getCurrentUser()));
	criteria.addOrder(Order.asc("position"));	
	
	setSections((List<Section>) criteria.list());	
	session.close();
    }
    
    public void addSection(){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
		
	try {
	    tx = session.beginTransaction();
	    
	    Section section = new Section();
	    section.setUser(getLoginBean().getCurrentUser());
	    section.setTitle(getSectionTitle());
	    section.setPosition(sections.size());
	    
	    session.save(section);
	    getSections().add(section);
	    
	    session.update(getLoginBean().getCurrentUser());
	    tx.commit();

	    RequestContext.getCurrentInstance().execute("PF('newSectionDialog').hide()");
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Une nouvelle séction a été ajoutée dans votre liste.", ""));

	    resetNewSectionValues();
	}  catch (Exception e) {
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Un problème est survenu sur le serveur. Veuillez réessayer ultérieurement.", ""));

	    if (tx != null) {
		tx.rollback();
	    }
	} finally {
	    session.close();
	}
    }

    private void resetNewSectionValues() {
	setSectionTitle("");
    }
    
    public void removeSection(Section section){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
		
	try {
	    tx = session.beginTransaction();
	    session.delete(section);
	    
	    getSections().remove(section);
	    
	    session.update(getLoginBean().getCurrentUser());
	    tx.commit();
	    
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "La séction '" + section.getTitle() + "' a bien été supprimé de la liste.", ""));
	    
	} catch (Exception e) {
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Un problème est survenu sur le serveur. Veuillez réessayer ultérieurement.", ""));

	    if (tx != null) {
		tx.rollback();
	    }
	} finally {
	    session.close();
	}
    }
    
    public void swapSections(ReorderEvent event){
	//When this method is called, two skills are already swaped in the skills array
	int from = event.getFromIndex();
	int to = event.getToIndex();
	
	if(from > to){
	    int tmp = from;
	    from = to;
	    to = tmp;
	}
	
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
	
	try {
	    tx = session.beginTransaction();
	    
	    Section sectionFrom = sections.get(from);
	    Section sectionTo = sections.get(to);
	    
	    sectionFrom.setPosition(from);
	    sectionTo.setPosition(to);
	    
	    session.update("position", sectionFrom);
	    session.update("position", sectionTo);
	    
	    Section s;
	    for(int i=from+1; i<to; i++){
		s = sections.get(i);
		s.setPosition(s.getPosition()+1);
		session.update("position", s);
	    }
	    
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
     * @return the sectionTitle
     */
    public String getSectionTitle() {
	return sectionTitle;
    }

    /**
     * @param sectionTitle the sectionTitle to set
     */
    public void setSectionTitle(String sectionTitle) {
	this.sectionTitle = sectionTitle;
    }

    /**
     * @return the sections
     */
    public List<Section> getSections() {
	return sections;
    }

    /**
     * @param sections the sections to set
     */
    public void setSections(List<Section> sections) {
	this.sections = sections;
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
    
}
