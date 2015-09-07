package redhawk.websocket.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteBufferUtil {
	/*
	 * TODO: This should be a helper in RedhawkDriver
	 */
    /**
     * Creates bytebuffer from incoming port data. 
     * @param data
     * @return
     */
    public static ByteBuffer createByteArray(Object data) {
        ByteBuffer b = null;
        if (data instanceof int[]) {
            int[] obj = (int[]) data;
            byte[] buffer = new byte[obj.length * Integer.SIZE / Byte.SIZE];
            b = ByteBuffer.wrap(buffer);
            b.order(ByteOrder.LITTLE_ENDIAN);
            for (int s : obj) {
                b.putInt(s);
            }
        } else if (data instanceof short[]) {
            short[] obj = (short[]) data;
            byte[] buffer = new byte[obj.length * Short.SIZE / Byte.SIZE];
            b = ByteBuffer.wrap(buffer);
            b.order(ByteOrder.LITTLE_ENDIAN);
            for (short s : obj) {
                b.putShort(s);
            }
        } else if (data instanceof char[]) {
            char[] obj = (char[]) data;
            byte[] buffer = new byte[obj.length * Character.SIZE / Byte.SIZE];
            b = ByteBuffer.wrap(buffer);
            b.order(ByteOrder.LITTLE_ENDIAN);
            for (char s : obj) {
                b.putChar(s);
            }
        } else if (data instanceof float[]) {
            float[] obj = (float[]) data;
            byte[] buffer = new byte[obj.length * Float.SIZE / Byte.SIZE];
            b = ByteBuffer.wrap(buffer);
            b.order(ByteOrder.LITTLE_ENDIAN);
            for (float s : obj) {
                b.putFloat(s);
            }
        } else if (data instanceof long[]) {
            long[] obj = (long[]) data;
            byte[] buffer = new byte[obj.length * Long.SIZE / Byte.SIZE];
            b = ByteBuffer.wrap(buffer);
            b.order(ByteOrder.LITTLE_ENDIAN);
            for (long s : obj) {
                b.putLong(s);
            }
        } else if (data instanceof double[]) {
            double[] obj = (double[]) data;
            byte[] buffer = new byte[obj.length * Double.SIZE / Byte.SIZE];
            b = ByteBuffer.wrap(buffer);
            b.order(ByteOrder.LITTLE_ENDIAN);
            for (double s : obj) {
                b.putDouble(s);
            }
        } else if (data instanceof byte[]) {
            byte[] obj = (byte[]) data;
            b = ByteBuffer.wrap(obj);
        } else if (data instanceof String) {
            byte[] str = ((String) data).getBytes();
            b = ByteBuffer.wrap(str);
        }

        // in order to read from the byte buffer (which is populated), it needs to be
        // rewound to the beginning.
        if (b != null) {
            b.rewind();
        }
        return b;

    }
}
