package spyder;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CrawlerGUI extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception, InterruptedException {
		// TODO Auto-generated method stub
		
		Scene myScene = new Scene(getPane(), 900, 500);
		primaryStage.setTitle("Web Crawler");
		primaryStage.setScene(myScene);
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(event -> {
			
				System.exit(0);
			
			
		});

	}

	public BorderPane getPane(){
		
		BorderPane pane = new BorderPane(); // Pane to return
		
		VBox myButtonPane = new VBox(10); // Pane for Radio Buttons
		VBox txtFileChoices = new VBox(5); 
		VBox txtFilePane = new VBox(10);
		
		HBox keyWordPane = new HBox(5); // Pane to include key word search field AND description Text
		HBox urlPane = new HBox(5); // Pane to include all things relating to the URL Seed to use
		HBox txtFilePathPane = new HBox(5); // Pane to include all things relating to the txt file path
		
		HBox myURLCountPane = new HBox(10);
		
		TextField searchKey = new TextField(); 
		TextField seed_URL = new TextField();
		TextField pathToTxtFile = new TextField();
		
		Button search_Confirm = new Button("Crawl"); // Confirmation user's search String and seed URL are all set.
		
		Text txtChoices = new Text("Do you want to write resulting web crawler hits to a txt file?");
		
		final ToggleGroup myToggleGroup = new ToggleGroup();
				
		RadioButton write_Button = new RadioButton("Write to File");
		RadioButton noWrite_Button = new RadioButton("Don't Write to File");
		
		write_Button.setToggleGroup(myToggleGroup);
		noWrite_Button.setToggleGroup(myToggleGroup);
		
		Text descriptionOf_Key = new Text("Insert your word or phrase to Crawl for:");
		Text descriptionOf_URL = new Text("Insert the seed URL:");
		Text descriptionOf_TxtFile = new Text("Insert txt file path: ");
		
		keyWordPane.getChildren().addAll(descriptionOf_Key, searchKey);
		urlPane.getChildren().addAll(descriptionOf_URL, seed_URL);
		txtFilePathPane.getChildren().addAll(descriptionOf_TxtFile, pathToTxtFile);
		
		
		List<String> newsURLs = new ArrayList<String>();
		newsURLs.add("http://www.cnn.com/");
		newsURLs.add("http://www.foxnews.com/");
		newsURLs.add("http://www.msnbc.com/");
		newsURLs.add("http://www.bbc.com/");
		newsURLs.add("http://abcnews.go.com/");
		newsURLs.add("http://www.nbcnews.com/");
		RadioButton newsButton = new RadioButton("News Sources");
		
		List<String> techSites = new ArrayList<String>();
		techSites.add("https://techcrunch.com/");
		techSites.add("http://www.techradar.com/");
		techSites.add("https://www.wired.com/");
		techSites.add("https://news.ycombinator.com/");
		RadioButton techButton = new RadioButton("Tech Sources");
		
		RadioButton noneButton = new RadioButton("My own set of seed URLs");
		
		ToggleGroup mySiteGroup = new ToggleGroup();
		
		newsButton.setToggleGroup(mySiteGroup);
		techButton.setToggleGroup(mySiteGroup);
		noneButton.setToggleGroup(mySiteGroup);
		
		noneButton.setSelected(true);

		
		VBox templateChecks = new VBox(5);
		templateChecks.getChildren().add(newsButton);
		templateChecks.getChildren().add(techButton);
		templateChecks.getChildren().add(noneButton);
		templateChecks.setPadding(new Insets(10,10,10,10));

		
		search_Confirm.setOnAction(event -> {
			

		if(write_Button.isSelected())
		{
			
			/*
			 * If the write to txt file button is selected, then we call the corresponding
			 * write to txt file crawl method.
			 */
			
			Integer myFlag = new Integer(0);
			
			if(newsButton.isSelected())
			{
				
				for(int i = 0; i < newsURLs.size(); i++)
				{
					
					Thread thread = new Thread(new SpyderTask(new Spyder(myFlag, newsURLs.get(i), searchKey.getText(), pathToTxtFile.getText()))); 
					thread.start();
				}
				
			}
			else if(techButton.isSelected())
			{
				
				for(int i = 0; i < techSites.size(); i++)
				{
					
					Thread thread = new Thread(new SpyderTask(new Spyder(myFlag, techSites.get(i), searchKey.getText(), pathToTxtFile.getText())));
					thread.start();
					
				}
				
			}
			else // no write button is selected
			{
				try
				{
					Spyder mySpyder = new Spyder(myFlag, seed_URL.getText(), searchKey.getText(), pathToTxtFile.getText());
					SpyderTask myTask = new SpyderTask(mySpyder);
					Thread thread = new Thread(myTask);
					thread.start();
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
				
			
		}
		else
		{ 
			
			/*
			 * If the button indicating no Write is selected, we call the crawl
			 * method that does not write to a .txt file. Default if none are selected is
			 * also noWrite Button
			 */
			
			Integer myFlag = new Integer(1);
		
		if(newsButton.isSelected()){	

			for(int i = 0; i < newsURLs.size(); i++)
			{
				
				Thread thread = new Thread(new SpyderTask(new Spyder(myFlag, newsURLs.get(i), searchKey.getText()))); 
				thread.start();
			}
			
		}
		else if(techButton.isSelected())
		{
			
			for(int i = 0; i < techSites.size(); i++)
			{
				
				Thread thread = new Thread(new SpyderTask(new Spyder(myFlag, techSites.get(i), searchKey.getText())));
				thread.start();
				
			}
			
		}
		else // no write button is selected
		{
			try
			{
				Spyder mySpyder = new Spyder(myFlag, seed_URL.getText(), searchKey.getText());
				SpyderTask myTask = new SpyderTask(mySpyder);
				Thread thread = new Thread(myTask);
				thread.start();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		}
		});
		
		HBox searchConfirmAndHitcount = new HBox(10);
		searchConfirmAndHitcount.getChildren().addAll(search_Confirm, myURLCountPane);
		
		searchConfirmAndHitcount.setPadding(new Insets(10,10,10,10));
		
		myButtonPane.getChildren().addAll(keyWordPane, urlPane, txtFilePathPane, searchConfirmAndHitcount); 
		txtFileChoices.getChildren().addAll(write_Button, noWrite_Button);
		txtFilePane.getChildren().addAll(txtChoices, txtFileChoices);
		
		myButtonPane.setPadding(new Insets(10,10,10,10));
		txtFilePane.setPadding(new Insets(10,10,10,10));
		
		txtFilePane.setAlignment(Pos.BOTTOM_CENTER);
		
		pane.setTop(txtFilePane);
		pane.setCenter(myButtonPane);
		pane.setBottom(templateChecks);
		
		return pane;
		
	}
	

}