package com.aimprosoft.glossary.test;

import com.aimprosoft.glossary.common.config.ApplicationProps;
import com.aimprosoft.glossary.common.model.impl.Glossary;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/spring-common.xml",//ignore security
                                    "classpath:spring/spring-db.xml",
                                    "classpath:servlet/glossary-servlet.xml"})
public class RestTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ApplicationProps props;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(this.wac).build();
    }


    @Test
    public void testGlossariesList() throws Exception {
        mockMvc
                //call glossaries list without parameters
                .perform(get("/glossaries").contentType(MediaType.APPLICATION_JSON))
                //expect result is valid
                .andExpect(status().isOk())
                //expect JSON output
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //there should be content
                .andExpect(jsonPath("$.content").isArray())
                //there should be 3 objects
                .andExpect(jsonPath("$.content").value(hasSize(3)))
                //number of elements should be also 3
                .andExpect(jsonPath("$.numberOfElements").value(3))
                //and total size should be also 3
                .andExpect(jsonPath("$.totalElements").value(3));
    }

    @Test
    public void testGlossariesListWithPagination() throws Exception {
        mockMvc
                //call glossaries list with parameters
                .perform(get("/glossaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("startRow", "2")
                        .param("pageSize", "2")
                )
                //expect result is valid
                .andExpect(status().isOk())
                //expect JSON output
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //there should be content
                .andExpect(jsonPath("$.content").isArray())
                //there should be 1 objects
                .andExpect(jsonPath("$.content", hasSize(1)))
                //number of elements should be also 1
                .andExpect(jsonPath("$.numberOfElements").value(1))
                //total number of elements should be 3
                .andExpect(jsonPath("$.totalElements").value(3));
    }

    @Test
    public void testAddValidGlossary() throws Exception {
        Glossary glossary = new Glossary();
        glossary.setName("Test glossary");
        glossary.setDescription("Test glossary's description");

        mockMvc
                //call glossaries list without parameters
                .perform(post("/glossaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(glossary))
                )
                //expect result is valid
                .andExpect(status().isOk())
                //and content is empty
                .andExpect(content().string(""));
    }

    @Test
    public void testAddInvalidGlossary() throws Exception {
        Glossary glossary = new Glossary();
        glossary.setName(null);
        glossary.setDescription("Test glossary's description");

        mockMvc
                //call glossaries list without parameters
                .perform(post("/glossaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(glossary))
                )
                //expect result is invalid
                .andExpect(status().isBadRequest())
                //content type is JSON
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                //error should be about name
                .andExpect(jsonPath("$.name").value(not(nullValue())))
                //but not description
                .andExpect(jsonPath("$.description").doesNotExist());
    }

    @Test
    public void testUpdateValidGlossary() throws Exception {
        Glossary glossary = new Glossary();
        glossary.setId(2L);
        glossary.setName("Test glossary");
        glossary.setDescription("Test glossary's description");

        mockMvc
                //call glossaries list without parameters
                .perform(post("/glossaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(glossary))
                )
                //expect result is valid
                .andExpect(status().isOk())
                //and content is empty
                .andExpect(content().string(""));
    }

    @Test
    public void testUpdateInvalidGlossary() throws Exception {
        Glossary glossary = new Glossary();
        glossary.setId(2L);
        glossary.setName("Test glossary");
        glossary.setDescription(null);

        mockMvc
                //call glossaries list without parameters
                .perform(put("/glossaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(glossary))
                )
                //expect result is invalid
                .andExpect(status().isBadRequest())
                //content type is JSON
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                //error should be about description
                .andExpect(jsonPath("$.description").value(not(nullValue())))
                //but not name
                .andExpect(jsonPath("$.name").doesNotExist());
    }

    @Test
    public void testRemoveInvalidGlossary() throws Exception {
        mockMvc
                //call glossaries with incorrect glossary ID
                .perform(post("/glossaries")
                        .contentType(MediaType.ALL)
                        .param("glossaryId", "100")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testRemoveValidGlossary() throws Exception {
        mockMvc
                //call glossaries with correct glossary ID
                .perform(post("/glossaries")
                        .contentType(MediaType.ALL)
                        .param("glossaryId", "1")
                )
                //everything should be OK
                .andExpect(status().isOk());
    }

}
