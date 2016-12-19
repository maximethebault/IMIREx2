package org.tud.imir.ex2.service;

public class ConfigurationHolder {
    private static ConfigurationHolder instance = new ConfigurationHolder();

    public static ConfigurationHolder getInstance() {
        return instance;
    }

    private String indexName;
    private String resourcePath;

    private ConfigurationHolder() {
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }
}
