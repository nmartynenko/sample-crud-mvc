package com.aimprosoft.glossary.test;

import com.aimprosoft.glossary.common.model.UserRole;
import com.aimprosoft.glossary.common.model.impl.Glossary;
import com.aimprosoft.glossary.util.InvokeAs;
import com.aimprosoft.glossary.util.InvokeAsRunListener;
import com.aimprosoft.glossary.util.RunListenerSpringJUnit4ClassRunner;
import com.aimprosoft.glossary.util.WithRunListener;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(RunListenerSpringJUnit4ClassRunner.class)
@WithRunListener(InvokeAsRunListener.class)
@ContextHierarchy({
        @ContextConfiguration(
                name = "base",
                //include security
                locations = "classpath:spring/spring-security.xml",
                inheritLocations = true
        ),
        @ContextConfiguration(
                name = "servlet",
                //include security
                locations = "classpath:servlet/glossary-servlet-security.xml",
                inheritLocations = true
        )
})
public class SecuredRestTest extends BaseTest {

    @After
    public void clearContext() {
        //clear security context after tests execution
        SecurityContextHolder.clearContext();
    }

    @Test
    public void tryToGetGlossariesAnonymously() throws Exception{
        mockMvc
                //call glossaries list without parameters
                .perform(get("/glossaries").contentType(MediaType.APPLICATION_JSON))
                        //expect result is OK, as there is no @PreAuthorize annotation
                        //it's secured with security:intercept-url mechanism
                .andExpect(status().isOk());
    }

    @Test
    @InvokeAs(UserRole.USER)
    public void tryToAddGlossaryAsUSER() throws Exception{
        long startGlossariesCount = glossaryPersistence.count();

        Glossary glossary = new Glossary();
        glossary.setName("User's glossary");
        glossary.setDescription("I want to add this");

        mockMvc
                //try to add new glossary
                .perform(post("/glossaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(glossary)))
                        //expect result is forbidden
                .andExpect(status().isForbidden());

        long endGlossariesCount = glossaryPersistence.count();

        //data integrity check
        //number of glossaries remains the same
        assertThat(startGlossariesCount, equalTo(endGlossariesCount));
    }

    @Test
    @InvokeAs(UserRole.ADMIN)
    public void tryToAddGlossaryAsADMIN() throws Exception {
        long startGlossariesCount = glossaryPersistence.count();

        Glossary glossary = new Glossary();
        glossary.setName("Admin's glossary");
        glossary.setDescription("I shall add this!");

        mockMvc
                //try to add new glossary
                .perform(post("/glossaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(glossary)))
                        //expect result is valid
                .andExpect(status().isOk());

        long endGlossariesCount = glossaryPersistence.count();

        //data integrity check
        //number of glossaries has increased by one
        assertThat(startGlossariesCount, equalTo(endGlossariesCount - 1));
    }


}
