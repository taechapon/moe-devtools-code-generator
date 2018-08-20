package th.in.moe.devtools.codegenerator;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import th.in.moe.devtools.codegenerator.view.MainPageController;

public class MainApp extends Application {
	
	private static final Logger logger = LoggerFactory.getLogger(MainApp.class);
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	
	@Override
	public void start(Stage primaryStage) {
		logger.debug("Start Application");
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Moe Devtools - Code Generator");

		initRootLayout();

		showMainPage();
	}
	
	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows the MainPage inside the RootLayout.
	 */
	public void showMainPage() {
		try {
			// Load MainPage.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/MainPage.fxml"));
			AnchorPane mainPage = (AnchorPane) loader.load();
			
			MainPageController controller = loader.getController();
			controller.initialize();
			controller.setMainApp(this);
			
			// Set MainPage into the center of RootLayout.
			rootLayout.setCenter(mainPage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
