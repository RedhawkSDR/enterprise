/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK __REDHAWK_PROJECT__.
 *
 * REDHAWK __REDHAWK_PROJECT__ is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK __REDHAWK_PROJECT__ is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package redhawk.impl;

import java.util.Date;

import org.junit.Test;

import BULKIO.PrecisionUTCTime;
import BULKIO.PrecisionUTCTimeHelper;

public class PrecisionUTCTimeTest{
	/*
	 * 	TCMode: 1
		TCStatus: 1
		TOFF: 0.0
		TWSec: 1.431378034E9
		TFSec: 0.8106180667877196
		{streamId=SigGen Stream, endOfStream=false, yunits=0, tcmode=1, ystart=0.0, tfsec=0.7129289627075195, 
		toff=0.0, subsize=0, mode=0, newSri=false, ydelta=0.0, twsec=1.431439997E9, xdelta=2.0E-4, blocking=false, 
		xstart=0.0, xunits=1, tcstatus=1, hversion=1}
	 */
	@Test
	public void test(){
		System.out.println(PrecisionUTCTimeHelper.type());
		
		PrecisionUTCTime time = new PrecisionUTCTime((short)1, (short)1, 0.0, new Date().getTime(), 0.0);
		System.out.println(time.twsec);
		System.out.println((double)new Date().getTime());
	}
}
