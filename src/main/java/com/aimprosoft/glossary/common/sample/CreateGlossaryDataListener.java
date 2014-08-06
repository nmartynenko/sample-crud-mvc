package com.aimprosoft.glossary.common.sample;

import com.aimprosoft.glossary.common.model.impl.Glossary;
import com.aimprosoft.glossary.common.service.GlossaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CreateGlossaryDataListener implements InitializingBean{

    private static final String[] TITLES =
            ("Lorem ipsum dolor sit amet, consectetur adipisicing elit," +
                    "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.").split(",");

    private static final String[] DESCRIPTIONS = ("Sed ut perspiciatis unde omnis iste natus" +
            " error sit voluptatemaccusantium doloremque laudantium, totam rem aperiam," +
            " eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae" +
            " vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas" +
            " sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores" +
            " eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est," +
            " qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit," +
            " sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam" +
            " aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem" +
            " ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? " +
            "Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil" +
            " molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?").split("\\.");

    private Logger _logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GlossaryService glossaryService;

    @Override
    public void afterPropertiesSet() throws Exception {
        Random random = new Random();

        int i = TITLES.length;
        while (i-- > 0){
            int descIndex = random.nextInt(DESCRIPTIONS.length);

            Glossary glossary = new Glossary();
            glossary.setName(TITLES[i]);
            glossary.setDescription(DESCRIPTIONS[descIndex]);

            glossaryService.add(glossary);
        }


    }
}
