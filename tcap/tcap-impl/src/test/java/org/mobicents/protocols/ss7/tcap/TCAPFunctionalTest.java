/*
 * TeleStax, Open Source Cloud Communications  
 * Copyright 2012, Telestax Inc and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.tcap;



import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test for call flow.
 * @author baranowb
 *
 */
public class TCAPFunctionalTest extends SccpHarness {
	public static final long WAIT_TIME = 500;
	private static final int _WAIT_TIMEOUT = 90000;
	private static final int _WAIT_REMOVE = 30000;
    public static final long[] _ACN_ = new long[]{0, 4, 0, 0, 1, 0, 19, 2};
    private TCAPStackImpl tcapStack1;
    private TCAPStackImpl tcapStack2;
    private SccpAddress peer1Address;
    private SccpAddress peer2Address;
    private Client client;
    private Server server;
    
    public TCAPFunctionalTest(){
    	
    }
    
	@BeforeClass
	public void setUpClass() {
		this.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
		this.sccpStack2Name = "TCAPFunctionalTestSccpStack2";
		System.out.println("setUpClass");
	}

	@AfterClass
	public void tearDownClass() throws Exception {
		System.out.println("tearDownClass");
	}

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
	@BeforeMethod
	public void setUp() throws Exception {
		System.out.println("setUp");
        super.setUp();
       
        peer1Address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 1, null, 8);
        peer2Address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 2,  null, 8);
        
        this.tcapStack1 = new TCAPStackImpl(this.sccpProvider1, 8);
        this.tcapStack2 = new TCAPStackImpl(this.sccpProvider2, 8);
        
        this.tcapStack1.setInvokeTimeout(0);
        this.tcapStack2.setInvokeTimeout(0);
        
       
        this.tcapStack1.start();
        this.tcapStack2.start();
        //create test classes
        this.client = new Client(this.tcapStack1, peer1Address, peer2Address);
        this.server = new Server(this.tcapStack2, peer2Address, peer1Address);

    }
    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
	@AfterMethod
	public void tearDown() {
        this.tcapStack1.stop();
        this.tcapStack2.stop();
        super.tearDown();

    }

	@Test(groups = { "functional.flow"})
    public void simpleTCWithDialogTest() throws Exception{
    	
        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0,stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, 1,stamp+WAIT_TIME);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.End, null, 2,stamp+WAIT_TIME*2);
        clientExpectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3,stamp+WAIT_TIME*2+_WAIT_REMOVE);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3,stamp+WAIT_TIME*2);
        clientExpectedEvents.add(te);
        
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0,stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, 1,stamp+WAIT_TIME);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.End, null, 2,stamp+WAIT_TIME*2);
        serverExpectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3,stamp+WAIT_TIME*2+_WAIT_REMOVE);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3,stamp+WAIT_TIME*2);
        serverExpectedEvents.add(te);

    	client.startClientDialog();
    	assertNotNull(client.dialog.getLocalAddress());
    	assertNull(client.dialog.getRemoteDialogId());
    	
        client.sendBegin();
        client.waitFor(WAIT_TIME);
        
        server.sendContinue();
        assertNotNull(server.dialog.getLocalAddress());
    	assertNotNull(server.dialog.getRemoteDialogId());
    	
        client.waitFor(WAIT_TIME);
        client.sendEnd(TerminationType.Basic);
    	assertNotNull(client.dialog.getLocalAddress());
    	assertNotNull(client.dialog.getRemoteDialogId());
    	
        client.waitFor(WAIT_TIME);
//        waitForEnd();

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

	@Test(groups = { "functional.flow"})
    public void uniMsgTest() throws Exception{

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Uni, null, 0, stamp);
		clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Uni, null, 0,stamp);
        serverExpectedEvents.add(te);

    	client.startUniDialog();
        client.sendUni();
        client.waitFor(WAIT_TIME);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    private void waitForEnd() {
        try {
            Thread.currentThread().sleep(_WAIT_TIMEOUT);
        } catch (InterruptedException e) {
            fail("Interrupted on wait!");
        }
    }
}
