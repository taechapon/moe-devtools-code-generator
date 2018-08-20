package th.in.moe.devtools.codegenerator.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableModel {

	private final SimpleBooleanProperty selected;
	private final StringProperty tableName;

	public TableModel(boolean selected, String tableName) {
		this.selected = new SimpleBooleanProperty(selected);
		this.tableName = new SimpleStringProperty(tableName);
	}

	public boolean getSelected() {
		return selected.get();
	}

	public void setSelected(boolean selected) {
		this.selected.set(selected);
	}

	public BooleanProperty selectedProperty() {
		return selected;
	}

	public String getTableName() {
		return tableName.get();
	}

	public void setTableName(String tableName) {
		this.tableName.set(tableName);
	}

	public StringProperty tableNameProperty() {
		return tableName;
	}
	
}
