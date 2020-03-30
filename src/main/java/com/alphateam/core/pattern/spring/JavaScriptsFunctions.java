package com.alphateam.core.pattern.spring;

import com.alphateam.core.template.Template;
import com.alphateam.properties.Global;
import com.alphateam.query.Column;
import com.alphateam.query.Table;
import com.alphateam.utiles.Conversor;

import java.util.List;

/**
 * Created by JairL on 10/5/2019.
 */
public class JavaScriptsFunctions extends Template {

    String formSelects = "";
    String tableColumns = "";

    @Override
    public void buildParameters(Table table, Column column) {
        String columna = column.getName();
        tableColumns +="{\"data\": \""+columna+"\"},\n";
    }

    @Override
    public void foreignKeys(Table fkTable, Column fk) {
        super.foreignKeys(fkTable, fk);
        String tableName = fk.getForeignTable();
        String tableEntity = Conversor.firstCharacterToUpper(tableName);

        String column = Conversor.firstCharacterToUpper(fk.getName());

        formSelects +=   "var select"+column+" = $('.select"+column+"');\n" +
                        "getSelectItems({url:'"+tableEntity+"',select:select"+column+",name:'"+fk.getName()+"'}); \n" ;

        String columna = fk.getName();

        tableColumns +="{\"data\": \""+columna+"\"},\n";

    }

    @Override
    public void buildMethods(Table tnc, List<String> pks) {
        super.buildMethods(tnc, pks);
        String tableName = tnc.getName();
        String tableEntity = Conversor.firstCharacterToUpper(tableName);

        content +="/*form variables*/   \n";
        content +="var form;\n" +
                "var endpoint = '"+tableEntity+"';\n" +
                "var urlCrud = '';\n" +
                "var "+tableEntity+"Records = $('."+tableEntity+"-datatable');\n" +
                "var type ='POST';\n" +
                "var id = '';\n" +
                "var updateMode = false;";

        content +="var modalObject = $('.modal-"+tableEntity+"');\n" +
                "var modalBody = $(\".areaModal\");\n";

        //functions
        content +="function save(){\n" +
                "    console.log('***Enter to save() function***');\n" +
                "    var data = jsonSerialize(form);\n" +
                "    data[\""+tableEntity+"\"] = {};\n" +
                "    data[\""+tableEntity+"\"]['id'] = id;\n" +
                "    console.log('[save] data:');\n" +
                "    console.log(data);\n" +
                "    console.log('[save] updateMode flag:'+updateMode);\n" +
                "    console.log('[save] id:'+id);\n" +
                "\n" +
                "    submitForm({form: form,url: urlCrud, data:data, id:id, type:type, success:function () {\n" +
                "        if (updateMode){\n" +
                "            //showDetails()\n" +
                "            //reloadRegisters()\n" +
                "        }else {\n" +
                "            //resetFormUser();\n" +
                "            //reloadRegisters();\n" +
                "        }\n" +
                "    }});\n" +
                "\n" +
                "}\n";

        content +="function initForm() {\n" +
                "form =  $('.form"+tableEntity+"');\n"
                + formSelects
                +"    console.log('***Enter to initForm() function***');\n" +
                "    urlCrud = endpoint + '/';\n" +
                "       form.bootstrapValidator({\n" +
                "        feedbackIcons: {\n" +
                "            valid: 'glyphicon glyphicon-ok',\n" +
                "            invalid: 'glyphicon glyphicon-remove',\n" +
                "            validating: 'glyphicon glyphicon-refresh'\n" +
                "        },excluded: [':disabled'], \n" +
                "       fields:{} \n" +
                "   }).on('success.form.bv', function (e) {\n" +
                "        /* do submitting with ajax*/\n" +
                "        e.preventDefault();\n" +
                "        save();\n" +
                "    }); \n" +

                "}\n";



        content += "function initTable() {\n" +
                "    var responsiveHelper_dt_basic = undefined;\n" +
                "    var breakpointDefinition = {\n" +
                "        tablet: 1024,\n" +
                "        phone: 480\n" +
                "    };\n" +
                "    "+tableEntity+"Records.DataTable({\n" +
                "        \"ajax\": {\n" +
                "            \"url\": endpoint+'/', \"type\": \"GET\",'dataSrc': ''\n" +
                "        },\n" +
                "        \"columns\": [\n" +
                "            {\n" +
                "                \"orderable\": false,\n" +
                "                \"data\": null,\n" +
                "                \"defaultContent\": \"\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"orderable\": false,\n" +
                "                \"data\": null,\n" +
                "                \"defaultContent\": \"\"\n" +
                "            },\n" +
                tableColumns +
                "        ],\n" +
                "        \"sDom\": \"<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>\" +\n" +
                "        \"t\" +\n" +
                "        \"<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>\",\n" +
                "        \"autoWidth\": true,\n" +
                "        \"preDrawCallback\": function () {\n" +
                "            // Initialize the responsive datatables helper once.\n" +
                "            if (!responsiveHelper_dt_basic) {\n" +
                "                responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('."+tableEntity+"-datatable'), breakpointDefinition);\n" +
                "            }\n" +
                "        },\n" +
                "        \"rowCallback\": function (row, data, index) {\n" +
                "            responsiveHelper_dt_basic.createExpandIcon(row);\n" +
                "            console.log(\":enter to rowCallback\");\n" +
                "            $('td:eq(1)', row).html(index+1);\n" +
                "\n" +
                "            var htmlTD = '';\n" +
                "            htmlTD += '<div class=\"btn-group\">'\n" +
                "                + '<button class=\"btn btn-primary dropdown-toggle bounceIn animated\" data-toggle=\"dropdown\">'\n" +
                "                + '<i class=\"fa fa-gear fa-lg\"></i>'\n" +
                "                + '</button>'\n" +
                "                + ' <ul class=\"dropdown-menu\">';\n" +
                "            htmlTD += '   <li>'\n" +
                "                + '<a href=\"javascript:void(0);\" class=\"btn-update\"><i class=\"fa fa-pencil\"></i> Update</a>'\n" +
                "                + '<a href=\"javascript:void(0);\" class=\"btn-remove\"><i class=\"fa fa-trash\"></i> Delete</a>'\n" +
                "                + '<a href=\"javascript:void(0);\" class=\"btn-password\"><i class=\"fa fa-lock\"></i> Password</a>'\n" +
                "                + '</li>';\n" +
                "            htmlTD += ' </ul>'\n" +
                "                + '</div>';\n" +
                "            $('td:eq(0)', row).html(htmlTD);\n" +
                "        },\n" +
                "        \"drawCallback\": function (oSettings) {\n" +
                "            responsiveHelper_dt_basic.respond();\n" +
                "            $('.btn-update').click(function () {\n" +
                "                id = $(this).data('id');\n" +
                "                showEditForm(id);\n" +
                "            });\n" +
                "            $('.btn-remove').click(function () {\n" +
                "                id = $(this).data('id');\n" +
                "\n" +
                "                $('.dialog_simple').dialog('open');\n" +
                "                return false;\n" +
                "            });\n" +
                "            $('."+tableEntity+"-datatable tbody tr td').dblclick(function () {\n" +
                "                id = $(this).parent().find('.btn-update').data('id');\n" +
                "                showDetails();\n" +
                "            });\n" +
                "            $('.btn-password').click(function () {\n" +
                "                id = $(this).data('id');\n" +
                "                initModal('Change password');\n" +
                "                modalBody.load('change-pwd', function () {\n" +
                "                    initFormPWDChange();\n" +
                "                });\n" +
                "            });\n" +
                "        }\n" +
                "    });\n" +
                "}";


        content +="var pagefunction = function () {\n" +
                "    pageSetUp();\n" +
                " initForm();\n"+
                " initTable();\n"+
                "\n" +
                "};\n";

        content +="loadScript('../js/plugin/datatables/jquery.dataTables.min.js',function () {\n" +
                "    loadScript('../js/plugin/datatables/dataTables.colVis.min.js',function () {\n" +
                "        loadScript('../js/plugin/datatables/dataTables.tableTools.min.js',function () {\n" +
                "            loadScript('../js/plugin/datatables/dataTables.bootstrap.min.js',function () {\n" +
                "                loadScript('../js/plugin/datatable-responsive/datatables.responsive.min.js',function () {\n" +
                "                    loadScript(\"../js/jsForm/jsForm.js\", pagefunction);\n" +
                "                });\n" +
                "            });\n" +
                "        });\n" +
                "    }); \n" +
                "});";
        generateProject(Global.JS_SCRIPT_LOCATION  + "\\"+tableEntity +"\\",  tableEntity+".js");

    }

    @Override
    public void resetValues() {
        super.resetValues();
        formSelects="";
        tableColumns="";
    }
}
