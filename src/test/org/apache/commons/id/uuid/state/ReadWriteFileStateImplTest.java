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

/**
 * Unit tests for {@link ReadWriteFileStateImpl}.
 *
 * @version $Revision: 480488 $ $Date: 2006-11-29 00:57:26 -0800 (Wed, 29 Nov 2006) $
 * @author Commons-Id team
 */
public class ReadWriteFileStateImplTest extends TestCase {

    /** Pre test value for ReadOnlyResourceStateImpl.CONFIG_FILE_KEY */
    private String currentConfigFile;

    protected void setUp() throws Exception {
        super.setUp();
        currentConfigFile = System.getProperty(ReadWriteFileStateImpl.CONFIG_FILENAME_KEY);
    }

    protected void tearDown() throws Exception {
        if (currentConfigFile != null) {
            System.setProperty(ReadWriteFileStateImpl.CONFIG_FILENAME_KEY, currentConfigFile);
        }
        super.tearDown();
    }

    /**
     * <p>Tests the void store method.</p>
     * @throws Exception a testing Exception.
     */
    public void testStore() throws Exception {
        System.setProperty(ReadWriteFileStateImpl.CONFIG_FILENAME_KEY, "uuid1.state");
        ReadWriteFileStateImpl impl = new ReadWriteFileStateImpl();
        impl.load();
        impl.store(impl.getNodes());
    }

}
