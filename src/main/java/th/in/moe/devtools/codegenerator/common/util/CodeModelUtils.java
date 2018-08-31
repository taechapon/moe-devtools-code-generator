package th.in.moe.devtools.codegenerator.common.util;

import javax.persistence.GenerationType;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;

import th.in.moe.devtools.codegenerator.common.constant.GeneratorConstant.DATABASE_PRODUCTION_NAME;

/**
 * @Author: Taechapon Himarat (Su)
 * @Create: May 5, 2013
 */
public abstract class CodeModelUtils {
	
	// Generate Getter Method
	public static JMethod generateGetterMethod(JDefinedClass jDefinedClass, Class<?> type, String attrName) {
		JMethod getterMethod = jDefinedClass.method(JMod.PUBLIC, type, NameUtils.genGetter(attrName));
		return generateGetterBlock(getterMethod, attrName);
	}
	
	public static JMethod generateGetterMethod(JDefinedClass jDefinedClass, JType jType, String attrName) {
		JMethod getterMethod = jDefinedClass.method(JMod.PUBLIC, jType, NameUtils.genGetter(attrName));
		return generateGetterBlock(getterMethod, attrName);
	}
	
	public static JMethod generateGetterBlock(JMethod getterMethod, String attrName) {
		JBlock getterBlock = getterMethod.body();
		getterBlock._return(JExpr.ref(attrName));
		return getterMethod;
	}
	
	
	// Generate Setter Method
	public static JMethod generateSetterMethod(JDefinedClass jDefinedClass, Class<?> type, String attrName) {
		JMethod setterMethod = jDefinedClass.method(JMod.PUBLIC, void.class, NameUtils.genSetter(attrName));
		setterMethod.param(type, attrName);
		return generateSetterBlock(setterMethod, attrName);
	}
	
	public static JMethod generateSetterMethod(JDefinedClass jDefinedClass, JType jType, String attrName) {
		JMethod setterMethod = jDefinedClass.method(JMod.PUBLIC, void.class, NameUtils.genSetter(attrName));
		setterMethod.param(jType, attrName);
		return generateSetterBlock(setterMethod, attrName);
	}
	
	private static JMethod generateSetterBlock(JMethod setterMethod, String attrName) {
		JBlock setterBlock = setterMethod.body();
		setterBlock.assign(JExpr._this().ref(attrName), JExpr.ref(attrName));
		return setterMethod;
	}
	
	// Generate toString() by using ToStringBuilder from Apache Common
	// result: ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	public static JMethod generateToStringMethod(JCodeModel codeModel, JDefinedClass jDefinedClass, String toStringStyle) {
		JMethod toStringMethod = jDefinedClass.method(JMod.PUBLIC, String.class, "toString");
		JBlock toStringBlock = toStringMethod.body();
		JClass toStringBuilderClass = codeModel.ref(org.apache.commons.lang3.builder.ToStringBuilder.class);
		JClass toStringStyleClass = codeModel.ref(org.apache.commons.lang3.builder.ToStringStyle.class);
		JInvocation toStringBuilder = toStringBuilderClass.staticInvoke("reflectionToString")
			.arg(JExpr._this())
			.arg(toStringStyleClass.staticRef(toStringStyle));
		toStringBlock._return(toStringBuilder);
		return toStringMethod;
	}
	
	// Generate @GeneratedValue for @Id
	public static JFieldVar generateJpaPrimaryKeyAnnotation(JFieldVar field, String dbProductionName, String tableName) {
		if (DATABASE_PRODUCTION_NAME.MYSQL.equals(dbProductionName)) {
			// @GeneratedValue(strategy = GenerationType.IDENTITY)
			field.annotate(javax.persistence.GeneratedValue.class).param("strategy", GenerationType.IDENTITY);
		} else if (DATABASE_PRODUCTION_NAME.ORACLE.equals(dbProductionName)) {
			// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "{tableName}_GEN")
			// @SequenceGenerator(name = "{tableName}_GEN", sequenceName = "{tableName}_SEQ", allocationSize = 1)
			field.annotate(javax.persistence.GeneratedValue.class)
				.param("strategy", GenerationType.SEQUENCE)
				.param("generator", NameUtils.genOracleGeneratedValueName(tableName));
			field.annotate(javax.persistence.SequenceGenerator.class)
				.param("name", NameUtils.genOracleGeneratedValueName(tableName))
				.param("sequenceName", NameUtils.genOracleSequenceName(tableName))
				.param("allocationSize", 1);
		}
		return field;
	}
	
	public static JDefinedClass getJDefinedClass(String className) throws JClassAlreadyExistsException {
		JCodeModel baseEntityModel = new JCodeModel();
		return baseEntityModel._class(className, ClassType.CLASS);
	}
	
}
