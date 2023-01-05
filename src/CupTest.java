import annotations.*;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

import static assertions.Assertions.*;

class CupTest {
    @Test
    void test1() {
        System.out.println("test1");
    }

    @Test
    void test2() {
        System.out.println("test2");
    }

    @Test(dependsOnMethods = {"test1", "test2"})
    void test3() {
        System.out.println("test3");
    }
}