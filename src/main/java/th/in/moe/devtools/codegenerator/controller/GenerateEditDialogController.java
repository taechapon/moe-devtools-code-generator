package th.in.moe.devtools.codegenerator.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import th.in.moe.devtools.codegenerator.common.bean.ColumnBean;
import th.in.moe.devtools.codegenerator.common.bean.CustomEntityBean;
import th.in.moe.devtools.codegenerator.common.bean.GeneratorCriteria;
import th.in.moe.devtools.codegenerator.common.bean.TableBean;
import th.in.moe.devtools.codegenerator.common.util.DialogUtils;
import th.in.moe.devtools.codegenerator.service.GeneratorService;

public class GenerateEditDialogController {

	@FXML
	private TreeView<CustomEntityBean> tableColumnTreeView;
	@FXML
	private Accordion entityAccordionPane;
	@FXML
	private TextField javaTableNameField;
	@FXML
	private CheckBox generateFlagCheckBox;
	@FXML
	private TextField javaColumnNameField;
	@FXML
	private ComboBox<String> javaTypeComboBox;

	// GeneratorService
	private GeneratorService generatorService;
	
	private GeneratorCriteria criteria;
	private List<TableBean> tableBeanList;
	private Stage dialogStage;
	private boolean okClicked = false;
	
	private Image tableImage = new Image(getClass().getResourceAsStream("/image/icon_table.png"));
	private Image keyImage = new Image(getClass().getResourceAsStream("/image/icon_key.png"));
	private Image columnImage = new Image(getClass().getResourceAsStream("/image/icon_column.png"));

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		generatorService = new GeneratorService();
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void init(GeneratorCriteria criteria, List<TableBean> tableBeanList) {
		this.criteria = criteria;
		this.tableBeanList = tableBeanList;
		
		// Root Item
		CustomEntityBean rootEntity = new CustomEntityBean("Root");
		rootEntity.setDisplayName("Root");
		TreeItem<CustomEntityBean> rootItem = new TreeItem<CustomEntityBean>(rootEntity);
		rootItem.setExpanded(false);
		
		CustomEntityBean tableEntity = null;
		TreeItem<CustomEntityBean> tableItem = null;
		CustomEntityBean columnEntity = null;
		for (TableBean tableBean : this.tableBeanList) {
			
			// Table Item
			tableEntity = new CustomEntityBean(tableBean.getTableName());
			tableEntity.setDbTableName(tableBean.getTableName());
			tableEntity.setJavaTableName(tableBean.getJavaName());
			tableItem = createTreeItem(tableEntity, tableImage);
			
			// Column Item
			for (ColumnBean columnBean : tableBean.getKeyList()) {
				columnEntity = new CustomEntityBean(columnBean.getColumnName());
				columnEntity.setGenerateFlag(columnBean.isGenerateFlag());
				columnEntity.setPrimaryKeyFlag(columnBean.isPrimaryKey());
				columnEntity.setDbTableName(tableBean.getTableName());
				columnEntity.setDbColumnName(columnBean.getColumnName());
				columnEntity.setJavaColumnName(columnBean.getJavaName());
				columnEntity.setJavaType(columnBean.getJavaType().getName());
				tableItem.getChildren().add(createTreeItem(columnEntity, keyImage));
			}
			for (ColumnBean columnBean : tableBean.getColumnList()) {
				columnEntity = new CustomEntityBean(columnBean.getColumnName());
				columnEntity.setGenerateFlag(columnBean.isGenerateFlag());
				columnEntity.setPrimaryKeyFlag(columnBean.isPrimaryKey());
				columnEntity.setDbTableName(tableBean.getTableName());
				columnEntity.setDbColumnName(columnBean.getColumnName());
				columnEntity.setJavaColumnName(columnBean.getJavaName());
				columnEntity.setJavaType(columnBean.getJavaType().getName());
				tableItem.getChildren().add(createTreeItem(columnEntity, columnImage));
			}
			
			rootItem.getChildren().add(tableItem);
		}
		
		tableColumnTreeView.setRoot(rootItem);
		tableColumnTreeView.setShowRoot(false);
		
		entityAccordionPane.setExpandedPane(null);
		entityAccordionPane.getPanes().get(0).setDisable(true);
		entityAccordionPane.getPanes().get(1).setDisable(true);
		
		javaTypeComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(
			Boolean.class.getName(),
			Byte.class.getName(),
			Float.class.getName(),
			Integer.class.getName(),
			Long.class.getName(),
			Object.class.getName(),
			Short.class.getName(),
			String.class.getName(),
			BigDecimal.class.getName(),
			BigInteger.class.getName(),
			LocalDate.class.getName(),
			LocalTime.class.getName(),
			LocalDateTime.class.getName(),
			Date.class.getName()
		)));
		
		attachListeners();
	}

	// https://stackoverflow.com/questions/32478383/updating-treeview-items-from-textfield
	private TreeItem<CustomEntityBean> createTreeItem(CustomEntityBean entityBean, Image image) {
		TreeItem<CustomEntityBean> treeItem = new TreeItem<>(entityBean, new ImageView(image));
		if (treeItem.getValue().isTable()) {
			// Table
			// Java Table Name
			ChangeListener<String> javaTableNameListener = (obs, oldName, newName) -> {
				TreeModificationEvent<CustomEntityBean> event = new TreeModificationEvent<>(TreeItem.valueChangedEvent(), treeItem);
				Event.fireEvent(treeItem, event);
			};
			entityBean.javaTableNameProperty().addListener(javaTableNameListener);
			treeItem.valueProperty().addListener((obs, oldValue, newValue) -> {
				if (oldValue != null) {
					oldValue.javaTableNameProperty().removeListener(javaTableNameListener);
				}
				if (newValue != null) {
					newValue.javaTableNameProperty().addListener(javaTableNameListener);
				}
			});
		} else {
			// Column
			// Generate Property
			ChangeListener<Boolean> generateFlagListener = (obs, oldName, newName) -> {
				TreeModificationEvent<CustomEntityBean> event = new TreeModificationEvent<>(TreeItem.valueChangedEvent(), treeItem);
				Event.fireEvent(treeItem, event);
			};
			entityBean.generateFlagProperty().addListener(generateFlagListener);
			treeItem.valueProperty().addListener((obs, oldValue, newValue) -> {
				if (oldValue != null) {
					oldValue.generateFlagProperty().removeListener(generateFlagListener);
				}
				if (newValue != null) {
					newValue.generateFlagProperty().addListener(generateFlagListener);
				}
			});
			// Java Column Name
			ChangeListener<String> javaColumnNameListener = (obs, oldName, newName) -> {
				TreeModificationEvent<CustomEntityBean> event = new TreeModificationEvent<>(TreeItem.valueChangedEvent(), treeItem);
				Event.fireEvent(treeItem, event);
			};
			entityBean.javaColumnNameProperty().addListener(javaColumnNameListener);
			treeItem.valueProperty().addListener((obs, oldValue, newValue) -> {
				if (oldValue != null) {
					oldValue.javaColumnNameProperty().removeListener(javaColumnNameListener);
				}
				if (newValue != null) {
					newValue.javaColumnNameProperty().addListener(javaColumnNameListener);
				}
			});
			// Java Type
			ChangeListener<String> javaTypeListener = (obs, oldName, newName) -> {
				TreeModificationEvent<CustomEntityBean> event = new TreeModificationEvent<>(TreeItem.valueChangedEvent(), treeItem);
				Event.fireEvent(treeItem, event);
			};
			entityBean.javaTypeProperty().addListener(javaTypeListener);
			treeItem.valueProperty().addListener((obs, oldValue, newValue) -> {
				if (oldValue != null) {
					oldValue.javaTypeProperty().removeListener(javaTypeListener);
				}
				if (newValue != null) {
					newValue.javaTypeProperty().addListener(javaTypeListener);
				}
			});
		}
		return treeItem;
	}

	private void attachListeners() {
		// https://stackoverflow.com/questions/13857041/tree-item-select-event-in-javafx2
		// TreeView
		tableColumnTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<CustomEntityBean>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<CustomEntityBean>> observable, TreeItem<CustomEntityBean> oldValue, TreeItem<CustomEntityBean> newValue) {
				TreeItem<CustomEntityBean> selectedItem = (TreeItem<CustomEntityBean>) newValue;
				CustomEntityBean entityBean = selectedItem.getValue();
				javaTableNameField.setText("");
				generateFlagCheckBox.setSelected(false);
				javaColumnNameField.setText("");
				if (entityBean.isTable()) {
					entityAccordionPane.setExpandedPane(entityAccordionPane.getPanes().get(0));
					entityAccordionPane.getPanes().get(0).setDisable(false);
					entityAccordionPane.getPanes().get(1).setDisable(true);
					javaTableNameField.setText(entityBean.getJavaTableName());
				} else {
					entityAccordionPane.setExpandedPane(entityAccordionPane.getPanes().get(1));
					entityAccordionPane.getPanes().get(0).setDisable(true);
					entityAccordionPane.getPanes().get(1).setDisable(false);
					generateFlagCheckBox.setSelected(entityBean.isGenerateFlag());
					if (entityBean.isPrimaryKeyFlag()) {
						generateFlagCheckBox.setDisable(true);
					} else {
						generateFlagCheckBox.setDisable(false);
					}
					javaColumnNameField.setText(entityBean.getJavaColumnName());
					javaTypeComboBox.setValue(entityBean.getJavaType());
				}
			}
		});
		
		// generateFlagCheckBox
		generateFlagCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					javaColumnNameField.setDisable(false);
					javaTypeComboBox.setDisable(false);
				} else {
					javaColumnNameField.setDisable(true);
					javaTypeComboBox.setDisable(true);
				}
			}
		});
	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleUpdateTable() {
		TreeItem<CustomEntityBean> selectedItem = tableColumnTreeView.getSelectionModel().getSelectedItem();
		CustomEntityBean entityBean = selectedItem.getValue();
		entityBean.javaTableNameProperty().set(javaTableNameField.getText());
		
		// Show Information Alert
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(dialogStage);
		alert.setTitle("Success");
		alert.setHeaderText(null);
		alert.setContentText("Update table succeeded!!");
		alert.showAndWait();
	}

	@FXML
	private void handleUpdateColumn() {
		TreeItem<CustomEntityBean> selectedItem = tableColumnTreeView.getSelectionModel().getSelectedItem();
		CustomEntityBean entityBean = selectedItem.getValue();
		entityBean.setGenerateFlag(generateFlagCheckBox.isSelected());
		entityBean.javaColumnNameProperty().set(javaColumnNameField.getText());
		entityBean.javaTypeProperty().set(javaTypeComboBox.getValue());
		
		// Show Information Alert
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(dialogStage);
		alert.setTitle("Success");
		alert.setHeaderText(null);
		alert.setContentText("Update column succeeded!!");
		alert.showAndWait();
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		try {
			int tableSize = tableBeanList.size();
			TableBean tableBean = null;
			TreeItem<CustomEntityBean> tableItem = null;
			ObservableList<TreeItem<CustomEntityBean>> columnItemList = null;
			for (int i = 0; i < tableSize; i++) {
				tableBean = tableBeanList.get(i);
				tableItem = tableColumnTreeView.getRoot().getChildren().get(i);
				tableBean.setJavaName(tableItem.getValue().getJavaTableName());
				for (ColumnBean columnBean : tableBean.getKeyList()) {
					columnItemList = tableItem.getChildren();
					for (TreeItem<CustomEntityBean> columnItem : columnItemList) {
						if (columnBean.getColumnName().equals(columnItem.getValue().getDbColumnName())) {
							columnBean.setJavaName(columnItem.getValue().getJavaColumnName());
							columnBean.setJavaType(Class.forName(columnItem.getValue().getJavaType()));
						}
					}
				}
				for (ColumnBean columnBean : tableBean.getColumnList()) {
					columnItemList = tableItem.getChildren();
					for (TreeItem<CustomEntityBean> columnItem : columnItemList) {
						if (columnBean.getColumnName().equals(columnItem.getValue().getDbColumnName())) {
							columnBean.setGenerateFlag(columnItem.getValue().isGenerateFlag());
							columnBean.setJavaName(columnItem.getValue().getJavaColumnName());
							columnBean.setJavaType(Class.forName(columnItem.getValue().getJavaType()));
						}
					}
				}
			}
			//tableBeanList.forEach(System.out::println);
			
			generatorService.genJavaFromTable(this.criteria, tableBeanList);
			
			// Show Information Alert
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(dialogStage);
			alert.setTitle("Success");
			alert.setHeaderText(null);
			alert.setContentText("Generate Java Class succeeded!!");
			alert.showAndWait();
		} catch (Exception e) {
			// Show Error Alert
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			DialogUtils.createExpandableException(alert, e);
			alert.showAndWait();
		}
		
		okClicked = true;
		dialogStage.close();
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

}
