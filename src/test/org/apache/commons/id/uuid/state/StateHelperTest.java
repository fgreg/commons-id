/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.id.uuid.state;

import junit.framework.TestCase;

import org.apache.commons.id.uuid.clock.Clock;
import org.apache.commons.id.uuid.clock.SystemClockImpl;
import org.apache.commons.id.uuid.clock.ThreadClockImpl;

import java.util.Arrays;

/**
 * Unit tests for {@link StateHelper}.
 *
 * @version $Revision: 480488 $ $Date: 2006-11-29 00:57:26 -0800 (Wed, 29 Nov 2006) $
 * @author Commons-Id team
 */
public class StateHelperTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <p>Test the randomNodeIdentifier method.</p>
     */
    public void testRandomNodeIdentifier() {
        byte[] nodeId = StateHelper.randomNodeIdentifier();
        // Check proper length
        assertTrue(nodeId.length == StateHelper.NODE_ID_BYTE_LENGTH);
        // Check multicast bit is set ( -1 since java is always signed and we shifted)
        assertEquals(-1, nodeId[0] >> 7);
    }

    /** Test newClockSequence method */
    public void testNewClockSequence() {
        short clockSq = StateHelper.newClockSequence();
        //Nothing to assert, it worked.
    }

    /** Test getClockImpl method */
    public void testGetClockImpl() {
        // Change the value for our tests only, then put it back.
        String currentClockImpl = System.getProperty(StateHelper.UUID_CLOCK_IMPL_PROPERTY_KEY);
        Clock ck = null;
        try {
            System.setProperty(StateHelper.UUID_CLOCK_IMPL_PROPERTY_KEY, "org.apache.commons.id.uuid.clock.SystemClockImpl");
            ck = StateHelper.getClockImpl();
            assertTrue(ck instanceof SystemClockImpl);
            System.setProperty(StateHelper.UUID_CLOCK_IMPL_PROPERTY_KEY, "org.apache.commons.id.uuid.clock.ThreadClockImpl");
            ck = StateHelper.getClockImpl();
            assertTrue(ck instanceof ThreadClockImpl);
            // Test native only if Windows OS
            if (System.getProperty("os.name").indexOf("Windows") != -1) {
                boolean runTest = true;
                try {
                    System.loadLibrary("JNativeWin32Clock");
                } catch (UnsatisfiedLinkError usle) {
                    runTest = false;
                }
                if (runTest) {
                    //System.out.println("Running Native Test.");
                    System.setProperty(StateHelper.UUID_CLOCK_IMPL_PROPERTY_KEY,
                        "org.apache.commons.id.uuid.clock.SystemClockImpl");
                    ck = StateHelper.getClockImpl();
                    assertTrue(ck instanceof SystemClockImpl);
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            // Make sure to put it back.
            if (currentClockImpl != null) {
                System.setProperty(StateHelper.UUID_CLOCK_IMPL_PROPERTY_KEY, currentClockImpl);
            }
        }
    }

    /** Test getStateImpl method */
    public void testGetStateImpl() {
        // Change the value for our tests only, then put it back.
        String currentStateImpl = System.getProperty(StateHelper.UUID_STATE_IMPL_PROPERTY_KEY);
        State st = null;
        try {
            System.setProperty(StateHelper.UUID_STATE_IMPL_PROPERTY_KEY, "org.apache.commons.id.uuid.state.InMemoryStateImpl");
            st = StateHelper.getStateImpl();
            assertTrue(st instanceof InMemoryStateImpl);
            System.setProperty(StateHelper.UUID_STATE_IMPL_PROPERTY_KEY,
                "org.apache.commons.id.uuid.state.ReadOnlyResourceStateImpl");
            st = StateHelper.getStateImpl();
            assertTrue(st instanceof ReadOnlyResourceStateImpl);
            System.setProperty(StateHelper.UUID_STATE_IMPL_PROPERTY_KEY, "org.apache.commons.id.uuid.state.ReadWriteFileStateImpl");
            st = StateHelper.getStateImpl();
            assertTrue(st instanceof ReadWriteFileStateImpl);
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            // Make sure to put it back.
            if (currentStateImpl != null) {
                System.setProperty(StateHelper.UUID_CLOCK_IMPL_PROPERTY_KEY, currentStateImpl);
            }
        }
    }

    /** Test the decodeMACAddress method. */
    public void testDecodeMACAddress() {
        // Test MAC address 02-00-4C-4F-4F-50
        byte[] macAsBytes = {0x02, 0x00, 0x4C, 0x4F, 0x4F, 0x50};
        assertTrue(Arrays.equals(macAsBytes, StateHelper.decodeMACAddress("02-00-4C-4F-4F-50")));
    }

    /**
     * <p>Test the encodeMACAddress method.</p>
     * @throws Exception a test Exception.
     */
    public void testEncodeMACAddress() throws Exception {
        // Test MAC address 02-00-4C-4F-4F-50
        byte[] macAsBytes = {0x02, 0x00, 0x4C, 0x4F, 0x4F, 0x50};
        assertEquals("02-00-4C-4F-4F-50", StateHelper.encodeMACAddress(macAsBytes));
    }

}
