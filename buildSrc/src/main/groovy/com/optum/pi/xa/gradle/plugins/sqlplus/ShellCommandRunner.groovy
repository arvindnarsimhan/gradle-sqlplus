/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optum.pi.xa.gradle.plugins.sqlplus

import groovy.transform.CompileStatic
import java.io.File
import org.apache.commons.lang3.StringUtils

/**
 *
 * @author opiusr
 */

@CompileStatic
public class ShellCommandRunner {
    
    public static int executeCommand(String command) {
        return execute(command, null, null ) 
    }
    
    public static int executeCommand(String command, String workingDir) {
        return execute(command,  workingDirFile(workingDir), null)
    }
    
    public static int executeCommand(String command, String workingDir, List<String> output) {
        return execute(command, workingDirFile(workingDir), output)
    }
 
    private static int execute(String command, File workingDir, List<String> output) {
                       
        Process process =  new ProcessBuilder(addShellPrefix(command))
        .directory(workingDir)
        .redirectErrorStream(true) 
        .start()
        
        if ( null != output ) {
            process.inputStream.eachLine { output.add(it) } 
        }
        
        process.waitFor();
        return process.exitValue()
    }
    
    private static String[] addShellPrefix(String command) {
        String[] commandArray = new String[3]
        commandArray[0] = "bash"
        commandArray[1] = "-c"
        commandArray[2] = command
        return commandArray
    }
    
    private static File workingDirFile(String workingDir) {
        if ( StringUtils.isBlank(workingDir) )
        return new File(System.getProperty('user.dir'))
        else
        return new File(workingDir);
    }
}

