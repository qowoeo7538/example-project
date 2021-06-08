package org.lucas.example.framework.spring.demo.beans.inject.support;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({ImportBeanSelector.class})
@Configuration
public class AnnotationImportSelectorConfig {
}
