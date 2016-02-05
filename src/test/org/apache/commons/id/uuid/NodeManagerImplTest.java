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

package org.apache.commons.id.uuid;

import junit.framework.TestCase;

import org.apache.commons.id.uuid.state.Node;
import org.apache.commons.id.uuid.state.ReadOnlyResourceStateImpl;

/**
 * Unit tests for {@link NodeManagerImpl}.
 *
 * @version $Revision: 480488 $ $Date: 2006-11-29 00:57:26 -0800 (Wed, 29 Nov 2006) $
 * @author Commons-id team
 */
public class NodeManagerImplTest extends TestCase {

    /** Pre test value for ReadOnlyResourceStateImpl.CONFIG_FILE_KEY */
    private String currentConfigFile;
    
    protected void setUp() throws Exception {
        super.setUp();
        currentConfigFile = System.getProperty(
                ReadOnlyResourceStateImpl.CONFIG_FILENAME_KEY);
    }

    protected void tearDown() throws Exception {
        if (currentConfigFile != null) {
            System.setProperty(
                    ReadOnlyResourceStateImpl.CONFIG_FILENAME_KEY,
                    currentConfigFile);
        }
        super.tearDown();
    }

    public void testInit() throws Exception {
        System.setProperty(ReadOnlyResourceStateImpl.CONFIG_FILENAME_KEY,
                "uuid1.state");  // sets up 2 different nodes
        NodeManagerImpl nodeManager = new NodeManagerImpl();
        nodeManager.init();
        Node node1 = nodeManager.currentNode();
        assertNotNull(node1);
        Node node2 = nodeManager.nextAvailableNode();
        assertNotNull(node2);
        assertFalse(node1.equals(node2));
        node2 = nodeManager.nextAvailableNode();  // should wrap back to node 1
        assertEquals(node1, node2);
    }

}
