package model.Tools;

public interface Expression<T, R>{
    R execute(T args);
}
