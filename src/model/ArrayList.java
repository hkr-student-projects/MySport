package model;

import java.util.Arrays;
import java.util.Collection;

public class ArrayList<T> {

    private static int avrg;
    private Object[] content;
    private int size;

    static {
        avrg = 10;
    }

    public ArrayList(){
        content = new Object[avrg];
        size = 0;
    }

    public ArrayList(Collection<? extends T> c) {
        content = c.toArray();
        size = content.length;
    }

    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0)
            this.content = new Object[initialCapacity];
        else if (initialCapacity == 0)
            content = new Object[avrg];
        else
            throw new IllegalArgumentException("Negative capacity provided: " + initialCapacity);
        size = 0;
    }

    public void add(T item){
        int last = size++;
        if(content.length == size)
            content = grow();
        content[last] = item;
    }

    private Object[] grow(){
        return Arrays.copyOf(content, size + avrg);
    }

    public void remove(int index){
        shiftElements(index);
    }

    public void remove(T item){
        int i = 0;
        for(; i < size; i++)
            if(content[i].equals(item))
                break;
        shiftElements(i);
    }

    private void shiftElements(int index){
        int temp;
        if((temp = size - 1) > index)
            for(int i = index; i < size - 1; i++)
                content[i] = content[i + 1];
        content[size = temp] = null;
    }

    public boolean contains(T item){
        return indexOf(item) >= 0;
    }

    public int indexOf(T item){
        if (item == null) {
            for (int i = 0; i < size; i++) {
                if (content[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (item.equals(content[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

    public void clear(){
        for(int count = size, i = size = 0; i < count; i++)
            content[i] = null;
    }

    public T get(int index){
        checkIndex(index);
        return (T) content[index];
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return content.length == 0;
    }

    private void checkIndex(int index){
        if(index < 0 || index >= content.length)
            throw new IndexOutOfBoundsException("Index: "+ index +" is less than 0 or greater than array bound: " + (content.length - 1) + ".");
    }

    public int getAvrg()
    {
        return avrg;
    }
}

//    public void remove(int index){
//        checkIndex(index);
//        Object[] ncontent = new Object[content.length - 1];
//        for(int i = 0, j = 0; i < content.length; i++, j++){
//            if(i == index){
//                j--;
//                continue;
//            }
//            ncontent[j] = content[i];
//        }
//        content = ncontent;
//    }
