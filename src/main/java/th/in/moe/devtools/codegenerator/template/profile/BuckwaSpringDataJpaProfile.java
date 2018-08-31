package th.in.moe.devtools.codegenerator.template.profile;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.codemodel.JCodeModel;

import th.in.moe.devtools.codegenerator.common.bean.GeneratorCriteria;
import th.in.moe.devtools.codegenerator.common.bean.TableBean;
import th.in.moe.devtools.codegenerator.common.exception.GeneratedException;
import th.in.moe.devtools.codegenerator.common.util.FileUtils;
import th.in.moe.devtools.codegenerator.template.BuckwaJpaEntityTemplate;
import th.in.moe.devtools.codegenerator.template.BuckwaSpringDataJpaRepositoryTemplate;
import th.in.moe.devtools.codegenerator.template.JpaEmbeddableTemplate;

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: Jul 26, 2018
 */
public class BuckwaSpringDataJpaProfile implements ProfileTemplate {
	
	private static final Logger logger = LoggerFactory.getLogger(BuckwaSpringDataJpaProfile.class);
	
	private JpaEmbeddableTemplate jpaEmbeddableTemplate;
	private BuckwaJpaEntityTemplate buckwaJpaEntityTemplate;
	private BuckwaSpringDataJpaRepositoryTemplate buckwaSpringDataJpaRepositoryTemplate;
	
	public BuckwaSpringDataJpaProfile(JpaEmbeddableTemplate jpaEmbeddableTemplate,
			BuckwaJpaEntityTemplate buckwaJpaEntityTemplate,
			BuckwaSpringDataJpaRepositoryTemplate buckwaSpringDataJpaRepositoryTemplate) {
		this.jpaEmbeddableTemplate = jpaEmbeddableTemplate;
		this.buckwaJpaEntityTemplate = buckwaJpaEntityTemplate;
		this.buckwaSpringDataJpaRepositoryTemplate = buckwaSpringDataJpaRepositoryTemplate;
	}
	
	@Override
	public void generate(GeneratorCriteria criteria, TableBean table) throws GeneratedException {
		try {
			if (table.isCompositeKeyFlag()) {
				JCodeModel pkModel = jpaEmbeddableTemplate.execute(criteria, table);
				FileUtils.buildJCodeModel(new File(criteria.getResultPath()), pkModel);
			}
			JCodeModel entityModel = buckwaJpaEntityTemplate.execute(criteria, table);
			FileUtils.buildJCodeModel(new File(criteria.getResultPath()), entityModel);
			if (table.getKeyList() != null && table.getKeyList().size() > 0) {
				JCodeModel crudRepositoryModel = buckwaSpringDataJpaRepositoryTemplate.execute(criteria, table);
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
