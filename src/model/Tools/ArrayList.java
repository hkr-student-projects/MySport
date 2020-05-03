package model.Tools;

import java.util.Arrays;

public class ArrayList<T> {

    private static int avrg;
    private Object[] content;
    private int size;

    static {
        avrg = 10;// configure somehow
    }

    public ArrayList(){
        content = new Object[avrg];
        size = 0;
    }

//    public ArrayList(Collection<? extends T> c) {
//        content = c.toArray();
//        contentID = new Object[content.length];
//        size = content.length;
//    }

    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0)
            content = new Object[initialCapacity];
        else if (initialCapacity == 0)
            content = new Object[avrg];
        else
            throw new IllegalArgumentException("Negative capacity provided: " + initialCapacity);
        size = 0;
    }

    public void add(T item){
        if(item == null)
            throw new NullPointerException("Parameter \"item\" is null");
        int last = size++;
        if(content.length == size)
            content = grow();
        content[last] = item;
    }

    public void addWithKey(T item){
        int last = size++;
        if(content.length == size)
            content = grow();
        content[last] = item;
    }

    private Object[] grow(){
        return Arrays.copyOf(content, size + avrg);
    }

    public void removeAt(int index){
        checkIndex(index);
        shiftElements(index);
    }

    public void remove(T item){
        int index = indexOf(item);
        if(indexOf(item) == -1)
            return;
        shiftElements(index);//aka deleting at index, shifting all after index to left
    }

    private void shiftElements(int index){
        if(size - 1 > index)// "to delete" exists before last element
            System.arraycopy(content, index + 1, content, index, size - index - 1);// -1 because anyway I will place null value on the last element
        content[--size] = null;
    }

    public void insertAt(T item, int index){ //insert item before index
        if(index < 0)
            return;
        if(content.length == ++size)
            content = grow();
        System.arraycopy(content, index, content, index + 1, size - index);//here all after index are copied
        content[index] = item;
    }

    private void shiftRight(int index){
        checkIndex(index);
        if(content.length == size + 1)
            content = grow();
        int temp;
        if((temp = size + 1) > index)
            System.arraycopy(content, index + 1, content, index, size - 1 - index);
        content[size = temp] = null;
    }

    public boolean contains(T item){
        return indexOf(item) >= 0;
    }

    public int indexOf(T item){
        if(item == null)
            return -1;
        for (int i = 0; i < size; i++)
            if (item.equals(content[i]))
                return i;

        return -1;
    }

    public void clear(){
        for(int count = size, i = size = 0; i < count; i++){
            content[i] = null;
        }
    }

    public T get(int index){
        checkIndex(index);
        return (T) content[index];
    }

    public int size(){
        return size;
    }

    public void setSize(int size){
        this.size = size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    private void checkIndex(int index){
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: "+ index +" is less than 0 or greater than array (size variable) bound: " + (size - 1) + ".");
    }

    public int getAvrg()
    {
        return avrg;
    }

    public T[] getContents(){
        return (T[]) this.content;
    }
}
