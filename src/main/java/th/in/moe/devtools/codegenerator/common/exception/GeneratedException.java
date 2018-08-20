package th.in.moe.devtools.codegenerator.common.exception;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: May 1, 2013
 */
public class GeneratedException extends Exception {

	private static final long serialVersionUID = -4743498322329519625L;
	
	public GeneratedException(String message) {
		super(message);
	}
	
	public GeneratedException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
