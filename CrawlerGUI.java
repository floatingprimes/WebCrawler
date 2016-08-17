package spyder;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CrawlerGUI extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		Scene myScene = new Scene(getPane(), 500, 100);
		primaryStage.setTitle("Web Crawler");
		primaryStage.setScene(myScene);
		primaryStage.show();

	}

	public BorderPane getPane(){

		BorderPane pane = new BorderPane();

		HBox myButtonPane = new HBox(10);

		TextField searchKey = new TextField("Word or phrase to crawl for");
		TextField seed_URL = new TextField("URL to begin at");

		Button search_Confirm = new Button("Crawl"); // Confirmation user's search String and seed URL are all set.

		search_Confirm.setOnAction(event -> {

			try {
				Crawler.crawl(seed_URL.getText(), searchKey.getText());
			} catch (Exception e) {				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});

		myButtonPane.getChildren().addAll(searchKey, seed_URL, search_Confirm);

		pane.setCenter(myButtonPane);

		return pane;

	}


}
