package model.Tools;

import java.util.Arrays;

public class ArrayList<T> {

    private static int avrg;
    private Object[] contents;
    private int size;

    static {
        avrg = 10;// configure somehow
    }

    public ArrayList(){
        contents = new Object[avrg];
        size = 0;
    }

//    public ArrayList(Collection<? extends T> c) {
//        content = c.toArray();
//        contentID = new Object[content.length];
//        size = content.length;
//    }

    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0)
            contents = new Object[initialCapacity];
        else if (initialCapacity == 0)
            contents = new Object[avrg];
        else
            throw new IllegalArgumentException("Negative capacity provided: " + initialCapacity);
        size = 0;
    }

    public void add(T item){
        if(item == null)
            throw new NullPointerException("Parameter \"item\" is null");
        int last = size++;
        if(contents.length == size)
            contents = grow();
        contents[last] = item;
    }

    public void addWithKey(T item){
        int last = size++;
        if(contents.length == size)
            contents = grow();
        contents[last] = item;
    }

    private Object[] grow(){
        return Arrays.copyOf(contents, size + avrg);
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
            System.arraycopy(contents, index + 1, contents, index, size - index - 1);// -1 because anyway I will place null value on the last element
        contents[--size] = null;
    }

    public void insertAt(T item, int index){ //insert item before index
        if(index < 0)
            return;
        if(contents.length == ++size)
            contents = grow();
        System.arraycopy(contents, index, contents, index + 1, size - index);//here all after index are copied
        contents[index] = item;
    }

    private void shiftRight(int index){
        checkIndex(index);
        if(contents.length == size + 1)
            contents = grow();
        int temp;
        if((temp = size + 1) > index)
            System.arraycopy(contents, index + 1, contents, index, size - 1 - index);
        contents[size = temp] = null;
    }

    public boolean contains(T item){
        return indexOf(item) >= 0;
    }

    public int indexOf(T item){
        if(item == null)
            return -1;
        for (int i = 0; i < size; i++)
            if (item.equals(contents[i]))
                return i;

        return -1;
    }

    public void clear(){
        for(int count = size, i = size = 0; i < count; i++){
            contents[i] = null;
        }
    }

    public T get(int index){
        checkIndex(index);
        return (T) contents[index];
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
        return (T[]) this.contents;
    }

    public T[] toArray(){
        return (T[]) Arrays.copyOf(contents, size);
    }
}
