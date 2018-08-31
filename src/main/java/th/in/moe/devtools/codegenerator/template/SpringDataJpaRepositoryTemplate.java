package th.in.moe.devtools.codegenerator.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

import th.in.moe.devtools.codegenerator.common.bean.GeneratorCriteria;
import th.in.moe.devtools.codegenerator.common.bean.TableBean;
import th.in.moe.devtools.codegenerator.common.util.CodeModelUtils;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: Jul 25, 2018
 */
public class SpringDataJpaRepositoryTemplate implements Template {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringDataJpaRepositoryTemplate.class);
	
	private static final String SPRING_CRUD_REPOSITORY_INTERFACE = "org.springframework.data.repository.CrudRepository";
	private static final String REPOSITORY = "Repository";
	
	@Override
	public JCodeModel execute(GeneratorCriteria criteria, TableBean table) throws JClassAlreadyExistsException {
		
		String fullyqualifiedName = criteria.getResultRepositoryPackage() + "." + table.getJavaName() + REPOSITORY;
		
		// ID
		JDefinedClass idClass = null;
		if (table.isCompositeKeyFlag()) {
			idClass = CodeModelUtils.getJDefinedClass(criteria.getResultEntityPackage() + "." + table.getJavaCompositeKeyName());
		} else {
			idClass = CodeModelUtils.getJDefinedClass(table.getKeyList().get(0).getJavaType().getName());
		}
		
		// CrudRepository
		JCodeModel springCrudRepositoryModel = new JCodeModel();
		JClass springCrudRepositoryClass = springCrudRepositoryModel._class(SPRING_CRUD_REPOSITORY_INTERFACE, ClassType.INTERFACE).narrow(
			CodeModelUtils.getJDefinedClass(criteria.getResultEntityPackage() + "." + table.getJavaName()),
			idClass
		);
		
		// Generate Class
		JCodeModel crudRepositoryModel = new JCodeModel();
		JDefinedClass crudRepositoryClass = crudRepositoryModel._class(fullyqualifiedName, ClassType.INTERFACE);
		crudRepositoryClass._extends(springCrudRepositoryClass);
		
		logger.info("Generate {} Success", fullyqualifiedName);
		
		return crudRepositoryModel;
	}
	
}
