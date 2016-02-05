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

import org.apache.commons.id.Hex;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Unit tests for {@link UUID}.
 *
 * @version $Revision: 480488 $ $Date: 2006-11-29 00:57:26 -0800 (Wed, 29 Nov 2006) $
 * @author Commons-id team
 */
public class UUIDTest extends TestCase {

    //-------------------------------------------------------------------------
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Test the nil UUID() contructor returns a zero'd out UUID
     *
     * @throws Exception an exception while testing
     */
    public void testNiltoString() throws Exception {
        assertEquals(
            (new UUID()).toString(),
            "00000000-0000-0000-0000-000000000000");
    }

    /**
     * Test copy constructor
     *
     *  @throws Exception an exception while testing
     */
    public void testCopyConstructor() throws Exception {
        UUID uuidFrom = new UUID("B4F00409-CEF8-4822-802C-DEB20704C365");
        UUID uuidTo = new UUID(uuidFrom);

        //Assert all equals and output methods match
        assertTrue(uuidFrom.equals(uuidTo));
        assertTrue(uuidTo.equals(uuidFrom));
        assertEquals(uuidTo.toString(), uuidFrom.toString());
        assertTrue(Bytes.areEqual(uuidTo.getRawBytes(), uuidFrom.getRawBytes()));
        assertEquals(uuidTo.hashCode(), uuidFrom.hashCode());
    }

    /**
     * Test byte[] constructor
     *
     *  @throws Exception an exception while testing
     */
    public void testByteArrayConstructor() throws Exception {
        String uuidString = "B4F00409CEF84822802CDEB20704C365".toLowerCase();
        String uuidFString = "B4F00409-CEF8-4822-802C-DEB20704C365".toLowerCase();
        byte[] bytes = Hex.decodeHex(uuidString.toCharArray());
        UUID uuid = new UUID(bytes);

        //Assert all equals and output methods match
        assertTrue(uuid.equals(new UUID(uuidFString)));
        assertEquals(uuidFString, uuid.toString());
        assertTrue(Bytes.areEqual(bytes, uuid.getRawBytes()));
    }

    /**
     * Test DataInput constructor
     *
     *  @throws Exception an exception while testing
     */
    public void testDataInputConstructor() throws Exception {
        String uuidString = "B4F00409CEF84822802CDEB20704C365".toLowerCase();
        String uuidFString = "B4F00409-CEF8-4822-802C-DEB20704C365".toLowerCase();
        byte[] bytes = Hex.decodeHex(uuidString.toCharArray());
        //Set DataInput
        ByteArrayOutputStream bytz = new ByteArrayOutputStream(Constants.UUID_BYTE_LENGTH);
        DataOutputStream dos = new DataOutputStream(bytz);
        dos.write(bytes);
        ByteArrayInputStream bas = new ByteArrayInputStream(bytz.toByteArray());
        DataInput di = new DataInputStream(bas);

        UUID uuid = new UUID(di);

        //Assert all equals and output methods match
        assertTrue(uuid.equals(new UUID(uuidFString)));
        assertEquals(uuidFString, uuid.toString());
        assertTrue(Bytes.areEqual(bytes, uuid.getRawBytes()));

        //Close all
        dos.close();
        bas.close();

        //Test a too short DataInput
        byte[] sBytes = Hex.decodeHex(uuidString.substring(2).toCharArray());
        //Set DataInput
        bytz = new ByteArrayOutputStream();
        dos = new DataOutputStream(bytz);
        dos.write(sBytes);
        bas = new ByteArrayInputStream(bytz.toByteArray());
        di = new DataInputStream(bas);

        try {
            UUID sUuid = new UUID(di);
            fail();
        } catch (IOException ioe) {
            //Expected
        }

        //Close all
        dos.close();
        bas.close();

    }

    /**
     * Test long long constructor
     *
     *  @throws Exception an exception while testing
     */
    public void testLongLongConstructor() throws Exception {
        String uuidFString = "B4F00409-CEF8-4822-802C-DEB20704C365".toLowerCase();
        long mostSig = -5408818712298371038L;
        long leastSig = -9210742281676209307L;

        UUID uuid = new UUID(mostSig, leastSig);

        //Assert all equals and output methods match
        assertTrue(uuid.equals(new UUID(uuidFString)));
        assertEquals(uuidFString, uuid.toString());
    }

    /**
     * Test String constructor
     *
     *  @throws Exception an exception while testing
     */
    public void testStringConstructor() throws Exception {
        UUID uuidFrom = new UUID("B4F00409-CEF8-4822-802C-DEB20704C365");
        UUID uuidTo = new UUID(uuidFrom.getRawBytes());

        //Assert all equals and output methods match
        assertTrue(uuidFrom.equals(uuidTo));
        assertTrue(uuidTo.equals(uuidFrom));
        assertEquals(uuidTo.toString(), uuidFrom.toString());
        assertTrue(Bytes.areEqual(uuidTo.getRawBytes(), uuidFrom.getRawBytes()));
        assertEquals(uuidTo.hashCode(), uuidFrom.hashCode());

        //Test constructing prefixed strings
        UUID fromPrefixed =
            new UUID("urn:uuid:B4F00409-CEF8-4822-802C-DEB20704C365");
        assertTrue(uuidFrom.equals(fromPrefixed));

        //Test constructing prefixed strings
        UUID fromShortPrefixed =
            new UUID("uuid:B4F00409-CEF8-4822-802C-DEB20704C365");
        assertTrue(uuidFrom.equals(fromPrefixed));

        //Test IllegalArgumentException in Construction
        UUID badConstruction = null;

        //try with not valid hexidecimal
        try {
            badConstruction = new UUID("G4F00409-CEF8-4822-802C-DEB20704C365");
            fail("Expecting UUIDFormatException -- invalid hex string");
        } catch (UUIDFormatException iea) {
            //Expected
        }

        //try with too long a string
        try {
            badConstruction = new UUID("FF4F00409-CEF8-4822-802C-DEB20704C365");
            fail("Expecting UUIDFormatException -- string too long");
        } catch (UUIDFormatException iea) {
            //Expected
        }

        //try with too short a string
        try {
            badConstruction = new UUID("4F00409-CEF8-4822-802C-DEB20704C365");
            fail("Expecting UUIDFormatException -- string too short");
        } catch (UUIDFormatException iea) {
            //Expected
        }

        //try right string wrong order / format
        try {
            badConstruction = new UUID("F4F00409-CEF8-4822-802CD-EB20704C365");
            fail("Expecting UUIDFormatException -- wrong format");
        } catch (UUIDFormatException iea) {
            //Expected
        }
    }

    /**
     * Test the static fromString method
     *
     *  @throws Exception an exception while testing
     */
    public void testFromString() throws Exception {
        UUID baseline = new UUID("B4F00409-CEF8-4822-802C-DEB20704C365");
        assertEquals(
            baseline,
            UUID.fromString("urn:uuid:B4F00409-CEF8-4822-802C-DEB20704C365"));
        assertEquals(
            baseline,
            UUID.fromString("uuid:B4F00409-CEF8-4822-802C-DEB20704C365"));
        //High value test
        String in = "FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF";
        UUID high = UUID.fromString(in);
        assertTrue(high.toString().equals(in.toLowerCase()));

        //Negative testing
        //try with not valid hexidecimal
        UUID bad = null;
        try {
            bad = UUID.fromString("G4F00409-CEF8-4822-802C-DEB20704C365");
            fail("Expecting UUIDFormatException -- invalid hex");
        } catch (UUIDFormatException iea) {
            //Expected
        }

        //try with too long a string
        try {
            bad = UUID.fromString("FF4F00409-CEF8-4822-802C-DEB20704C365");
            fail("Expecting UUIDFormatException -- string too long");
        } catch (UUIDFormatException iea) {
            //Expected
        }

        //try with too short a string
        try {
            bad = UUID.fromString("4F00409-CEF8-4822-802C-DEB20704C365");
            fail("Expecting UUIDFormatException -- string too short");
        } catch (UUIDFormatException iea) {
            //Expected
        }

        //try right string wrong order / format
        try {
            bad = UUID.fromString("F4F00409-CEF8-4822-802CD-EB20704C365");
            fail("Expecting UUIDFormatException -- wrong format");
        } catch (UUIDFormatException iea) {
            //Expected
        }
    }


    /**
     * Test the static nameUUIDFromString(String name, UUID namespaceUUID) method.
     *
     *  @throws Exception an exception while testing
     */
    public void testNameUUIDFromString() throws Exception {
    	//UUID assigned to URL Namespace  
    	UUID ns = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");
    	//UUID assigned to ISO OID  
    	UUID nsAlt = UUID.fromString("6ba7b812-9dad-11d1-80b4-00c04fd430c8");
    	
    	String name = "www.apache.org";
    	String nameAlt = "people.apache.org";
    	UUID test1 = UUID.nameUUIDFromString(name, ns);

    	assertEquals(Constants.VARIANT_IETF_DRAFT, test1.variant());
    	assertEquals(Constants.VERSION_THREE, test1.version());

    	//Assert not same - same name from different namespace
    	UUID test2 = UUID.nameUUIDFromString(name, nsAlt);
    	assertTrue(!test2.equals(test1));
    	//Assert not same - same namespace different names
    	UUID test3 = UUID.nameUUIDFromString(nameAlt, ns);
    	assertTrue(!test3.equals(test1));
    	//Assert equals different UUID instance from same name, namespace
    	UUID test4 = UUID.nameUUIDFromString(name, ns);
    	assertTrue(test4.equals(test1));
    	
    	//Sample output from IETF sample code
    	UUID known = UUID.nameUUIDFromString("www.widgets.com", ns);
    	assertEquals("3d813cbb-47fb-32ba-91df-831e1593ac29", known.toString());
    }

    /**
     * Test the static #{link UUID.nameUUIDFromString} method, with explicit SHA-1 encoding
     * as specified in version 4 of the UUID draft.
     */
    public void testNameUUIDFromStringSha1() throws Exception {
    	//UUID assigned to URL Namespace  
    	UUID ns = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");
    	//UUID assigned to ISO OID  
    	UUID nsAlt = UUID.fromString("6ba7b812-9dad-11d1-80b4-00c04fd430c8");
    	
    	String name = "www.apache.org";
    	String nameAlt = "people.apache.org";
    	UUID test1 = UUID.nameUUIDFromString(name, ns, UUID.SHA1_ENCODING);

    	assertEquals(Constants.VARIANT_IETF_DRAFT, test1.variant());
    	assertEquals(Constants.VERSION_FIVE, test1.version());

    	//Assert not same - same name from different namespace
    	UUID test2 = UUID.nameUUIDFromString(name, nsAlt, UUID.SHA1_ENCODING);
    	assertTrue(!test2.equals(test1));
    	//Assert not same - same namespace different names
    	UUID test3 = UUID.nameUUIDFromString(nameAlt, ns, UUID.SHA1_ENCODING);
    	assertTrue(!test3.equals(test1));
    	//Assert equals different UUID instance from same name, namespace
    	UUID test4 = UUID.nameUUIDFromString(name, ns, UUID.SHA1_ENCODING);
    	assertTrue(test4.equals(test1));
		    
    }

    
    /**
     * Test the toString of UUID
     *
     *  @throws Exception an exception while testing
     */
    public void testToString() throws Exception {
        assertEquals(
            (new UUID("f81d4fae-7dec-11d0-a765-00a0c91e6bf6")).toString(),
            "f81d4fae-7dec-11d0-a765-00a0c91e6bf6");
        assertEquals(
            (new UUID("00000000-7dec-11d0-a765-00a0c91e6bf6")).toString(),
            "00000000-7dec-11d0-a765-00a0c91e6bf6");
    }

    /**
     * Test the toUrn method
     *
     *  @throws Exception an exception while testing
     */
    public void testToUrn() throws Exception {
        assertEquals(
            (new UUID("f81d4fae-7dec-11d0-a765-00a0c91e6bf6")).toUrn(),
            "urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6");
    }

    /**
     * <p>Test compareTo of Comparable interface impl.</p>
     *
     * @throws Exception a testing Exception.
     */
    public void testCompareTo() throws Exception {
        UUID baseline = new UUID("ffffffff-ffff-ffef-ffff-ffffffffffff");
        UUID less =     new UUID("ffffffff-ffff-ffdf-ffff-ffffffffffff");
        UUID same =     new UUID("ffffffff-ffff-ffef-ffff-ffffffffffff");
        UUID more =     new UUID("ffffffff-ffff-ffff-ffff-ffffffffffff");
        assertEquals(+1, baseline.compareTo(less));
        assertEquals(0, baseline.compareTo(same));
        assertEquals(-1, baseline.compareTo(more));
    }

    /**
     * <p>Test the clockSequence() method.</p>
     * @throws Exception a testing Exception.
     */
    public void testClockSequence() throws Exception {
        //Test against known value
        UUID test = new UUID("c079ef59-f5b1-1801-a348-c38429e61be7");
        short val = 9032;
        assertEquals(val, test.clockSequence());
    }

    /**
     * <p>Test the version method.</p>
     *
     * @throws Exception a testing Exception.
     */
    public void testVersion() throws Exception {
        UUID v1 = new UUID("3051a8d7-aea7-1801-e0bf-bc539dd60cf3"); //Version one   0x18 = 0001 1000
        UUID v2 = new UUID("3051a8d7-aea7-2801-e0bf-bc539dd60cf3"); //Version two   0x28 = 0010 1000
        UUID v3 = new UUID("3051a8d7-aea7-3801-e0bf-bc539dd60cf3"); //Version three 0x38 = 0011 1000
        UUID v4 = new UUID("3051a8d7-aea7-4801-e0bf-bc539dd60cf3"); //Version four  0x48 = 0100 1000
	UUID v5 = new UUID("3051a8d7-aea7-3801-e0bf-bc539dd60cf3"); //Version five  0x38 = 0011 1000
        assertEquals(UUID.VERSION_ONE, v1.version());
        assertEquals(UUID.VERSION_TWO, v2.version());
        assertEquals(UUID.VERSION_THREE, v3.version());
        assertEquals(UUID.VERSION_FOUR, v4.version());
	assertEquals(UUID.VERSION_FIVE, v5.version());
    }

    /**
     * <p>Test the variant method.</p>
     *
     * @throws Exception a testing Exception.
     */
    public void testVariant() throws Exception {
        UUID testVariant0 = new UUID("d0e817e1-e4b1-1801-3fe6-b4b60ccecf9d");
        UUID testVariant2 = new UUID("d0e817e1-e4b1-1801-bfe6-b4b60ccecf9d");
        UUID testVariant6 = new UUID("d0e817e1-e4b1-1801-dfe6-b4b60ccecf9d");
        UUID testVariant7 = new UUID("d0e817e1-e4b1-1801-ffe6-b4b60ccecf9d");

        assertEquals(UUID.VARIANT_NCS_COMPAT, testVariant0.variant());
        assertEquals(UUID.VARIANT_IETF_DRAFT, testVariant2.variant());
        assertEquals(UUID.VARIANT_MS, testVariant6.variant());
        assertEquals(UUID.VARIANT_FUTURE, testVariant7.variant());
    }

    /**
     * <p>Test the timestamp method.</p>
     *
     * @throws Exception a testing Exception.
     */
    public void testTimestamp() throws Exception {
        //Test against known value
        UUID test = new UUID("f8636b90-b207-11d8-b231-e33c9df047ca");
        long val = 133051936309210000L;
        assertEquals(val, test.timestamp());
    }

    /**
     * <p>Test the node method.</p>
     *
     * @throws Exception a testing Exception.
     */
    public void testNode() throws Exception {
        //Test against known value
        UUID test = new UUID("547fa190-b209-11d8-bc4e-95ef8f69921e");
        long val = 164856135782942L;
        assertEquals(val, test.node());
    }
}
