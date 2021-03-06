package com.faforever.moderatorclient.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jasminb.jsonapi.ResourceConverter;
import com.github.jasminb.jsonapi.annotations.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.ArrayList;
import java.util.List;

import static com.github.nocatch.NoCatch.noCatch;
import static java.lang.Class.forName;

@Configuration
public class JsonApiConfig {

    @Bean
    public ResourceConverter resourceConverter(ObjectMapper objectMapper) {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return new ResourceConverter(objectMapper, findJsonApiTypes("com.faforever.moderatorclient.api.dto", "com.faforever.commons.api.dto"));
    }

    private Class<?>[] findJsonApiTypes(String... scanPackages) {
        List<Class<?>> classes = new ArrayList<>();
        for (String packageName : scanPackages) {
            ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
            provider.addIncludeFilter(new AnnotationTypeFilter(Type.class));
            provider.findCandidateComponents(packageName).stream()
                    .map(beanDefinition -> noCatch(() -> (Class) forName(beanDefinition.getBeanClassName())))
                    .forEach(classes::add);
        }
        return classes.toArray(new Class<?>[classes.size()]);
    }
}
