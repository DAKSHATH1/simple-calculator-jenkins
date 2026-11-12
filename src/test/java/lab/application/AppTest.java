
package lab.application;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    // TEST 1: Validate mobile number formats
    @Test
    void testMobileNumberValidation() {

        assertAll(
            () -> assertTrue(
                    App.isValidMobileNumber("9876543210")),

            () -> assertTrue(
                    App.isValidMobileNumber("6123456789")),

            () -> assertFalse(
                    App.isValidMobileNumber("987654321")),

            () -> assertFalse(
                    App.isValidMobileNumber("98765432100")),

            () -> assertFalse(
                    App.isValidMobileNumber("5876543210")),

            () -> assertFalse(
                    App.isValidMobileNumber("98AB543210")),

            () -> assertFalse(
                    App.isValidMobileNumber(null))
        );
    }

    // TEST 2: Display and select available plans
    @Test
    void testDisplayAndSelectPlans() {

        List<App.RechargePlan> plans =
                App.getAvailablePlans();

        assertEquals(3, plans.size());

        assertEquals(
                List.of(101, 102, 103),
                plans.stream()
                    .map(App.RechargePlan::getId)
                    .toList()
        );

        App.RechargePlan selected =
                App.findPlanById(102).orElseThrow();

        assertEquals("Value Plan", selected.getName());

        assertEquals(
                new BigDecimal("299.00"),
                selected.getAmount()
        );

        assertTrue(App.findPlanById(999).isEmpty());
    }

    // TEST 3: Calculate amount and complete transaction
    @Test
    void testRechargeAmountAndTransaction() {

        assertEquals(
                0,
                new BigDecimal("299.00").compareTo(
                        App.calculateRechargeAmount(102))
        );

        App.RechargeTransaction transaction =
                App.recharge("9876543210", 102);

        assertAll(
            () -> assertNotNull(transaction.getTransactionId()),

            () -> assertFalse(
                    transaction.getTransactionId().isBlank()),

            () -> assertEquals(
                    "9876543210",
                    transaction.getMobileNumber()),

            () -> assertEquals(
                    102,
                    transaction.getPlan().getId()),

            () -> assertEquals(
                    0,
                    new BigDecimal("299.00").compareTo(
                            transaction.getAmount())),

            () -> assertEquals(
                    "SUCCESS",
                    transaction.getStatus()),

            () -> assertTrue(transaction.isSuccessful())
        );
    }

    // TEST 4: Invalid plan selection
    @Test
    void testInvalidPlanSelection() {

        assertAll(
            () -> assertThrows(
                    IllegalArgumentException.class,
                    () -> App.calculateRechargeAmount(999)),

            () -> assertThrows(
                    IllegalArgumentException.class,
                    () -> App.recharge("9876543210", 999))
        );
    }

    // TEST 5: Invalid recharge amounts
    @Test
    void testInvalidRechargeAmounts() {

        assertAll(
            () -> assertThrows(
                    IllegalArgumentException.class,
                    () -> App.recharge(
                            "9876543210", 102, BigDecimal.ZERO)),

            () -> assertThrows(
                    IllegalArgumentException.class,
                    () -> App.recharge(
                            "9876543210", 102,
                            new BigDecimal("-1.00"))),

            () -> assertThrows(
                    IllegalArgumentException.class,
                    () -> App.recharge(
                            "9876543210", 102,
                            new BigDecimal("199.00"))),

            () -> assertThrows(
                    IllegalArgumentException.class,
                    () -> App.recharge(
                            "9876543210", 102, null)),

            () -> assertThrows(
                    IllegalArgumentException.class,
                    () -> App.recharge("12345", 102))
        );
    }
}
