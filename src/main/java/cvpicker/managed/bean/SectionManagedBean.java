/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.managed.bean;

import cvpicker.hibernate.Element;
import cvpicker.hibernate.HibernateUtil;
import cvpicker.hibernate.Section;
import cvpicker.hibernate.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
 * Sections Managed Bean
 * Add, remove, swap, get list 
 */
public class SectionManagedBean implements Serializable{

    private String sectionTitle;
    
    private HashMap<Section, List<Element>> sectionsHashMap = new HashMap<Section, List<Element>>();
    
    private List<Section> sections;
    
    
    private Section sectionWhereAddElement;
    private String elementTitle;
    private String elementDescription;
    private String elementInformations;
    private Date elementDateStart;
    private Date elementDateEnd;
    
    private LoginManagedBean loginBean;
    
    @PostConstruct
    public void init(){
	Session session = HibernateUtil.getSessionFactory().openSession();
	
	Criteria criteria = session.createCriteria(Section.class);
	criteria.add(Restrictions.eq("user", getLoginBean().getCurrentUser()));
	criteria.addOrder(Order.asc("position"));	
	
	sections = criteria.list();
	
	Criteria criteriaForElements;
	for(Section s : sections){
	    criteriaForElements = session.createCriteria(Element.class);
	    criteriaForElements.add(Restrictions.eq("section", s));
	    criteriaForElements.addOrder(Order.asc("position"));
	    
	    sectionsHashMap.put(s, criteriaForElements.list());
	}
	
		
	session.close();
    }
    
    /**
     * Get sections list by user
     * @param u user to get sections for 
     * @return 
     */
    public List<Section> getUserSectionsByUser(User u){
	List<Section> result;
	
	Session session = HibernateUtil.getSessionFactory().openSession();
	
	Criteria criteria = session.createCriteria(Section.class);
	criteria.add(Restrictions.eq("user", u));
	criteria.addOrder(Order.asc("position"));	
	
	result = criteria.list();

	session.close();
	return result; 
    }
    
    /**
     * Add a new section
     */
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
	    sections.add(section);
	    sectionsHashMap.put(section, new ArrayList<Element>());
	    loginBean.getCurrentUser().setLastEditionDate(new Date());

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

    /**
     * Clear values in the section form
     */
    private void resetNewSectionValues() {
	setSectionTitle("");
    }
    
    /**
     * Remove section
     * @param section section to remove
     */
    public void removeSection(Section section){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
		
	try {
	    tx = session.beginTransaction();
	    session.delete(section);
	    
	    sectionsHashMap.remove(section);
	    sections.remove(section);
	    loginBean.getCurrentUser().setLastEditionDate(new Date());
	    
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
    
    /**
     * Swap sections
     * @param event 
     */
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
	    
	    loginBean.getCurrentUser().setLastEditionDate(new Date());
	    session.update("lastEditionDate", loginBean.getCurrentUser());
	    
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
     * Add new element in section
     */
    public void addElement(){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
	System.out.println(elementTitle);
	try {
	    tx = session.beginTransaction();
	    
	    Element element = new Element();
	    element.setSection(sectionWhereAddElement);
	    element.setTitle(elementTitle);
	    element.setDescription(elementDescription);
	    element.setDateStart(elementDateStart);
	    element.setDateEnd(elementDateEnd);
	    element.setInformations(elementInformations);
	    element.setPosition(sectionsHashMap.get(sectionWhereAddElement).size());
	    
	    session.save(element);
	    sectionsHashMap.get(sectionWhereAddElement).add(element);
	    
	    loginBean.getCurrentUser().setLastEditionDate(new Date());
	    session.update("lastEditionDate", loginBean.getCurrentUser());
	    
	    tx.commit();

	    RequestContext.getCurrentInstance().execute("PF('newElementDialog').hide()");
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Un nouvel élément a bien été ajouté dans la séction \"" + sectionWhereAddElement.getTitle() + "\".", ""));

	    resetNewElementValues();
	}  catch (Exception e) {
	    System.out.println(e);
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Un problème est survenu sur le serveur. Veuillez réessayer ultérieurement.", ""));

	    if (tx != null) {
		tx.rollback();
	    }
	} finally {
	    session.close();
	}
    }
    
    /**
     * Remove element from section
     * @param section section to remove element from
     * @param element element to remove
     */
    public void removeElement(Section section, Element element){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
		
	try {
	    tx = session.beginTransaction();
	    session.delete(element);
	    
	    sectionsHashMap.get(section).remove(element);
	    
	    loginBean.getCurrentUser().setLastEditionDate(new Date());
	    session.update("lastEditionDate", loginBean.getCurrentUser());
	    
	    tx.commit();
	    
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "L'élément '" + element.getTitle() + "' a bien été supprimé de la séction '" + section.getTitle() + "'.", ""));
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
     * Swap elements
     * @param event 
     */
    public void swapElements(ReorderEvent event){
	//When this method is called, two elements are already swaped in the skills array
	int from = event.getFromIndex();
	int to = event.getToIndex();
	
	if(from > to){
	    int tmp = from;
	    from = to;
	    to = tmp;
	}
	
	int sectionPosition = Integer.parseInt(event.getComponent().getParent().getClientId().split(":")[2]);
	Section concernedSection = sections.get(sectionPosition);
	List<Element> concernedElementsList = sectionsHashMap.get(concernedSection);
		
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
	
	try {
	    tx = session.beginTransaction();
	    
	    Element elementFrom = concernedElementsList.get(from);
	    Element elementTo = concernedElementsList.get(to);
	    
	    elementFrom.setPosition(from);
	    elementTo.setPosition(to);
	    
	    session.update("position", elementFrom);
	    session.update("position", elementTo);
	    
	    Element e;
	    for(int i=from+1; i<to; i++){
		e = concernedElementsList.get(i);
		e.setPosition(e.getPosition()+1);
		session.update("position", e);
	    }
	    
	    loginBean.getCurrentUser().setLastEditionDate(new Date());
	    session.update("lastEditionDate", loginBean.getCurrentUser());
	    
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
     * Clear values in the element form
     */
    public void resetNewElementValues(){
	setElementDescription("");
	setElementTitle("");
	setElementInformations("");
	setElementDateStart(null);
	setElementDateEnd(null);
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
    
    /**
     * Get elements for section
     * @param section section to get elements from
     * @return 
     */
    public List<Element> getElementsBySection(Section section){
	return sectionsHashMap.get(section);
    }
    
    /**
     * Same that getElementsBySection(). This one refresh the list from database.
     * @param section
     * @return 
     */
    public List<Element> getElementsBySectionFromDatabase(Section section){
	List<Element> result;
	
	Session session = HibernateUtil.getSessionFactory().openSession();
	
	Criteria criteria = session.createCriteria(Element.class);
	criteria.add(Restrictions.eq("section", section));
	criteria.addOrder(Order.asc("position"));
	
	result = criteria.list();
	
	return result;
    }
    
    /**
     * Method to set the section where new element will be inserted
     * @param section 
     */
    public void prepareElementDialog(Section section){
	sectionWhereAddElement = section;	
    }

    /**
     * @return the elementTitle
     */
    public String getElementTitle() {
	return elementTitle;
    }

    /**
     * @param elementTitle the elementTitle to set
     */
    public void setElementTitle(String elementTitle) {
	this.elementTitle = elementTitle;
    }

    /**
     * @return the elementDescription
     */
    public String getElementDescription() {
	return elementDescription;
    }

    /**
     * @param elementDescription the elementDescription to set
     */
    public void setElementDescription(String elementDescription) {
	this.elementDescription = elementDescription;
    }

    /**
     * @return the elementInformations
     */
    public String getElementInformations() {
	return elementInformations;
    }

    /**
     * @param elementInformations the elementInformations to set
     */
    public void setElementInformations(String elementInformations) {
	this.elementInformations = elementInformations;
    }

    /**
     * @return the elementDateStart
     */
    public Date getElementDateStart() {
	return elementDateStart;
    }

    /**
     * @param elementDateStart the elementDateStart to set
     */
    public void setElementDateStart(Date elementDateStart) {
	this.elementDateStart = elementDateStart;
    }

    /**
     * @return the elementDateEnd
     */
    public Date getElementDateEnd() {
	return elementDateEnd;
    }

    /**
     * @param elementDateEnd the elementDateEnd to set
     */
    public void setElementDateEnd(Date elementDateEnd) {
	this.elementDateEnd = elementDateEnd;
    }
}
