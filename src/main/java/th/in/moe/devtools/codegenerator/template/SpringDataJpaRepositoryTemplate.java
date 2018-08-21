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
import th.in.moe.devtools.codegenerator.common.exception.GeneratedException;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: Jul 25, 2018
 */
public class SpringDataJpaRepositoryTemplate implements Template {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringDataJpaRepositoryTemplate.class);
	
	private static final String SPRING_CRUD_REPOSITORY_INTERFACE = "org.springframework.data.repository.CrudRepository";
	private static final String REPOSITORY = "Repository";
	
	@Override
	public JCodeModel execute(GeneratorCriteria criteria, TableBean table, Object... obj) throws GeneratedException, JClassAlreadyExistsException {
		
		String packageName = criteria.getResultRepositoryPackage();
		String fullyqualifiedName = packageName + "." + table.getJavaName() + REPOSITORY;
		
		// CrudRepository
		JCodeModel springCrudRepositoryModel = new JCodeModel();
		JClass springCrudRepositoryClass = springCrudRepositoryModel._class(SPRING_CRUD_REPOSITORY_INTERFACE, ClassType.INTERFACE).narrow(
			springCrudRepositoryModel.ref(criteria.getResultEntityPackage() + "." + table.getJavaName()),
			springCrudRepositoryModel.ref(table.getKeyList().get(0).getJavaType())
		);
		
		// Generate Class
		JCodeModel crudRepositoryModel = new JCodeModel();
		JDefinedClass crudRepositoryClass = crudRepositoryModel._class(fullyqualifiedName, ClassType.INTERFACE);
		crudRepositoryClass._extends(springCrudRepositoryClass);
		
		logger.info("Generate {} Success", fullyqualifiedName);
		
		return crudRepositoryModel;
	}

}
