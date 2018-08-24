package th.in.moe.devtools.codegenerator.common.bean;

import org.apache.commons.lang3.StringUtils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: Aug 23, 2018
 */
public class CustomEntityBean {

	// For TreeView
	private StringProperty displayName;
	// Table Detail
	private StringProperty dbTableName;
	private StringProperty javaTableName;
	// Column Detail
	private BooleanProperty generateFlag;
	private StringProperty dbColumnName;
	private StringProperty javaColumnName;
	private StringProperty javaType;

	public CustomEntityBean(String displayName) {
		super();
		this.displayName = new SimpleStringProperty(displayName);
		this.dbTableName = new SimpleStringProperty();
		this.javaTableName = new SimpleStringProperty();
		this.generateFlag = new SimpleBooleanProperty();
		this.dbColumnName = new SimpleStringProperty();
		this.javaColumnName = new SimpleStringProperty();
		this.javaType = new SimpleStringProperty();
	}

	public String getDisplayName() {
		return displayName.get();
	}

	public void setDisplayName(String displayName) {
		this.displayName.set(displayName);
	}

	public StringProperty displayNameProperty() {
		return displayName;
	}

	public String getDbTableName() {
		return dbTableName.get();
	}

	public void setDbTableName(String dbTableName) {
		this.dbTableName.set(dbTableName);
	}

	public StringProperty dbTableNameProperty() {
		return dbTableName;
	}

	public String getJavaTableName() {
		return javaTableName.get();
	}

	public void setJavaTableName(String javaTableName) {
		this.javaTableName.set(javaTableName);
	}

	public StringProperty javaTableNameProperty() {
		return javaTableName;
	}

	public boolean isGenerateFlag() {
		return generateFlag.get();
	}

	public void setGenerateFlag(boolean generateFlag) {
		this.generateFlag.set(generateFlag);
	}

	public BooleanProperty generateFlagProperty() {
		return generateFlag;
	}

	public String getDbColumnName() {
		return dbColumnName.get();
	}

	public void setDbColumnName(String dbColumnName) {
		this.dbColumnName.set(dbColumnName);
	}

	public StringProperty dbColumnNameProperty() {
		return dbColumnName;
	}

	public String getJavaColumnName() {
		return javaColumnName.get();
	}

	public void setJavaColumnName(String javaColumnName) {
		this.javaColumnName.set(javaColumnName);
	}

	public StringProperty javaColumnNameProperty() {
		return javaColumnName;
	}

	public String getJavaType() {
		return javaType.get();
	}

	public void setJavaType(String javaType) {
		this.javaType.set(javaType);
	}

	public StringProperty javaTypeProperty() {
		return javaType;
	}

	public boolean isTable() {
		return StringUtils.isNotEmpty(dbTableName.get()) && StringUtils.isEmpty(dbColumnName.get());
	}

	@Override
	public String toString() {
		return displayName.get();
	}

}
