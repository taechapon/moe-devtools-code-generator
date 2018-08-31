package th.in.moe.devtools.codegenerator.common.util;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: Sep 14, 2012
 */
public abstract class NameUtils {
	
	public static final String PK_ENTITY = "PK";
	public static final String PK_ENTITY_ATTR_NAME = "id";
	
	// Getter and Setter
	private static final String GET = "get";
	private static final String SET = "set";
	// Oracle Database
	private static final String GENERATOR_NAME = "_GEN";
	private static final String SEQUENCE_NAME = "_SEQ";

	// Getter and Setter
	public static String toUpperCaseFirstChar(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
	public static String toLowerCaseFirstChar(String s) {
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}
	
	public static String genGetter(String attrName) {
		return GET + toUpperCaseFirstChar(attrName);
	}
	
	public static String genSetter(String attrName) {
		return SET + toUpperCaseFirstChar(attrName);
	}
	
	// Oracle Database Sequence
	public static String genOracleGeneratedValueName(String tableName) {
		return tableName + GENERATOR_NAME;
	}
	
	public static String genOracleSequenceName(String tableName) {
		return tableName + SEQUENCE_NAME;
	}
	
}
