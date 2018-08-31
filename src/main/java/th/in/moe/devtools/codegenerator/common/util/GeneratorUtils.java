package th.in.moe.devtools.codegenerator.common.util;

import th.in.moe.devtools.codegenerator.common.constant.GeneratorConstant;
import th.in.moe.devtools.codegenerator.common.constant.GeneratorConstant.CLASS_NAME;
import th.in.moe.devtools.codegenerator.common.constant.GeneratorConstant.DATABASE_PRODUCTION_NAME;
import th.in.moe.devtools.codegenerator.common.exception.GeneratedException;
import th.in.moe.devtools.codegenerator.template.BuckwaJpaEntityTemplate;
import th.in.moe.devtools.codegenerator.template.BuckwaSpringDataJpaRepositoryTemplate;
import th.in.moe.devtools.codegenerator.template.JpaEmbeddableTemplate;
import th.in.moe.devtools.codegenerator.template.JpaEntityTemplate;
import th.in.moe.devtools.codegenerator.template.PojoTemplate;
import th.in.moe.devtools.codegenerator.template.SpringDataJpaRepositoryTemplate;
import th.in.moe.devtools.codegenerator.template.profile.BuckwaJpaEntityProfile;
import th.in.moe.devtools.codegenerator.template.profile.BuckwaSpringDataJpaProfile;
import th.in.moe.devtools.codegenerator.template.profile.JpaEntityProfile;
import th.in.moe.devtools.codegenerator.template.profile.PojoProfile;
import th.in.moe.devtools.codegenerator.template.profile.ProfileTemplate;
import th.in.moe.devtools.codegenerator.template.profile.SpringDataJpaProfile;
import th.in.moe.devtools.codegenerator.typeconvert.DbTypeConverter;

public abstract class GeneratorUtils {
	
	public static DbTypeConverter getDbTypeConverter(String databaseProductionName) throws GeneratedException {
		String className = null;
		
		if (DATABASE_PRODUCTION_NAME.MYSQL.equals(databaseProductionName)) {
			className = CLASS_NAME.TYPE_CONVERTER.MYSQL;
		} else if (DATABASE_PRODUCTION_NAME.ORACLE.equals(databaseProductionName)) {
			className = CLASS_NAME.TYPE_CONVERTER.ORACLE;
		}
		
		DbTypeConverter dbTypeConverter = null;
		try {
			dbTypeConverter = (DbTypeConverter) Class.forName(className).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new GeneratedException(e.getMessage(), e);
		}
		
		return dbTypeConverter;
	}
	
	public static ProfileTemplate getProfileTemplate(GeneratorConstant.ProfileTemplate profile) throws GeneratedException {
		ProfileTemplate profileTemplate = null;
		
		if (GeneratorConstant.ProfileTemplate.POJO.equals(profile)) {
			PojoTemplate pojoTemplate = new PojoTemplate();
			profileTemplate = new PojoProfile(pojoTemplate);
		} else if (GeneratorConstant.ProfileTemplate.JPA_ENTITY.equals(profile)) {
			JpaEmbeddableTemplate jpaEmbeddableTemplate = new JpaEmbeddableTemplate();
			JpaEntityTemplate jpaEntityTemplate = new JpaEntityTemplate();
			profileTemplate = new JpaEntityProfile(jpaEmbeddableTemplate, jpaEntityTemplate);
		} else if (GeneratorConstant.ProfileTemplate.SPRING_DATA_JPA.equals(profile)) {
			JpaEmbeddableTemplate jpaEmbeddableTemplate = new JpaEmbeddableTemplate();
			JpaEntityTemplate jpaEntityTemplate = new JpaEntityTemplate();
			SpringDataJpaRepositoryTemplate springDataJpaRepositoryTemplate = new SpringDataJpaRepositoryTemplate();
			profileTemplate = new SpringDataJpaProfile(jpaEmbeddableTemplate, jpaEntityTemplate, springDataJpaRepositoryTemplate);
		} else if (GeneratorConstant.ProfileTemplate.BUCKWA_JPA_ENTITY.equals(profile)) {
			JpaEmbeddableTemplate jpaEmbeddableTemplate = new JpaEmbeddableTemplate();
			BuckwaJpaEntityTemplate buckwaJpaEntityTemplate = new BuckwaJpaEntityTemplate();
			profileTemplate = new BuckwaJpaEntityProfile(jpaEmbeddableTemplate, buckwaJpaEntityTemplate);
		} else if (GeneratorConstant.ProfileTemplate.BUCKWA_SPRING_DATA_JPA.equals(profile)) {
			JpaEmbeddableTemplate jpaEmbeddableTemplate = new JpaEmbeddableTemplate();
			BuckwaJpaEntityTemplate buckwaJpaEntityTemplate = new BuckwaJpaEntityTemplate();
			BuckwaSpringDataJpaRepositoryTemplate buckwaSpringDataJpaRepositoryTemplate = new BuckwaSpringDataJpaRepositoryTemplate();
			profileTemplate = new BuckwaSpringDataJpaProfile(jpaEmbeddableTemplate, buckwaJpaEntityTemplate, buckwaSpringDataJpaRepositoryTemplate);
		}
		
		return profileTemplate;
	}
	
}
