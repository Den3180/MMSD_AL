package org.example.mmsd_al.ServiceClasses.Comparators;

import org.example.mmsd_al.Classes.ClassChannel;

import java.util.Comparator;

public class ChannelCompareAddress implements Comparator<ClassChannel> {
    @Override
    public int compare(ClassChannel o1, ClassChannel o2) {
        if(o1.get_Address()>o2.get_Address()){
            return 1;
        }
        else if(o1.get_Address()<o2.get_Address()){
            return -1;
        }
        else return 0;
    }
}
