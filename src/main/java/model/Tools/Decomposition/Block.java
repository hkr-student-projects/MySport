package model.Tools.Decomposition;

import model.Logging.Logger;
import java.util.Formatter;

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
        float single = BitConverter.toSingle(this.block, this.step);
        this.step += 4;
        return single;
    }

    public String readColor()
    {
        short r = readInt16();
        short g = readInt16();
        short b = readInt16();
        //byte a = readByte();
        return "-fx-background-color: " + String.format("#%02x%02x%02x", r, g, b) + ";";
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

    public void writeColor(String hex)
    {
        short r, g, b;
        try {
//            r = (byte) Short.parseShort(rgb[0]);
//            g = (byte) Short.parseShort(rgb[1]);
//            b = (byte) Short.parseShort(rgb[2]);
            r = Short.valueOf(hex.substring(0, 2), 16 );
            g = Short.valueOf(hex.substring(2, 4), 16 );
            b = Short.valueOf(hex.substring(4, 6), 16 );
            writeColor(r, g, b);
        }
        catch (NumberFormatException ex){
            Logger.logException(ex);
        }
    }

    public void writeColor(short r, short g, short b)
    {
        this.writeInt16(r);
        this.writeInt16(g);
        this.writeInt16(b);
        //this.writeByte((byte) (a * 100.0f));
    }

    public void writeBoolean(boolean value)
    {
        this.buffer[this.step] = BitConverter.getBytes(value);
        ++this.step;
    }

    public void writeSingle(float value)
    {
        byte[] bytes = BitConverter.getBytes(value);
        System.arraycopy(bytes, 0, this.buffer, this.step, bytes.length);
        this.step += 4;
    }

    public void writeInt16(short value)
    {
        byte[] bytes = BitConverter.getBytes(value);
        System.arraycopy(bytes, 0, this.buffer, this.step, bytes.length);
        this.step += 2;
    }

    public void writeInt32(int value)
    {
        byte[] bytes = BitConverter.getBytes(value);
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
        byte[] bytes = BitConverter.getBytes(value);
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
