package th.in.moe.devtools.codegenerator.persistence.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import th.in.moe.devtools.codegenerator.common.bean.DatasourceBean;
import th.in.moe.devtools.codegenerator.common.constant.GeneratorConstant.DATABASE_PRODUCTION_NAME;
import th.in.moe.devtools.codegenerator.common.exception.GeneratedException;

public class GeneralDao {
	
	private static final Logger logger = LoggerFactory.getLogger(GeneralDao.class);
	
	public int testConnection(DatasourceBean datasourceBean) throws GeneratedException {
		logger.info("testConnection");
		
		String sql = null;
		if (DATABASE_PRODUCTION_NAME.MYSQL.equals(datasourceBean.getDatabaseProductionName())) {
			sql = "SELECT 1";
		} else if (DATABASE_PRODUCTION_NAME.ORACLE.equals(datasourceBean.getDatabaseProductionName())) {
			sql = "SELECT 1 FROM DUAL";
		}
		
		try (Connection connection = DriverManager.getConnection(datasourceBean.getUrl(), datasourceBean.getUsername(), datasourceBean.getPassword());
				ResultSet rs = connection.createStatement().executeQuery(sql)) {
			if (rs.next()) {
				logger.info("Ping succeeded!");
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new GeneratedException(e.getMessage(), e);
		}
		
		return 1;
	}
	
}
