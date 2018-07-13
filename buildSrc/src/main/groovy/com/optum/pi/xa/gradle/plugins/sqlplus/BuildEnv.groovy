package com.optum.pi.xa.gradle.plugins.sqlplus

import org.gradle.api.Project

import groovy.transform.CompileStatic

@CompileStatic
class BuildEnv  {
    
    static String FILE_SEPARATOR = System.getProperty("file.separator")

    static final String NAME = "buildEnv"
    
    static String LOCALDB_DIRNAME = "local-db" + FILE_SEPARATOR;
    static String LOCALDB_SQL_DIRNAME = "sql" + FILE_SEPARATOR;
    static String LOCALDB_LOG_DIRNAME = "logs" + FILE_SEPARATOR;
    static String LOCALDB_CONF_DIRNAME = "conf" + FILE_SEPARATOR;
    
    static String BUILDTOOLS_DIRNAME = "build-tools" + FILE_SEPARATOR;
    static String DEVTOOLS_DIRNAME = "dev-tools" + FILE_SEPARATOR;
    
    static String APP_DIRNAME = "app" + FILE_SEPARATOR;

    static String CLEAN_SCHEMA_DIRNAME  = "01-clean-db"
    static String CREATE_SCHEMA_DIRNAME = "02-schema-setup"
    static String SETUP_DATA_DIRNAME    = "03-data-setup"
    
    static String SQLPLUS_WRAPPER_SQL_FILENAME = "run_sql.sql"
    static String LOCALDB_PROPERTIES_FILENAME = "db.properties"
    
    File sqlDir
    File logDir
    File confDir
    File wrapperSqlFile
    File localDbPropertiesFile

    File cleanSchemaSqlDir
    File createSchemaSqlDir
    File setupDataSqlDir

    long totalRunTime = 0

    Properties db = new Properties();

    BuildEnv (Project project) {
        sqlDir = new File (project.projectDir,  LOCALDB_SQL_DIRNAME);
        logDir = new File (project.projectDir,  LOCALDB_LOG_DIRNAME);
        confDir = new File (project.projectDir,  LOCALDB_CONF_DIRNAME);

        wrapperSqlFile = new File ( confDir,  SQLPLUS_WRAPPER_SQL_FILENAME);
        localDbPropertiesFile = new File (confDir,  LOCALDB_PROPERTIES_FILENAME);

        cleanSchemaSqlDir = new File (sqlDir,  CLEAN_SCHEMA_DIRNAME)
        createSchemaSqlDir = new File (sqlDir,  CREATE_SCHEMA_DIRNAME)
        setupDataSqlDir = new File (sqlDir,  SETUP_DATA_DIRNAME)

        localDbPropertiesFile.withInputStream { db.load(it) }
        
    }
}