package com.group3.monitorServer.Tests;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.*;

public class RandomAccessFileTest {
    @Test
    public void RAFTest() throws IOException {
        File file = new File("src/main/resources/test/test.html");

        Document doc = Jsoup.parse(file, "utf-8");

        System.out.println(doc.outerHtml());
    }
}
