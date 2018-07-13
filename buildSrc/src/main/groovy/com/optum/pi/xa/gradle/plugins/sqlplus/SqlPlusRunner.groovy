/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optum.pi.xa.gradle.plugins.sqlplus

import org.apache.commons.io.FilenameUtils
import sun.misc.Resource

import groovy.time.TimeCategory 
import groovy.time.TimeDuration
import groovy.transform.CompileStatic

import com.optum.pi.xa.gradle.plugins.sqlplus.BuildEnv
import com.optum.pi.xa.gradle.plugins.sqlplus.ShellCommandRunner

import java.nio.file.Paths
import java.io.File

import org.apache.commons.io.FilenameUtils

/**
 *
 * @author opiusr
 */

@CompileStatic
class SqlPlusRunner {
    private static String SQL_PLUS_CMD = "sqlplus "
    private static String SQL_PLUS_VERSION_CMD = SQL_PLUS_CMD + "-V"
    
    private static String TNS_PING_CMD = "tnsping %s"
    
    private static String STATUS_STRING_YES = "( YES )"
    private static String STATUS_STRING_NO =  "( NO  )"
    
    
    // private long totalRunTime = 0;
    
    public static void printSqlDirBanner(String sqlDirPath) {
        String sqlDirName = FilenameUtils.getName(sqlDirPath)
        FormatedConsoleWriter.printBanner(sqlDirName)
    }
    
    public static void printSuccess(String message, long totalRunTime) {
        FormatedConsoleWriter.printLine()
        println "SUCCESS :- " + message
        println "TOTAL TIME " + totalRunTime / 1000 + " seconds. "
    }
    
    public static void printFailure(String message, long totalRunTime) {
        FormatedConsoleWriter.printLine()
        println "FAILURE :- " + message
        println "TOTAL TIME " + totalRunTime / 1000 + " seconds. "
    }    
    
    public static void run(List<String> sqlDirPathList, BuildEnv buildEnv) {
        
        boolean returnStatus = true;
        
        for (String sqlDirPath : sqlDirPathList ) {
            
            returnStatus = this.runSqlFilesInDir(sqlDirPath, buildEnv )  
        
            if (returnStatus == false)  {
                break
            }
        }
        
        if (returnStatus ) {
            printSuccess("Database build completed.", buildEnv.totalRunTime)
        } 
    }

    
    public static boolean runSqlFilesInDir(String sqlDirPath, BuildEnv buildEnv) {
        
        printSqlDirBanner(sqlDirPath) 
        
        File dir = new File(sqlDirPath) 
        
        FormatedConsoleWriter.printBeginPhrase(sqlDirPath)        
        if ( ! dir.isDirectory() ) {
            FormatedConsoleWriter.printEndPhrase("FAILED ( NOT A DIRECTORY )")
            return false
        } 
        
        def fileNameList = []
        dir.listFiles().sort { it.name }.each { fileNameList.add(it.toString() ) }
        
        FormatedConsoleWriter.printEndPhrase( String.format(" ( Total :- %d files )", fileNameList.size()) )
      
        
        for ( String sqlFileName : fileNameList ) {
            if ( ! runSqlFile(sqlFileName, buildEnv) ) {
                return false
            }
        };

        return true;        
    }
    
    
    public static boolean runSqlFile(String sqlFilePath, BuildEnv buildEnv)   {
               
        def outputStringList = new ArrayList<String>();
       
        String logFilePath = logFilePath(sqlFilePath, buildEnv)   
        
        String sqlPlusShellCommand = String.format("sqlplus %s/%s@%s as sysdba @%s %s %s", 
            buildEnv.db.userName, 
            buildEnv.db.password, 
            buildEnv.db.serviceName,
            buildEnv.wrapperSqlFile,
            sqlFilePath,
            logFilePath )
        
        FormatedConsoleWriter.printBeginPhrase(FilenameUtils.getName(sqlFilePath) )
        
        Date startTime = new Date()
        int returnValue = ShellCommandRunner.executeCommand(sqlPlusShellCommand, null, outputStringList) 
        Date endTime = new Date();
        
        TimeDuration duration = TimeCategory.minus(endTime, startTime)
        
        buildEnv.totalRunTime += duration.toMilliseconds()
  
        if (returnValue.equals(0)) {
            FormatedConsoleWriter.printEndPhrase(String.format("SUCCESS ( %.3f seconds )", duration.toMilliseconds() / 1000 ) )
        }
        else  {
            FormatedConsoleWriter.printEndPhrase(String.format("FAILED  ( %.3f seconds )", duration.toMilliseconds() / 1000 ) )
            printFailure ( "Refer log file :- " + logFilePath, buildEnv.totalRunTime)
        }
            
        return (returnValue.equals(0) )
    }
    
    public static String logFilePath(String sqlFilePath, BuildEnv buildEnv) {
        String sqlFileBaseName = FilenameUtils.getName(sqlFilePath)
        String logFileName = FilenameUtils.removeExtension(sqlFileBaseName) + FilenameUtils.EXTENSION_SEPARATOR + "log"
        File logFilePath = new File ( buildEnv.logDir, logFileName ); 
        
        return logFilePath.canonicalPath;
    }
    
    public static boolean checkProperites() {
        return ( true )
    }
    
    public static boolean isOracleAvailable(String serviceName) {
        return ( isOracleRunning(serviceName) && isSqlPlusAvailable() )
    }
    
    public static boolean isSqlPlusAvailable() {
        
        FormatedConsoleWriter.printBeginPhrase("CHECKING IF SQLPLUS IS AVAILABLE")
        
        if ( ShellCommandRunner.executeCommand(SQL_PLUS_VERSION_CMD) == 0 ) {
            FormatedConsoleWriter.printEndPhrase(STATUS_STRING_YES)
            return true;
        } else {
            FormatedConsoleWriter.printEndPhrase(STATUS_STRING_NO)
            return false
        }
    }
    
    public static boolean isOracleRunning(String serviceName) {
        
        FormatedConsoleWriter.printBeginPhrase("CHECKING IF IS ORACLE RUNNING")
        
        String tnsPingShellCommand = String.format(TNS_PING_CMD,  serviceName)
       
        if ( ShellCommandRunner.executeCommand(tnsPingShellCommand) == 0 ) {
            FormatedConsoleWriter.printEndPhrase(STATUS_STRING_YES)
            return true;
        } else {
            FormatedConsoleWriter.printEndPhrase(STATUS_STRING_NO)
            return false
        }
    }
}

