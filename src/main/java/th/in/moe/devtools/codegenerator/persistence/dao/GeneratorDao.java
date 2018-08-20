package th.in.moe.devtools.codegenerator.persistence.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import th.in.moe.devtools.codegenerator.common.bean.ColumnBean;
import th.in.moe.devtools.codegenerator.common.bean.DatasourceBean;
import th.in.moe.devtools.codegenerator.common.bean.GeneratorCriteria;
import th.in.moe.devtools.codegenerator.common.bean.TableBean;
import th.in.moe.devtools.codegenerator.common.exception.GeneratedException;
import th.in.moe.devtools.codegenerator.common.util.JdbcUtils;
import th.in.moe.devtools.codegenerator.common.util.NameUtils;
import th.in.moe.devtools.codegenerator.typeconvert.DbTypeConverter;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: Sep 13, 2012
 */
public class GeneratorDao {
	
	private static final Logger logger = LoggerFactory.getLogger(GeneratorDao.class);
	
	private DbTypeConverter dbTypeConverter;
	
	public void setDbTypeConverter(DbTypeConverter dbTypeConverter) {
		this.dbTypeConverter = dbTypeConverter;
	}
	
	public List<String> getAllTableName(GeneratorCriteria criteria) throws GeneratedException {
		logger.info("getAllTableName schemaPattern={}, tableNamePattern={}", criteria.getDbSchema(), criteria.getDbTableNamePattern());
		
		List<String> tableNameList = new ArrayList<>();
		DatasourceBean datasourceBean = criteria.getDatasourceBean();
		try (Connection connection = DriverManager.getConnection(datasourceBean.getUrl(), datasourceBean.getUsername(), datasourceBean.getPassword());
				ResultSet rs = connection.getMetaData().getTables(criteria.getDbCatalog(), criteria.getDbSchema(), criteria.getDbTableNamePattern(), new String[] {"TABLE"})) {
			
			while (rs.next()) {
				String name = rs.getString("TABLE_NAME");
				logger.info("TABLE_NAME={}", name);
				tableNameList.add(name);
			}
			
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new GeneratedException(e.getMessage(), e);
		}
		
		return tableNameList;
	}
	
	public List<TableBean> getTableDescribe(GeneratorCriteria criteria, List<String> tableNameList) throws GeneratedException {
		
		List<TableBean> tableBeanList = new ArrayList<>();
		DatasourceBean datasourceBean = criteria.getDatasourceBean();
		try (Connection connection = DriverManager.getConnection(datasourceBean.getUrl(), datasourceBean.getUsername(), datasourceBean.getPassword())) {
			
			// Initial
			TableBean table = null;
			List<ColumnBean> keyList = null;
			List<ColumnBean> columnList = null;
			ColumnBean column = null;
			
			for (String tableName : tableNameList) {
				logger.info("getTableDescribe schemaPattern={}, tableName={}", criteria.getDbSchema(), tableName);
				
				table = new TableBean();
				table.setTableName(tableName);
				table.setJavaName(NameUtils.toUpperCaseFirstChar(JdbcUtils.convertUnderscoreNameToPropertyName(tableName)));
				keyList = new ArrayList<ColumnBean>();
				columnList = new ArrayList<ColumnBean>();
				
				// Primary Key
				try (ResultSet rs = connection.getMetaData().getPrimaryKeys(criteria.getDbCatalog(), criteria.getDbSchema(), tableName)) {
					while (rs.next()) {
						column = new ColumnBean();
						column.setColumnName(rs.getString("COLUMN_NAME"));
						column.setPrimaryKey(Boolean.TRUE);
						keyList.add(column);
						
						logger.debug(
							"key seq : [{}]," +
							"\tcolumn name: [{}]," +
							"\tpk name: [{}]",
						rs.getShort("KEY_SEQ"), StringUtils.rightPad(rs.getString("COLUMN_NAME"), 30), rs.getString("PK_NAME"));
					}
					table.setKeyList(keyList);
				}
				
				// Column
				try (ResultSet rs = connection.getMetaData().getColumns(criteria.getDbCatalog(), criteria.getDbSchema(), tableName, null)) {
					int index;
					while (rs.next()) {
						column = new ColumnBean();
						column.setColumnName(rs.getString("COLUMN_NAME"));
						
						index = keyList.indexOf(column);
						if (index != -1) {
							column = keyList.get(index);
						}
						
						// Database Column Detail
						column.setDataType(rs.getInt("DATA_TYPE"));
						column.setTypeName(rs.getString("TYPE_NAME"));
						column.setColumnSize(rs.getInt("COLUMN_SIZE"));
						column.setDecimalDigits(rs.getInt("DECIMAL_DIGITS"));
						column.setNumPrecRadix(rs.getInt("NUM_PREC_RADIX"));
						column.setNullable(rs.getInt("NULLABLE"));
						column.setRemarks(rs.getString("REMARKS"));
						column.setColumnDef(rs.getString("COLUMN_DEF"));
						column.setOrdinalPosition(rs.getInt("ORDINAL_POSITION"));
						column.setIsNullable(rs.getString("IS_NULLABLE"));
						
						// Java Properties Detail
						column.setJavaName(JdbcUtils.convertUnderscoreNameToPropertyName(column.getColumnName()));
						column.setJavaType(dbTypeConverter.convert(column.getDataType(), column.getTypeName()));
						
						if (index == -1) {
							columnList.add(column);
						}
						
						logger.debug(
							"order: [{}]," +
							"\tcolumn name: [{}]," +
							"\ttypeName: [{}]," +
							"\tdataType: [{}]," +
							"\tsize: [{}]," +
							"\tdecimalDigits: [{}]",
						StringUtils.leftPad(String.valueOf(column.getOrdinalPosition()), 3),
						StringUtils.rightPad(column.getColumnName(), 30),
						column.getTypeName(), column.getDataType(), column.getColumnSize(), column.getDecimalDigits());
					}
					table.setColumnList(columnList);
				}
				
				tableBeanList.add(table);
			}
			
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new GeneratedException(e.getMessage(), e);
		}
		
		return tableBeanList;
	}
	
}