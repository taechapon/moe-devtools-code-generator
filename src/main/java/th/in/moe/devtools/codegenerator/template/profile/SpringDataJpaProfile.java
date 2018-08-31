package th.in.moe.devtools.codegenerator.template.profile;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.codemodel.JCodeModel;

import th.in.moe.devtools.codegenerator.common.bean.GeneratorCriteria;
import th.in.moe.devtools.codegenerator.common.bean.TableBean;
import th.in.moe.devtools.codegenerator.common.exception.GeneratedException;
import th.in.moe.devtools.codegenerator.common.util.FileUtils;
import th.in.moe.devtools.codegenerator.template.JpaEmbeddableTemplate;
import th.in.moe.devtools.codegenerator.template.JpaEntityTemplate;
import th.in.moe.devtools.codegenerator.template.SpringDataJpaRepositoryTemplate;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: Jul 25, 2018
 */
public class SpringDataJpaProfile implements ProfileTemplate {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringDataJpaProfile.class);
	
	private JpaEmbeddableTemplate jpaEmbeddableTemplate;
	private JpaEntityTemplate jpaEntityTemplate;
	private SpringDataJpaRepositoryTemplate springDataJpaRepositoryTemplate;
	
	public SpringDataJpaProfile(JpaEmbeddableTemplate jpaEmbeddableTemplate,
			JpaEntityTemplate jpaEntityTemplate,
			SpringDataJpaRepositoryTemplate springDataJpaRepositoryTemplate) {
		this.jpaEmbeddableTemplate = jpaEmbeddableTemplate;
		this.jpaEntityTemplate = jpaEntityTemplate;
		this.springDataJpaRepositoryTemplate = springDataJpaRepositoryTemplate;
	}
	
	@Override
	public void generate(GeneratorCriteria criteria, TableBean table) throws GeneratedException {
		try {
			if (table.isCompositeKeyFlag()) {
				JCodeModel pkModel = jpaEmbeddableTemplate.execute(criteria, table);
				FileUtils.buildJCodeModel(new File(criteria.getResultPath()), pkModel);
			}
			JCodeModel entityModel = jpaEntityTemplate.execute(criteria, table);
			FileUtils.buildJCodeModel(new File(criteria.getResultPath()), entityModel);
			if (table.getKeyList() != null && table.getKeyList().size() > 0) {
				JCodeModel crudRepositoryModel = springDataJpaRepositoryTemplate.execute(criteria, table);
				FileUtils.buildJCodeModel(new File(criteria.getResultPath()), crudRepositoryModel);
			} else {
				logger.warn("Table:{} hasn't primary key. It can't generate Repository class", table.getTableName());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new GeneratedException(e.getMessage(), e);
		}
	}

}
