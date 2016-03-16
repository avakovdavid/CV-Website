/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.hibernate;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "USER_SKILL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserSkill.findAll", query = "SELECT u FROM UserSkill u"),
    @NamedQuery(name = "UserSkill.findByUserId", query = "SELECT u FROM UserSkill u WHERE u.userSkillPK.userId = :userId"),
    @NamedQuery(name = "UserSkill.findBySkillId", query = "SELECT u FROM UserSkill u WHERE u.userSkillPK.skillId = :skillId"),
    @NamedQuery(name = "UserSkill.findByMark", query = "SELECT u FROM UserSkill u WHERE u.mark = :mark"),
    @NamedQuery(name = "UserSkill.findByDescription", query = "SELECT u FROM UserSkill u WHERE u.description = :description")})
public class UserSkill implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "position")
    private int position;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UserSkillPK userSkillPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mark")
    private int mark;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;
    @JoinColumn(name = "skillId", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Skill skill;

    public UserSkill() {
    }

    public UserSkill(UserSkillPK userSkillPK) {
	this.userSkillPK = userSkillPK;
    }

    public UserSkill(UserSkillPK userSkillPK, int mark, String description) {
	this.userSkillPK = userSkillPK;
	this.mark = mark;
	this.description = description;
    }

    public UserSkill(int userId, int skillId) {
	this.userSkillPK = new UserSkillPK(userId, skillId);
    }

    public UserSkillPK getUserSkillPK() {
	return userSkillPK;
    }

    public void setUserSkillPK(UserSkillPK userSkillPK) {
	this.userSkillPK = userSkillPK;
    }

    public int getMark() {
	return mark;
    }

    public void setMark(int mark) {
	this.mark = mark;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Skill getSkill() {
	return skill;
    }

    public void setSkill(Skill skill) {
	this.skill = skill;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (userSkillPK != null ? userSkillPK.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof UserSkill)) {
	    return false;
	}
	UserSkill other = (UserSkill) object;
	if ((this.userSkillPK == null && other.userSkillPK != null) || (this.userSkillPK != null && !this.userSkillPK.equals(other.userSkillPK))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "cvpicker.hibernate.UserSkill[ userSkillPK=" + userSkillPK + " ]";
    }

    public int getPosition() {
	return position;
    }

    public void setPosition(int position) {
	this.position = position;
    }
    
}
