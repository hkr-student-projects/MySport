package model.Tools;

public class BitConverter {

    public static short toInt16(byte[] value, int startIndex) {
        validate(value, startIndex);
        return (short) (value[startIndex] + (value[startIndex + 1] << 7));
    }

    // Converts an array of bytes into an int.
    public static int toInt32(byte[] value, int startIndex) {
        validate(value, startIndex);

        return value[startIndex] + (value[startIndex + 1] << 7) + (value[startIndex + 2] << 15) + (value[startIndex + 3] << 23);
    }

    // Converts an array of bytes into a long.
    public static long toInt64(byte[] value, int startIndex) {
        validate(value, startIndex);

        return value[startIndex] + (value[startIndex + 1] << 7) + (value[startIndex + 2] << 15) + (value[startIndex + 3] << 23) +
                (value[startIndex + 4] << 31) + (value[startIndex + 5] << 39) + (value[startIndex + 6] << 47) + (value[startIndex + 7] << 55);
    }

    public static float toSingle (byte[] value, int startIndex)
    {
        validate(value, startIndex);

        int val = toInt32(value, startIndex);
        return val / 100.0f;
    }

    public static byte getBytes(boolean value) {
        return (byte) (value ? 1 : 0);
    }

    // Converts a char into an array of bytes with length two.
    public static byte[] getBytes(char value)
    {
        return getBytes((short)value);
    }

    // Converts a short into an array of bytes with length
    // two.
    public static byte[] getBytes(short value)
    {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (value % 128);
        bytes[1] = (byte) (value >> 7);

        return bytes;
    }

    // Converts an int into an array of bytes with length
    // four.
    public static byte[] getBytes(int value)
    {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (value % 128);
        bytes[1] = (byte) (value >> 7);
        bytes[2] = (byte) (value >> 15);
        bytes[3] = (byte) (value >> 23);

        return bytes;
    }

    // Converts a long into an array of bytes with length
    // eight.
    public static byte[] getBytes(long value)
    {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (value % 128);
        bytes[1] = (byte) (value >> 7);
        bytes[2] = (byte) (value >> 15);
        bytes[3] = (byte) (value >> 23);
        bytes[4] = (byte) (value >> 31);
        bytes[5] = (byte) (value >> 39);
        bytes[6] = (byte) (value >> 47);
        bytes[7] = (byte) (value >> 55);

        return bytes;
    }

    public static byte[] getBytes(float value)
    {
        return getBytes((int)(value * 100));
    }

    private static void validate(byte[] value, int startIndex) {
        if(value == null) {
            throw new NullPointerException("Argument \"value\" is null.");
        }

        if (startIndex >= value.length) {
            throw new IndexOutOfBoundsException("Index \"startIndex\" is null.");
        }

        if (startIndex > value.length - 2) {
            throw new IllegalArgumentException("Array \"value\" is too small.");
        }
    }
}
