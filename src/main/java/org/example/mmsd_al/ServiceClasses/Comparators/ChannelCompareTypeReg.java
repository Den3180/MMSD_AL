package org.example.mmsd_al.ServiceClasses.Comparators;

import org.example.mmsd_al.Classes.ClassChannel;

import java.util.Comparator;

public class ChannelCompareTypeReg implements Comparator<ClassChannel> {

    @Override
    public int compare(ClassChannel o1, ClassChannel o2) {
        return  o1.get_TypeRegistry().compareTo(o2.get_TypeRegistry());
    }
}
