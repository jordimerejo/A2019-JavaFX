/*
 * Copyright (c) 2019 Automation Anywhere.
 * All rights reserved.
 *
 * This software is the proprietary information of Automation Anywhere.
 * You shall use it only in accordance with the terms of the license agreement
 * you entered into with Automation Anywhere.
 */
/**
 * 
 */
package com.automationanywhere.botcommand.sk;

import static com.automationanywhere.commandsdk.model.AttributeType.SELECT;
import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

import java.awt.Insets;
import java.util.concurrent.Semaphore;
import javax.swing.JFrame;

import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;




/**
 * @author Stefan Karsten
 *
 */

@BotCommand
@CommandPkg(label="Alert", name="Alert", description="Shows Alert message", icon="",
node_label="Alert")
public class Alert  {
	
	private static Semaphore sem;
	private JFrame frame = null;
	
	@Execute
	public void action(@Idx(index = "1", type = TEXT) @Pkg(label = "Message", default_value_type = STRING) @NotEmpty String message,
					   @Idx(index = "2", type = SELECT, options = {
								@Idx.Option(index = "2.1", pkg = @Pkg(label = "Error", value = "ERROR")),
								@Idx.Option(index = "2.2", pkg = @Pkg(label = "Information", value = "INFORMATION")),
								@Idx.Option(index = "2.3", pkg = @Pkg(label = "Warning", value = "WARNING")),
						}) @Pkg(label = "Alert Type", default_value = "INFORMATION", default_value_type = STRING) @NotEmpty String type) throws Exception
	{
		
		
		  if (this.sem == null) {
		      this.sem = new Semaphore(1);		    
		  }

		   initAndShowGUI(message,type);
		   this.sem.acquire();
		   this.sem.release();
    
	}


    private  void initAndShowGUI(String message,String type) throws Exception  {
        // This method is invoked on the JavaFX thread
    	 this.sem.acquire();
	        Platform.runLater(new Runnable() {
	            @Override
	            public void run() {
	                initFX(message,type);
	            }
	       });
	        

	        
    }

  private  void initFX(String message, String type) {
	  
    
      javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.valueOf(type));
      alert.setTitle(type);
      alert.setHeaderText(null);
      alert.setContentText(message);

      DialogPane root = alert.getDialogPane();
      root.setStyle("-fx-font-size: 12pt;");
      Stage dialogStage = new Stage(StageStyle.UTILITY);
      

      dialogStage.setOnCloseRequest((WindowEvent event1) -> {
          dialogStage.close();
          quit();
      });
      for (ButtonType buttonType : root.getButtonTypes()) {
          ButtonBase button = (ButtonBase) root.lookupButton(buttonType);
          button.setStyle("-fx-font-size: 12pt;");
          button.setOnAction(evt -> {
              dialogStage.close();
              quit();
          });
      }
      
      
      root.getScene().setRoot(new Group());
      Scene scene = new Scene(root);	
      dialogStage.setScene(scene);
      dialogStage.setTitle(type);
      dialogStage.initModality(Modality.APPLICATION_MODAL);
      dialogStage.setAlwaysOnTop(true);
      dialogStage.setResizable(false);
      dialogStage.showAndWait();
   }


	private  void quit() {
		 this.sem.release();
	}
}

	

