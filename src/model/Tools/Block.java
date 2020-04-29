package model.Tools;

import java.util.Formatter;

public class Block {
    public static final int BUFFER_SIZE = Short.MAX_VALUE << 1;
    public static byte[] buffer = new byte[Block.BUFFER_SIZE];
    public int step;
    public byte[] block;
    public boolean useCompression;
    public boolean longBinaryData;

    public Block(int prefix, byte[] contents)
    {
        this.reset(prefix, contents);
    }

    public Block(byte[] contents)
    {
        this.reset(contents);
    }

    public Block(int prefix)
    {
        this.reset(prefix);
    }

    public Block()
    {
        this.reset();
    }

    public byte readByte()
    {
        if (this.block == null || this.step > this.block.length - 1)
            return 0;
        byte num = this.block[this.step];
        ++this.step;
        return num;
    }

    public byte checkByte()
    {
        if (this.block == null || this.step > this.block.length - 1)
            return 0;
        return this.block[this.step];
    }

    public byte[] readByteArray()
    {
        if (this.block == null || this.step >= this.block.length)
            return new byte[0];
        byte[] numArray;
        if (this.longBinaryData)
        {
            int length = this.readInt32();
            if (length >= 30000)
                return new byte[0];
            numArray = new byte[length];
        }
        else
        {
            numArray = new byte[(int)this.block[this.step]];
            ++this.step;
        }
        if (this.step + numArray.length <= this.block.length)
        {
            System.arraycopy(this.block, this.step, numArray, 0, numArray.length);
        }
        this.step += numArray.length;
        return numArray;
    }

    public short readInt16()
    {
        if (this.block == null || this.step > this.block.length - 2)
            return 0;
        short int16 = BitConverter.toInt16(this.block, this.step);
        this.step += 2;
        return int16;
    }

    public int readInt32()
    {
        if (this.block == null || this.step > this.block.length - 4)
            return 0;
        int int32 = BitConverter.toInt32(this.block, this.step);
        this.step += 4;
        return int32;
    }

    public void writeByte(byte value)
    {
        buffer[this.step] = value;
        ++this.step;
    }

    public void writeByteArray(byte[] values)
    {
        if (values.length >= 30000)
            return;
        if (this.longBinaryData)
        {
            this.writeInt32(values.length);
            System.arraycopy(values, 0, Block.buffer, this.step, values.length);
            this.step += values.length;
        }
        else
        {
            byte length = (byte)values.length;
            Block.buffer[this.step] = length;
            ++this.step;
            System.arraycopy(values, 0, Block.buffer, this.step, length);
            this.step += (int)length;
        }
    }

    public void writeString(String value)
    {
        byte[] bytes = value.getBytes();
        byte length = (byte)bytes.length;
        Block.buffer[this.step] = length;
        ++this.step;
        System.arraycopy(bytes, 0, Block.buffer, this.step, length);
        this.step += (int)length;
    }

    public void writeStringArray(String[] values)
    {
        byte length = (byte)values.length;
        this.writeByte(length);
        for (byte index = 0; (int)index < (int)length; ++index)
            this.writeString(values[(int)index]);
    }

    public void writeBoolean(boolean value)
    {
        Block.buffer[this.step] = BitConverter.getBytes(value);
        ++this.step;
    }

    public void writeInt16(short value)
    {
        byte[] bytes = BitConverter.getBytes(value);
        System.arraycopy(bytes, 0, Block.buffer, this.step, bytes.length);
        this.step += 2;
    }

    public void writeInt32(int value)
    {
        byte[] bytes = BitConverter.getBytes(value);
        System.arraycopy(bytes, 0, Block.buffer, this.step, bytes.length );
        this.step += 4;
    }

    public void writeInt32Array(int[] values)
    {
        this.writeInt16((short)values.length);
        for (short index = 0; (int)index < values.length; ++index)
            this.writeInt32(values[(int)index]);
    }

    public void writeInt64(long value)
    {
        byte[] bytes = BitConverter.getBytes(value);
        //Buffer.BlockCopy((Array)bytes, 0, (Array)Block.buffer, this.step, bytes.length);
        System.arraycopy(bytes, 0, Block.buffer, this.step, bytes.length );
        this.step += 4;
    }

    public ByteSize getBytes()
    {
        return this.block == null ? new ByteSize(Block.buffer, this.step) : new ByteSize(this.block, this.block.length);
    }

    public static class ByteSize{
        byte[] bytes;
        int size;

        public ByteSize(byte[] bytes, int size){

            this.bytes = bytes;
            this.size = size;
        }
    }

    private String getHash(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public void reset(int prefix, byte[] contents)
    {
        this.step = prefix;
        this.block = contents;
    }

    public void reset(byte[] contents)
    {
        this.step = 0;
        this.block = contents;
    }

    public void reset(int prefix)
    {
        this.step = prefix;
        this.block = (byte[])null;
    }

    public void reset()
    {
        this.step = 0;
        this.block = (byte[])null;
    }
}
//
//    private static final char[] DIGITS_LOWER =
//            {'0', '1', '2', '3', '4', '5', '6', '7',
//                    '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
//
//    private static final char[] DIGITS_UPPER =
//            {'0', '1', '2', '3', '4', '5', '6', '7',
//                    '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
//
//    protected static char[] encodeHex(byte[] data, char[] toDigits) {
//        int l = data.length;
//        char[] out = new char[l << 1];
//        // two characters form the hex value.
//        for (int i = 0, j = 0; i < l; i++) {
//            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
//            out[j++] = toDigits[0x0F & data[i]];
//        }
//        return out;
//    }
//
//    protected static int toDigit(char ch, int index) throws DecoderException {
//        int digit = Character.digit(ch, 16);
//        if (digit == -1) {
//            throw new DecoderException(
//                    "Illegal hexadecimal character "
//                            + ch + " at index " + index);
//        }
//        return digit;
//    }
//
//    public static String exampleSha1(String convertme){
//        MessageDigest md = MessageDigest.getInstance("SHA-1");
//        byte[] encodeHex = md.digest(convertme));
//        return new String(encodeHex);
//    }