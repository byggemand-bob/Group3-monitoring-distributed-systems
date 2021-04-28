package com.group3.monitorServer.testClasses;

import com.group3.monitorServer.messageProcessor.notifier.htmlNotifier.HTMLWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

public abstract class AbstractHTMLWriterTest {
    public static HTMLWriter htmlWriter;
    public static String HTMLTestPath;

    @BeforeEach
    public void PrepareForTest(){
        deleteHtml();
        htmlWriter = createHtml();
        String separator = File.separator;
        HTMLTestPath = "src" + separator + "main" + separator + "resources" +
                       separator + "html_test" + separator + "test.html";
    }

    public static HTMLWriter createHtml(){
        return htmlWriter = new HTMLWriter(HTMLTestPath);
    }

    public static void deleteHtml(){
        new File(HTMLTestPath).delete();
    }

    @AfterEach
    public void cleanUp(){
        deleteHtml();
    }
}
