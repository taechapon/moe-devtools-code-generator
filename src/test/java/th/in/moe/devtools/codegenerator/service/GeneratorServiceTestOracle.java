package th.in.moe.devtools.codegenerator.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.BeforeClass;
import org.junit.Test;

import th.in.moe.devtools.codegenerator.common.bean.DatasourceBean;
import th.in.moe.devtools.codegenerator.common.bean.GeneratorCriteria;
import th.in.moe.devtools.codegenerator.common.bean.TableBean;
import th.in.moe.devtools.codegenerator.common.constant.GeneratorConstant;
import th.in.moe.devtools.codegenerator.common.constant.GeneratorConstant.ProfileTemplate;
import th.in.moe.devtools.codegenerator.common.constant.GeneratorConstant.TO_STRING_STYLE;
import th.in.moe.devtools.codegenerator.common.exception.GeneratedException;

public class GeneratorServiceTestOracle {
	
	private static GeneratorService generatorService;
	private static GeneratorCriteria criteria;
	
	@BeforeClass
	public static void init() {
		DatasourceBean datasourceBean = new DatasourceBean();
		datasourceBean.setDatabaseProductionName(GeneratorConstant.DATABASE_PRODUCTION_NAME.MYSQL);
		datasourceBean.setUrl("jdbc:oracle:thin:@150.95.24.42:1521:orcl");
		datasourceBean.setUsername("exciseadm");
		datasourceBean.setPassword("exciseadm");
		
		criteria = new GeneratorCriteria();
		criteria.setDatasourceBean(datasourceBean);
		criteria.setDbCatalog("");
		criteria.setDbSchema("EXCISEADM");
		criteria.setDbTableNamePattern("%");
		criteria.setResultPath("./src/test/java");
		criteria.setResultEntityPackage("com.ss.erp.ap.persistence.entity");
		criteria.setResultRepositoryPackage("com.ss.erp.ap.persistence.repository");
		criteria.setExcludeColumn(Arrays.asList("IS_DELETED","VERSION","CREATED_BY","CREATED_DATE","UPDATED_BY","UPDATED_DATE"));
		criteria.setProfile(ProfileTemplate.POJO);
		criteria.setToStringMethodStyle(TO_STRING_STYLE.JSON_STYLE);
		
		generatorService = new GeneratorService();
	}
	
	@Test
	public void test_getAllTableName() throws GeneratedException {
		List<String> tableNameList = generatorService.getAllTableName(criteria);
		tableNameList.forEach(System.out::println);
	}
	
	//@Test
	public void test_process() throws GeneratedException {
		List<String> tableNameList = generatorService.getAllTableName(criteria);
		List<TableBean> tableBeanList = generatorService.getTableDescribe(criteria, tableNameList);
		tableBeanList.forEach(t -> System.out.println(ToStringBuilder.reflectionToString(t, ToStringStyle.JSON_STYLE)));
		generatorService.genJavaFromTable(criteria, tableBeanList);
	}
	
}
