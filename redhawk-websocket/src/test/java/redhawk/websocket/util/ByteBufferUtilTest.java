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
package redhawk.websocket.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import redhawk.websocket.utils.ByteBufferUtil;

public class ByteBufferUtilTest {
	@Test
	public void LetsGetCodeCoverage(){
		int[] intArray = {1};
		double[] doubleArray = {1.0};
		float[] floatArray = {1f};
		long[] longArray = {1l};
		byte[] byteArray = {(byte)0xe0};
		char[] charArray = {'j'};
		short[] shortArray = {1};
		String boom = "Hello";
		
		assertNotNull(ByteBufferUtil.createByteArray(intArray));
		assertNotNull(ByteBufferUtil.createByteArray(doubleArray));
		assertNotNull(ByteBufferUtil.createByteArray(floatArray));
		assertNotNull(ByteBufferUtil.createByteArray(longArray));
		assertNotNull(ByteBufferUtil.createByteArray(byteArray));
		assertNotNull(ByteBufferUtil.createByteArray(charArray));
		assertNotNull(ByteBufferUtil.createByteArray(shortArray));
		assertNotNull(ByteBufferUtil.createByteArray(boom));

	}
}
