package tests;

import annotations.*;

public class CupTest {
    public CupTest() {}
    @Test
    void test1() {
        System.out.println("test1");
    }

    @Test
    void test2() {
        System.out.println("test2");
    }

    @Test(expected = NullPointerException.class)
    void test3() {
        throw new NullPointerException();
    }
}