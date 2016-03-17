/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.managed.bean;

import cvpicker.hibernate.Privacy;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class PrivacyConverter implements Converter{
    
    private PrivacyManagedBean privacyBean;
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
	return string.length() == 0 ? null : getPrivacyBean().getPrivacyById(Integer.parseInt(string));
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
	if (o == null || o.equals("")) {  
            return "";  
        } else {
            return String.valueOf(((Privacy) o).getId());  
        }  
    }

    /**
     * @return the userBean
     */
    public PrivacyManagedBean getPrivacyBean() {
	return privacyBean;
    }

    /**
     * @param userBean the userBean to set
     */
    public void setPrivacyBean(PrivacyManagedBean privacyBean) {
	this.privacyBean = privacyBean;
    }
}