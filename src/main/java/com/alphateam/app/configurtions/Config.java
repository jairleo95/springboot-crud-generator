package com.alphateam.app.configurtions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Config {

    private String hostname;
    private String port;
    private String db;
    private String dbName;
    private String username;
    private String password;

    private String outputLocation;
    private String zipFile;

    @JsonProperty("template")
    private TemplateConfig templateConfig;

    public TemplateConfig getTemplateConfig() {
        return templateConfig;
    }

    public void setTemplateConfig(TemplateConfig templateConfig) {
        this.templateConfig = templateConfig;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOutputLocation() {
        return outputLocation;
    }

    public void setOutputLocation(String outputLocation) {
        this.outputLocation = outputLocation;
    }

    public String getZipFile() {
        return zipFile;
    }

    public void setZipFile(String zipFile) {
        this.zipFile = zipFile;
    }

    @Override
    public String toString() {
        return "Config{" +
                "hostname='" + hostname + '\'' +
                ", port='" + port + '\'' +
                ", db='" + db + '\'' +
                ", dbName='" + dbName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", outputLocation='" + outputLocation + '\'' +
                ", zipFile='" + zipFile + '\'' +
                ", templateConfig=" + templateConfig +
                '}';
    }
}
