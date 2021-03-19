package com.Group3.monitorClient.messenger.lazyMessenger;

import com.Group3.monitorClient.Messenger.LazyMessenger.LazyMessenger;
import com.Group3.monitorClient.Messenger.messages.MessageCreator;
import com.Group3.monitorClient.Messenger.messages.MessageInterface;
import com.Group3.monitorClient.Messenger.messages.TimingMonitorDataMessage;
import com.Group3.monitorClient.testClasses.LazyMessenger_TestClass;
import com.Group3.monitorClient.testClasses.TrueFalseRequirement_TestClass;
import com.Group3.monitorClient.Messenger.Queue.SynchronizedQueue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.TimingMonitorData;

public class LazyMessenger_Test {
    /* tests the control functions of the GreedyMessenger, Start(), Stop(), Pause() and Resume() */
    @Test
    public void ThreadControl() throws InterruptedException {
        SynchronizedQueue<MessageInterface> queue = new SynchronizedQueue<MessageInterface>();
        LazyMessenger messenger = new LazyMessenger_TestClass("http://1.1.1.1:8080", queue);

        TrueFalseRequirement_TestClass trueRequirement = new TrueFalseRequirement_TestClass(true);
        messenger.AddRequirement(trueRequirement);

        for(int x = 0; x < 5; x++){
            queue.Put(new TimingMonitorDataMessage(new TimingMonitorData(), 0));
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
            queue.Put(new TimingMonitorDataMessage(new TimingMonitorData(), 0));
        }

        /* confirms that the messenger is paused, and is not sending messages */
        Assertions.assertEquals(SizeOfQueueBefore + 5, queue.Size());

        SizeOfQueueBefore = queue.Size();

        messenger.Resume();
        Thread.sleep(100);
        messenger.Stop();

        /* confirms the messenger was sending messages again after being resumed */
//        Assertions.assertTrue(SizeOfQueueBefore > queue.Size());

        Thread.sleep(500);
        /* confirms that the messenger thread is now dead */
        Assertions.assertFalse(messenger.MessengerIsAlive());
    }

    /* Ensures the AddMonitorData() method works as intended */
    @Test
    public void AddDataTest(){
        SynchronizedQueue<MessageInterface> queue = new SynchronizedQueue<MessageInterface>();
        LazyMessenger messenger = new LazyMessenger("1.1.1.1:8080", queue);
        MessageCreator messageCreator = new MessageCreator();

        messenger.AddMessage(messageCreator.MakeMessage(new TimingMonitorData()));

        Assertions.assertEquals(1, queue.Size());
    }

    /* tests if changing requirements between true and false, stops and runs message sending as intended */
    @Test
    public void RequirementsTest() throws InterruptedException {
        SynchronizedQueue<MessageInterface> queue = new SynchronizedQueue<MessageInterface>();
        LazyMessenger messenger = new LazyMessenger_TestClass("http://1.1.1.1:8080", queue);

        TrueFalseRequirement_TestClass Requirement1 = new TrueFalseRequirement_TestClass(false);
        TrueFalseRequirement_TestClass Requirement2 = new TrueFalseRequirement_TestClass(false);
        TrueFalseRequirement_TestClass Requirement3 = new TrueFalseRequirement_TestClass(false);

        messenger.AddRequirement(Requirement1);
        messenger.AddRequirement(Requirement2);
        messenger.AddRequirement(Requirement3);

        for(int x = 0; x < 5; x++){
            queue.Put(new TimingMonitorDataMessage(new TimingMonitorData(), 0));
        }

        /* ensures the the messenger isn't sending messages before being started */
        Assertions.assertEquals(5, queue.Size());

        messenger.Start();
        Thread.sleep(100);

        /* Test that the messenger continues to not send messages, as all requirements report false */
        Assertions.assertEquals(5, queue.Size());

        Requirement1.bool = true;
        messenger.Resume();
        Thread.sleep(100);

        /* Test that the messenger continues to not send messages, as only one requirement is true */
        Assertions.assertEquals(5, queue.Size());

        Requirement2.bool = true;
        Requirement3.bool = true;
        messenger.Resume();
        Thread.sleep(100);

        /* tests that the messenger is now sending messages as all requirements tests true */
        Assertions.assertTrue(queue.Size() < 5);
    }
}

