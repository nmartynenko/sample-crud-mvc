package com.aimprosoft.glossary.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * This class provides application properties on JSP pages
 */
@Service("props")
public class ApplicationProps {

    @Value("${paginator.default.pageSize}")
    private int defaultPaginatorPageSize;

    @Value("${paginator.default.maxPage}")
    private int defaultPaginatorMaxPage;

    @Value("${paginator.default.fastStep}")
    private int defaultPaginatorFastStep;

    public int getDefaultPaginatorPageSize() {
        return defaultPaginatorPageSize;
    }

    public int getDefaultPaginatorMaxPage() {
        return defaultPaginatorMaxPage;
    }

    public int getDefaultPaginatorFastStep() {
        return defaultPaginatorFastStep;
    }

}
