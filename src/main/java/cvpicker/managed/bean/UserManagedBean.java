package cvpicker.managed.bean;

import cvpicker.hibernate.HibernateUtil;
import cvpicker.hibernate.User;
import java.io.Serializable;
import java.security.MessageDigest;
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

    /**
     * Add User
     *
     * @return String - Response Message
     */
    public void save() {
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
	
	try {
	    User user = new User();
	    user.setId(getId());
	    user.setFirstName(getFirstName());
	    user.setLastName(getLastName());
	    user.setEmail(getEmail());
	    
	    if(!getPassword().equals(getPasswordRepeat())){
		FacesContext.getCurrentInstance().addMessage("registration-form", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Les deux mot de passes doivent être identiques", "Les deux mot de passes doivent être identiques"));
		return ;
	    }
	    
	    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	    messageDigest.update(getPassword().getBytes());
	    String encryptedString = new String(messageDigest.digest());
	    
	    user.setPassword(encryptedString);

	    tx = session.beginTransaction();
	    session.save(user);
	    tx.commit();
	    
	    FacesContext.getCurrentInstance().addMessage("registration-form", new FacesMessage(FacesMessage.SEVERITY_INFO, "Votre profil a bien été créé.", "Votre profil a bien été créé."));
	    
	} catch (Exception e) {
	    FacesContext.getCurrentInstance().addMessage("registration-form", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Un problème est survenu sur le serveur. Veuillez réessayer ultérieurement.", "Un problème est survenu sur le serveur. Veuillez réessayer ultérieurement."));

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
}