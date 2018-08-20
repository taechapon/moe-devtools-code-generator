package th.in.moe.devtools.codegenerator.template;

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
public class BuckwaSpringDataJpaRepositoryTemplate implements Template {
	
	private static final String BUCKWA_CRUD_REPOSITORY_INTERFACE = "th.co.baiwa.buckwaframework.common.persistence.repository.CommonJpaCrudRepository";
	private static final String REPOSITORY = "Repository";
	
	@Override
	public JCodeModel execute(GeneratorCriteria criteria, TableBean table, Object... obj) throws GeneratedException, JClassAlreadyExistsException {
		
		String packageName = criteria.getResultRepositoryPackage();
		String fullyqualifiedName = packageName + "." + table.getJavaName() + REPOSITORY;
		
		// CommonJpaCrudRepository
		JCodeModel buckwaCrudRepositoryModel = new JCodeModel();
		JClass buckwaCrudRepositoryClass = buckwaCrudRepositoryModel._class(BUCKWA_CRUD_REPOSITORY_INTERFACE, ClassType.INTERFACE).narrow(
			buckwaCrudRepositoryModel.ref(criteria.getResultEntityPackage() + "." + table.getJavaName()),
			buckwaCrudRepositoryModel.ref(table.getKeyList().get(0).getJavaType())
		);
		
		// Generate Class
		JCodeModel crudRepositoryModel = new JCodeModel();
		JDefinedClass crudRepositoryClass = crudRepositoryModel._class(fullyqualifiedName, ClassType.INTERFACE);
		crudRepositoryClass._extends(buckwaCrudRepositoryClass);
		
		return crudRepositoryModel;
	}

}
