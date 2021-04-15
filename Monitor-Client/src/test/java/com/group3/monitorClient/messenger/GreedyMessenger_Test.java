package com.group3.monitorClient.messenger;

import com.group3.monitorClient.AbstractSQLDeleter;
import com.group3.monitorClient.testClasses.GreedyMessenger_TestClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GreedyMessenger_Test extends AbstractSQLDeleter {

    /* tests the control functions of the GreedyMessenger, Start(), Stop(), Pause() and Resume() */
    @Test
    public void testThreadControl() throws InterruptedException {
        GreedyMessenger_TestClass messenger = new GreedyMessenger_TestClass("1.1.1.1:8080", getSQLPath(), getSQLFileName());

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

        Thread.sleep(100);
        /* confirms that the messenger thread is now dead */
        Assertions.assertFalse(messenger.MessengerIsAlive());

        messenger.CloseConnection();
    }

    /* Ensures the AddMonitorData() method works as intended */
    @Test
    public void testAddDataTest(){
        //Setup
        GreedyMessenger_TestClass messenger = new GreedyMessenger_TestClass("1.1.1.1:8080", getSQLPath(), getSQLFileName());

        //Act
        messenger.AddMessage(getDefaultTimingMessage());

        //Assert
        Assertions.assertEquals(1, messenger.MessageQueueSize());
        messenger.CloseConnection();
    }

    /* Ensures that an ErrorMessage doesn't create another ErrorMessage even if it fails to send itself */
    @Test
    public void testErrorMessageCreationFail () throws InterruptedException {
        //Setup
        GreedyMessenger_TestClass messenger = new GreedyMessenger_TestClass("1.1.1.1:8080", getSQLPath(), getSQLFileName(), 400);

        messenger.AddMessage(getDefaultErrorMessage());

        //Act
        messenger.Start();
        Thread.sleep(1000);
        messenger.Stop();

        //Assert
        Assertions.assertEquals(0, messenger.MessageQueueSize());
        messenger.CloseConnection();
    }
}

