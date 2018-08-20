package th.in.moe.devtools.codegenerator.common.constant;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: May 2, 2013
 */
public class GeneratorConstant {
	
	public static final class CLASS_NAME {
		public static final class TYPE_CONVERTER {
			private static final String PACKAGE = "th.in.moe.devtools.codegenerator.typeconvert.";
			public static final String MYSQL = PACKAGE + "MySQLTypeConverter";
			public static final String ORACLE = PACKAGE + "OracleTypeConverter";
		}
	}
	
	public static final class DATABASE_PRODUCTION_NAME {
		public static final String MYSQL = "MySQL";
		public static final String ORACLE = "Oracle";
	}
	
	public enum ProfileTemplate {
		POJO,
		JPA_ENTITY,
		SPRING_DATA_JPA,
		BUCKWA_JPA_ENTITY,
		BUCKWA_SPRING_DATA_JPA;
	}
	
}
