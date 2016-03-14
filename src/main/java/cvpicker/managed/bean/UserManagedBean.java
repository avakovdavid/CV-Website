package cvpicker.managed.bean;

import cvpicker.hibernate.Cv;
import cvpicker.hibernate.HibernateUtil;
import cvpicker.hibernate.Privacy;
import cvpicker.hibernate.User;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.imageio.stream.FileImageInputStream;
import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

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
    
    private StreamedContent photoByUser;
    
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
	    cv.setTemplate("full-width");
	    
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
    
    public StreamedContent getPhotoByUser() throws FileNotFoundException, IOException{
	FacesContext context = FacesContext.getCurrentInstance();
	
	if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            String userId = context.getExternalContext().getRequestParameterMap().get("userId");
	    	    
	    User user = getUserById(Integer.parseInt(userId));
	    
	    if(user.getPhotoOriginal() == null){
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String img = servletContext.getRealPath("")+ File.separator 
		+ "img" + File.separator 
		+ "gallery" + File.separator 
		+ "gallery-img-1-4col-cir.jpg";
		
		File f = new File(img);
		FileImageInputStream stream = new FileImageInputStream(f);
		
		byte[] b = IOUtils.toByteArray(new FileInputStream(f));
		
		return new DefaultStreamedContent(new ByteArrayInputStream(b), "image/png");
	    }
	    
	    InputStream is = new ByteArrayInputStream(user.getPhotoOriginal());

	    return new DefaultStreamedContent(is, "image/png");
	}
    }
    
    public void upload(FileUploadEvent event) throws IOException {
	UploadedFile uploadedFile = event.getFile();
	byte [] uploadedFileIS = uploadedFile.getContents();	
	
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = null;
	try {
	    tx = session.beginTransaction();
	    loginBean.getCurrentUser().setPhotoOriginal(uploadedFileIS);
	    session.update(loginBean.getCurrentUser());
	    tx.commit();
	    //photoByUser.setBytes(loginBean.getCurrentUser().getPhotoOriginal());
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Votre photo a bien été chargée.", ""));
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
    
    public List<User> getPopularUsers(){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Criteria criteria = session.createCriteria(User.class).createCriteria("cv");
	criteria.addOrder(Order.desc("views"));
	criteria.setMaxResults(5);
	
	List<User> userList = criteria.list();
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