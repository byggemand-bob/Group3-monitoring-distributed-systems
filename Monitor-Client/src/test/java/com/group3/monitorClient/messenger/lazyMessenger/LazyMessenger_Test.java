package com.group3.monitorClient.messenger.lazyMessenger;

import com.group3.monitorClient.AbstractSQLDeleter;
import com.group3.monitorClient.AbstractSQLMessageManagerTest;
import com.group3.monitorClient.testClasses.LazyMessenger_TestClass;
import com.group3.monitorClient.testClasses.TrueFalseRequirement_TestClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LazyMessenger_Test extends AbstractSQLDeleter {
    /* tests the control functions of the GreedyMessenger, Start(), Stop(), Pause() and Resume() */
    @Test
    public void ThreadControl() throws InterruptedException {
        LazyMessenger_TestClass messenger = new LazyMessenger_TestClass("http://1.1.1.1:8080", getSQLPath(), getSQLFileName());

        TrueFalseRequirement_TestClass trueRequirement = new TrueFalseRequirement_TestClass(true);
        messenger.AddRequirement(trueRequirement);

        for(int x = 0; x < 5; x++){
            messenger.AddMessage(getDefaultTimingMessage());
        }

        /* Confirms no messages was send before the messenger was started */
        Assertions.assertEquals(5, messenger.MessageQueueSize());

        messenger.Start();
        Thread.sleep(100);
        messenger.Pause();

        /* confirms the messenger is now sending messages */
        Assertions.assertTrue(messenger.MessageQueueSize() < 5);

        long SizeOfQueueBefore = messenger.MessageQueueSize();

        for(int x = 0; x < 5; x++){
            messenger.AddMessage(getDefaultTimingMessage());
        }

        /* confirms that the messenger is paused, and is not sending messages */
        Assertions.assertEquals(SizeOfQueueBefore + 5, messenger.MessageQueueSize());

        SizeOfQueueBefore = messenger.MessageQueueSize();

        messenger.Resume();
        Thread.sleep(100);
        messenger.Stop();

        /* confirms the messenger was sending messages again after being resumed */
        Assertions.assertTrue(SizeOfQueueBefore > messenger.MessageQueueSize());

        Thread.sleep(500);
        /* confirms that the messenger thread is now dead */
        Assertions.assertFalse(messenger.MessengerIsAlive());
        messenger.CloseConnection();
    }

    /* Ensures the AddMonitorData() method works as intended */
    @Test
    public void AddDataTest(){
        LazyMessenger_TestClass messenger = new LazyMessenger_TestClass("1.1.1.1:8080", getSQLPath(), getSQLFileName());

        messenger.AddMessage(getDefaultTimingMessage());

        Assertions.assertEquals(1, messenger.MessageQueueSize());
        messenger.CloseConnection();
    }

    /* tests if changing requirements between true and false, stops and runs message sending as intended */
    @Test
    public void RequirementsTest() throws InterruptedException {
        LazyMessenger_TestClass messenger = new LazyMessenger_TestClass("http://1.1.1.1:8080", getSQLPath(), getSQLFileName());

        TrueFalseRequirement_TestClass Requirement1 = new TrueFalseRequirement_TestClass(false);
        TrueFalseRequirement_TestClass Requirement2 = new TrueFalseRequirement_TestClass(false);
        TrueFalseRequirement_TestClass Requirement3 = new TrueFalseRequirement_TestClass(false);

        messenger.AddRequirement(Requirement1);
        messenger.AddRequirement(Requirement2);
        messenger.AddRequirement(Requirement3);

        for(int x = 0; x < 5; x++){
            messenger.AddMessage(getDefaultTimingMessage());
        }

        /* ensures the the messenger isn't sending messages before being started */
        Assertions.assertEquals(5, messenger.MessageQueueSize());

        messenger.Start();
        Thread.sleep(100);

        /* Test that the messenger continues to not send messages, as all requirements report false */
        Assertions.assertEquals(5, messenger.MessageQueueSize());

        Requirement1.bool = true;
        messenger.Resume();
        Thread.sleep(100);

        /* Test that the messenger continues to not send messages, as only one requirement is true */
        Assertions.assertEquals(5, messenger.MessageQueueSize());

        Requirement2.bool = true;
        Requirement3.bool = true;
        messenger.Resume();
        Thread.sleep(100);

        /* tests that the messenger is now sending messages as all requirements tests true */
        Assertions.assertTrue(messenger.MessageQueueSize() < 5);
        messenger.CloseConnection();
    }
}

