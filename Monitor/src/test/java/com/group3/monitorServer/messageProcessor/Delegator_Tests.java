package com.group3.monitorServer.messageProcessor;

import com.group3.monitorServer.testClasses.AbstractSQLMessageManagerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Delegator_Tests extends AbstractSQLMessageManagerTest {
    /* Tests the control functions start(), stop(), pause() and resume() */
    @Test
    public void ThreadControl() throws InterruptedException {
        //setup
        Delegator delegator = new Delegator(sqlMessageManager);

        for(int i = 0; i < 5; i++){
            getDefaultTimingMessage().makeSQL(sqlMessageManager);
        }

        //Act & Assert
        /* verifies there is five messages in the table, before the Delegator is started */
        Assertions.assertEquals(5L, sqlMessageManager.TableSize());

        delegator.start();
        Thread.sleep(500);
        delegator.pause();

        /* Tests that the Delegator was processing messages after being started */
        Assertions.assertTrue(sqlMessageManager.TableSize() < 5);

        for(long i = sqlMessageManager.TableSize(); i < 5; i++){
            getDefaultTimingMessage().makeSQL(sqlMessageManager);
        }
        Thread.sleep(500);

        /* verifies the Delegator wasn't running while paused */
        Assertions.assertEquals(5L, sqlMessageManager.TableSize());

        delegator.resume();
        Thread.sleep(500);
        delegator.stop();

        /* tests the Delegator was running again after being unpaused */
        Assertions.assertTrue(sqlMessageManager.TableSize() < 5);

        Thread.sleep(500);

        /* verifies that the delegator thread was terminated after calling stop() */
        Assertions.assertTrue(delegator.isAlive());
    }
}
