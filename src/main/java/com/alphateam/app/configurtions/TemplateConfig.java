package com.alphateam.app.configurtions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TemplateConfig {

    @JsonProperty("html")
    private Html html;

    public Html getHtml() {
        return html;
    }

    public void setHtml(Html html) {
        this.html = html;
    }

    @Override
    public String toString() {
        return "TemplateConfig{" +
                "html=" + html +
                '}';
    }

    public static class Html{
        private String parameter;
        private String form;
        private String datatable;

        public String getParameter() {
            return parameter;
        }

        public void setParameter(String parameter) {
            this.parameter = parameter;
        }

        public String getForm() {
            return form;
        }

        public void setForm(String form) {
            this.form = form;
        }

        public String getDatatable() {
            return datatable;
        }

        public void setDatatable(String datatable) {
            this.datatable = datatable;
        }

        @Override
        public String toString() {
            return "html{" +
                    "parameter='" + parameter + '\'' +
                    ", form='" + form + '\'' +
                    ", datatable='" + datatable + '\'' +
                    '}';
        }
    }



}

