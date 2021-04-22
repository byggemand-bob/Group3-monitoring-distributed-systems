package com.group3.monitorServer.messageProcessor;

import com.group3.monitorServer.messages.ErrorDataMessage;
import com.group3.monitorServer.messages.SQLMessageManager;
import com.group3.monitorServer.messages.TimingMonitorDataMessage;
import com.group3.monitorServer.testClasses.AbstractSQLMessageManagerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.model.TimingMonitorData;

public class Delegator_Tests extends AbstractSQLMessageManagerTest {
    /* Tests the control functions start(), stop(), pause() and resume() */
    @Test
    public void ThreadControl() throws InterruptedException {
        //setup
        int loopCount;
        Delegator delegator = new Delegator(sqlMessageManager);

        AddMessages();

        //Act & Assert
        /* verifies there is five messages in the table, before the Delegator is started */
        Assertions.assertEquals(7L, sqlMessageManager.TableSize());

        delegator.start();

        loopCount = 0;
        while(sqlMessageManager.TableSize() != 0 && loopCount < 50){
            Thread.sleep(50);
            loopCount++;
        }

        delegator.pause();

        /* Tests that the Delegator processed all messages before continuing */
        Assertions.assertEquals(0, sqlMessageManager.TableSize());

        AddMessages();
        Thread.sleep(100);

        /* verifies the Delegator wasn't running while paused */
        Assertions.assertEquals(7L, sqlMessageManager.TableSize());

        delegator.resume();
        loopCount = 0;
        while(sqlMessageManager.TableSize() != 0 && loopCount < 50){
            Thread.sleep(50);
            loopCount++;
        }
        delegator.stop();

        /* tests the Delegator was running again after being unpaused */
        Assertions.assertEquals(0, sqlMessageManager.TableSize());

        Thread.sleep(100);

        /* verifies that the delegator thread was terminated after calling stop() */
        Assertions.assertFalse(delegator.isAlive());
    }

    private void AddMessages(){
        TimingMonitorDataMessage timingMonitorDataMessage = getDefaultTimingMessage();
        ErrorDataMessage errorDataMessage = getDefaultErrorMessage();

        //msg1
        errorDataMessage.getErrorData().setSenderID(1L);
        errorDataMessage.makeSQL(sqlMessageManager);

        //msg2
        timingMonitorDataMessage.getTimingMonitorData().setEventCode(TimingMonitorData.EventCodeEnum.RECEIVEREQUEST);
        timingMonitorDataMessage.makeSQL(sqlMessageManager);

        //msg3
        errorDataMessage.getErrorData().setSenderID(2L);
        errorDataMessage.makeSQL(sqlMessageManager);

        //msg4
        timingMonitorDataMessage.getTimingMonitorData().setEventCode(TimingMonitorData.EventCodeEnum.RECEIVERESPONSE);
        timingMonitorDataMessage.makeSQL(sqlMessageManager);

        //msg5
        errorDataMessage.getErrorData().setSenderID(3L);
        errorDataMessage.makeSQL(sqlMessageManager);

        //msg6
        timingMonitorDataMessage.getTimingMonitorData().setEventCode(TimingMonitorData.EventCodeEnum.SENDREQUEST);
        timingMonitorDataMessage.makeSQL(sqlMessageManager);

        //msg7
        timingMonitorDataMessage.getTimingMonitorData().setEventCode(TimingMonitorData.EventCodeEnum.SENDRESPONSE);
        timingMonitorDataMessage.makeSQL(sqlMessageManager);
    }
}
