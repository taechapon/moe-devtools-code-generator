package th.in.moe.devtools.codegenerator.common.util;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.codemodel.JCodeModel;

/**
 * @Author: Taechapon Himarat (Su)
 * @Create: May 1, 2013
 */
public abstract class FileUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
	
	public static void buildJCodeModel(File path, JCodeModel jCodeModel) throws IOException {
		if (path.isDirectory()) {
			path.mkdirs();
			jCodeModel.build(path);
		} else {
			String msg = "This path is not directory.";
			logger.warn(msg);
			throw new IOException(msg);
		}
	}
	
}
