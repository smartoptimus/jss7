/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement;

import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * 
 * 	UpdateLocationArg ::= SEQUENCE {
 *		imsi								IMSI,
 *		msc-Number					[1] 	ISDN-AddressString,
 *		vlr-Number							ISDN-AddressString,
 *		lmsi						[10] 	LMSI		OPTIONAL,
 *		extensionContainer					ExtensionContainer	OPTIONAL,
 *		... ,
 *		vlr-Capability				[6] 	VLR-Capability	OPTIONAL,
 *		informPreviousNetworkEntity	[11]	NULL		OPTIONAL,
 *		cs-LCS-NotSupportedByUE		[12]	NULL		OPTIONAL,
 *		v-gmlc-Address				[2]		GSN-Address	OPTIONAL,
 *		add-info					[13] 	ADD-Info	OPTIONAL,
 *		pagingArea					[14] 	PagingArea	OPTIONAL,
 *		skipSubscriberDataUpdate	[15] 	NULL		OPTIONAL, 
 *		-- The skipSubscriberDataUpdate parameter in the UpdateLocationArg and the ADD-Info
 *		-- structures carry the same semantic.
 *		restorationIndicator		[16]	NULL		OPTIONAL
 *		 }
 *
 * 
 * @author sergey vetyutnev
 * 
 */
public interface UpdateLocationRequest {

	public IMSI getImsi();

	public ISDNAddressString getMscNumber();

	public ISDNAddressString getVlrNumber();

	public LMSI getLmsi();

	public MAPExtensionContainer getExtensionContainer();

	public VlrCapability getVlrCapability();

	public boolean getInformPreviousNetworkEntity();

	public boolean getCsLCSNotSupportedByUE();

	public GSNAddress getVGmlcAddress();

	public ADDInfo getADDInfo();

	public PagingArea getPagingArea();

	public boolean getSkipSubscriberDataUpdate();

	public boolean getRestorationIndicator();

}
