package com.alphateam.app.core.pattern.spring;

import com.alphateam.app.core.builder.Builder;
import com.alphateam.properties.Global;
import com.alphateam.app.bean.Table;
import com.alphateam.util.Conversor;
import com.alphateam.util.FileBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PageBaseView extends Builder {
    private final Logger log = LogManager.getLogger(getClass().getName());

    String list = "";
    String projectID = Global.PACKAGE_NAME;

    @Override
    public Table processTable(Table table) {
        super.processTable(table);

        String tableName = table.getName();
        String tableEntity = Conversor.firstCharacterToUpper(tableName);

        log.info("//TABLA :" + table.getName());

        list += " <li>" +"<a href=\"form"+tableEntity+"\"><i class=\"fa fa-lg fa-fw fa-form\"></i> <span class=\"menu-item-parent\">"+tableEntity+"</span></a>" +
                "<li>\n";

        return table;
    }

    @Override
    public void build() {
        super.build();

        String view ="";
        view += FileBuilder.readAllBytes("D:\\Software Development\\crud-generator-project\\template\\pageBase.html");

        System.out.println("list:"+list);
        content = view.replace("{navlist}", list);

        generateProject(Global.VIEW_LOCATION  + "\\security\\",  "pageBase.html");
    }

    @Override
    public void resetValues() {
        super.resetValues();
        content = "";
    }
}
