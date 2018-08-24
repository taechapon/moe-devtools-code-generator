package th.in.moe.devtools.codegenerator.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import th.in.moe.devtools.codegenerator.common.bean.ColumnBean;
import th.in.moe.devtools.codegenerator.common.bean.DatasourceBean;
import th.in.moe.devtools.codegenerator.common.bean.GeneratorCriteria;
import th.in.moe.devtools.codegenerator.common.bean.TableBean;
import th.in.moe.devtools.codegenerator.common.exception.GeneratedException;
import th.in.moe.devtools.codegenerator.common.util.GeneratorUtils;
import th.in.moe.devtools.codegenerator.common.util.JdbcUtils;
import th.in.moe.devtools.codegenerator.common.util.NameUtils;
import th.in.moe.devtools.codegenerator.persistence.dao.GeneralDao;
import th.in.moe.devtools.codegenerator.persistence.dao.GeneratorDao;
import th.in.moe.devtools.codegenerator.template.profile.ProfileTemplate;
import th.in.moe.devtools.codegenerator.typeconvert.DbTypeConverter;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: May 1, 2013
 */
public class GeneratorService {
	
	private static final Logger logger = LoggerFactory.getLogger(GeneratorService.class);
	
	private GeneratorDao generatorDao;
	private GeneralDao generalDao;
	
	public GeneratorService() {
		super();
		this.generatorDao = new GeneratorDao();
		this.generalDao = new GeneralDao();
	}
	
	public void validateDatabaseConnection(DatasourceBean dataSourceBean) throws GeneratedException {
		try {
			String dbVendor = dataSourceBean.getUrl().split("\\:")[1];
			if (!dataSourceBean.getDatabaseProductionName().equalsIgnoreCase(dbVendor)) {
				throw new GeneratedException("databaserProductionName don't match with url");
			}
		} catch (Exception e) {
			logger.warn("Invalid Database Connection URL: {}", dataSourceBean.getUrl());
			throw new GeneratedException("Invalid Database Connection URL");
		}
	}
	
	public int testDbConnection(DatasourceBean datasourceBean) throws GeneratedException {
		validateDatabaseConnection(datasourceBean);
		return generalDao.testConnection(datasourceBean);
	}
	
	public List<String> getAllTableName(GeneratorCriteria criteria) throws GeneratedException {
		return generatorDao.getAllTableName(criteria);
	}
	
	public List<TableBean> getTableDescribe(GeneratorCriteria criteria, List<String> tableNameList) throws GeneratedException {
		List<TableBean> tableBeanList = generatorDao.getTableDescribe(criteria, tableNameList);
		convertDb2Java(criteria, tableBeanList);
		return tableBeanList;
	}
	
	private void convertDb2Java(GeneratorCriteria criteria, List<TableBean> tableBeanList) throws GeneratedException {
		DbTypeConverter dbTypeConverter = GeneratorUtils.getDbTypeConverter(criteria.getDatasourceBean().getDatabaseProductionName());
		
		for (TableBean tableBean : tableBeanList) {
			tableBean.setJavaName(NameUtils.toUpperCaseFirstChar(JdbcUtils.convertUnderscoreNameToPropertyName(tableBean.getTableName())));
			for (ColumnBean keyColumnBean : tableBean.getKeyList()) {
				keyColumnBean.setJavaName(JdbcUtils.convertUnderscoreNameToPropertyName(keyColumnBean.getColumnName()));
				keyColumnBean.setJavaType(dbTypeConverter.convert(keyColumnBean.getDataType(), keyColumnBean.getTypeName()));
			}
			for (ColumnBean columnBean : tableBean.getKeyList()) {
				columnBean.setJavaName(JdbcUtils.convertUnderscoreNameToPropertyName(columnBean.getColumnName()));
				columnBean.setJavaType(dbTypeConverter.convert(columnBean.getDataType(), columnBean.getTypeName()));
				if (!criteria.getExcludeColumn().contains(columnBean.getColumnName())) {
					columnBean.setGenerateFlag(Boolean.TRUE);
				}
			}
		}
	}
	
	public void genJavaFromTable(GeneratorCriteria criteria, List<TableBean> tableList) throws GeneratedException {
		logger.info("genJavaFromTable profile={}", criteria.getProfile());
		
		ProfileTemplate profileTemplate = GeneratorUtils.getProfileTemplate(criteria.getProfile());
		for (TableBean table : tableList) {
			profileTemplate.generate(criteria, table);
		}
		
		logger.info("Generate Java Class succeeded!!");
	}
	
}
