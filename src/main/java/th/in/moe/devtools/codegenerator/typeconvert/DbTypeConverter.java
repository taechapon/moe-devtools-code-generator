package th.in.moe.devtools.codegenerator.typeconvert;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: Sep 14, 2012
 */
public interface DbTypeConverter {
	
	public Class<?> convert(int dataType, Object... optional);
	
}
