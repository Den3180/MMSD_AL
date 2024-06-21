package org.example.mmsd_al.ServiceClasses;

public class ClassDelay extends Thread{
    public static void delay(int delay){
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
