package com.group3.monitorServer.testClasses;

import com.group3.monitorServer.messageProcessor.notifier.htmlNotifier.HTMLWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

public abstract class AbstractHTMLWriterTest {
    public static HTMLWriter htmlWriter;
    public static String HTMLTestPath = "src" + File.separator + "main" + File.separator + "resources" +
            File.separator + "html_test" + File.separator + "test.html";

    @BeforeEach
    public void PrepareForTest(){
        deleteHtml();
        htmlWriter = createHtml(); 
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
