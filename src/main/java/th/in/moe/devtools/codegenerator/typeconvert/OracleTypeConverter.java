package th.in.moe.devtools.codegenerator.typeconvert;

import java.math.BigDecimal;
import java.sql.Types;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: Jan 24, 2016
 */
public class OracleTypeConverter implements DbTypeConverter {

	@Override
	public Class<?> convert(int dataType, Object... optional) {
		
		// TODO: Add all type of database
		switch (dataType) {
		
			// Number
			case Types.DECIMAL:
			case Types.NUMERIC: {
				return BigDecimal.class;
			}
			case Types.BIT: {
				return Boolean.class;
			}
			case Types.TINYINT: {
				return Byte.class;
			}
			case Types.SMALLINT: {
				return Short.class;
			}
			case Types.INTEGER: {
				return Integer.class;
			}
			case Types.BIGINT: {
				return Long.class;
			}
			case Types.REAL: {
				return Float.class;
			}
			case Types.FLOAT:
			case Types.DOUBLE: {
				return Double.class;
			}
			
			
			// Character
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
			case Types.NCHAR: {
				return String.class;
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
