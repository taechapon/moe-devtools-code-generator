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

/*
 * @Author: Taechapon Himarat (Su)
 * @Create: Jul 25, 2018
 */
public class JpaEntityProfile implements ProfileTemplate {
	
	private static final Logger logger = LoggerFactory.getLogger(JpaEntityProfile.class);
	
	private JpaEmbeddableTemplate jpaEmbeddableTemplate;
	private JpaEntityTemplate jpaEntityTemplate;
	
	public JpaEntityProfile(JpaEmbeddableTemplate jpaEmbeddableTemplate,
			JpaEntityTemplate jpaEntityTemplate) {
		this.jpaEmbeddableTemplate = jpaEmbeddableTemplate;
		this.jpaEntityTemplate = jpaEntityTemplate;
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
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new GeneratedException(e.getMessage(), e);
		}
	}

}
