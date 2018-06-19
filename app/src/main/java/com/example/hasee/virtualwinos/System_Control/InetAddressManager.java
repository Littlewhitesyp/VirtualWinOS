package com.example.hasee.virtualwinos.System_Control;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hasee on 2018/5/30.
 */

public class InetAddressManager {
    /**
     * 服务器地址
     */
    public static String ServerIp = "192.168.137.1";

    /**
     * 本机地址
     * @return
     */
    public static String getLocalIp(){
        try {
            Enumeration<InetAddress> iasEnum;
            Enumeration<NetworkInterface> Interface= NetworkInterface.getNetworkInterfaces();
            while(Interface.hasMoreElements())
            {
                NetworkInterface nif = Interface.nextElement();
                if(nif.getName().startsWith("wlan")){
                    iasEnum = nif.getInetAddresses();
                    while(iasEnum.hasMoreElements()){
                        InetAddress ias = iasEnum.nextElement();
                        if(ias.getAddress().length==4)
                            return ias.getHostAddress();
                    }

                }

            }
            return null;
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isvaild(String ipv4){
        Pattern pattern = Pattern.compile("(\\d{1,3}.){3}(\\d{1,3})");
        Matcher m = pattern.matcher(ipv4);
        return m.matches();
    }
}
