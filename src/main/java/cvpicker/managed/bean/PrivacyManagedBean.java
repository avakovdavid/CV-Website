/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.managed.bean;

import cvpicker.hibernate.Friend;
import cvpicker.hibernate.HibernateUtil;
import cvpicker.hibernate.Privacy;
import cvpicker.hibernate.User;
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
    private FriendManagedBean friendBean;
    
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
    
    public boolean isAccessibleGlobalInfoOf(User user){
	Privacy privacyLevel = user.getGlobalInfoPriavcy();
	return testPrivacyAccess(user, privacyLevel);
    }
    
    public boolean isAccessibleSkillsOf(User user){
	Privacy privacyLevel = user.getSkillsPrivacy();
	return testPrivacyAccess(user, privacyLevel);
    }
    
    public boolean isAccessibleSectionsOf(User user){
	Privacy privacyLevel = user.getSectionsPrivacy();
	return testPrivacyAccess(user, privacyLevel);
    }
    
    public boolean isAccessibleSendMessagesOf(User user){
	Privacy privacyLevel = user.getSendMessagePrivacy();
	return testPrivacyAccess(user, privacyLevel);
    }
    
    public boolean isAccessibleAddFriendOf(User user){
	Privacy privacyLevel = user.getAddFriendPrivacy();
	return testPrivacyAccess(user, privacyLevel);
    }
    
    public boolean isAccessibleOnSearchOf(User user){
	Privacy privacyLevel = user.getAppearOnSearchPrivacy();
	return testPrivacyAccess(user, privacyLevel);
    }
    
    private boolean testPrivacyAccess(User user, Privacy privacyLevel){
	if(privacyLevel == null || loginBean.getCurrentUser().equals(user)){
	    return true;
	}
	
	if(privacyLevel.getValue().equals("connected_user")){
	    return loginBean.getCurrentUser() != null;
	}
	
	if(privacyLevel.getValue().equals("friend")){
	    return getFriendBean().alreadyFriendWith(user);
	}
	
	if(privacyLevel.getValue().equals("none")){
	    return false;
	}
	
	return false;	
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

    /**
     * @return the friendBean
     */
    public FriendManagedBean getFriendBean() {
	return friendBean;
    }

    /**
     * @param friendBean the friendBean to set
     */
    public void setFriendBean(FriendManagedBean friendBean) {
	this.friendBean = friendBean;
    }
}
