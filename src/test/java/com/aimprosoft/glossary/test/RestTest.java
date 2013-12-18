package com.aimprosoft.glossary.test;

import com.aimprosoft.glossary.common.model.impl.Glossary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy(
        @ContextConfiguration(
                name = "base",
                //include sample data
                locations = "classpath:spring/spring-sample-data.xml",
                inheritLocations = true
        )
)
public class RestTest extends BaseTest{

    @Test
    public void getGlossariesList() throws Exception {
        int glossariesCount = Long.valueOf(glossaryPersistence.count()).intValue();

        mockMvc
                //call glossaries list without parameters
                .perform(get("/glossaries").contentType(MediaType.APPLICATION_JSON))
                //expect result is valid
                .andExpect(status().isOk())
                //expect JSON output
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //there should be content
                .andExpect(jsonPath("$.content").isArray())
                //there should be all objects
                .andExpect(jsonPath("$.content").value(hasSize(glossariesCount)))
                //number of elements should be the same value
                .andExpect(jsonPath("$.numberOfElements", is(glossariesCount)))
                //and total size should be also the same
                .andExpect(jsonPath("$.totalElements", is(glossariesCount)));
    }

    @Test
    public void getGlossariesListWithPagination() throws Exception {
        int glossariesCount = Long.valueOf(glossaryPersistence.count()).intValue();

        int pageSize = 2;

        //since start row equals to 0, then
        //expected size is equal to page size
        //if all number of elements is greater than page size
        int expectedSize = pageSize < glossariesCount ? pageSize : glossariesCount;

        mockMvc
                //call glossaries list with parameters
                .perform(get("/glossaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("startRow", "0")
                        .param("pageSize", String.valueOf(pageSize))
                )
                //expect result is valid
                .andExpect(status().isOk())
                //expect JSON output
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //there should be content
                .andExpect(jsonPath("$.content").isArray())
                //there should be as expected size
                .andExpect(jsonPath("$.content", hasSize(expectedSize)))
                //number of elements should be as expected size
                .andExpect(jsonPath("$.numberOfElements", is(expectedSize)))
                //total number of elements should be all number of elements
                .andExpect(jsonPath("$.totalElements", is(glossariesCount)));
    }

    @Test
    public void getGlossariesListWithWrongPagination() throws Exception {
        int glossariesCount = Long.valueOf(glossaryPersistence.count()).intValue();

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
                //but total number of elements should be all elements
                .andExpect(jsonPath("$.totalElements", is(glossariesCount)));
    }

    @Test
    public void getExistingGlossary() throws Exception {
        long id = 2L;

        //this glossary is present in DB
        assertThat(glossaryPersistence.exists(id), is(true));

        mockMvc
                //call glossaries list with incorrect parameters
                .perform(get("/glossaries/" + id).contentType(MediaType.APPLICATION_JSON))
                //expect result is valid
                .andExpect(status().isOk())
                //expect JSON output
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //and it has contain ID as 2
                .andExpect(jsonPath("$.id", is((int)id)));

    }

    @Test
    public void getNonExistingGlossary() throws Exception {
        long id = 100L;

        //this glossary is NOT present in DB
        assertThat(glossaryPersistence.exists(id), is(false));

        mockMvc
                //call glossaries list with incorrect parameters
                .perform(get("/glossaries/" + id).contentType(MediaType.APPLICATION_JSON))
                //expect result is invalid
                .andExpect(status().isBadRequest())
                //and message should contain ID of wrong value
                .andExpect(content().string(containsString(String.valueOf(id))));
    }

    @Test
    public void addValidGlossary() throws Exception {
        long startGlossariesCount = glossaryPersistence.count();

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

        long endGlossariesCount = glossaryPersistence.count();

        //data integrity check
        //number of glossaries has increased by one
        assertThat(startGlossariesCount, equalTo(endGlossariesCount - 1));
    }

    @Test
    public void addGlossaryWithNonExistingId() throws Exception {
        long startGlossariesCount = glossaryPersistence.count();

        long id = 100500L;

        //this glossary is NOT present in DB
        assertThat(glossaryPersistence.exists(id), is(false));

        Glossary glossary = new Glossary();
        glossary.setId(id);
        glossary.setName("Try to add not-existing glossary");
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

        long endGlossariesCount = glossaryPersistence.count();

        //data integrity check
        //number of glossaries has increased by one
        assertThat(startGlossariesCount, equalTo(endGlossariesCount - 1));

        //glossary with defined ID is NOT present in DB
        //as IDs are generated by DB
        assertThat(glossaryPersistence.exists(glossary.getId()), is(false));
    }

    @Test
    public void addInvalidGlossary() throws Exception {
        long startGlossariesCount = glossaryPersistence.count();

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

        long endGlossariesCount = glossaryPersistence.count();

        //data integrity check
        //number of glossaries remains the same
        assertThat(startGlossariesCount, equalTo(endGlossariesCount));
    }

    @Test
    public void updateValidGlossary() throws Exception {
        long id = 2L;

        Glossary glossary = new Glossary();
        glossary.setId(id);
        glossary.setName("Test valid glossary");
        glossary.setDescription("Test valid glossary's description");

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

        Glossary dbGlossary = glossaryPersistence.findOne(id);

        //glossary taken from DB is the same as created one
        assertThat(dbGlossary, is(glossary));
    }

    @Test
    public void updateInvalidGlossary() throws Exception {
        long startGlossariesCount = glossaryPersistence.count();

        long id = 2L;

        Glossary glossary = new Glossary();
        glossary.setId(id);
        glossary.setName("Test invalid glossary");
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

        long endGlossariesCount = glossaryPersistence.count();

        //data integrity check
        //number of glossaries remains the same
        assertThat(startGlossariesCount, equalTo(endGlossariesCount));

        Glossary dbGlossary = glossaryPersistence.findOne(id);

        //and glossary taken from DB is the same as created one
        assertThat(dbGlossary, is(not(glossary)));
    }
    @Test
    public void updateGlossaryWithNonExistingId() throws Exception {
        long startGlossariesCount = glossaryPersistence.count();

        long id = 100500L;

        //this glossary is NOT present in DB
        assertThat(glossaryPersistence.exists(id), is(false));

        Glossary glossary = new Glossary();
        glossary.setId(id);
        glossary.setName("Try to update glossary with non-existing ID");
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

        long endGlossariesCount = glossaryPersistence.count();

        //data integrity check
        //number of glossaries has increased by one
        assertThat(startGlossariesCount, equalTo(endGlossariesCount - 1));

        //glossary with defined ID is NOT present in DB
        //as IDs are generated by DB
        assertThat(glossaryPersistence.exists(id), is(false));
    }

    @Test
    public void updateGlossaryWithNullId() throws Exception {
        long startGlossariesCount = glossaryPersistence.count();

        Glossary glossary = new Glossary();
        glossary.setId(null);
        glossary.setName("Try to update glossary with NULL id");
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

        long endGlossariesCount = glossaryPersistence.count();

        //data integrity check
        //number of glossaries has increased by one
        assertThat(startGlossariesCount, equalTo(endGlossariesCount - 1));
    }

    @Test
    public void removeExistingGlossary() throws Exception {
        long startGlossariesCount = glossaryPersistence.count();

        long id = 1L;

        //this glossary is present in DB
        assertThat(glossaryPersistence.exists(id), is(true));

        mockMvc
                //call glossaries with correct glossary ID
                .perform(delete("/glossaries/" + id)
                        .contentType(MediaType.ALL)
                )
                //everything should be OK
                .andExpect(status().isOk());

        long endGlossariesCount = glossaryPersistence.count();

        //data integrity check
        //number of glossaries has decreased by one
        assertThat(startGlossariesCount, equalTo(endGlossariesCount + 1));

        //this glossary is no longer present in DB
        assertThat(glossaryPersistence.exists(id), is(false));
    }

    @Test
    public void removeNonExistingGlossary() throws Exception {
        long startGlossariesCount = glossaryPersistence.count();

        long id = 100L;

        //this glossary is NOT present in DB
        assertThat(glossaryPersistence.exists(id), is(false));

        mockMvc
                //call glossaries with incorrect glossary ID
                .perform(delete("/glossaries/" + id)
                        .contentType(MediaType.ALL)
                )
                //it should return error status
                .andExpect(status().isBadRequest())
                //and message should contain ID of wrong value
                .andExpect(content().string(containsString(String.valueOf(id))));

        long endGlossariesCount = glossaryPersistence.count();

        //data integrity check
        //number of glossaries remains the same
        assertThat(startGlossariesCount, equalTo(endGlossariesCount));
    }

}
