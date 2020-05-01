package model.Tools;

public interface Serializable<T> {

    public byte[] serialize();

    public T deserialize(byte[] bytes);
}
