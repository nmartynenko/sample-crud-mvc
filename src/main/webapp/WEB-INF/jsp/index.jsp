<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title><spring:message code="sample.message.index.title"/></title>

    <link type="text/css" href="/html/css/jquery-ui.css" rel="stylesheet"/>
    <link type="text/css" href="/html/css/jquery.dataTables.css" rel="stylesheet"/>
    <link type="text/css" href="/html/css/glossary.css" rel="stylesheet"/>

    <%--Fallback for older browsers--%>
    <script type="text/javascript" src="/html/js/json2.js"></script>

    <script type="text/javascript" src="/html/js/globalize.js"></script>
    <script type="text/javascript" src="/html/js/jquery.min.js"></script>
    <script type="text/javascript" src="/html/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="/html/js/jquery.dataTables.js"></script>
    <script type="text/javascript" src="/html/js/jquery.validate.js"></script>
    <script type="text/javascript" src="/html/js/handlebars.js"></script>
    <script type="text/javascript" src="/html/js/main.js"></script>

    <%--Template for CRUD links--%>
    <script type="text/x-handlebars-template" id="editLink">
        <a href='#' class="edit" data-id='{{data}}'><spring:message code="sample.message.edit"/></a>
        <a href='#' class="remove" data-id='{{data}}'><spring:message code="sample.message.delete"/></a>
    </script>

    <%--Template for edit form--%>
    <script type="text/x-handlebars-template" id="editGlossaryForm">

        <form>
            <input type="hidden" name="id" value="{{id}}">
            <div>
                <label for="name"><spring:message code="sample.message.name"/></label>
                <input type="text" id="name" name="name" value="{{name}}" class="required">
            </div>
            <div>
                <label for="name"><spring:message code="sample.message.description"/></label>
                <input type="text" id="description" name="description" value="{{description}}" class="required">
            </div>

            <div>
                <input type="submit" value="<spring:message code="sample.message.save"/>">
            </div>
        </form>

    </script>

    <script type="text/javascript">

        jQuery(document).ready(function($){

            //Actions dockbar for data-table
            var mRenderFn = Handlebars.compile($("#editLink").html());

            //setup locales
            Globalize.addCultureInfo( "<c:out value="${locale}" default="en"/>", {
                messages: {
                    "sample.js.message.confirm.deleting": "<spring:message code="sample.js.message.confirm.deleting"/>",
                    "sample.js.error.delete": "<spring:message code="sample.js.error.delete"/>",
                    "sample.js.error.retrieve": "<spring:message code="sample.js.error.retrieve"/>",
                    "sample.js.error.update": "<spring:message code="sample.js.error.update"/>"
                }
            });

            //create instance of page controller
            var controller = new GlossaryController({
                locale : "<c:out value="${locale}" default="en"/>",
                containerId : "content",
                popupId : "dialog",
                templateId : "editGlossaryForm",
                tableOpts : {
                    sAjaxSource : "/glossaries",
                    sAjaxDataProp : "content",
                    bProcessing : true,
                    bServerSide : true,
                    bFilter : false,
                    iDisplayStart : 0,
                    iDisplayLength : ${props.defaulPaginatorPageSize},
                    sPaginationType : "full_numbers",
                    aoColumns : [
                        { mData: "id" ,bSortable: false},
                        { mData: "name", bSortable: false },
                        { mData: "description", bSortable: false }
                        <security:authorize access="hasRole('ADMIN')">
                        //edit links (only for admins)
                        ,{
                            mData: "id",
                            bSortable: false,
                            mRender: function(data){
                                return mRenderFn({
                                    data : data
                                });
                            }
                        }
                        </security:authorize>
                    ],
                    aLengthMenu: [2, 3, 5]
                }
            });
        });

    </script>

</head>
<body>
    <div id="header">
        <div>
            <span class="userGreeting">
                <spring:message code="sample.message.welcome"/>&nbsp;<security:authentication property="principal.user.name"/>
            </span>
            <a href="/logout"><spring:message code="sample.message.logout"/></a>
        </div>
        <div>
            <span><spring:message code="sample.message.change.locale"/></span>
            <a href="?lang=en"><spring:message code="sample.message.locale.en"/></a>
            <a href="?lang=ru"><spring:message code="sample.message.locale.ru"/></a>
        </div>
    </div>

    <br/>

    <div id="content">

        <div id="dialog"></div>

        <table id="jqTable">
            <thead>
                <tr>
                    <th><spring:message code="sample.message.id"/></th>
                    <th><spring:message code="sample.message.name"/></th>
                    <th><spring:message code="sample.message.description"/></th>
                    <security:authorize access="hasRole('ADMIN')">
                    <th><spring:message code="sample.message.actions"/></th>
                    </security:authorize>
                </tr>
            </thead>
            <tbody></tbody>
        </table>

        <security:authorize access="hasRole('ADMIN')">
        <div>
            <input type="button" value="<spring:message code="sample.message.add"/>" class="edit" data-id="0">
        </div>
        </security:authorize>

    </div>

</body>
</html>