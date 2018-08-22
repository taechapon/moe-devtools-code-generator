package th.in.moe.devtools.codegenerator.common.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import th.in.moe.devtools.codegenerator.common.constant.GeneratorConstant.ProfileTemplate;

@XmlRootElement
public class GeneratorCriteria {

	private DatasourceBean datasourceBean;
	private String dbCatalog;
	private String dbSchema;
	private String dbTableNamePattern;
	private String resultPath;
	private String resultEntityPackage;
	private String resultRepositoryPackage;
	private List<String> excludeColumn;
	private ProfileTemplate profile;
	private String toStringMethodStyle;

	public DatasourceBean getDatasourceBean() {
		return datasourceBean;
	}

	public void setDatasourceBean(DatasourceBean datasourceBean) {
		this.datasourceBean = datasourceBean;
	}

	public String getDbCatalog() {
		return dbCatalog;
	}

	public void setDbCatalog(String dbCatalog) {
		this.dbCatalog = dbCatalog;
	}

	public String getDbSchema() {
		return dbSchema;
	}

	public void setDbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}

	public String getDbTableNamePattern() {
		return dbTableNamePattern;
	}

	public void setDbTableNamePattern(String dbTableNamePattern) {
		this.dbTableNamePattern = dbTableNamePattern;
	}

	public String getResultPath() {
		return resultPath;
	}

	public void setResultPath(String resultPath) {
		this.resultPath = resultPath;
	}

	public String getResultEntityPackage() {
		return resultEntityPackage;
	}

	public void setResultEntityPackage(String resultEntityPackage) {
		this.resultEntityPackage = resultEntityPackage;
	}

	public String getResultRepositoryPackage() {
		return resultRepositoryPackage;
	}

	public void setResultRepositoryPackage(String resultRepositoryPackage) {
		this.resultRepositoryPackage = resultRepositoryPackage;
	}

	public List<String> getExcludeColumn() {
		return excludeColumn;
	}

	public void setExcludeColumn(List<String> excludeColumn) {
		this.excludeColumn = excludeColumn;
	}

	public ProfileTemplate getProfile() {
		return profile;
	}

	public void setProfile(ProfileTemplate profile) {
		this.profile = profile;
	}

	public String getToStringMethodStyle() {
		return toStringMethodStyle;
	}

	public void setToStringMethodStyle(String toStringMethodStyle) {
		this.toStringMethodStyle = toStringMethodStyle;
	}

}
