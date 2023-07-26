package org.secwallet.core.engine;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.io.StringWriter;

public class FreemarkEngine {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Bean
    public Configuration getFreeMarkerConfiguration(){
        return freeMarkerConfigurer.getConfiguration();
    }

    /**
     * Generate the corresponding string from the specified template.
     * @param templateName Template name, the base path of the template is: WEB-INF/template directory.
     * @param model passes in the data object.
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String mergeTemplateIntoString(String templateName,Object model) throws IOException, TemplateException {

        Template template=this.getFreeMarkerConfiguration().getTemplate(templateName);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }




    /**
     * Parse the content according to the string template
     * @param obj The object to resolve.
     * @param templateSource String template.
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    public  String parseByStringTemplate(Object obj,String templateSource) throws TemplateException, IOException
    {
        StringTemplateLoader loader = new StringTemplateLoader();
        this.getFreeMarkerConfiguration().setTemplateLoader(loader);
        this.getFreeMarkerConfiguration().setClassicCompatible(true);
        loader.putTemplate("freemaker", templateSource);
        Template template = this.getFreeMarkerConfiguration().getTemplate("freemaker");
        StringWriter writer = new StringWriter();
        template.process(obj, writer);
        return writer.toString();

    }
}
