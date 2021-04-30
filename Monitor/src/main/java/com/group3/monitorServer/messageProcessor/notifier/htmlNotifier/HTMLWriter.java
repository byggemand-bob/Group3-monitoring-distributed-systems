package com.group3.monitorServer.messageProcessor.notifier.htmlNotifier;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class HTMLWriter {
    private RandomAccessFile raf = null;

    /*
     * Checks the user-specification file for html location
     * otherwise creates html in default location
     * "src/main/resources/Warnings.html"
     */
    public HTMLWriter() {
        String htmlPath = GetUserDefinedHtmlPath();
        try {
            File file = PrepareHTMLFile(htmlPath);
            raf = new RandomAccessFile(file, "rw");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Creates a html at the specified path */
    public HTMLWriter(String htmlPath){
        try {
            File file = PrepareHTMLFile(htmlPath);
            raf = new RandomAccessFile(file, "rw");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* checks the html file location, if the html doesn't exists it creates an empty html */
    private File PrepareHTMLFile(String htmlPath) throws IOException {
        File file = new File(htmlPath);
        createDirectoryIfNotExist(htmlPath);

        //If the file didn't exist, create a new one, with an empty html
        if(file.createNewFile()){
            String emptyHTML = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "\t<body>\n" +
                    "\t\t<ul>\n" +
                    "\t\t\t<!--$-->\n" +
                    "\t\t</ul>\n" +
                    "\t</body>\n" +
                    "</html>";

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(emptyHTML);
            fileWriter.flush();
            fileWriter.close();
        }

        return file;
    }
    
    /* Creates the necessary directory for the html file, if it does not already exist*/
    private void createDirectoryIfNotExist(String htmlFilePath) {
    	final int fileNameIndex = htmlFilePath.lastIndexOf(File.separator);
    	final String path = htmlFilePath.substring(0, fileNameIndex);
    	File dir = new File(path);
    	if (!dir.exists()) {
    		dir.mkdir();
    	}
    }

    /* Returns the user specified path if one exists, otherwise returns default path */
    private String GetUserDefinedHtmlPath() {
        String htmlPath = CheckSpecificationFile();

        if(htmlPath != null){
            return htmlPath;
        } else {
            //if nothing was specified in the user specification file, return default location
            return "src/main/resources/Warnings.html";
        }
    }

    /* Returns the user specified html path */
    private String CheckSpecificationFile() {
        //TODO: create implementation for user specified html location
        return null;
    }

    /*
     * Adds specified message to html
     * returns true, if message was successfully added
     * false if unsuccessful
     */
    public boolean AddMessage(String message){
        if(raf == null){
            return false;
        }
        message = prepMessage(message);

        try {
            writeMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private synchronized void writeMessage(String message) throws IOException {
        raf.seek(raf.length() - 36);
        raf.writeBytes("\t\t\t<li>" + message + "</li>\n");
        appendClosingTags();
    }

    private String prepMessage(String message) {
        if(message.endsWith("\n")){
            return message.substring(0, message.length() - 1);
        } else {
            return message;
        }
    }

    private synchronized void appendClosingTags() throws IOException {
        raf.writeBytes("\t\t\t<!--$-->\n");
        raf.writeBytes("\t\t</ul>\n");
        raf.writeBytes("\t</body>\n");
        raf.writeBytes("</html>");
    }
    
    public void close() throws IOException {
    	raf.close();
    }
}
