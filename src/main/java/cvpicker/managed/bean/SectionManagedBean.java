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
 *
 * @author DAVID
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
	    
	    sectionsHashMap.remove(section);
	    sections.remove(section);
	    
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
    
    public void removeElement(Section section, Element element){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
		
	try {
	    tx = session.beginTransaction();
	    session.delete(element);
	    
	    sectionsHashMap.get(section).remove(element);
	    
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
    
    public void resetNewElementValues(){
	setElementDescription("");
	setElementTitle("");
	setElementInformations("");
	
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
    
    public List<Element> getElementsBySection(Section section){
	return sectionsHashMap.get(section);
    }
    
    public List<Element> getElementsBySectionFromDatabase(Section section){
	List<Element> result;
	
	Session session = HibernateUtil.getSessionFactory().openSession();
	
	Criteria criteria = session.createCriteria(Element.class);
	criteria.add(Restrictions.eq("section", section));
	criteria.addOrder(Order.asc("position"));
	
	result = criteria.list();
	
	return result;
    }
    
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
