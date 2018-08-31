package th.in.moe.devtools.codegenerator.template;

import java.util.List;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMod;

import th.in.moe.devtools.codegenerator.common.bean.ColumnBean;
import th.in.moe.devtools.codegenerator.common.bean.GeneratorCriteria;
import th.in.moe.devtools.codegenerator.common.bean.TableBean;
import th.in.moe.devtools.codegenerator.common.util.CodeModelUtils;
import th.in.moe.devtools.codegenerator.common.util.NameUtils;

public abstract class AbstractJpaEntityTemplate {
	
	protected void generatePrimaryKeyField(GeneratorCriteria criteria, JDefinedClass entityClass, TableBean table) throws JClassAlreadyExistsException {
		if (table.isCompositeKeyFlag()) {
			String pkFullyqualifiedName = criteria.getResultEntityPackage() + "." + table.getJavaCompositeKeyName();
			JDefinedClass pkEntityClass = CodeModelUtils.getJDefinedClass(pkFullyqualifiedName);
			
			// Generate Field
			JFieldVar field = entityClass.field(JMod.PRIVATE, pkEntityClass, NameUtils.PK_ENTITY_ATTR_NAME);
			field.annotate(javax.persistence.EmbeddedId.class);
			
			// Generate Getter Method
			CodeModelUtils.generateGetterMethod(entityClass, pkEntityClass, NameUtils.PK_ENTITY_ATTR_NAME);
			
			// Generate Setter Method
			CodeModelUtils.generateSetterMethod(entityClass, pkEntityClass, NameUtils.PK_ENTITY_ATTR_NAME);
		} else {
			for (ColumnBean column : table.getKeyList()) {
				// Generate Field
				JFieldVar field = entityClass.field(JMod.PRIVATE, column.getJavaType(), column.getJavaName());
				field.annotate(javax.persistence.Id.class);
				CodeModelUtils.generateJpaPrimaryKeyAnnotation(field, criteria.getDatasourceBean().getDatabaseProductionName(), table.getTableName());
				field.annotate(javax.persistence.Column.class).param("name", column.getColumnName());
				
				// Generate Getter Method
				CodeModelUtils.generateGetterMethod(entityClass, column.getJavaType(), column.getJavaName());
				
				// Generate Setter Method
				CodeModelUtils.generateSetterMethod(entityClass, column.getJavaType(), column.getJavaName());
			}
		}
	}
	
	protected void generateColumnField(JDefinedClass entityClass, String tableName, List<ColumnBean> columnList) {
		for (ColumnBean column : columnList) {
			if (column.isGenerateFlag()) {
				// Generate Field
				JFieldVar field = entityClass.field(JMod.PRIVATE, column.getJavaType(), column.getJavaName());
				field.annotate(javax.persistence.Column.class).param("name", column.getColumnName());
				
				// Generate Getter Method
				CodeModelUtils.generateGetterMethod(entityClass, column.getJavaType(), column.getJavaName());
				
				// Generate Setter Method
				CodeModelUtils.generateSetterMethod(entityClass, column.getJavaType(), column.getJavaName());
			}
		}
	}
	
}
