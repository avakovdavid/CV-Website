/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.hibernate;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "FRIEND")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Friend.findAll", query = "SELECT f FROM Friend f"),
    @NamedQuery(name = "Friend.findById", query = "SELECT f FROM Friend f WHERE f.id = :id"),
    @NamedQuery(name = "Friend.findByAccepted", query = "SELECT f FROM Friend f WHERE f.accepted = :accepted")})
public class Friend implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "accepted")
    private boolean accepted;
    @JoinColumn(name = "userB", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userB;
    @JoinColumn(name = "userA", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userA;

    public Friend() {
    }

    public Friend(Integer id) {
	this.id = id;
    }

    public Friend(Integer id, boolean accepted) {
	this.id = id;
	this.accepted = accepted;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public boolean getAccepted() {
	return accepted;
    }

    public void setAccepted(boolean accepted) {
	this.accepted = accepted;
    }

    public User getUserB() {
	return userB;
    }

    public void setUserB(User userB) {
	this.userB = userB;
    }

    public User getUserA() {
	return userA;
    }

    public void setUserA(User userA) {
	this.userA = userA;
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
	if (!(object instanceof Friend)) {
	    return false;
	}
	Friend other = (Friend) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "cvpicker.hibernate.Friend[ id=" + id + " ]";
    }
    
}
