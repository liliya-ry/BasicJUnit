package tests;

import annotations.*;

public class CupTest {
    public CupTest() {}
    @Test(dependsOnMethods = {"a"})
    void b() {
        System.out.println("test1");
    }

    @Test(dependsOnMethods = "b")
    void a() {
        System.out.println("test2");
    }

    @Test(dependsOnMethods = {"a"})
    void c() {
        System.out.println("test3");
    }
}