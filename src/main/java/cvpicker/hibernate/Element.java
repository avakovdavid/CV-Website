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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DAVID
 */
@Entity
@Table(name = "ELEMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Element.findAll", query = "SELECT e FROM Element e"),
    @NamedQuery(name = "Element.findById", query = "SELECT e FROM Element e WHERE e.id = :id"),
    @NamedQuery(name = "Element.findByTitle", query = "SELECT e FROM Element e WHERE e.title = :title"),
    @NamedQuery(name = "Element.findByDescription", query = "SELECT e FROM Element e WHERE e.description = :description"),
    @NamedQuery(name = "Element.findByDateStart", query = "SELECT e FROM Element e WHERE e.dateStart = :dateStart"),
    @NamedQuery(name = "Element.findByDateEnd", query = "SELECT e FROM Element e WHERE e.dateEnd = :dateEnd"),
    @NamedQuery(name = "Element.findByInformations", query = "SELECT e FROM Element e WHERE e.informations = :informations")})
public class Element implements Serializable {
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "position")
    private int position;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title")
    private String title;
    @Column(name = "dateStart")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStart;
    @Column(name = "dateEnd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnd;
    @Size(max = 255)
    @Column(name = "informations")
    private String informations;
    @JoinColumn(name = "section", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Section section;

    public Element() {
    }

    public Element(Integer id) {
	this.id = id;
    }

    public Element(Integer id, String title, Date dateStart) {
	this.id = id;
	this.title = title;
	this.dateStart = dateStart;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Date getDateStart() {
	return dateStart;
    }

    public void setDateStart(Date dateStart) {
	this.dateStart = dateStart;
    }

    public Date getDateEnd() {
	return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
	this.dateEnd = dateEnd;
    }

    public String getInformations() {
	return informations;
    }

    public void setInformations(String informations) {
	this.informations = informations;
    }

    public Section getSection() {
	return section;
    }

    public void setSection(Section section) {
	this.section = section;
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
	if (!(object instanceof Element)) {
	    return false;
	}
	Element other = (Element) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "cvpicker.hibernate.Element[ id=" + id + " ]";
    }

    public int getPosition() {
	return position;
    }

    public void setPosition(int position) {
	this.position = position;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }
    
}
