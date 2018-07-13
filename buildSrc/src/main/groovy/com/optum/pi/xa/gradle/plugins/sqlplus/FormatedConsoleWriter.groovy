package com.optum.pi.xa.gradle.plugins.sqlplus

import groovy.transform.CompileStatic

@CompileStatic
public class FormatedConsoleWriter {
    static private String lineSeperator = System.getProperty("line.separator")   
    static private String bannerCornerCharacter = '*'
    static private String bannerLineCharacter = '*'
    static private String bannerFillCharacter = '*'

    static private String lineBeginCharacter = '*'
    static private String lineEndCharacter = '*'
    static private String lineFillCharacter = '.'


    static private int lineWidth = 120
    static private int leftPhraseWidth = 90


    static private String line = bannerCornerCharacter + bannerLineCharacter.multiply(lineWidth - 2) +  bannerCornerCharacter << lineSeperator

    static void printBanner(String bannerText)   {
        def banner = new StringBuffer();
        banner << line 
        banner << bannerCornerCharacter << bannerText.toUpperCase().replace("", " ").center(lineWidth-2, " ") << bannerCornerCharacter << lineSeperator
        banner << line

        print banner.toString()
    }

    static void printBeginPhrase(String phrase) {
        def beginPhraseWidth = leftPhraseWidth;

        def buffer = new StringBuffer();
        buffer << lineBeginCharacter << phrase.padRight(beginPhraseWidth - 1, lineFillCharacter);
        print buffer.toString();
    }

    static void printEndPhrase(String phrase) {
        def endPhraseWidth = lineWidth - leftPhraseWidth
        
        def buffer = new StringBuffer();
        buffer << phrase.padLeft(endPhraseWidth - 1, lineFillCharacter) << lineEndCharacter << lineSeperator;
        print buffer.toString();
    }

    static void printLine() {
        print line;
    }
}