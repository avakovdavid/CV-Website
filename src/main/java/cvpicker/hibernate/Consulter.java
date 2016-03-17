/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.hibernate;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "CONSULTER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Consulter.findAll", query = "SELECT c FROM Consulter c"),
    @NamedQuery(name = "Consulter.findByCvId", query = "SELECT c FROM Consulter c WHERE c.consulterPK.cvId = :cvId"),
    @NamedQuery(name = "Consulter.findBySessionId", query = "SELECT c FROM Consulter c WHERE c.consulterPK.sessionId = :sessionId")})
public class Consulter implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ConsulterPK consulterPK;
    @JoinColumn(name = "cvId", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cv cv;

    public Consulter() {
    }

    public Consulter(ConsulterPK consulterPK) {
	this.consulterPK = consulterPK;
    }

    public Consulter(int cvId, String sessionId) {
	this.consulterPK = new ConsulterPK(cvId, sessionId);
    }

    public ConsulterPK getConsulterPK() {
	return consulterPK;
    }

    public void setConsulterPK(ConsulterPK consulterPK) {
	this.consulterPK = consulterPK;
    }

    public Cv getCv() {
	return cv;
    }

    public void setCv(Cv cv) {
	this.cv = cv;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (consulterPK != null ? consulterPK.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof Consulter)) {
	    return false;
	}
	Consulter other = (Consulter) object;
	if ((this.consulterPK == null && other.consulterPK != null) || (this.consulterPK != null && !this.consulterPK.equals(other.consulterPK))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "cvpicker.hibernate.Consulter[ consulterPK=" + consulterPK + " ]";
    }
    
}
