package org.example.mmsd_al.Tests;

import org.example.mmsd_al.Classes.ClassChannel;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class ClassChannelTest {

    ClassChannel ch=new ClassChannel();
    @Test
    void get_StrBaseValue() {
        ch.set_BaseValue(new int[]{1});
        ch.set_TypeRegistry(ClassChannel.EnumTypeRegistry.CoilOutput);
        String actual= ch.get_StrBaseValue();
        String expected="0x01";
        assertEquals(expected,actual);
    }

    @Test
    void get_StrDTAct(){
        ch.set_DTAct(LocalDateTime.now());
        String actual=ch.get_StrDTAct();
        String expected=LocalDateTime.now().format(DateTimeFormatter. ofPattern("dd.MM.yyyy HH:mm:ss"));
        assertEquals(expected,actual);
    }
}