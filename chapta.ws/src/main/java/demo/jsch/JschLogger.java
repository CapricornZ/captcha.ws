package demo.jsch;

public class JschLogger implements com.jcraft.jsch.Logger{  
	  
    @Override  
    public boolean isEnabled(int level) {  
        return true;  
    }  

    @Override  
    public void log(int level, String message) {  
        System.out.println(String.format("[JSCH --> %s]", message));  
    }  
}   