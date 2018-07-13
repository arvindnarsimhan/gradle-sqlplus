package com.optum.pi.xa.gradle.plugins.sqlplus.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.GradleException

import com.optum.pi.xa.gradle.plugins.sqlplus.BuildEnv
import com.optum.pi.xa.gradle.plugins.sqlplus.ShellCommandRunner

import com.optum.pi.xa.gradle.plugins.sqlplus.FormatedConsoleWriter
import com.optum.pi.xa.gradle.plugins.sqlplus.SqlPlusRunner


/**
 * This task is responsible for checking if Oracle is Running and SQL*Plus command line is available
 *
 * @author arvind.narsimhan@optum.com
 * @since 1.0.0
 */
class CheckOracleStatus extends DefaultTask {

  static final String DESCRIPTION = "Check if Oracle is running and SQL*Plus is available"

  @TaskAction
  run() {
    FormatedConsoleWriter.printBanner("Oracle")

    if ( ! SqlPlusRunner.isOracleRunning( "${project.buildEnv.db.serviceName}" ) ) {
      throw new GradleException("Oracle Service :- ${project.buildEnv.db.serviceName} is not available !!! ")
    }

    if ( ! SqlPlusRunner.isSqlPlusAvailable() )  {
      throw new GradleException("Oracle SQL*Plus is available. Check environment PATH variable for sqlplus ")
    }

    // project.file(project.querydsl.querydslSourcesDir).mkdirs()
  }
}