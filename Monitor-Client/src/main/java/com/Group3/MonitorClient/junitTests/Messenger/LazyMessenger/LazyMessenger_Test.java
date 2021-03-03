package com.Group3.MonitorClient.junitTests.Messenger.LazyMessenger;

import com.Group3.MonitorClient.junitTests.TestClasses.LazyMessenger_TestClass;
import com.Group3.MonitorClient.junitTests.TestClasses.TrueFalseRequirement_TestClass;
import com.Group3.MonitorClient.Messenger.LazyMessenger.LazyMessenger;
import com.Group3.MonitorClient.Messenger.SynchronizedQueue;
import org.junit.Assert;
import org.junit.Test;
import org.openapitools.client.model.TimingMonitorData;

public class LazyMessenger_Test {
    /* tests the control functions of the GreedyMessenger, Start(), Stop(), Pause() and Resume() */
    @Test
    public void ThreadControl() throws InterruptedException {
        SynchronizedQueue queue = new SynchronizedQueue();
        LazyMessenger messenger = new LazyMessenger_TestClass("http://1.1.1.1:8080", queue);

        TrueFalseRequirement_TestClass trueRequirement = new TrueFalseRequirement_TestClass(true);
        messenger.AddRequirement(trueRequirement);

        for(int x = 0; x < 5; x++){
            queue.Add(new TimingMonitorData());
        }

        /* Confirms no messages was send before the messenger was started */
        Assert.assertEquals(5, queue.Size());

        messenger.Start();
        Thread.sleep(100);
        messenger.Pause();

        /* confirms the messenger is now sending messages */
        Assert.assertTrue(queue.Size() < 5);

        int SizeOfQueueBefore = queue.Size();

        for(int x = 0; x < 5; x++){
            queue.Add(new TimingMonitorData());
        }

        /* confirms that the messenger is paused, and is not sending messages */
        Assert.assertEquals(SizeOfQueueBefore + 5, queue.Size());

        SizeOfQueueBefore = queue.Size();

        messenger.Resume();
        Thread.sleep(100);
        messenger.Stop();

        /* confirms the messenger was sending messages again after being resumed */
//        Assert.assertTrue(SizeOfQueueBefore > queue.Size());

        Thread.sleep(500);
        /* confirms that the messenger thread is now dead */
        Assert.assertFalse(messenger.MessengerIsAlive());
    }

    /* Ensures the AddMonitorData() method works as intended */
    @Test
    public void AddDataTest(){
        SynchronizedQueue queue = new SynchronizedQueue();
        LazyMessenger messenger = new LazyMessenger("1.1.1.1:8080", queue);

        messenger.AddMonitorData(new TimingMonitorData());

        Assert.assertEquals(1, queue.Size());
    }

    /* tests if changing requirements between true and false, stops and runs message sending as intended */
    @Test
    public void RequirementsTest() throws InterruptedException {
        SynchronizedQueue queue = new SynchronizedQueue();
        LazyMessenger messenger = new LazyMessenger_TestClass("http://1.1.1.1:8080", queue);

        TrueFalseRequirement_TestClass Requirement1 = new TrueFalseRequirement_TestClass(false);
        TrueFalseRequirement_TestClass Requirement2 = new TrueFalseRequirement_TestClass(false);
        TrueFalseRequirement_TestClass Requirement3 = new TrueFalseRequirement_TestClass(false);

        messenger.AddRequirement(Requirement1);
        messenger.AddRequirement(Requirement2);
        messenger.AddRequirement(Requirement3);

        for(int x = 0; x < 5; x++){
            queue.Add(new TimingMonitorData());
        }

        /* ensures the the messenger isn't sending messages before being started */
        Assert.assertEquals(5, queue.Size());

        messenger.Start();
        Thread.sleep(100);

        /* Test that the messenger continues to not send messages, as all requirements report false */
        Assert.assertEquals(5, queue.Size());

        Requirement1.bool = true;
        messenger.Resume();
        Thread.sleep(100);

        /* Test that the messenger continues to not send messages, as only one requirement is true */
        Assert.assertEquals(5, queue.Size());

        Requirement2.bool = true;
        Requirement3.bool = true;
        messenger.Resume();
        Thread.sleep(100);

        /* tests that the messenger is now sending messages as all requirements tests true */
        Assert.assertTrue(queue.Size() < 5);
    }
}

