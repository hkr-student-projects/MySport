package model.Tools;

public class BitConverter {

    public static short toInt16(byte[] value, int startIndex) {
        checkNull(value, startIndex);

        return (short) (value[startIndex] + (value[startIndex + 1] << 8));
    }

    // Converts an array of bytes into an int.
    public static int toInt32(byte[] value, int startIndex) {
        checkNull(value, startIndex);

        return value[startIndex] + (value[startIndex + 1] << 8) + (value[startIndex + 2] << 16) + (value[startIndex + 3] << 24);
    }

    // Converts an array of bytes into a long.
    public static long toInt64(byte[] value, int startIndex) {
        checkNull(value, startIndex);

        return value[startIndex] + (value[startIndex + 1] << 8) + (value[startIndex + 2] << 16) + (value[startIndex + 3] << 24) +
                (value[startIndex + 4] << 32) + (value[startIndex + 5] << 40) + (value[startIndex + 6] << 48) + (value[startIndex + 7] << 56);
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
        bytes[0] = (byte) value;
        bytes[1] = (byte) (value >> 8);

        return bytes;
    }

    // Converts an int into an array of bytes with length
    // four.
    public static byte[] getBytes(int value)
    {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) value;
        bytes[1] = (byte) (value >> 8);
        bytes[2] = (byte) (value >> 16);
        bytes[3] = (byte) (value >> 24);

        return bytes;
    }

    // Converts a long into an array of bytes with length
    // eight.
    public static byte[] getBytes(long value)
    {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) value;
        bytes[1] = (byte) (value >> 8);
        bytes[2] = (byte) (value >> 16);
        bytes[3] = (byte) (value >> 24);
        bytes[4] = (byte) (value >> 32);
        bytes[5] = (byte) (value >> 40);
        bytes[6] = (byte) (value >> 48);
        bytes[7] = (byte) (value >> 56);

        return bytes;
    }

    private static void checkNull(byte[] value, int startIndex) {
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
