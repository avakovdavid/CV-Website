/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.managed.bean;

import cvpicker.hibernate.User;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;




/**
 *
 * @author DAVID
 */
//@FacesConverter("UserConverter")
public class UserConverter implements Converter{
    
    private UserManagedBean userBean;
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
	return getUserBean().getUserById(Integer.parseInt(string));
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
	if (o == null || o.equals("")) {  
            return "";  
        } else {
            return String.valueOf(((User) o).getId());  
        }  
    }

    /**
     * @return the userBean
     */
    public UserManagedBean getUserBean() {
	return userBean;
    }

    /**
     * @param userBean the userBean to set
     */
    public void setUserBean(UserManagedBean userBean) {
	this.userBean = userBean;
    }
    
    
}