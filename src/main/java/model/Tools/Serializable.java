package model.Tools;

public interface Serializable<T> {

    public byte[] serialize(T obj);

    public T deserialize(byte[] bytes);
}
