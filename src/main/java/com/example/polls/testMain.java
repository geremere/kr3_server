package com.example.polls;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class testMain {

    public static void main(String[] args)  throws Exception{
        File file = new File("file");
        file.mkdir();
        File fil = new File("file/text.txt");

    }
}

@Getter
@Setter
class A {
   private int a;
   private String b;
}
class B{
    public double cr(int a, double b){
        return a+b;
    }
    public static void cr(String a, String b){
        a+=" ! "+b;
    }
    public int cr(int a, int b){
        return a+b;
    }
    public double cr(double a, double b){
        return a+b;
    }
}
abstract class C{
    public void b(){

    }

}

