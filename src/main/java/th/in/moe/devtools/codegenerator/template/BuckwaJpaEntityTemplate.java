package th.in.moe.devtools.codegenerator.template;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMod;

import th.in.moe.devtools.codegenerator.common.bean.ColumnBean;
import th.in.moe.devtools.codegenerator.common.bean.GeneratorCriteria;
import th.in.moe.devtools.codegenerator.common.bean.TableBean;
import th.in.moe.devtools.codegenerator.common.constant.GeneratorConstant.TO_STRING_STYLE;
import th.in.moe.devtools.codegenerator.common.util.CodeModelUtils;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: Jul 25, 2018
 */
public class BuckwaJpaEntityTemplate implements Template {
	
	private static final Logger logger = LoggerFactory.getLogger(BuckwaJpaEntityTemplate.class);
	
	private static final String BUCKWA_BASE_ENTITY_CLASS = "th.co.baiwa.buckwaframework.common.persistence.entity.BaseEntity";
	
	public JCodeModel execute(GeneratorCriteria criteria, TableBean table, Object... obj) throws JClassAlreadyExistsException {
		
		String packageName = criteria.getResultEntityPackage();
		String fullyqualifiedName = packageName + "." + table.getJavaName();
		JDefinedClass baseEntityClass = getBaseEntityClass();
		
		// Generate Class
		JCodeModel entityModel = new JCodeModel();
		JDefinedClass entityClass = entityModel._class(fullyqualifiedName, ClassType.CLASS);
		entityClass._extends(baseEntityClass);
		entityClass.annotate(javax.persistence.Entity.class);
		entityClass.annotate(javax.persistence.Table.class).param("name", table.getTableName());
		
		// Generate Primary Key Field and Method
		generateColumnField(criteria, entityClass, table.getTableName(), table.getKeyList());
		
		// Generate Column Field and Method
		generateColumnField(criteria, entityClass, table.getTableName(), table.getColumnList());
		
		// Generate toString()
		if (!TO_STRING_STYLE.NONE.equals(criteria.getToStringMethodStyle())) {
			CodeModelUtils.generateToStringMethod(entityModel, entityClass, criteria.getToStringMethodStyle());
		}
		
		logger.info("Generate {} Success", fullyqualifiedName);
		
		return entityModel;
	}
	
	private void generateColumnField(GeneratorCriteria criteria, JDefinedClass entityClass, String tableName, List<ColumnBean> columnList) {
		for (ColumnBean column : columnList) {
			if (!criteria.getExcludeColumn().contains(column.getColumnName())) {
				// Generate Field
				JFieldVar field = entityClass.field(JMod.PRIVATE, column.getJavaType(), column.getJavaName());
				if (column.isPrimaryKey()) {
					field.annotate(javax.persistence.Id.class);
					CodeModelUtils.generateJpaPrimaryKeyAnnotation(field, criteria.getDatasourceBean().getDatabaseProductionName(), tableName);
				}
				field.annotate(javax.persistence.Column.class).param("name", column.getColumnName());
				
				// Generate Getter Method
				CodeModelUtils.generateGetterMethod(entityClass, column.getJavaType(), column.getJavaName());
				
				// Generate Setter Method
				CodeModelUtils.generateSetterMethod(entityClass, column.getJavaType(), column.getJavaName());
			}
		}
	}
	
	private JDefinedClass getBaseEntityClass() throws JClassAlreadyExistsException {
		JCodeModel baseEntityModel = new JCodeModel();
		baseEntityModel._class(BUCKWA_BASE_ENTITY_CLASS, ClassType.CLASS);
		return baseEntityModel._getClass(BUCKWA_BASE_ENTITY_CLASS);
	}
	
}
