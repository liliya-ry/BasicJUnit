import annotations.BeforeEach;
import annotations.Disabled;
import annotations.DisplayName;
import annotations.Test;

import java.util.Optional;

import static assertions.Assertions.assertEquals;

public class Test1 {
    private Cup cup;

    @BeforeEach
    void setCup() {
        cup = new Cup("Orange Juice", 85.5);
    }

    @Test
    @DisplayName("Liquid test")
    @Disabled("Why")
    void getLiquidType() {
        assertEquals("Orange Juice", cup.getLiquidType(), "simple message");
    }

    @Test
    @Disabled
    void getPercentFull() {
        assertEquals(1, cup.getPercentFull());
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
}
