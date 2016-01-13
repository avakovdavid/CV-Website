/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cvpicker.managed.bean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author DAVID
 */
public class CropperManagedBean implements Serializable{
    
    private LoginManagedBean loginBean;
    
    private String uploadedImagePath;    
    private CroppedImage croppedImage;     
    private String newImageName;
 
    public CroppedImage getCroppedImage() {
        return croppedImage;
    }
 
    public void setCroppedImage(CroppedImage croppedImage) {
        this.croppedImage = croppedImage;
    }
 
    public void upload(FileUploadEvent event) throws IOException {
	UploadedFile uploadedFile = event.getFile();
	byte [] uploadedFileIS = uploadedFile.getContents();
	
	ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	
	String userImageDir = servletContext.getRealPath("")+ File.separator 
		+ "resources" + File.separator 
		+ "img" + File.separator 
		+ getLoginBean().getCurrentUser().getId() + File.separator;
	String newFileName = userImageDir + "original.jpg";
	
	FileImageOutputStream img;
        try {
	    File dir = new File(userImageDir);
	    File f = new File(newFileName);
	    
	    if(!dir.exists()){
		dir.mkdirs();
	    }
	    
	    if(!f.exists()){
		f.createNewFile();
	    }
	    
            img = new FileImageOutputStream(f);
            img.write(uploadedFileIS, 0, uploadedFileIS.length);
            img.close();
	    setUploadedImagePath(getLoginBean().getCurrentUser().getId() + File.separator + "original.jpg?"+new Date().getTime());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Upload image failed."));
        }
    }
    
    public void crop() {
        if(croppedImage == null) {
            return;
        }
         
        //setNewImageName(getRandomImageName());
	ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	String newFileName = servletContext.getRealPath("")+ File.separator + "resources" + File.separator + "img" + File.separator + getNewImageName() + ".jpg";
         
        FileImageOutputStream imageOutput;
        try {
	    File f = new File(newFileName);
	    if(!f.exists()){
		f.createNewFile();
	    }
            imageOutput = new FileImageOutputStream(f);
            imageOutput.write(croppedImage.getBytes(), 0, croppedImage.getBytes().length);
            imageOutput.close();
        } catch (Exception e) {
	    System.out.println("ooooh : " + e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cropping failed."));
        }
         
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Cropping finished."));
    }
         
    public String getNewImageName() {
        return newImageName;
    }
 
    public void setNewImageName(String newImageName) {
        this.newImageName = newImageName;
    }

    /**
     * @return the loginManagedBean
     */
    public LoginManagedBean getLoginManagedBean() {
	return getLoginBean();
    }

    /**
     * @param loginManagedBean the loginManagedBean to set
     */
    public void setLoginManagedBean(LoginManagedBean loginManagedBean) {
	this.setLoginBean(loginManagedBean);
    }

    /**
     * @return the loginBean
     */
    public LoginManagedBean getLoginBean() {
	return loginBean;
    }

    /**
     * @param loginBean the loginBean to set
     */
    public void setLoginBean(LoginManagedBean loginBean) {
	this.loginBean = loginBean;
    }

    /**
     * @return the uploadedImagePath
     */
    public String getUploadedImagePath() {
	return uploadedImagePath;
    }

    /**
     * @param uploadedImagePath the uploadedImagePath to set
     */
    public void setUploadedImagePath(String uploadedImagePath) {
	this.uploadedImagePath = uploadedImagePath;
    }
}
