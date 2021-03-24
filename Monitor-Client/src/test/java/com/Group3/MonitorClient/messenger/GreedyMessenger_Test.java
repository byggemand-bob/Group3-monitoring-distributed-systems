package com.group3.monitorClient.messenger;

import com.group3.monitorClient.messenger.messages.MessageCreator;
import com.group3.monitorClient.messenger.messages.MessageInterface;
import com.group3.monitorClient.messenger.queue.SynchronizedQueue;
import com.group3.monitorClient.messenger.messages.TimingMonitorDataMessage;
import com.group3.monitorClient.testClasses.GreedyMessenger_TestClass;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.TimingMonitorData;

public class GreedyMessenger_Test {

    /* tests the control functions of the GreedyMessenger, Start(), Stop(), Pause() and Resume() */
    @Test
    public void ThreadControl() throws InterruptedException {
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
    public void AddDataTest(){
        SynchronizedQueue<MessageInterface> queue = new SynchronizedQueue<MessageInterface>();
        GreedyMessenger messenger = new GreedyMessenger("1.1.1.1:8080", queue);
        MessageCreator messageCreator = new MessageCreator();

        messenger.AddMessage(messageCreator.MakeMessage(new TimingMonitorData()));

        Assertions.assertEquals(1, queue.Size());
    }

    /* verifies that the messenger doesn't lose messages, when the return http status code isn't 200 */
    @Test
    public void UnsuccessfulMessageSendingHandling() throws InterruptedException {
        SynchronizedQueue<MessageInterface> queue = new SynchronizedQueue<MessageInterface>();
        GreedyMessenger messenger = new GreedyMessenger_TestClass("1.1.1.1:8080", queue, 400);

        for(int x = 0; x < 5; x++){
            queue.Put(new TimingMonitorDataMessage(new TimingMonitorData()));
        }

        messenger.Resume();
        Thread.sleep(100);
        messenger.Stop();

        Assertions.assertEquals(5, queue.Size());
    }
}

