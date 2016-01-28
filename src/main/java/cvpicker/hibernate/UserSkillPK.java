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

/**
 *
 * @author DAVID
 */
@Embeddable
public class UserSkillPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "userId")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "skillId")
    private int skillId;

    public UserSkillPK() {
    }

    public UserSkillPK(int userId, int skillId) {
	this.userId = userId;
	this.skillId = skillId;
    }

    public int getUserId() {
	return userId;
    }

    public void setUserId(int userId) {
	this.userId = userId;
    }

    public int getSkillId() {
	return skillId;
    }

    public void setSkillId(int skillId) {
	this.skillId = skillId;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (int) userId;
	hash += (int) skillId;
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof UserSkillPK)) {
	    return false;
	}
	UserSkillPK other = (UserSkillPK) object;
	if (this.userId != other.userId) {
	    return false;
	}
	if (this.skillId != other.skillId) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "cvpicker.hibernate.UserSkillPK[ userId=" + userId + ", skillId=" + skillId + " ]";
    }
    
}
