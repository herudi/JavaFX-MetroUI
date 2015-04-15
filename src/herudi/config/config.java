/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package herudi.config;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.omg.CORBA.portable.ValueFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author herudi-pc
 */
public class config {
    private ApplicationContext applicationContext;
    private static config provider;

    public config() throws ExceptionInInitializerError {
        try {
            this.applicationContext = new ClassPathXmlApplicationContext("appContex.xml");
        } catch (BeansException ex) {
            System.err.print("error " + ex);
        }
    }

    public synchronized static config getInstance() throws ExceptionInInitializerError {
        config tempProvider;
        if (provider == null) {
            provider = new config();
            tempProvider = provider;
        }else if(provider.getApplicationContext()==null){
            provider=new config();
            tempProvider=provider;
        }else{
            tempProvider=provider;
        }

        return tempProvider;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    
}
