package model.Tools;

import model.Logging.Logger;

import java.security.InvalidParameterException;
import java.util.Formatter;
import static model.Tools.BitConverterKt.*;

public class Block {
    public static final int BUFFER_SIZE = Short.MAX_VALUE;
    public byte[] buffer = new byte[Block.BUFFER_SIZE];
    public int step;
    public int size;
    public byte[] block;
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

    public byte checkByte()
    {
        if (this.block == null || this.step > this.block.length - 1)
            return 0;
        return this.block[this.step];
    }

    public byte[] getBytes()
    {
        if(this.block == null){
            size = this.step;
            return this.buffer;
        }
        else {
            size = this.block.length;
            return this.block;
        }
    }

    public byte readByte()
    {
        if (this.block == null || this.step > this.block.length - 1){
            System.out.println("ZERO");
            return 0;
        }

        byte num = this.block[this.step];
        ++this.step;
        return num;
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
        short int16 = toInt16(this.block, this.step);
        this.step += 2;
        return int16;
    }

    public int readInt32()
    {
        if (this.block == null || this.step > this.block.length - 4)
            return 0;
        int int32 = toInt32(this.block, this.step);
        this.step += 4;
        return int32;
    }

     public String readString()
    {
        if (this.block == null || this.step >= this.block.length)
            return "";
        byte num = this.block[this.step];
        String str = this.step + (int)num > this.block.length ? "" : new String(this.block, this.step + 1, num);
        this.step = this.step + 1 + (int)num;
        return str;
    }

    public float readSingle()
    {
        if (this.block == null || this.step > this.block.length - 4)
            return 0.0f;
        float single = toSingle(this.block, this.step);
        this.step += 4;
        return single;
    }

    public String readColor()
    {
        byte r = readByte();
        byte g = readByte();
        byte b = readByte();
        byte a = readByte();
        return String.format("%s,%s,%s,%s", r < 0 ? 256 - r : r, g < 0 ? 256 - g : g, b < 0 ? 256 - b : b, a / 100.0f);
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
            System.arraycopy(values, 0, this.buffer, this.step, values.length);
            this.step += values.length;
        }
        else
        {
            byte length = (byte)values.length;
            this.buffer[this.step] = length;
            ++this.step;
            System.arraycopy(values, 0, this.buffer, this.step, length);
            this.step += length;
        }
    }

    public void writeString(String value)
    {
        byte[] bytes = value.getBytes();
        byte length = (byte)bytes.length;
        this.buffer[this.step] = length;
        ++this.step;
        System.arraycopy(bytes, 0, this.buffer, this.step, length);
        this.step += length;
    }

    public void writeStringArray(String[] values)
    {
        byte length = (byte)values.length;
        this.writeByte(length);
        for (byte index = 0; (int)index < (int)length; ++index)
            this.writeString(values[(int)index]);
    }

    public void writeColor(String[] rgba)
    {
        if(rgba.length != 4)
            throw new InvalidParameterException("Argument \"rgba[]\" must have exactly 4 elements");
        byte r, g, b;
        float a;
        try {
            r = (byte) Short.parseShort(rgba[0]);
            g = (byte) Short.parseShort(rgba[1]);
            b = (byte) Short.parseShort(rgba[2]);
            a = Float.parseFloat(rgba[3]);
            writeColor(r, g, b, a);
        }
        catch (NumberFormatException ex){
            Logger.logException(ex);
        }
    }

    public void writeColor(byte r, byte g, byte b, float a)
    {
        this.writeByte(r);
        this.writeByte(g);
        this.writeByte(b);
        this.writeByte((byte) (a * 100.0f));
    }

    public void writeBoolean(boolean value)
    {
        this.buffer[this.step] = BitConverterKt.getBytes(value);
        ++this.step;
    }

    public void writeSingle(float value)
    {
        byte[] bytes = BitConverterKt.getBytes(value);
        System.arraycopy(bytes, 0, this.buffer, this.step, bytes.length);
        this.step += 4;
    }

    public void writeInt16(short value)
    {
        byte[] bytes = BitConverterKt.getBytes(value);
        System.arraycopy(bytes, 0, this.buffer, this.step, bytes.length);
        this.step += 2;
    }

    public void writeInt32(int value)
    {
        byte[] bytes = BitConverterKt.getBytes(value);
        System.arraycopy(bytes, 0, this.buffer, this.step, bytes.length );
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
        byte[] bytes = BitConverterKt.getBytes(value);
        System.arraycopy(bytes, 0, this.buffer, this.step, bytes.length );
        this.step += 4;
    }

    private String getHash(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
            formatter.format("%02x", b);
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
        this.block = null;
    }

    public void reset()
    {
        this.step = 0;
        this.block = null;
    }

    public byte[] getBuffer(){
        return buffer;
    }

    public int getSize(){
        return block == null ? this.step : this.size;
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