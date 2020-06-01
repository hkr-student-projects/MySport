package model.Logging;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static java.lang.System.out;


enum ELogType{

    Error("\u001B[31m"),
    Warning("\u001B[33m"),
    Log("\u001B[0m"),
    Exception("\u001B[35m");
    //more

    private String code;

    ELogType(String code){
        this.code = code;
    }
    public String getCode(){
        return code;
    }
}

class LogEntry{
    String message;
    ELogType logType;

    public LogEntry(String message, ELogType logType){
        this.message = message;
        this.logType = logType;
    }
}

public class Logger {


   public static void log(String message){
       writeConsole(message, ELogType.Log);
   }

   public static void LogWarning(String message){
       writeConsole(message, ELogType.Warning);
   }

   public static void logError(String message){
       writeConsole(message, ELogType.Error);
   }

   public static void logException(Exception ex){
       logException(ex, ex.getMessage());
   }

    public static void logException(Exception ex, String message){
        writeConsole("Exception message: " + message, ELogType.Exception);
        StringBuilder exm = new StringBuilder();
        for(StackTraceElement se : ex.getStackTrace())
            exm.append("\n\tat ").append(se.getClassName()).append(".").append(se.getMethodName()).append("()");
        writeConsole(exm.toString(), ELogType.Exception);
    }

   private static void writeConsole(String message, ELogType logType){

       switch (logType){
           case Log:
               out.println(logType.Log.getCode() + message + "\u001B[0m");
               new LogThread("logThread", new LogEntry(message, logType)).start();
               break;
           case Error:
               out.println(logType.Error.getCode() + message + "\u001B[0m");
               new LogThread("logErrorThread", new LogEntry(message, logType)).start();
               break;
           case Warning:
               out.println(logType.Warning.getCode() + message + "\u001B[0m");
               new LogThread("logWarningThread", new LogEntry(message, logType)).start();
               break;
           case Exception:
               out.print(logType.Exception.getCode() + message + "\u001B[0m");
               new LogThread("logExceptionThread", new LogEntry(message, logType)).start();
               break;
           default:
               out.println("\u001B[0m" + message + "\u001B[0m");
               break;
       }
   }


    public static void logUML(Class type, boolean alsoFields){
        try(FileWriter writer = new FileWriter(""+ type.getName() +".UML.txt", false))
        {
            if(alsoFields){
                for(Field field : type.getDeclaredFields()){
                    String[] par = field.getAnnotatedType().toString().split("\\.");
                    String out = par.length == 0 ? par[0] : par[par.length - 1];
                    writer.write(("- " + field.getName() + ": "+ out + "\n"));
                }
                writer.write('\n');
            }

            for(Method method : type.getMethods()){
                String params = "";
                for (Parameter param : method.getParameters()){

                    String[] par = param.getAnnotatedType().toString().split("\\.");
                    String out = par.length == 0 ? par[0] : par[par.length - 1];
                    params += "" + param.getName() + ": "+ out +", ";
                }
                String[] par2 = method.getAnnotatedReturnType().toString().split("\\.");
                String out2 = par2.length == 0 ? par2[0] : par2[par2.length - 1];
                params = params.isEmpty() ? params : params.substring(0, params.length() - 2);
                writer.write(("+ " + method.getName() + "(" + params + "): "+ out2 + "\n"));
            }
            writer.flush();
        }
        catch(IOException ex){
            writeConsole(ex.getMessage(), ELogType.Exception);
        }
    }
}
