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
