package com.group3.monitorServer.messageProcessor.notifier;

import com.group3.monitorServer.messageProcessor.notifier.htmlNotifier.HTMLWriter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

public class HTMLWriter_test {
	
	private final static String htmlPath = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "html_test";
	
	@AfterAll
	public static void cleanUp() {
		File dir = new File(htmlPath);
		recursiveClean(dir);
	}
	
	public static void recursiveClean(File file) {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				recursiveClean(f);
			}
		}
		file.delete();
	}
	
	
    @Test
    public void Constructor_NoExistingHtml() throws IOException {
        //setup
        final String htmlFilePath = htmlPath + File.separator + "constructorTest.html";
    	File file = new File(htmlFilePath);
        file.delete();

        String htmlExpected = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\t<body>\n" +
                "\t\t<ul>\n" +
                "\t\t\t<!--$-->\n" +
                "\t\t</ul>\n" +
                "\t</body>\n" +
                "</html>\n";

        //Act
        HTMLWriter htmlWriter = new HTMLWriter(htmlFilePath);

        StringBuilder htmlActual = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String currentString;
        while((currentString = bufferedReader.readLine()) != null){
            htmlActual.append(currentString).append("\n");
        }

        //Assert
        Assertions.assertTrue(file.exists());
        Assertions.assertEquals(htmlExpected, htmlActual.toString());
        
        //Cleanup
        bufferedReader.close();
        htmlWriter.close();
    }

    @Test
    public void Constructor_AlreadyExistingHtml() throws IOException {
        //setup
    	final String htmlFilePath = htmlPath + File.separator + "constructorTest.html";
        File file = new File(htmlFilePath);
        file.delete();
        file.createNewFile();

        String htmlExpected = "<!DOCTYPE html>\n" +
                              "<html>\n" +
                              "\t<body>\n" +
                              "\t\t<ul>\n" +
                              "\t\t\t<li>test1</li>\n" +
                              "\t\t\t<!--$-->\n" +
                              "\t\t</ul>\n" +
                              "\t</body>\n" +
                              "</html>\n";

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(htmlExpected);
        fileWriter.flush();
        fileWriter.close();

        //Act
        HTMLWriter htmlWriter = new HTMLWriter(htmlFilePath);

        StringBuilder htmlActual = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String currentString;
        while((currentString = bufferedReader.readLine()) != null){
            htmlActual.append(currentString).append("\n");
        }

        //Assert
        Assertions.assertTrue(file.exists());
        Assertions.assertEquals(htmlExpected, htmlActual.toString());
        
        //Cleanup
        bufferedReader.close();
        htmlWriter.close();
    }

    @Test
    public void AddMessage() throws IOException {
        //Setup
    	final String htmlFilePath = htmlPath + File.separator + "test.html";
        File file = new File(htmlFilePath);
        file.delete();
        file.createNewFile();

        String htmlBefore = "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "\t<body>\n" +
                            "\t\t<ul>\n" +
                            "\t\t\t<li>Error1</li>\n" +
                            "\t\t\t<li>Error2</li>\n" +
                            "\t\t\t<li>Error3</li>\n" +
                            "\t\t\t<!--$-->\n" +
                            "\t\t</ul>\n" +
                            "\t</body>\n" +
                            "</html>";

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(htmlBefore);
        fileWriter.flush();
        fileWriter.close();

        //Act
        HTMLWriter htmlWriter = new HTMLWriter(htmlFilePath);
        htmlWriter.AddMessage("Error4");

        StringBuilder htmlAfter = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String currentString;
        while((currentString = bufferedReader.readLine()) != null){
            htmlAfter.append(currentString).append("\n");
        }

        //Assert
        String expectedHtml = "<!DOCTYPE html>\n" +
                              "<html>\n" +
                              "\t<body>\n" +
                              "\t\t<ul>\n" +
                              "\t\t\t<li>Error1</li>\n" +
                              "\t\t\t<li>Error2</li>\n" +
                              "\t\t\t<li>Error3</li>\n" +
                              "\t\t\t<li>Error4</li>\n" +
                              "\t\t\t<!--$-->\n" +
                              "\t\t</ul>\n" +
                              "\t</body>\n" +
                             "</html>\n"; //note the real html would not have a "\n" at the end on this line, the way htmlAfter is constructed adds this.

        Assertions.assertEquals(expectedHtml, htmlAfter.toString());
        
        //Cleanup
        bufferedReader.close();
        htmlWriter.close();
    }
}
