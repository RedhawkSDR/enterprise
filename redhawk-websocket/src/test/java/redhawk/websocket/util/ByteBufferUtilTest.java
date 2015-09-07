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
