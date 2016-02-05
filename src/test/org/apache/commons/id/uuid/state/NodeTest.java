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

import java.util.Arrays;

/**
 * Unit tests for {@link Node}.
 *
 * @version $Revision: 480488 $ $Date: 2006-11-29 00:57:26 -0800 (Wed, 29 Nov 2006) $
 * @author Commons-Id team
 */
public class NodeTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <p>Tests the Node hashCode contract - for same id return same hashCode.</p>
     */
    public void testHashCode() {
        Node one = new Node(StateHelper.decodeMACAddress("02-00-4C-4F-4F-50"));
        Node two = new Node(StateHelper.decodeMACAddress("02-00-4C-4F-4F-50"));
        // Assert hashCodes equal on same values
        assertEquals(one.hashCode(), two.hashCode());
        // Assert hashCode different on different values
        two = new Node(StateHelper.decodeMACAddress("02-00-4C-4F-4F-55"));
        assertTrue(two.hashCode() != one.hashCode());
    }

    /**
     * <p>Test for constructor Node(byte[])</p>
     *
     * @throws Exception a test exception.
     */
    public void testNodebyteArray() throws Exception {
        byte[] bytz = StateHelper.decodeMACAddress("02-00-4C-4F-4F-50");
        Node test = new Node(bytz);
        assertTrue(Arrays.equals(bytz, test.getNodeIdentifier()));
        assertTrue(test.getClockSequence() != 0);
        long last = test.getUUIDTime();
        assertEquals(last, test.getLastTimestamp());
    }

    /**
     * Test for constructor Node(byte[], long, short)
     */
    public void testNodebyteArraylongshort() {
        byte[] bytz = StateHelper.decodeMACAddress("02-00-4C-4F-4F-50");
        Node test = new Node(bytz, 10L, (short) 20);
        assertTrue(Arrays.equals(bytz, test.getNodeIdentifier()));
        assertEquals(test.getClockSequence(), 20);
        assertEquals(test.getLastTimestamp(), 10L);
    }

    /**
     * Test for byte[] getNodeIdentifier
     */
    public void testGetNodeIdentifier() {
        byte[] bytz = StateHelper.decodeMACAddress("02-00-4C-4F-4F-50");
        Node test = new Node(bytz, 10L, (short) 20);
        assertTrue(Arrays.equals(bytz, test.getNodeIdentifier()));
    }

    /**
     * <p>Test for short getClockSequence.</p>
     */
    public void testGetClockSequence() {
        byte[] bytz = StateHelper.decodeMACAddress("02-00-4C-4F-4F-50");
        Node test = new Node(bytz, 10L, (short) 20);
        assertEquals(20, test.getClockSequence());
    }

    /**
     * Test for long getUUIDTime
     *
     * @throws Exception a test exception.
     */
    public void testGetUUIDTime() throws Exception {
        byte[] bytz = StateHelper.decodeMACAddress("02-00-4C-4F-4F-50");
        Node test = new Node(bytz, 10L, (short) 20);
        assertTrue(132962443266870000L < test.getUUIDTime());
    }

    /**
     * Test for boolean equals(Object)
     */
    public void testEqualsObject() {
        byte[] bytz = StateHelper.decodeMACAddress("02-00-4C-4F-4F-50");
        byte[] byts = StateHelper.decodeMACAddress("02-00-4C-4F-4F-50");
        Node one = new Node(bytz);
        Node two = new Node(byts);
        assertTrue(one.equals(two));
        assertTrue(two.equals(one));
        two = new Node(StateHelper.decodeMACAddress("02-00-4C-4F-4F-55"));
        assertFalse(two.equals(one));
    }

    /**
     * Test for getLastTimestamp.
     *
     * @throws Exception a test exception.
     */
    public void testGetLastTimestamp() throws Exception {
        byte[] bytz = StateHelper.decodeMACAddress("02-00-4C-4F-4F-50");
        Node test = new Node(bytz);
        long last = test.getUUIDTime();
        assertEquals(last, test.getLastTimestamp());
    }
}
