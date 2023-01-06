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

    @Test(expected = NullPointerException.class)
    void test3() {
        throw new NullPointerException();
    }
}