/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.hibernate;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class ConsulterPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "cvId")
    private int cvId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "sessionId")
    private String sessionId;

    public ConsulterPK() {
    }

    public ConsulterPK(int cvId, String sessionId) {
	this.cvId = cvId;
	this.sessionId = sessionId;
    }

    public int getCvId() {
	return cvId;
    }

    public void setCvId(int cvId) {
	this.cvId = cvId;
    }

    public String getSessionId() {
	return sessionId;
    }

    public void setSessionId(String sessionId) {
	this.sessionId = sessionId;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (int) cvId;
	hash += (sessionId != null ? sessionId.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof ConsulterPK)) {
	    return false;
	}
	ConsulterPK other = (ConsulterPK) object;
	if (this.cvId != other.cvId) {
	    return false;
	}
	if ((this.sessionId == null && other.sessionId != null) || (this.sessionId != null && !this.sessionId.equals(other.sessionId))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "cvpicker.hibernate.ConsulterPK[ cvId=" + cvId + ", sessionId=" + sessionId + " ]";
    }
    
}
