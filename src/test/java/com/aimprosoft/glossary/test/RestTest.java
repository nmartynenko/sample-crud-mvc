package com.aimprosoft.glossary.test;

import com.aimprosoft.glossary.common.model.impl.Glossary;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
@FixMethodOrder(MethodSorters.JVM)
public class RestTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(this.wac).build();
    }


    @Test
    public void getGlossariesList() throws Exception {
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
                .andExpect(jsonPath("$.numberOfElements", is(3)))
                //and total size should be also 3
                .andExpect(jsonPath("$.totalElements", is(3)));
    }

    @Test
    public void getGlossariesListWithPagination() throws Exception {
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
                .andExpect(jsonPath("$.numberOfElements", is(1)))
                //total number of elements should be 3
                .andExpect(jsonPath("$.totalElements", is(3)));
    }

    @Test
    public void getGlossariesListWithWrongPagination() throws Exception {
        mockMvc
                //call glossaries list with incorrect parameters
                .perform(get("/glossaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("startRow", "100500")
                        .param("pageSize", "2")
                )
                //expect result is valid
                .andExpect(status().isOk())
                //expect JSON output
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //there should be content
                .andExpect(jsonPath("$.content").isArray())
                //there should be no objects
                .andExpect(jsonPath("$.content", empty()))
                //number of elements should be also 0
                .andExpect(jsonPath("$.numberOfElements", is(0)))
                //but total number of elements should be 3
                .andExpect(jsonPath("$.totalElements", is(3)));
    }

    @Test
    public void getExistingGlossary() throws Exception {
        mockMvc
                //call glossaries list with incorrect parameters
                .perform(get("/glossaries/1").contentType(MediaType.APPLICATION_JSON))
                //expect result is valid
                .andExpect(status().isOk())
                //expect JSON output
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //and it has contain ID as 1
                .andExpect(jsonPath("$.id", is(1)));

    }

    @Test
    public void getNonExistingGlossary() throws Exception {
        mockMvc
                //call glossaries list with incorrect parameters
                .perform(get("/glossaries/100").contentType(MediaType.APPLICATION_JSON))
                //expect result is invalid
                .andExpect(status().isBadRequest())
                        //and message should contain ID of wrong value
                .andExpect(content().string(containsString("100")));
    }

    @Test
    public void addValidGlossary() throws Exception {
        Glossary glossary = new Glossary();
        glossary.setName("Test glossary");
        glossary.setDescription("Test glossary's description");

        mockMvc
                //try to add a new glossary
                .perform(put("/glossaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(glossary))
                )
                //expect result is valid
                .andExpect(status().isOk())
                //and content is empty
                .andExpect(content().string(""));
    }

    @Test
    public void addGlossaryWithPredefinedId() throws Exception {
        Glossary glossary = new Glossary();
        glossary.setId(2L);
        glossary.setName("Test glossary");
        glossary.setDescription("Test glossary's description");

        mockMvc
                //try to add a new glossary
                .perform(put("/glossaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(glossary))
                )
                //expect result is valid
                .andExpect(status().isOk())
                //and content is empty
                .andExpect(content().string(""));
    }

    @Test
    public void addInvalidGlossary() throws Exception {
        Glossary glossary = new Glossary();
        glossary.setName(null);//incorrect value
        glossary.setDescription("Test glossary's description");

        mockMvc
                //try to add a new glossary, which contains invalid value
                .perform(put("/glossaries")
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
    public void updateValidGlossary() throws Exception {
        Glossary glossary = new Glossary();
        glossary.setId(2L);
        glossary.setName("Test glossary");
        glossary.setDescription("Test glossary's description");

        mockMvc
                //try to update existing glossary
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
    public void updateInvalidGlossary() throws Exception {
        Glossary glossary = new Glossary();
        glossary.setId(2L);
        glossary.setName("Test glossary");
        glossary.setDescription(null);//incorrect value

        mockMvc
                //try to update existing glossary, which contains invalid value
                .perform(post("/glossaries")
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
    public void updateGlossaryWithNonExistingId() throws Exception {
        Glossary glossary = new Glossary();
        glossary.setId(100L);
        glossary.setName("Test glossary");
        glossary.setDescription("Test glossary description");

        mockMvc
                //try to update existing glossary, which contains non-existing ID
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
    public void updateGlossaryWithNullId() throws Exception {
        Glossary glossary = new Glossary();
        glossary.setId(null);
        glossary.setName("Test glossary");
        glossary.setDescription("Test glossary description");

        mockMvc
                //try to update existing glossary, which contains null ID value
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
    public void removeExistingGlossary() throws Exception {
        mockMvc
                //call glossaries with correct glossary ID
                .perform(post("/glossaries")
                        .contentType(MediaType.ALL)
                        .param("glossaryId", "1")
                )
                //everything should be OK
                .andExpect(status().isOk());
    }

    @Test
    public void removeNonExistingGlossary() throws Exception {
        mockMvc
                //call glossaries with incorrect glossary ID
                .perform(post("/glossaries")
                        .contentType(MediaType.ALL)
                        .param("glossaryId", "100")
                )
                //it should return error status
                .andExpect(status().isBadRequest())
                //and message should contain ID of wrong value
                .andExpect(content().string(containsString("100")));

    }

}
