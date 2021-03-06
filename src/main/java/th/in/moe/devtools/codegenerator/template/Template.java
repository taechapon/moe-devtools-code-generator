package th.in.moe.devtools.codegenerator.template;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;

import th.in.moe.devtools.codegenerator.common.bean.GeneratorCriteria;
import th.in.moe.devtools.codegenerator.common.bean.TableBean;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: May 1, 2013
 */
public interface Template {
	
	public JCodeModel execute(GeneratorCriteria criteria, TableBean table) throws JClassAlreadyExistsException;
	
}
