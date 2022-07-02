public class MyException extends Exception{
    public MyException(String description){
        System.out.println("throws Exception //т.к. " + description);
    }
}