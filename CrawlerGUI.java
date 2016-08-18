package spyder;

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
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		Scene myScene = new Scene(getPane(), 700, 250);
		primaryStage.setTitle("Web Crawler");
		primaryStage.setScene(myScene);
		primaryStage.show();

	}

	public BorderPane getPane(){

		BorderPane pane = new BorderPane(); // Pane to return

		VBox myButtonPane = new VBox(10); // Pane for Radio Buttons
		VBox txtFileChoices = new VBox(5);
		VBox txtFilePane = new VBox(10);

		HBox keyWordPane = new HBox(5); // Pane to include key word search field AND description Text
		HBox urlPane = new HBox(5); // Pane to include all things relating to the URL Seed to use
		HBox txtFilePathPane = new HBox(5); // Pane to include all things relating to the txt file path

		TextField searchKey = new TextField();
		TextField seed_URL = new TextField();
		TextField pathToTxtFile = new TextField();

		Button search_Confirm = new Button("Crawl"); // Confirmation user's search String and seed URL are all set.

		Text txtChoices = new Text("Do you want to write resulting web crawler hits to a txt file?");

		final ToggleGroup myToggleGroup = new ToggleGroup();

		RadioButton write_Button = new RadioButton("Write to File");
		RadioButton noWrite_Button = new RadioButton("Don't Write to File");

		Text descriptionOf_Key = new Text("Insert your word or phrase to Crawl for:");
		Text descriptionOf_URL = new Text("Insert the seed URL:");
		Text descriptionOf_TxtFile = new Text("Insert txt file path: ");

		keyWordPane.getChildren().addAll(descriptionOf_Key, searchKey);
		urlPane.getChildren().addAll(descriptionOf_URL, seed_URL);
		txtFilePathPane.getChildren().addAll(descriptionOf_TxtFile, pathToTxtFile);

		write_Button.setToggleGroup(myToggleGroup);
		noWrite_Button.setToggleGroup(myToggleGroup);

		search_Confirm.setOnAction(event -> {

		if(noWrite_Button.isSelected()){

			/*
			 * If the button indicating no Write is selected, we call the crawl
			 * method that does not write to a txt file.
			 */

			try {
				Crawler.crawl(seed_URL.getText(), searchKey.getText());
			} catch (Exception e) {				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else if(write_Button.isSelected()){

			/*
			 * If the write to txt file button is selected, then we call the corresponding
			 * write to txt file crawl method.
			 */

			try{
				Crawler.crawl(seed_URL.getText(), searchKey.getText(), pathToTxtFile.getText());
			} catch (Exception ex){
				ex.printStackTrace();
			}


		} else{
			/*
			 * Else neither button is selected.
			 */
		}
		});


		myButtonPane.getChildren().addAll(keyWordPane, urlPane, txtFilePathPane, search_Confirm);
		txtFileChoices.getChildren().addAll(write_Button, noWrite_Button);
		txtFilePane.getChildren().addAll(txtChoices, txtFileChoices);

		myButtonPane.setPadding(new Insets(10,10,10,10));
		txtFilePane.setPadding(new Insets(10,10,10,10));

		txtFilePane.setAlignment(Pos.BOTTOM_CENTER);

		pane.setTop(txtFilePane);
		pane.setCenter(myButtonPane);

		return pane;

	}


}
