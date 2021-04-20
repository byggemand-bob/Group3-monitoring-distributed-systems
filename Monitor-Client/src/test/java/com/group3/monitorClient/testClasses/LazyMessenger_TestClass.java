package com.group3.monitorClient.testClasses;

import com.group3.monitorClient.messenger.GreedyMessenger;
import com.group3.monitorClient.messenger.LazyMessenger;

/*
 * Test class set subMessenger in LazyMessenger to GreedyMessenger_TestClass,
 * this removes the need for connecting to a server
 */
public class LazyMessenger_TestClass extends LazyMessenger {
    public LazyMessenger_TestClass(String monitorIP, String sqlPath, String sqlFileName) {
        super(monitorIP, sqlPath, sqlFileName);
        ((GreedyMessenger)subMessenger).CloseConnectionToSQL();
        subMessenger = new GreedyMessenger_TestClass(monitorIP, sqlPath, sqlFileName);
    }

    public void CloseConnection(){
        ((GreedyMessenger_TestClass)subMessenger).CloseConnection();
    }
}
