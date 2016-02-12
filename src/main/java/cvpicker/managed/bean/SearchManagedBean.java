/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.managed.bean;

import cvpicker.hibernate.HibernateUtil;
import cvpicker.hibernate.User;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author DAVID
 */
public class SearchManagedBean implements Serializable{
    private LoginManagedBean loginBean;
    private String query;
    
    private List<User> result;
    
    public String doSearch(){
	if(query.length() > 0){
	    String[] splitQuery = query.split(" ");

	    Session session = HibernateUtil.getSessionFactory().openSession();
	    Criteria criteria = session.createCriteria(User.class);

	    for(String s : splitQuery){
		criteria.add(Restrictions.or(
			Restrictions.like("firstName", s, MatchMode.ANYWHERE),
			Restrictions.like("lastName", s, MatchMode.ANYWHERE)));
	    }

	    result = criteria.list();
	    return "/search.xhtml?faces-redirect=true";
	}
	return "";
    }

    /**
     * @return the query
     */
    public String getQuery() {
	return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(String query) {
	this.query = query;
    }

    /**
     * @return the result
     */
    public List<User> getResult() {
	return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(List<User> result) {
	this.result = result;
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
