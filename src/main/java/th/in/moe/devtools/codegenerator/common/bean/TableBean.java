package th.in.moe.devtools.codegenerator.common.bean;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: Sep 14, 2012
 */
public class TableBean {
	
	// Database
	private String tableName;
	private List<ColumnBean> keyList;
	private List<ColumnBean> columnList;
	// Java
	private String javaName;
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<ColumnBean> getKeyList() {
		return keyList;
	}

	public void setKeyList(List<ColumnBean> keyList) {
		this.keyList = keyList;
	}

	public List<ColumnBean> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<ColumnBean> columnList) {
		this.columnList = columnList;
	}
	
	public String getJavaName() {
		return javaName;
	}

	public void setJavaName(String javaName) {
		this.javaName = javaName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
	
}
