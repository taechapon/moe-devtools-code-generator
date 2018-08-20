package th.in.moe.devtools.codegenerator.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import th.in.moe.devtools.codegenerator.MainApp;
import th.in.moe.devtools.codegenerator.common.bean.DatasourceBean;
import th.in.moe.devtools.codegenerator.common.bean.GeneratorCriteria;
import th.in.moe.devtools.codegenerator.common.bean.TableBean;
import th.in.moe.devtools.codegenerator.common.constant.GeneratorConstant.DATABASE_PRODUCTION_NAME;
import th.in.moe.devtools.codegenerator.common.constant.GeneratorConstant.ProfileTemplate;
import th.in.moe.devtools.codegenerator.common.exception.GeneratedException;
import th.in.moe.devtools.codegenerator.common.util.DialogUtils;
import th.in.moe.devtools.codegenerator.model.TableModel;
import th.in.moe.devtools.codegenerator.service.GeneratorService;

public class MainPageController {
	
	// Database Connection
	@FXML
	private ComboBox<String> dbProductionNameComboBox;
	@FXML
	private TextField dbUrlField;
	@FXML
	private TextField dbUsernameField;
	@FXML
	private TextField dbPasswordField;
	// Database Criteria
	@FXML
	private TextField dbCatalogField;
	@FXML
	private TextField dbSchemaField;
	@FXML
	private TextField dbTableNamePatternField;
	// Generator Criteria
	@FXML
	private Label resultPathLabel;
	@FXML
	private Button resultPathButton;
	@FXML
	private TextField resultEntityPackageField;
	@FXML
	private TextField resultRepositoryPackageField;
	@FXML
	private TextField excludeColumnField;
	@FXML
	private ComboBox<ProfileTemplate> profileComboBox;
	@FXML
	private CheckBox generateGeneratedValueCheckBox;
	@FXML
	private CheckBox generateToStringMethodCheckBox;
	@FXML
	private TableView<TableModel> tableNameTableView;
	@FXML
	private CheckBox selectAllCheckBox;
	@FXML
	private TableColumn<TableModel, Boolean> selectedColumn;
	@FXML
	private TableColumn<TableModel, String> tableNameColumn;
	
	// Reference to the main application.
	private MainApp mainApp;
	
	// GeneratorService
	private GeneratorService generatorService;
	
	private static final String RESULT_PATH_LABEL_INIT_TEXT = "No Directory selected";
	
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
	public void initialize() {
		generatorService = new GeneratorService();
		
		// Database
		ObservableList<String> dbProductionNameList = FXCollections.observableArrayList(Arrays.asList(
			DATABASE_PRODUCTION_NAME.MYSQL,
			DATABASE_PRODUCTION_NAME.ORACLE
		));
		dbProductionNameComboBox.setItems(dbProductionNameList);
		dbProductionNameComboBox.getSelectionModel().select(0);
		// Table Pattern Name
		dbTableNamePatternField.setText("%");
		
		// Result Path
		// http://java-buddy.blogspot.com/2013/03/javafx-simple-example-of.html
		resultPathButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DirectoryChooser directoryChooser = new DirectoryChooser();
				File selectedDirectory = directoryChooser.showDialog(mainApp.getPrimaryStage());
				if (selectedDirectory == null) {
					resultPathLabel.setText(RESULT_PATH_LABEL_INIT_TEXT);
				} else {
					resultPathLabel.setText(selectedDirectory.getAbsolutePath());
				}
			}
		});
		
		// Profile
		profileComboBox.setItems(FXCollections.observableArrayList(ProfileTemplate.values()));
		profileComboBox.getSelectionModel().select(0);
		
		// TableNameView
		// "Selected" column
		selectedColumn.setGraphic(getSelectAllCheckBox());
		selectedColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
		selectedColumn.setCellFactory(new Callback<TableColumn<TableModel, Boolean>, TableCell<TableModel, Boolean>>() {
			public TableCell<TableModel, Boolean> call(TableColumn<TableModel, Boolean> tableColumn) {
				final TableCell<TableModel, Boolean> tableCell = new TableCell<TableModel, Boolean>() {
					@Override
					public void updateItem(final Boolean item, boolean empty) {
						if (empty || item == null) {
							setGraphic(null);
						} else {
							final TableModel tableModel = getTableView().getItems().get(getIndex());
							CheckBox checkBox = new CheckBox();
							checkBox.selectedProperty().bindBidirectional(tableModel.selectedProperty());
							checkBox.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									// Checking for an unselected tableName in the table view.
									boolean unSelectedFlag = false;
									for (TableModel tableModel : tableNameTableView.getItems()) {
										if (!tableModel.getSelected()) {
											unSelectedFlag = true;
											break;
										}
									}
									/*
									 * If at least one tableName is not selected, then deselecting the check box in the table column header,
									 * else if all tableName are selected, then selecting the check box in the header.
									 */
									if (unSelectedFlag) {
										getSelectAllCheckBox().setSelected(false);
									} else {
										getSelectAllCheckBox().setSelected(true);
									}
								}
							});
							setGraphic(checkBox);
						}
					}
				};
				tableCell.setAlignment(Pos.CENTER);
				return tableCell;
			}
		});
		// "Table Name" column
		tableNameColumn.setCellValueFactory(cellData -> cellData.getValue().tableNameProperty());
		
		// TODO Debug
//		dbProductionNameComboBox.setValue(DATABASE_PRODUCTION_NAME.MYSQL);
//		dbUrlField.setText("jdbc:mysql://localhost:3306/buckwa_framework");
//		dbUsernameField.setText("root");
//		dbPasswordField.setText("root");
		dbProductionNameComboBox.setValue(DATABASE_PRODUCTION_NAME.ORACLE);
		dbUrlField.setText("jdbc:oracle:thin:@150.95.24.42:1521:orcl");
		dbUsernameField.setText("exciseadm");
		dbPasswordField.setText("exciseadm");
		dbSchemaField.setText("EXCISEADM");
		
	}
	
	public CheckBox getSelectAllCheckBox() {
		if (selectAllCheckBox == null) {
			final CheckBox selectAllCheckBox = new CheckBox();
			// Adding EventHandler to the CheckBox to select/deselect all employees in table.
			selectAllCheckBox.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// Setting the value in all the tableNameTableView.
					for (TableModel item : tableNameTableView.getItems()) {
						item.setSelected(selectAllCheckBox.isSelected());
					}
				}
			});
			this.selectAllCheckBox = selectAllCheckBox;
		}
		return selectAllCheckBox;
	}
	
	@FXML
    private void handleTestConnection() {
		GeneratorCriteria criteria = bindingModel();
		
		try {
			generatorService.testDbConnection(criteria.getDatasourceBean());
			// Show Information Alert
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Success");
			alert.setHeaderText(null);
			alert.setContentText("Ping succeeded!");
			alert.showAndWait();
		} catch (GeneratedException e) {
			// Show Error Alert
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Ping failed!");
			DialogUtils.createExpandableException(alert, e);
			alert.showAndWait();
		}
	}
	
	@FXML
    private void handleFetchTable() {
		GeneratorCriteria criteria = bindingModel();
		
		try {
			tableNameTableView.getItems().clear();
			
			// Check Connection
			generatorService.testDbConnection(criteria.getDatasourceBean());
			
			List<String> tableNameList = generatorService.getAllTableName(criteria);
			ObservableList<TableModel> tableModelList = FXCollections.observableArrayList();
			for (String tableName : tableNameList) {
				tableModelList.add(new TableModel(false, tableName));
			}
			
			tableNameTableView.setItems(tableModelList);
			
		} catch (GeneratedException e) {
			// Show Error Alert
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			DialogUtils.createExpandableException(alert, e);
			alert.showAndWait();
		}
	}
	
    @FXML
    private void handleGenerate() {
    	GeneratorCriteria criteria = bindingModel();
    	String errorMsg = validateCriteria(criteria);
    	if (StringUtils.isEmpty(errorMsg)) {
    		try {
    			// Check Connection
    			generatorService.testDbConnection(criteria.getDatasourceBean());
    			
    			List<String> tableNameList = new ArrayList<>();
				ObservableList<TableModel> tableModelList = tableNameTableView.getItems();
				for (TableModel tableModel : tableModelList) {
					if (tableModel.getSelected()) {
						tableNameList.add(tableModel.getTableName());
					}
				}
				
				List<TableBean> tableBeanList = generatorService.getTableDescribe(criteria, tableNameList);
				tableBeanList.forEach(t -> System.out.println(ToStringBuilder.reflectionToString(t, ToStringStyle.JSON_STYLE)));
				
				generatorService.genJavaFromTable(criteria, tableBeanList);
				
				// Show Information Alert
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.initOwner(mainApp.getPrimaryStage());
				alert.setTitle("Success");
				alert.setHeaderText(null);
				alert.setContentText("Generate Java Class succeeded!!");
				alert.showAndWait();
				
			} catch (GeneratedException e) {
				// Show Error Alert
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(mainApp.getPrimaryStage());
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				DialogUtils.createExpandableException(alert, e);
				alert.showAndWait();
			}
    	} else {
    		// Show Error Alert
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Warning");
			alert.setHeaderText(null);
			alert.setContentText(errorMsg);
			alert.showAndWait();
    	}
    }
    
    private GeneratorCriteria bindingModel() {
    	DatasourceBean datasourceBean = new DatasourceBean();
		datasourceBean.setDatabaseProductionName(dbProductionNameComboBox.getValue());
		datasourceBean.setUrl(dbUrlField.getText());
		datasourceBean.setUsername(dbUsernameField.getText());
		datasourceBean.setPassword(dbPasswordField.getText());
		
		GeneratorCriteria criteria = new GeneratorCriteria();
		criteria.setDatasourceBean(datasourceBean);
		criteria.setDbCatalog(dbCatalogField.getText());
		criteria.setDbSchema(dbSchemaField.getText());
		criteria.setDbTableNamePattern(dbTableNamePatternField.getText());
		criteria.setResultPath(resultPathLabel.getText());
		criteria.setResultEntityPackage(resultEntityPackageField.getText());
		criteria.setResultRepositoryPackage(resultRepositoryPackageField.getText());
		criteria.setExcludeColumn(Arrays.asList(excludeColumnField.getText()));
		criteria.setProfile(profileComboBox.getValue());
		criteria.setGenerateGeneratedValueFlag(generateGeneratedValueCheckBox.isSelected());
		criteria.setGenerateToStringMethodFlag(generateToStringMethodCheckBox.isSelected());
		
		return criteria;
    }
    
    private String validateCriteria(GeneratorCriteria criteria) {
    	List<String> errorMsgList = new ArrayList<>();
    	if (tableNameTableView.getItems().size() == 0) {
    		errorMsgList.add("Pleach select TableName for generate.");
    	}
    	if (RESULT_PATH_LABEL_INIT_TEXT.equals(resultPathLabel.getText())) {
    		errorMsgList.add("Result Path is empty.");
    	}
    	if (StringUtils.isEmpty(resultEntityPackageField.getText())) {
    		errorMsgList.add("Entity Package is empty.");
    	}
    	if (ProfileTemplate.SPRING_DATA_JPA.equals(profileComboBox.getValue())
    			|| ProfileTemplate.BUCKWA_SPRING_DATA_JPA.equals(profileComboBox.getValue())) {
    		if (StringUtils.isEmpty(resultRepositoryPackageField.getText())) {
    			errorMsgList.add("Repository Package is empty.");
    		}
    	}
    	
    	StringBuilder errorMsg = new StringBuilder();
    	for (int i = 0; i < errorMsgList.size(); i++) {
			errorMsg.append(errorMsgList.get(i));
			if (i < errorMsgList.size() - 1) {
				errorMsg.append(System.lineSeparator());
			}
		}
    	
    	return errorMsg.toString();
    }
    
}
