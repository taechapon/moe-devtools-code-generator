package th.in.moe.devtools.codegenerator.template.profile;

import th.in.moe.devtools.codegenerator.common.bean.GeneratorCriteria;
import th.in.moe.devtools.codegenerator.common.bean.TableBean;
import th.in.moe.devtools.codegenerator.common.exception.GeneratedException;

public interface ProfileTemplate {
	
	public void generate(GeneratorCriteria criteria, TableBean table) throws GeneratedException;
	
}
