package th.in.moe.devtools.codegenerator.typeconvert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;

import org.apache.commons.lang3.ArrayUtils;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: Sep 14, 2012
 */
public class MySQLTypeConverter implements DbTypeConverter {
	
	private static final String UNSIGNED = "UNSIGNED";
	
	public Class<?> convert(int dataType, Object... optional) {
		
		String opt = "";
		if (ArrayUtils.isNotEmpty(optional)) {
			opt = (String) optional[0];
		}
		
		// TODO: Add all type of database
		switch (dataType) {
			
			// String
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR: {
				return String.class;
			}
			
			// Numeric
			case Types.BIT:
			case Types.TINYINT:
			case Types.BOOLEAN:
			case Types.SMALLINT: {
				return Integer.class;
			}
			case Types.INTEGER: {
				if (!opt.contains(UNSIGNED)) {
					return Integer.class;
				}
				else {
					return Long.class;
				}
			}
			case Types.BIGINT: {
				if (!opt.contains(UNSIGNED)) {
					return Long.class;
				}
				else {
					return BigInteger.class;
				}
			}
			case Types.FLOAT: {
				return Float.class;
			}
			case Types.DOUBLE: {
				return Double.class;
			}
			case Types.DECIMAL:
			case Types.REAL:
			case Types.NUMERIC: {
				return BigDecimal.class;
			}
			
			// Date, Time Stamp
			case Types.TIMESTAMP:
			case Types.DATE:
			case Types.TIME: {
				return java.util.Date.class;
			}
			
			// Default
			default:
				return Object.class;
		}
	}
	
}
