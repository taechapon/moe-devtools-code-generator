package th.in.moe.devtools.codegenerator.common.bean;

public class DatasourceBean {

	private String databaseProductionName;
	private String url;
	private String username;
	private String password;

	public String getDatabaseProductionName() {
		return databaseProductionName;
	}

	public void setDatabaseProductionName(String databaseProductionName) {
		this.databaseProductionName = databaseProductionName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
