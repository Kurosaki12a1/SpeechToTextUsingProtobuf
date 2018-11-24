package com.bku.speechtotext.data.database;

public class SettingsLanguage {

    public static final String TABLE_NAME = "settings_language";

    public static final String DEFAULT_LANGUAGE = "Language";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ( " + DEFAULT_LANGUAGE + " TEXT PRIMARY KEY " + " );";


    private String language;

    public SettingsLanguage(String language) {
        this.language = language;
    }


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
