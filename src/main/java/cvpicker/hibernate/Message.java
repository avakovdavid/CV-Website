/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.hibernate;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DAVID
 */
@Entity
@Table(name = "MESSAGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Message.findAll", query = "SELECT m FROM Message m"),
    @NamedQuery(name = "Message.findById", query = "SELECT m FROM Message m WHERE m.id = :id"),
    @NamedQuery(name = "Message.findByDateCreation", query = "SELECT m FROM Message m WHERE m.dateCreation = :dateCreation"),
    @NamedQuery(name = "Message.findBySubject", query = "SELECT m FROM Message m WHERE m.subject = :subject"),
    @NamedQuery(name = "Message.findByContent", query = "SELECT m FROM Message m WHERE m.content = :content"),
    @NamedQuery(name = "Message.findByWasRead", query = "SELECT m FROM Message m WHERE m.wasRead = :wasRead"),
    @NamedQuery(name = "Message.findByDeletedByFromU", query = "SELECT m FROM Message m WHERE m.deletedByFromU = :deletedByFromU"),
    @NamedQuery(name = "Message.findByDeletedByToU", query = "SELECT m FROM Message m WHERE m.deletedByToU = :deletedByToU")})
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "dateCreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    @Basic(optional = false)
    @Column(name = "subject")
    private String subject;
    @Basic(optional = false)
    @Column(name = "content")
    private String content;
    @Basic(optional = false)
    @Column(name = "wasRead")
    private boolean wasRead;
    @Basic(optional = false)
    @Column(name = "deletedByFromU")
    private boolean deletedByFromU;
    @Basic(optional = false)
    @Column(name = "deletedByToU")
    private boolean deletedByToU;
    @JoinColumn(name = "toU", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User toU;
    @JoinColumn(name = "fromU", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User fromU;

    public Message() {
    }

    public Message(Integer id) {
	this.id = id;
    }

    public Message(Integer id, Date dateCreation, String subject, String content, boolean wasRead, boolean deletedByFromU, boolean deletedByToU) {
	this.id = id;
	this.dateCreation = dateCreation;
	this.subject = subject;
	this.content = content;
	this.wasRead = wasRead;
	this.deletedByFromU = deletedByFromU;
	this.deletedByToU = deletedByToU;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Date getDateCreation() {
	return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
	this.dateCreation = dateCreation;
    }

    public String getSubject() {
	return subject;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public boolean getWasRead() {
	return wasRead;
    }

    public void setWasRead(boolean wasRead) {
	this.wasRead = wasRead;
    }

    public boolean getDeletedByFromU() {
	return deletedByFromU;
    }

    public void setDeletedByFromU(boolean deletedByFromU) {
	this.deletedByFromU = deletedByFromU;
    }

    public boolean getDeletedByToU() {
	return deletedByToU;
    }

    public void setDeletedByToU(boolean deletedByToU) {
	this.deletedByToU = deletedByToU;
    }

    public User getToU() {
	return toU;
    }

    public void setToU(User toU) {
	this.toU = toU;
    }

    public User getFromU() {
	return fromU;
    }

    public void setFromU(User fromU) {
	this.fromU = fromU;
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
	if (!(object instanceof Message)) {
	    return false;
	}
	Message other = (Message) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "cvpicker.hibernate.Message[ id=" + id + " ]";
    }
    
}
