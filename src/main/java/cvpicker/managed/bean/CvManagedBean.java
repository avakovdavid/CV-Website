package cvpicker.managed.bean;

import cvpicker.hibernate.Consulter;
import cvpicker.hibernate.ConsulterPK;
import cvpicker.hibernate.Cv;
import cvpicker.hibernate.HibernateUtil;
import cvpicker.hibernate.User;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author DAVID
 */
public class CvManagedBean implements Serializable{

    private String title;
    private String description;
    
    private String[] templates;

    private LoginManagedBean loginBean;
    
    @PostConstruct
    public void init(){
	if(loginBean.getCurrentUser() != null){
	    Cv cv = loginBean.getCurrentUser().getCv();
	    title = cv.getTitle();
	    description = cv.getDescription();

	    setTemplates(new String[]{"full-width","right-sidebar","left-sidebar","double-sidebar"});
	}
    }
    
    public void update(){
	Session session = HibernateUtil.getSessionFactory().openSession();
	
	Cv cv = loginBean.getCurrentUser().getCv();
	
	Transaction tx = null;
		
	try {	
	    cv.setTitle(getTitle());
	    cv.setDescription(getDescription());

	    tx = session.beginTransaction();
	    session.update(cv);
	    loginBean.getCurrentUser().setLastEditionDate(new Date());
	    session.update("lastEditionDate", loginBean.getCurrentUser());
	    tx.commit();
	    
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Le CV a bien été mis à jour.", ""));
	    
	} catch (Exception e) {
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Un problème est survenu sur le serveur. Veuillez réessayer ultérieurement.", ""));

	    if (tx != null) {
		tx.rollback();
	    }
	} finally {
	    session.close();
	}
    }
    
    public void registerTheView(Cv cv){
	Session session = HibernateUtil.getSessionFactory().openSession();
	User user = loginBean.getCurrentUser();
	
	FacesContext fCtx = FacesContext.getCurrentInstance();
	HttpSession s = (HttpSession) fCtx.getExternalContext().getSession(false);
	String sessionId = s.getId();
	
	//if the current user is visiting his own cv
	if(user != null && user.getCv().equals(cv)){
	    return ;
	}
	
	Transaction tx = null;
		
	try {
	    tx = session.beginTransaction();
	    	
	    Consulter consulter;
	    
	    ConsulterPK cpk = new ConsulterPK();
	    cpk.setCvId(cv.getId());
	    cpk.setSessionId(sessionId);
	    
	    Criteria criteria = session.createCriteria(Consulter.class);
	    criteria.add(Restrictions.eq("consulterPK", cpk));
	    
	    if(criteria.uniqueResult() == null){
		consulter = new Consulter(cpk);

		session.update(cv);
		session.saveOrUpdate(consulter);

		cv.setViews(cv.getViews()+1);
		session.update(cv);
	    }
	    tx.commit();
	} catch (Exception e) {
	    if (tx != null) {
		tx.rollback();
	    }
	} finally {
	    session.close();
	}
    }
    
    public int getViewsCounter(){
	Session session = HibernateUtil.getSessionFactory().openSession();
	session.refresh(loginBean.getCurrentUser().getCv());
	session.close();
	return loginBean.getCurrentUser().getCv().getViews();
    }
    
    /**
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
	this.description = description;
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
     * @return the templates
     */
    public String[] getTemplates() {
	return templates;
    }

    /**
     * @param templates the templates to set
     */
    public void setTemplates(String[] templates) {
	this.templates = templates;
    }
}
