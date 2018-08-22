package th.in.moe.devtools.codegenerator;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import th.in.moe.devtools.codegenerator.common.bean.GeneratorCriteria;
import th.in.moe.devtools.codegenerator.common.util.DialogUtils;
import th.in.moe.devtools.codegenerator.controller.MainPageController;
import th.in.moe.devtools.codegenerator.controller.RootLayoutController;

public class MainApp extends Application {
	
	private static final Logger logger = LoggerFactory.getLogger(MainApp.class);
	
	private static final String TITLE = "Moe Devtools::Code Generator";
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	private File currentFile;
	
	@Override
	public void start(Stage primaryStage) {
		logger.debug("Start Application");
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(TITLE);
		
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
			
			// Give the controller access to the main app.
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);
			
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
			controller.initForm();
			controller.setMainApp(this);
			mainPage.setUserData(controller);
			
			// Set MainPage into the center of RootLayout.
			rootLayout.setCenter(mainPage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initGeneratorCriteria() {
		MainPageController controller =  (MainPageController) rootLayout.getCenter().getUserData();
		controller.initForm();
	}
	
	/**
	 * Returns the generator criteria file preference, i.e. the file that was last opened.
	 * The preference is read from the OS specific registry.
	 * If no such preference can be found, null is returned.
	 * 
	 * @return
	 */
	public File getGeneratorCriteriaFilePath() {
		if (currentFile != null) {
			return currentFile;
		} else {
			return null;
		}
	}
	
	/**
	 * Sets the file path of the currently loaded file.
	 * The path is persisted in the OS specific registry.
	 * 
	 * @param file the file or null to remove the path
	 */
	public void setGeneratorCriteriaFilePath(File file) {
		if (file != null) {
			currentFile = file;
			
			// Update the stage title.
			primaryStage.setTitle(TITLE + " - " + file.getName());
		} else {
			currentFile = null;
			
			// Update the stage title.
			primaryStage.setTitle(TITLE);
		}
	}

	/**
	 * Loads generator criteria data from the specified file.
	 * The current person data will be replaced.
	 * 
	 * @param file
	 */
	public void loadGeneratorCriteriaFromFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(GeneratorCriteria.class);
			Unmarshaller um = context.createUnmarshaller();
			
			// Reading XML from the file and unmarshalling.
			GeneratorCriteria criteria = (GeneratorCriteria) um.unmarshal(file);
			
			MainPageController controller =  (MainPageController) rootLayout.getCenter().getUserData();
			controller.bindingField(criteria);
			
			// Save the file path to the registry.
			setGeneratorCriteriaFilePath(file);
		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());
			DialogUtils.createExpandableException(alert, e);
			alert.showAndWait();
		}
	}
	
	/**
	 * Saves the current generator criteria data to the specified file.
	 * 
	 * @param file
	 */
	public void saveGeneratorCriteriaDataToFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(GeneratorCriteria.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Prepare generator criteria data.
			MainPageController controller =  (MainPageController) rootLayout.getCenter().getUserData();
			GeneratorCriteria criteria = controller.bindingModel();

			// Marshalling and saving XML to the file.
			m.marshal(criteria, file);

			// Save the file path to the registry.
			setGeneratorCriteriaFilePath(file);
		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + file.getPath());
			DialogUtils.createExpandableException(alert, e);
			alert.showAndWait();
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
