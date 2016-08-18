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

		BorderPane pane = new BorderPane();

		VBox myButtonPane = new VBox(10);
		VBox txtFileChoices = new VBox(5);
		VBox txtFilePane = new VBox(10);

		HBox keyWordPane = new HBox(5);
		HBox urlPane = new HBox(5);
		HBox txtFilePathPane = new HBox(5);

		TextField searchKey = new TextField();
		TextField seed_URL = new TextField();
		TextField pathToTxtFile = new TextField();

		Button search_Confirm = new Button("Crawl"); // Confirmation user's search String and seed URL are all set.

		Text txtChoices = new Text("Do you want to write resulting web crawler hits to a txt file?");

		final ToggleGroup myToggleGroup = new ToggleGroup();

		RadioButton write_Button = new RadioButton("Write to File");
		RadioButton noWrite_Button = new RadioButton("Don't Write to File");

		Text descriptionOf_Key = new Text("Insert your word or phrase to Search for:");
		Text descriptionOf_URL = new Text("Insert the seed URL:");
		Text descriptionOf_TxtFile = new Text("Insert txt file path: ");

		keyWordPane.getChildren().addAll(descriptionOf_Key, searchKey);
		urlPane.getChildren().addAll(descriptionOf_URL, seed_URL);
		txtFilePathPane.getChildren().addAll(descriptionOf_TxtFile, pathToTxtFile);

		write_Button.setToggleGroup(myToggleGroup);
		noWrite_Button.setToggleGroup(myToggleGroup);

		search_Confirm.setOnAction(event -> {

			try {
				Crawler.crawl(seed_URL.getText(), searchKey.getText());
			} catch (Exception e) {				// TODO Auto-generated catch block
				e.printStackTrace();
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
