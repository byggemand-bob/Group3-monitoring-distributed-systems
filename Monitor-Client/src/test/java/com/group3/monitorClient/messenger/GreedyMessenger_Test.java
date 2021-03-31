package com.group3.monitorClient.messenger;

import com.group3.monitorClient.AbstractPersistentSQLQueueTest;
import com.group3.monitorClient.AbstractSQLTest;
import com.group3.monitorClient.messenger.messages.ErrorDataMessage;
import com.group3.monitorClient.messenger.messages.MessageCreator;
import com.group3.monitorClient.messenger.messages.MessageInterface;
import com.group3.monitorClient.messenger.queue.PersistentSQLQueue;
import com.group3.monitorClient.messenger.queue.SynchronizedQueue;
import com.group3.monitorClient.messenger.messages.TimingMonitorDataMessage;
import com.group3.monitorClient.testClasses.GreedyMessenger_TestClass;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.ErrorData;
import org.openapitools.client.model.TimingMonitorData;
import org.threeten.bp.OffsetDateTime;

public class GreedyMessenger_Test extends AbstractPersistentSQLQueueTest {

    /* tests the control functions of the GreedyMessenger, Start(), Stop(), Pause() and Resume() */
    @Test
    public void testThreadControl() throws InterruptedException {
        SynchronizedQueue<MessageInterface> queue = new SynchronizedQueue<MessageInterface>();
        GreedyMessenger messenger = new GreedyMessenger_TestClass("1.1.1.1:8080", queue);

        for(int x = 0; x < 5; x++){
            queue.Put(new TimingMonitorDataMessage(new TimingMonitorData()));
        }

        /* Confirms no messages was send before the messenger was started */
        Assertions.assertEquals(5, queue.Size());

        messenger.Start();
        Thread.sleep(100);
        messenger.Pause();

        /* confirms the messenger is now sending messages */
        Assertions.assertTrue(queue.Size() < 5);

        int SizeOfQueueBefore = queue.Size();

        for(int x = 0; x < 5; x++){
            queue.Put(new TimingMonitorDataMessage(new TimingMonitorData()));
        }

        /* confirms that the messenger is paused, and is not sending messages */
        Assertions.assertEquals(SizeOfQueueBefore + 5, queue.Size());

        SizeOfQueueBefore = queue.Size();

        messenger.Resume();
        Thread.sleep(100);
        messenger.Stop();

        /* confirms the messenger was sending messages again after being resumed */
        Assertions.assertTrue(SizeOfQueueBefore > queue.Size());

        Thread.sleep(100);
        /* confirms that the messenger thread is now dead */
        Assertions.assertFalse(messenger.MessengerIsAlive());
    }

    /* Ensures the AddMonitorData() method works as intended */
    @Test
    public void testAddDataTest(){
        GreedyMessenger messenger = new GreedyMessenger("1.1.1.1:8080", messageQueue);
        MessageCreator messageCreator = new MessageCreator();

        TimingMonitorData timingMonitorData = new TimingMonitorData();
        timingMonitorData.setEventID(1L);
        timingMonitorData.setTimestamp(OffsetDateTime.now());
        timingMonitorData.setSenderID(21L);

        messenger.AddMessage(messageCreator.MakeMessage(timingMonitorData));

        Assertions.assertEquals(1, messageQueue.Size());
    }

    /* Ensures that an ErrorMessage doesn't create another ErrorMessage even if it fails to send itself */
    @Test
    public void testErrorMessageCreationFail () throws InterruptedException {
        //Setup
        GreedyMessenger_TestClass messenger = new GreedyMessenger_TestClass("1.1.1.1:8080", messageQueue, 400);
        ErrorData errorData = new ErrorData();
        errorData.setErrorMessageType(ErrorData.ErrorMessageTypeEnum.NOCONNECTION);
        errorData.setSenderID(1L);
        errorData.setTimestamp(OffsetDateTime.now());
        errorData.setComment("hej");
        ErrorDataMessage errorDataMessage = new ErrorDataMessage(errorData);

        messageQueue.Put(errorDataMessage);

        //Act
        messenger.Start();
        Thread.sleep(100);
        messenger.Stop();

        //Assert
        Assertions.assertEquals(0, messageQueue.Size());
    }
}

