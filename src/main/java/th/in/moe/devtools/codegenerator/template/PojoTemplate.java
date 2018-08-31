package th.in.moe.devtools.codegenerator.template;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMod;

import th.in.moe.devtools.codegenerator.common.bean.ColumnBean;
import th.in.moe.devtools.codegenerator.common.bean.GeneratorCriteria;
import th.in.moe.devtools.codegenerator.common.bean.TableBean;
import th.in.moe.devtools.codegenerator.common.constant.GeneratorConstant.TO_STRING_STYLE;
import th.in.moe.devtools.codegenerator.common.util.CodeModelUtils;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: Sep 14, 2012
 */
public class PojoTemplate implements Template {
	
	private static final Logger logger = LoggerFactory.getLogger(PojoTemplate.class);
	
	public JCodeModel execute(GeneratorCriteria criteria, TableBean table) throws JClassAlreadyExistsException {
		
		String fullyqualifiedName = criteria.getResultEntityPackage() + "." + table.getJavaName();
		
		// Generate Class
		JCodeModel pojoModel = new JCodeModel();
		JDefinedClass pojoClass = pojoModel._class(fullyqualifiedName, ClassType.CLASS);
		
		// Generate Primary Key Field and Method
		generateColumnField(criteria, pojoClass, table.getKeyList());
		
		// Generate Column Field and Method
		generateColumnField(criteria, pojoClass, table.getColumnList());
		
		// Generate toString()
		if (!TO_STRING_STYLE.NONE.equals(criteria.getToStringMethodStyle())) {
			CodeModelUtils.generateToStringMethod(pojoModel, pojoClass, criteria.getToStringMethodStyle());
		}
		
		logger.info("Generate {} Success", fullyqualifiedName);
		
		return pojoModel;
	}
	
	private void generateColumnField(GeneratorCriteria criteria, JDefinedClass pojoClass, List<ColumnBean> columnList) {
		for (ColumnBean column : columnList) {
			if (column.isGenerateFlag()) {
				// Generate Field
				pojoClass.field(JMod.PRIVATE, column.getJavaType(), column.getJavaName());
				
				// Generate Getter Method
				CodeModelUtils.generateGetterMethod(pojoClass, column.getJavaType(), column.getJavaName());
				
				// Generate Setter Method
				CodeModelUtils.generateSetterMethod(pojoClass, column.getJavaType(), column.getJavaName());
			}
		}
	}
	
}
