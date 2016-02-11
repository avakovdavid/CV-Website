package cvpicker.managed.bean;

import cvpicker.hibernate.Cv;
import cvpicker.hibernate.HibernateUtil;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
	Cv cv = loginBean.getCurrentUser().getCv();
	title = cv.getTitle();
	description = cv.getDescription();
	
	setTemplates(new String[]{"full-width","right-sidebar","left-sidebar","double-sidebar"});
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
