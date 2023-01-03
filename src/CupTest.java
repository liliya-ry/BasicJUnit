import annotations.BeforeEach;
import annotations.DisplayName;
import annotations.Test;
import static assertions.Assertions.*;

class CupTest {
    private Cup cup;

    @BeforeEach
    void setCup() {
        cup = new Cup("Orange Juice", 85.5);
    }
    @Test
    @DisplayName("Liquid test")
    void getLiquidType() {
        assertEquals(1, cup.getLiquidType(), "simple message");
    }

    @Test
    void getPercentFull() {
        assertEquals(85.5, cup.getPercentFull());
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