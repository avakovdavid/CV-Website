/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.hibernate;

import cvpicker.hibernate.Message;
import java.io.Serializable;
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
    
}
