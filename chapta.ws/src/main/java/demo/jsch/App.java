package demo.jsch;

import com.jcraft.jsch.JSchException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception {
        System.out.println( "Hello World!" );
        
        SshUserInfo userInfo = new SshUserInfo("Pass2012");
        JschHandler jhandler = new JschHandler("Bvcomm","111.235.97.12",22,userInfo);
        jhandler.init();
        jhandler.exec("admin/bin/solaris-AdminScript.sh 7 GVS_IN41");
        
        //jhandler.exec("ps -ef");
        jhandler.destory();
    }
}
