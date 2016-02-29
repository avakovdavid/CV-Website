/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.managed.bean;

import cvpicker.hibernate.HibernateUtil;
import cvpicker.hibernate.Privacy;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author DAVID
 */
public class PrivacyManagedBean implements Serializable{
    
    private Privacy[] privacyLevels;
    
    private LoginManagedBean loginBean;
    
    @PostConstruct
    public void init(){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Criteria criteria = session.createCriteria(Privacy.class);
	List<Privacy> l = criteria.list();
	privacyLevels = new Privacy[l.size()];
	privacyLevels = (Privacy[]) l.toArray(privacyLevels);
    }

    public Privacy getPrivacyById(int id){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Privacy privacy = (Privacy) session.createCriteria(Privacy.class).add(Restrictions.eq("id", id)).uniqueResult();
	return privacy;
    }
    
    /**
     * @return the privacyLevels
     */
    public Privacy[] getPrivacyLevels() {
	return privacyLevels;
    }

    /**
     * @param privacyLevels the privacyLevels to set
     */
    public void setPrivacyLevels(Privacy[] privacyLevels) {
	this.privacyLevels = privacyLevels;
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
