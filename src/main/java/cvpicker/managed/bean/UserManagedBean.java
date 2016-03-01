package cvpicker.managed.bean;

import cvpicker.hibernate.Cv;
import cvpicker.hibernate.HibernateUtil;
import cvpicker.hibernate.Privacy;
import cvpicker.hibernate.User;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * User Managed Bean
 *
 *
 */
public class UserManagedBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String passwordRepeat;
    
    private LoginManagedBean loginBean;
    
    /**
     * Add User
     *
     * @return String - Response Message
     */
    public void save() {
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
		
	try {
	    User testUser = (User) session.createCriteria(User.class).add(Restrictions.eq("email", email)).uniqueResult();
	    if(testUser != null){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "L'utilisateur avec l'adresse '"+email+"' existe déjà", ""));
		return ;
	    }
	    Cv cv = new Cv();
	    cv.setTemplate("full_width");
	    
	    Privacy defaultPrivacy = (Privacy)session.createCriteria(Privacy.class).add(Restrictions.eq("value", "connected_user")).uniqueResult();

	    User user = new User();
	    user.setId(getId());
	    user.setFirstName(getFirstName());
	    user.setLastName(getLastName());
	    user.setEmail(getEmail());
	    user.setCv(cv);
	    user.setAddFriendPrivacy(defaultPrivacy);
	    user.setSendMessagePrivacy(defaultPrivacy);
	    user.setLastEditionDate(new Date());
	    
	    if(!getPassword().equals(getPasswordRepeat())){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Les deux mot de passes doivent être identiques", ""));
		return ;
	    }
	    
	    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	    messageDigest.update(getPassword().getBytes());
	    String encryptedString = new String(messageDigest.digest());
	    
	    user.setPassword(encryptedString);

	    tx = session.beginTransaction();
	    session.save(cv);
	    session.save(user);
	    tx.commit();
	    
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Votre profil a bien été créé.", ""));
	    
	    loginBean.setCurrentUser(user);	    
	} catch (Exception e) {
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Un problème est survenu sur le serveur. Veuillez réessayer ultérieurement.", ""));

	    if (tx != null) {
		tx.rollback();
	    }
	} finally {
	    session.close();
	}
    }
    
    public void update(){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
	try {
	    tx = session.beginTransaction();
	    loginBean.getCurrentUser().setLastEditionDate(new Date());
	    session.update(loginBean.getCurrentUser());
	    tx.commit();

	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Vos paramètres ont été mis à jour.", ""));
	} catch (Exception e) {
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Un problème est survenu sur le serveur. Veuillez réessayer ultérieurement.", ""));

	    if (tx != null) {
		tx.rollback();
	    }
	} finally {
	    session.close();
	}
    }
 
    public List<User> getUsers() {
	Session session = HibernateUtil.getSessionFactory().openSession();
	List<User>  userList = session.createCriteria(User.class).list();
	return userList;
    }
    
    public User getUserById(int id){
	Session session = HibernateUtil.getSessionFactory().openSession();
	User user = (User) session.createCriteria(User.class).add(Restrictions.eq("id", id)).uniqueResult();
	return user;
    }

    /**
     * Reset Fields
     *
     */
    public void reset() {
       this.setId(0);
       this.setFirstName("");
       this.setLastName("");
       this.setEmail("");
       this.setPassword("");
    }

    /**
     * Get User Id
     *
     * @return int - User Id
     */
    public int getId() {
	return id;
    }

    /**
     * Set User Id
     *
     * @param int - User Id
     */
    public void setId(int id) {
	this.id = id;
    }

    /**
     * Get User Name
     *
     * @return String - User Name
     */
    public String getFirstName() {
	return firstName;
    }

    /**
     * Get User Name
     *
     * @return String - User Name
     */
    public String getLastName() {
	return lastName;
    }

    /**
     * Set User Name
     *
     * @param String - User Name
     */
    public void setFirstName(String name) {
	this.firstName = name;
    }

    public void setLastName(String name) {
	this.lastName = name;
    }

    /**
     * Get User email
     *
     * @return String - User email
     */
    public String getEmail() {
	return email;
    }

    /**
     * Set User email
     *
     * @param String - User email
     */
    public void setEmail(String email) {
	this.email = email;
    }
 
    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }
    
    public String getPasswordRepeat() {
	return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
	this.passwordRepeat = passwordRepeat;
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