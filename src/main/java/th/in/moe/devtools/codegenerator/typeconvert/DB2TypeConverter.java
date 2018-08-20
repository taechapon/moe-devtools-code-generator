package th.in.moe.devtools.codegenerator.typeconvert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: May 1, 2013
 */
public class DB2TypeConverter implements DbTypeConverter {

	@Override
	public Class<?> convert(int dataType, Object... optional) {
		
		// TODO: Add all type of DB2 database
		switch (dataType) {
			
			// Numeric
			case Types.SMALLINT: {
				return Short.class;
			}
			case Types.INTEGER: {
				return Integer.class;
			}
			case Types.BIGINT: {
				return BigInteger.class;
			}
			case Types.REAL: {
				return Float.class;
			}
			case Types.DOUBLE: {
				return Double.class;
			}
			case Types.DECIMAL: {
				return BigDecimal.class;
			}
			
			// String
			case Types.CHAR:
			case Types.VARCHAR: {
				return String.class;
			}
			
			// Date, Time Stamp
			case Types.DATE:
			case Types.TIMESTAMP:
				return java.util.Date.class;
			
			// Default
			default:
				return Object.class;
		}
	}

}
