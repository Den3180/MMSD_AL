package org.example.mmsd_al.DevicesClasses;

import org.example.mmsd_al.Classes.ClassChannel;

import java.util.ArrayList;
import java.util.List;

public class ClassGroupRequest {

    private ClassChannel.EnumTypeRegistry _TypeRegistry;
    private List<ClassChannel> _Channels;
    private int startAddress;

    public ClassGroupRequest(ClassChannel.EnumTypeRegistry typeRegistry){
        _TypeRegistry = typeRegistry;
        _Channels = new ArrayList<ClassChannel>();
    }

    public void addChannel(ClassChannel channel)
    {
        _Channels.add(channel);
    }

    public int GetSize()
    {
        if (_Channels.size() == 0) return 0;
        //Адрес последнего регистра минус адрес первого регистра в группе регистров.
        int lastIndex=_Channels.size()-1;
        int Size = _Channels.get(lastIndex).get_Address() - _Channels.get(0).get_Address();
        switch (_Channels.get(lastIndex).get_Format())
        {
            case UINT,SINT ->Size+=1;
            case Float, UInt32,swFloat -> Size+=2;
        }
        return Size;
    }

    public int getLastAddress()
    {
        if (_Channels.size() == 0) return Integer.MAX_VALUE;
        return _Channels.get(_Channels.size()-1).get_Address();
    }

    public static ClassGroupRequest CGR;
    //Отступ от начального(не всегда нулевого) адреса.
    public int getOffset(int index)
    {
        CGR=this;
        return _Channels.get(index).get_Address() - getStartAddress();
    }

    public int getStartAddress() {
        if(_Channels.size()>0) return _Channels.get(0).get_Address();
        else return 0;
    }

    public ClassChannel.EnumTypeRegistry get_TypeRegistry() {
        return _TypeRegistry;
    }

    public List<ClassChannel> get_Channels() {
        return _Channels;
    }

}
