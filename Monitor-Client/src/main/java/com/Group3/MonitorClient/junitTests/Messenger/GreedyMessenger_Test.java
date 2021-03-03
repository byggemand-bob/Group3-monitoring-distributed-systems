package com.Group3.MonitorClient.junitTests.Messenger;

import com.Group3.MonitorClient.Messenger.SynchronizedQueue;
import com.Group3.MonitorClient.junitTests.TestClasses.GreedyMessenger_TestClass;
import org.junit.Assert;
import org.junit.Test;
import com.Group3.MonitorClient.Messenger.GreedyMessenger;
import org.openapitools.client.model.TimingMonitorData;

public class GreedyMessenger_Test {

    /* tests the control functions of the GreedyMessenger, Start(), Stop(), Pause() and Resume() */
    @Test
    public void GreedyMessenger_test1() throws InterruptedException {
        SynchronizedQueue queue = new SynchronizedQueue();
        GreedyMessenger messenger = new GreedyMessenger_TestClass("1.1.1.1:8080", queue);

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
        Assert.assertTrue(SizeOfQueueBefore > queue.Size());

        Thread.sleep(500);
        /* confirms that the messenger thread is now dead */
        Assert.assertFalse(messenger.MessengerIsAlive());
    }

    /* Ensures the AddMonitorData() method works as intended */
    @Test
    public void GreedyMessenger_test2(){
        SynchronizedQueue queue = new SynchronizedQueue();
        GreedyMessenger messenger = new GreedyMessenger("1.1.1.1:8080", queue);

        messenger.AddMonitorData(new TimingMonitorData());

        Assert.assertEquals(1, queue.Size());
    }
}

