package eParticipation.backend.backoffice;

import java.io.IOException;
import java.io.InputStream;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import eParticipation.backend.service.InsigniaService;

@Named("fileUploadController")
@RequestScoped
public class ManagerUpload {
	
	private byte[] logo;
    @EJB 
    private InsigniaService i;
    
    public void upload(FileUploadEvent event) {
        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);        
        UploadedFile file = event.getFile();
        // Do what you want with the file
        try {
            copyFile(event.getFile().getFileName(), file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyFile(String fileName, InputStream in) {
        try {        	        
        	logo = IOUtils.toByteArray(in);
            System.out.println("New file uploaded: " + (fileName));
            i.sendLogo(logo);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }    
    
}