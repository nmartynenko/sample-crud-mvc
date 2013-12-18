package com.aimprosoft.glossary.test;

import com.aimprosoft.glossary.common.persistence.GlossaryPersistence;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(
                name = "base",
                locations = {
                "classpath:spring/spring-common.xml",
                //ignore security
                "classpath:spring/spring-db.xml"
        }),
        @ContextConfiguration(
                name = "servlet",
                locations = "classpath:servlet/glossary-servlet.xml"
        )
})
@FixMethodOrder(MethodSorters.DEFAULT)
public abstract class BaseTest {
    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    protected GlossaryPersistence glossaryPersistence;

    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(this.wac).build();
    }
}
