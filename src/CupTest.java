import annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.Duration;

import static assertions.Assertions.*;

class CupTest {
    private Cup cup;

    @BeforeEach
    void setCup() {
        cup = new Cup("Orange Juice", 85.5);
    }
    @Test

    void getLiquidType() {
        assertEquals("Orange Juice", cup.getLiquidType(), "simple message");
    }

    @Test
    void getPercentFull() {
        assertEquals(1, cup.getPercentFull(), () -> "lambda msg");
    }

    @Test
    void setLiquidType() {
        cup.setLiquidType("Water");
        assertEquals("Water", cup.getLiquidType());
    }

    @Test
    void setPercentFull() {
        cup.setPercentFull(100.0);
        assertEquals(100.0, cup.getPercentFull());
    }

    @Test
    void throwsTest() {
        assertThrows(IOException.class, () -> System.out.println("src"));
    }

    @Test
    void testTimeout() {
        assertTimeoutPreemptively(Duration.ofMillis(2), () -> {
            FileWriter writer = new FileWriter("index.txt");
            for (int i = 0; i < 1000000000; i++) {
                writer.append((char) i);
            }
        });
    }
}