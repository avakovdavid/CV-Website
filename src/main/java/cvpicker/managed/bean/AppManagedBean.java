/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.managed.bean;

import cvpicker.hibernate.HibernateUtil;
import cvpicker.hibernate.Privacy;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * App Managed Bean
 * Will insert some data to database the first time
 */
public class AppManagedBean implements Serializable{
    
    @PostConstruct
    public void init() {
        Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
	
	try {
	    Criteria criteria = session.createCriteria(Privacy.class);
	    if(criteria.list().isEmpty()){
		tx = session.beginTransaction();
		session.save(new Privacy(1, "connected_user", "Utilisateurs connectés"));
		session.save(new Privacy(2, "friend", "Amis"));
		session.save(new Privacy(3, "none", "Personne"));
		tx.commit();
	    }		
	} catch (Exception e) {
	    if (tx != null) {
		tx.rollback();
	    }
	} finally {
	    session.close();
	}
    }
    
    public void test() {
        System.err.println("MY TEST");
    }
}
