package com.heycine.slash.common.test.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class TestGeneric<TEST extends InputStream> {

     private TEST content;

    TestGeneric(TEST content) {

        this.content = content;
    }
    public void print() {
        System.out.println(content);
    }

    public static <TEST extends String> void printText(TEST content) {
        System.out.println(content);
    }

    public static void main(String[] args) {
//        TestGeneric<String> objectTestGeneric = new TestGeneric<>("Hello generic");
//        objectTestGeneric.print();

        TestGeneric<? extends InputStream> objectTestGeneric2 = new TestGeneric<>(new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        });


        printText("456");
    }

}
