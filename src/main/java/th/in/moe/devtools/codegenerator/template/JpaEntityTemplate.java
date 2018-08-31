package th.in.moe.devtools.codegenerator.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

import th.in.moe.devtools.codegenerator.common.bean.GeneratorCriteria;
import th.in.moe.devtools.codegenerator.common.bean.TableBean;
import th.in.moe.devtools.codegenerator.common.constant.GeneratorConstant.TO_STRING_STYLE;
import th.in.moe.devtools.codegenerator.common.util.CodeModelUtils;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: Jul 25, 2018
 */
public class JpaEntityTemplate extends AbstractJpaEntityTemplate implements Template {
	
	private static final Logger logger = LoggerFactory.getLogger(JpaEntityTemplate.class);
	
	public JCodeModel execute(GeneratorCriteria criteria, TableBean table) throws JClassAlreadyExistsException {
		
		String fullyqualifiedName = criteria.getResultEntityPackage() + "." + table.getJavaName();
		
		// Generate Class
		JCodeModel entityModel = new JCodeModel();
		JDefinedClass entityClass = entityModel._class(fullyqualifiedName, ClassType.CLASS);
		entityClass._implements(java.io.Serializable.class);
		entityClass.annotate(javax.persistence.Entity.class);
		entityClass.annotate(javax.persistence.Table.class).param("name", table.getTableName());
		
		// Generate Primary Key Field and Method
		generatePrimaryKeyField(criteria, entityClass, table);
		
		// Generate Column Field and Method
		generateColumnField(entityClass, table.getTableName(), table.getColumnList());
		
		// Generate toString()
		if (!TO_STRING_STYLE.NONE.equals(criteria.getToStringMethodStyle())) {
			CodeModelUtils.generateToStringMethod(entityModel, entityClass, criteria.getToStringMethodStyle());
		}
		
		logger.info("Generate {} Success", fullyqualifiedName);
		
		return entityModel;
	}
	
}
