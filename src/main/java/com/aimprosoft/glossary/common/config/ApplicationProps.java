package com.aimprosoft.glossary.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * This class provides application properties on JSP pages
 */
@Service("props")
public class ApplicationProps {

    @Value("${paginator.default.pageSize}")
    private int defaulPaginatorPageSize;

    @Value("${paginator.default.maxPage}")
    private int defaulPaginatorMaxPage;

    @Value("${paginator.default.fastStep}")
    private int defaulPaginatorFastStep;

    public int getDefaulPaginatorPageSize() {
        return defaulPaginatorPageSize;
    }

    public int getDefaulPaginatorMaxPage() {
        return defaulPaginatorMaxPage;
    }

    public int getDefaulPaginatorFastStep() {
        return defaulPaginatorFastStep;
    }

}
