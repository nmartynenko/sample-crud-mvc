/*
 * JavaScript for Glossary
 *
 * @depends on: jQuery 1.7+
 * @depends on: jQuery Validation plugin 1.9+
 * @depends on: Handlebars 1.0+
 * @depends on: Globalize
 *
 */

(function(window, $, undefined){

    //add new functionality to jQuery
    //borrowed from http://stackoverflow.com/questions/1184624/convert-form-data-to-js-object-with-jquery
    $.extend($.fn, {
        serializeObject : function(){
            var o = {};
            var a = this.serializeArray();
            $.each(a, function() {
                if (o[this.name] !== undefined) {
                    if (!o[this.name].push) {
                        o[this.name] = [o[this.name]];
                    }
                    o[this.name].push(this.value || '');
                } else {
                    o[this.name] = this.value || '';
                }
            });
            return o;
        }
    });

    var GlossaryController = function(settings){
        this._init(settings);
    };

    $.extend(true, GlossaryController, {

        defaults : {
            locale : "en",
            containerId : "content",
            popupId : "dialog",
            templateId : "",
            tableOpts : {
                sAjaxSource : "",
                //adjust names in the way the server needs it
                fnServerParams : function(aoData){
                    var startRow = 0;
                    var pageSize = 0;

                    for (var i = 0; i < aoData.length; i++){
                        var elem = aoData[i];
                        var elemName = elem["name"];
                        if (elemName === "iDisplayStart"){
                            startRow = elem.value;
                        } else if (elemName === "iDisplayLength"){
                            pageSize = elem.value;
                        }
                    }

                    aoData.push({
                        name : "startRow",
                        value : startRow
                    });

                    aoData.push({
                        name : "pageSize",
                        value : pageSize
                    });
                },
                //re-write AJAX-processing a bit
                fnServerData: function ( sUrl, aoData, fnCallback, oSettings ) {
                    oSettings.jqXHR = $.ajax( {
                        "url":  sUrl,
                        "data": aoData,
                        "success": function (json) {
                            if ( json.sError ) {
                                oSettings.oApi._fnLog( oSettings, 0, json.sError );
                            }

                            //add needed parameters to resulting JSON
                            json["iTotalRecords"] = json["totalElements"];
                            json["iTotalDisplayRecords"] = json["totalElements"];

                            $(oSettings.oInstance).trigger('xhr', [oSettings, json]);
                            fnCallback( json );
                        },
                        "dataType": "json",
                        "cache": false,
                        "type": oSettings.sServerMethod,
                        "error": function (xhr, error, thrown) {
                            if ( error == "parsererror" ) {
                                oSettings.oApi._fnLog( oSettings, 0, "DataTables warning: JSON data from "+
                                    "server could not be parsed. This is caused by a JSON formatting error." );
                            }
                        }
                    } );
                }
            },
            //empty object, by defalt validation is based on CSS classes
            validationOpts : {

            }
        }

    });

    $.extend(true, GlossaryController.prototype, {

        _init : function(settings){
            var controller = this;

            //apply settings
            $.extend(true, this, GlossaryController.defaults, settings || {});

            //container holder
            controller.container = $("#" + controller.containerId);

            //table holder
            controller.table = controller.container.find("table").first();

            //dialog holder
            controller.dialog = $("#" + controller.popupId);

            //compiled EDIT form template
            controller.template = Handlebars.compile($("#" + controller.templateId).html());

            //init GUI
            controller._initGUI();

            //bind events
            controller._bindEvents();
        },

        _initGUI : function(){
            var controller = this;

            controller._dataTable = controller.table.dataTable(controller.tableOpts);
        },

        //bind events
        _bindEvents : function(){
            var controller = this;

            controller.container
                //edit link/add button
                .on("click", ".edit", function(e){
                    e.preventDefault();

                    controller._editItem($(e.target).data("id"));
                })
                //remove link
                .on("click", "a.remove", function(e){
                    e.preventDefault();

                    if (window.confirm(Globalize.localize("sample.js.message.confirm.deleting", controller.locale))){
                        controller._removeItem($(e.target).data("id"));
                    }
                });

        },

        _bindValidation: function(e){
            var controller = this;

            //find form in target popup, add applied properties, but submit handler always refers to _saveItem
            $(e.target)
                .find("form")
                .first()
                .validate($.extend(controller.validationOpts, {
                    submitHandler :$.proxy(controller._saveItem, controller)
                }));
        },

        _editItem : function(id){
            var controller = this;

            //common Deferred object
            var $processing;

            if (id){
                $processing = $.ajax({
                    url: controller.tableOpts.sAjaxSource + "/" + id,
                    type: "GET",
                    dataType: "json"
                });
            } else {
                //if add, then mark processing as resolved and pass empty object
                $processing = $.Deferred().resolve({});
            }

            $processing
                .done(function(data){
                    controller.dialog
                        .html(controller.template(data))
                        .dialog({
                            //bind validation on every dialog's open
                            open: $.proxy(controller._bindValidation, controller)
                        });
                })
                .fail(function(){
                    alert(Globalize.localize("sample.js.error.retrieve", controller.locale));
                });

        },

        _saveItem: function(form){
            var controller = this;

            var data = $(form).serializeObject();

            $.ajax({
                    url : controller.tableOpts.sAjaxSource,
                    //add/edit checking (POST - update, PUT - add
                    type: data.id ? "POST" : "PUT",
                    contentType: "application/json; charset=UTF-8",
                    data : JSON.stringify(data)
                })
                .done(function(){
                    controller._refreshTable();
                })
                .fail(function(){
                    alert(Globalize.localize("sample.js.error.update", controller.locale));
                });

        },

        _removeItem: function(id){
            var controller = this;

            $.ajax({
                    url: controller.tableOpts.sAjaxSource + "/" + id,
                    type: "DELETE"
                })
                .done(function(){
                    controller._dataTable.fnDraw(false);
                })
                .fail(function(){
                    alert(Globalize.localize("sample.js.error.delete", controller.locale));
                });
        },

        _refreshTable: function(){
            var controller = this;

            //close popup
            controller.dialog.dialog("close");

            //refresh datatable
            controller._dataTable.fnDraw(false)
        }
});

    //expose to global scope
    window.GlossaryController = GlossaryController;

})(this, jQuery);