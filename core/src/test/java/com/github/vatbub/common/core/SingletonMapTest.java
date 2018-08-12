package com.github.vatbub.common.core;

/*-
 * #%L
 * FOKProjects Common Core
 * %%
 * Copyright (C) 2016 - 2017 Frederik Kammel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.github.vatbub.common.core.logging.FOKLogger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Level;

public class SingletonMapTest extends CoreBaseTestClass {

    @Before
    public void setUp() {
        SingletonMap.resetSingletonMap();
        TestDummyClass.setCallCount(0);
    }

    @Test
    public void basicSingletonMapTest() throws InstantiationException, IllegalAccessException {
        TestDummyClass instance1 = SingletonMap.getInstance(TestDummyClass.class);
        TestDummyClass instance2 = SingletonMap.getInstance(TestDummyClass.class);
        Assert.assertEquals(1, TestDummyClass.getCallCount());
        Assert.assertTrue(instance1 == instance2);
    }

    @Test
    public void resetInstanceTest() throws InstantiationException, IllegalAccessException {
        TestDummyClass instance1 = SingletonMap.getInstance(TestDummyClass.class);
        SingletonMap.resetInstance(TestDummyClass.class);
        TestDummyClass instance2 = SingletonMap.getInstance(TestDummyClass.class);
        Assert.assertEquals(2, TestDummyClass.getCallCount());
        Assert.assertTrue(instance1 != instance2);
    }

    @Test
    public void resetSingletonMapTest() throws InstantiationException, IllegalAccessException {
        TestDummyClass instance1 = SingletonMap.getInstance(TestDummyClass.class);
        SingletonMap.resetSingletonMap();
        TestDummyClass instance2 = SingletonMap.getInstance(TestDummyClass.class);
        Assert.assertEquals(2, TestDummyClass.getCallCount());
        Assert.assertTrue(instance1 != instance2);
    }

    @Test
    public void throwIllegalStateExceptionIfAlreadyInstantiatedTest() throws InstantiationException, IllegalAccessException {
        // should pass
        SingletonMap.throwIllegalStateExceptionIfAlreadyInstantiated(TestDummyClass.class);

        // Create instance
        SingletonMap.getInstance(TestDummyClass.class);

        try {
            // Should throw an exception
            SingletonMap.throwIllegalStateExceptionIfAlreadyInstantiated(TestDummyClass.class);
            Assert.fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            FOKLogger.log(SingletonMapTest.class.getName(), Level.INFO, "Expected exception occurred", e);
        }
    }

    private static class TestDummyClass {
        private static int callCount = 0;

        public TestDummyClass() {
            callCount++;
        }

        public static int getCallCount() {
            return callCount;
        }

        public static void setCallCount(int callCount) {
            TestDummyClass.callCount = callCount;
        }
    }
}
