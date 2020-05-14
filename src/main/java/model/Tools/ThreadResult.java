package model.Tools;

public class ThreadResult<T, R> implements Runnable {
    private volatile R value;
    private final Expression<T, R> method;
    private T arg;

    public ThreadResult(Expression<T, R> method, T arg){
        this.arg = arg;
        this.method = method;
    }

    @Override
    public void run() {
        value = method.execute(arg);
    }

    public R getValue(){
        return value;
    }
}

