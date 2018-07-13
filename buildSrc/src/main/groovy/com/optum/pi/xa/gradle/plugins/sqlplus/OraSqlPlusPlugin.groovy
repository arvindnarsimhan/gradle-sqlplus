package com.optum.pi.xa.gradle.plugins.sqlplus

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import com.optum.pi.xa.gradle.plugins.sqlplus.BuildEnv
import com.optum.pi.xa.gradle.plugins.sqlplus.SqlPlusRunner

import com.optum.pi.xa.gradle.plugins.sqlplus.tasks.CheckOracleStatus
import com.optum.pi.xa.gradle.plugins.sqlplus.tasks.CleanDatabase
import com.optum.pi.xa.gradle.plugins.sqlplus.tasks.CreateSchema
import com.optum.pi.xa.gradle.plugins.sqlplus.tasks.SetupData

import groovy.transform.CompileStatic

// @CompileStatic
// Commenting static compile since project.tasks.build is resolved only at run time and not compile time
class OraSqlPlusPlugin implements Plugin<Project> {
    private static final Logger LOG = Logging.getLogger(OraSqlPlusPlugin.class)

    @Override
    void apply(final Project project) {
        
        // do nothing if plugin is already applied
        if (project.plugins.hasPlugin(OraSqlPlusPlugin.class)) {
            LOG.info("OraSqlPlusPlugin plugin has been already applied")
            return
        }

        LOG.info("Applying OraSqlPlusPlugin plugin")

        BuildEnv buildEnv = project.extensions.create(BuildEnv.NAME, BuildEnv, project)

        def taskCheckOracleStatus = project.tasks.create(type: CheckOracleStatus, name:"checkOracleStatus")
        def taskCleanDatabase = project.tasks.create(type: CleanDatabase, name:"cleanDatabase")
        def taskCreateSchema = project.tasks.create(type: CreateSchema, name:"createSchema")
        def taskSetupData = project.tasks.create(type: SetupData, name:"setupData")

        taskCleanDatabase.dependsOn taskCheckOracleStatus
        taskCreateSchema.dependsOn taskCleanDatabase
        taskSetupData.dependsOn taskCreateSchema
   
        project.tasks.build.dependsOn taskSetupData
        project.tasks.clean.dependsOn taskCleanDatabase
     
    }
}
