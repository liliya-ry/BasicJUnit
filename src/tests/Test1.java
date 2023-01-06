package tests;

import annotations.BeforeEach;
import annotations.Disabled;
import annotations.DisplayName;
import annotations.Test;
import assertions.Assertions;
import classes.Cup;

public class Test1 {
    private Cup cup;

    public Test1 () {}
    @BeforeEach
    void setCup() {
        cup = new Cup("Orange Juice", 85.5);
    }

    @Test
    @DisplayName("Liquid test")
    @Disabled("Why")
    void getLiquidType() {
        Assertions.assertEquals("Orange Juice", cup.getLiquidType(), "simple message");
    }

    @Test
    @Disabled
    void getPercentFull() {
        Assertions.assertEquals(1, cup.getPercentFull());
    }

    @Test
    void setLiquidType() {
        cup.setLiquidType("Water");
        Assertions.assertEquals("Water", cup.getLiquidType());
    }

    @Test
    void setPercentFull() {
        cup.setPercentFull(100.0);
        Assertions.assertEquals(100.0, cup.getPercentFull());
    }
}
