package com.optum.pi.xa.gradle.plugins.sqlplus.tasks


import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.GradleException

import com.optum.pi.xa.gradle.plugins.sqlplus.BuildEnv
import com.optum.pi.xa.gradle.plugins.sqlplus.ShellCommandRunner

import com.optum.pi.xa.gradle.plugins.sqlplus.FormatedConsoleWriter
import com.optum.pi.xa.gradle.plugins.sqlplus.SqlPlusRunner



/**
 * This task is responsible for running the scripts to Create the Schema(s)
 *
 * @author arvind.narsimhan@optum.com
 * @since 1.0.0
 */
class SetupData extends DefaultTask {

  static final String DESCRIPTION = "Run SQL scripts to setup Data"

  @TaskAction
  run() {
    if ( ! SqlPlusRunner.runSqlFilesInDir( "${project.buildEnv.setupDataSqlDir}", project.buildEnv) ) {
      throw new GradleException( "Error in running SQL script(s) from ${project.buildEnv.setupDataSqlDir}" )
    }

  }
}