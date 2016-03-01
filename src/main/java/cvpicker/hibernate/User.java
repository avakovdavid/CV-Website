/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.hibernate;

import cvpicker.hibernate.Message;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DAVID
 */
@Entity
@Table(name = "USER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByFirstName", query = "SELECT u FROM User u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "User.findByLastName", query = "SELECT u FROM User u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password")})
public class User implements Serializable {
    @JoinColumn(name = "appearOnSearchPrivacy", referencedColumnName = "id")
    @ManyToOne
    private Privacy appearOnSearchPrivacy;
    @JoinColumn(name = "addFriendPrivacy", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Privacy addFriendPrivacy;
    @JoinColumn(name = "sendMessagePrivacy", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Privacy sendMessagePrivacy;
    @JoinColumn(name = "sectionsPrivacy", referencedColumnName = "id")
    @ManyToOne
    private Privacy sectionsPrivacy;
    @JoinColumn(name = "skillsPrivacy", referencedColumnName = "id")
    @ManyToOne
    private Privacy skillsPrivacy;
    @JoinColumn(name = "globalInfoPriavcy", referencedColumnName = "id")
    @ManyToOne
    private Privacy globalInfoPriavcy;
    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @Size(max = 255)
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Size(max = 255)
    @Column(name = "website")
    private String website;
    @Size(max = 255)
    @Column(name = "address")
    private String address;
    @Column(name = "drivingLicence")
    private Boolean drivingLicence;
    @Column(name = "car")
    private Boolean car;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Section> sectionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<UserSkill> userSkillList;
    @JoinColumn(name = "cv", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cv cv;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userB")
    private List<Friend> friendList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userA")
    private List<Friend> friendList1;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "firstName")
    private String firstName;
    @Basic(optional = false)
    @Column(name = "lastName")
    private String lastName;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @JoinTable(name = "FRIEND", joinColumns = {
        @JoinColumn(name = "userA", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "userB", referencedColumnName = "id")})
    @ManyToMany
    private List<User> userList;
    @ManyToMany(mappedBy = "userList")
    private List<User> userList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "toU")
    private List<Message> messageList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fromU")
    private List<Message> messageList1;

    public User() {
    }

    public User(Integer id) {
	this.id = id;
    }

    public User(Integer id, String firstName, String lastName, String email, String password) {
	this.id = id;
	this.firstName = firstName;
	this.lastName = lastName;
	this.email = email;
	this.password = password;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    @XmlTransient
    public List<User> getUserList() {
	return userList;
    }

    public void setUserList(List<User> userList) {
	this.userList = userList;
    }

    @XmlTransient
    public List<User> getUserList1() {
	return userList1;
    }

    public void setUserList1(List<User> userList1) {
	this.userList1 = userList1;
    }

    @XmlTransient
    public List<Message> getMessageList() {
	return messageList;
    }

    public void setMessageList(List<Message> messageList) {
	this.messageList = messageList;
    }

    @XmlTransient
    public List<Message> getMessageList1() {
	return messageList1;
    }

    public void setMessageList1(List<Message> messageList1) {
	this.messageList1 = messageList1;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (id != null ? id.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof User)) {
	    return false;
	}
	User other = (User) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "cvpicker.hibernate.User[ id=" + id + " ]";
    }

    @XmlTransient
    public List<Friend> getFriendList() {
	return friendList;
    }

    public void setFriendList(List<Friend> friendList) {
	this.friendList = friendList;
    }

    @XmlTransient
    public List<Friend> getFriendList1() {
	return friendList1;
    }

    public void setFriendList1(List<Friend> friendList1) {
	this.friendList1 = friendList1;
    }

    public Cv getCv() {
	return cv;
    }

    public void setCv(Cv cv) {
	this.cv = cv;
    }

    @XmlTransient
    public List<UserSkill> getUserSkillList() {
	return userSkillList;
    }

    public void setUserSkillList(List<UserSkill> userSkillList) {
	this.userSkillList = userSkillList;
    }

    @XmlTransient
    public List<Section> getSectionList() {
	return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
	this.sectionList = sectionList;
    }

    public Date getBirthday() {
	return birthday;
    }

    public void setBirthday(Date birthday) {
	this.birthday = birthday;
    }

    public String getPhoneNumber() {
	return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
	return website;
    }

    public void setWebsite(String website) {
	this.website = website;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public Boolean getDrivingLicence() {
	return drivingLicence;
    }

    public void setDrivingLicence(Boolean drivingLicence) {
	this.drivingLicence = drivingLicence;
    }

    public Boolean getCar() {
	return car;
    }

    public void setCar(Boolean car) {
	this.car = car;
    }

    public Privacy getAppearOnSearchPrivacy() {
	return appearOnSearchPrivacy;
    }

    public void setAppearOnSearchPrivacy(Privacy appearOnSearchPrivacy) {
	this.appearOnSearchPrivacy = appearOnSearchPrivacy;
    }

    public Privacy getAddFriendPrivacy() {
	return addFriendPrivacy;
    }

    public void setAddFriendPrivacy(Privacy addFriendPrivacy) {
	this.addFriendPrivacy = addFriendPrivacy;
    }

    public Privacy getSendMessagePrivacy() {
	return sendMessagePrivacy;
    }

    public void setSendMessagePrivacy(Privacy sendMessagePrivacy) {
	this.sendMessagePrivacy = sendMessagePrivacy;
    }

    public Privacy getSectionsPrivacy() {
	return sectionsPrivacy;
    }

    public void setSectionsPrivacy(Privacy sectionsPrivacy) {
	this.sectionsPrivacy = sectionsPrivacy;
    }

    public Privacy getSkillsPrivacy() {
	return skillsPrivacy;
    }

    public void setSkillsPrivacy(Privacy skillsPrivacy) {
	this.skillsPrivacy = skillsPrivacy;
    }

    public Privacy getGlobalInfoPriavcy() {
	return globalInfoPriavcy;
    }

    public void setGlobalInfoPriavcy(Privacy globalInfoPriavcy) {
	this.globalInfoPriavcy = globalInfoPriavcy;
    }
    
}
