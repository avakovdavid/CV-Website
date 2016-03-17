/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.hibernate;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "PRIVACY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Privacy.findAll", query = "SELECT p FROM Privacy p"),
    @NamedQuery(name = "Privacy.findById", query = "SELECT p FROM Privacy p WHERE p.id = :id"),
    @NamedQuery(name = "Privacy.findByValue", query = "SELECT p FROM Privacy p WHERE p.value = :value"),
    @NamedQuery(name = "Privacy.findByTitle", query = "SELECT p FROM Privacy p WHERE p.title = :title")})
public class Privacy implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "value")
    private String value;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title")
    private String title;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appearOnSearchPrivacy")
    private List<User> userList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "addFriendPrivacy")
    private List<User> userList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sendMessagePrivacy")
    private List<User> userList2;
    @OneToMany(mappedBy = "sectionsPrivacy")
    private List<User> userList3;
    @OneToMany(mappedBy = "skillsPrivacy")
    private List<User> userList4;
    @OneToMany(mappedBy = "globalInfoPriavcy")
    private List<User> userList5;

    public Privacy() {
    }

    public Privacy(Integer id) {
	this.id = id;
    }

    public Privacy(Integer id, String value, String title) {
	this.id = id;
	this.value = value;
	this.title = title;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
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
    public List<User> getUserList2() {
	return userList2;
    }

    public void setUserList2(List<User> userList2) {
	this.userList2 = userList2;
    }

    @XmlTransient
    public List<User> getUserList3() {
	return userList3;
    }

    public void setUserList3(List<User> userList3) {
	this.userList3 = userList3;
    }

    @XmlTransient
    public List<User> getUserList4() {
	return userList4;
    }

    public void setUserList4(List<User> userList4) {
	this.userList4 = userList4;
    }

    @XmlTransient
    public List<User> getUserList5() {
	return userList5;
    }

    public void setUserList5(List<User> userList5) {
	this.userList5 = userList5;
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
	if (!(object instanceof Privacy)) {
	    return false;
	}
	Privacy other = (Privacy) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "cvpicker.hibernate.Privacy[ id=" + id + " ]";
    }
    
}
