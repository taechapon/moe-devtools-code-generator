package th.in.moe.devtools.codegenerator.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import th.in.moe.devtools.codegenerator.common.bean.DatasourceBean;
import th.in.moe.devtools.codegenerator.common.bean.GeneratorCriteria;
import th.in.moe.devtools.codegenerator.common.bean.TableBean;
import th.in.moe.devtools.codegenerator.common.exception.GeneratedException;
import th.in.moe.devtools.codegenerator.common.util.GeneratorUtils;
import th.in.moe.devtools.codegenerator.persistence.dao.GeneralDao;
import th.in.moe.devtools.codegenerator.persistence.dao.GeneratorDao;
import th.in.moe.devtools.codegenerator.template.profile.ProfileTemplate;

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
		generatorDao.setDbTypeConverter(GeneratorUtils.getDbTypeConverter(criteria.getDatasourceBean().getDatabaseProductionName()));
		return generatorDao.getAllTableName(criteria);
	}
	
	public List<TableBean> getTableDescribe(GeneratorCriteria criteria, List<String> tableNameList) throws GeneratedException {
		generatorDao.setDbTypeConverter(GeneratorUtils.getDbTypeConverter(criteria.getDatasourceBean().getDatabaseProductionName()));
		return generatorDao.getTableDescribe(criteria, tableNameList);
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
