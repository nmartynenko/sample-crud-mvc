package com.aimprosoft.glossary.common.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class ObjectFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ObjectFactory.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }
}
