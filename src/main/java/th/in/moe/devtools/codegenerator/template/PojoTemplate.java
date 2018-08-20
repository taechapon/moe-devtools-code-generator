package th.in.moe.devtools.codegenerator.template;

import java.util.List;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMod;

import th.in.moe.devtools.codegenerator.common.bean.ColumnBean;
import th.in.moe.devtools.codegenerator.common.bean.GeneratorCriteria;
import th.in.moe.devtools.codegenerator.common.bean.TableBean;
import th.in.moe.devtools.codegenerator.common.util.CodeModelUtils;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: Sep 14, 2012
 */
public class PojoTemplate implements Template {
	
	public JCodeModel execute(GeneratorCriteria criteria, TableBean table, Object... obj) throws JClassAlreadyExistsException {
		
		String packageName = criteria.getResultEntityPackage();
		String fullyqualifiedName = packageName + "." + table.getJavaName();
		
		// Generate Class
		JCodeModel pojoModel = new JCodeModel();
		JDefinedClass pojoClass = pojoModel._class(fullyqualifiedName, ClassType.CLASS);
		
		// Generate Primary Key Field and Method
		generateColumnField(criteria, pojoClass, table.getKeyList());
		
		// Generate Column Field and Method
		generateColumnField(criteria, pojoClass, table.getColumnList());
		
		// Generate toString()
		if (criteria.isGenerateToStringMethodFlag()) {
			CodeModelUtils.generateToStringMethod(pojoModel, pojoClass);
		}
		
		return pojoModel;
	}
	
	private void generateColumnField(GeneratorCriteria criteria, JDefinedClass pojoClass, List<ColumnBean> columnList) {
		for (ColumnBean column : columnList) {
			if (!criteria.getExcludeColumn().contains(column.getColumnName())) {
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
