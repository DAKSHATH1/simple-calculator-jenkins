
package lab.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class App {

    private static final List<RechargePlan> PLANS = List.of(
        new RechargePlan(101, "Basic Plan",
                new BigDecimal("199.00"), 28, "1 GB/day"),
        new RechargePlan(102, "Value Plan",
                new BigDecimal("299.00"), 28, "1.5 GB/day"),
        new RechargePlan(103, "Extended Plan",
                new BigDecimal("399.00"), 56, "2 GB/day")
    );

    // Validate Indian mobile number
    public static boolean isValidMobileNumber(String number) {
        return number != null && number.matches("[6-9][0-9]{9}");
    }

    // Display available recharge plans
    public static List<RechargePlan> getAvailablePlans() {
        return PLANS;
    }

    // Find recharge plan using ID
    public static Optional<RechargePlan> findPlanById(int planId) {
        return PLANS.stream()
                .filter(plan -> plan.getId() == planId)
                .findFirst();
    }

    // Calculate recharge amount
    public static BigDecimal calculateRechargeAmount(int planId) {
        return findPlanById(planId)
                .map(RechargePlan::getAmount)
                .orElseThrow(() ->
                    new IllegalArgumentException("Invalid plan selection"));
    }

    // Recharge using selected plan
    public static RechargeTransaction recharge(
            String mobileNumber, int planId) {

        if (!isValidMobileNumber(mobileNumber)) {
            throw new IllegalArgumentException("Invalid mobile number");
        }

        BigDecimal amount = calculateRechargeAmount(planId);
        return recharge(mobileNumber, planId, amount);
    }

    // Recharge with amount validation
    public static RechargeTransaction recharge(
            String mobileNumber, int planId, BigDecimal amount) {

        if (!isValidMobileNumber(mobileNumber)) {
            throw new IllegalArgumentException("Invalid mobile number");
        }

        RechargePlan plan = findPlanById(planId)
                .orElseThrow(() ->
                    new IllegalArgumentException("Invalid plan selection"));

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Recharge amount must be positive");
        }

        if (amount.compareTo(plan.getAmount()) != 0) {
            throw new IllegalArgumentException(
                    "Recharge amount does not match selected plan");
        }

        return new RechargeTransaction(
                UUID.randomUUID().toString(),
                mobileNumber,
                plan,
                plan.getAmount(),
                "SUCCESS"
        );
    }

    // Main application
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("       MOBILE RECHARGE APP");
        System.out.println("=================================");

        System.out.print("Enter your 10-digit mobile number: ");
        String mobile = sc.nextLine().trim();

        if (!isValidMobileNumber(mobile)) {
            System.out.println("Invalid mobile number!");
            sc.close();
            return;
        }

        System.out.println("\nAVAILABLE RECHARGE PLANS:");

        for (RechargePlan plan : getAvailablePlans()) {
            System.out.printf(
                    "%d | %s | Rs.%s | %d Days | %s%n",
                    plan.getId(),
                    plan.getName(),
                    plan.getAmount().toPlainString(),
                    plan.getValidityDays(),
                    plan.getDataAllowance()
            );
        }

        System.out.print("\nEnter Plan ID: ");
        String input = sc.nextLine().trim();

        try {
            int planId = Integer.parseInt(input);

            RechargeTransaction transaction =
                    recharge(mobile, planId);

            System.out.println("\n======= TRANSACTION RESULT =======");
            System.out.println("Transaction ID : "
                    + transaction.getTransactionId());
            System.out.println("Mobile Number  : "
                    + transaction.getMobileNumber());
            System.out.println("Selected Plan  : "
                    + transaction.getPlan().getName());
            System.out.println("Recharge Amount: Rs."
                    + transaction.getAmount().toPlainString());
            System.out.println("Status         : "
                    + transaction.getStatus());

            System.out.println("\nRecharge completed successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Error: Plan ID must be numeric.");

        } catch (IllegalArgumentException e) {
            System.out.println("Recharge failed: " + e.getMessage());
        }

        sc.close();
    }

    // Recharge Plan class
    public static class RechargePlan {

        private final int id;
        private final String name;
        private final BigDecimal amount;
        private final int validityDays;
        private final String dataAllowance;

        public RechargePlan(int id, String name,
                BigDecimal amount, int validityDays,
                String dataAllowance) {

            this.id = id;
            this.name = name;
            this.amount = amount;
            this.validityDays = validityDays;
            this.dataAllowance = dataAllowance;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public int getValidityDays() {
            return validityDays;
        }

        public String getDataAllowance() {
            return dataAllowance;
        }
    }

    // Recharge Transaction class
    public static class RechargeTransaction {

        private final String transactionId;
        private final String mobileNumber;
        private final RechargePlan plan;
        private final BigDecimal amount;
        private final String status;

        public RechargeTransaction(String transactionId,
                String mobileNumber, RechargePlan plan,
                BigDecimal amount, String status) {

            this.transactionId = transactionId;
            this.mobileNumber = mobileNumber;
            this.plan = plan;
            this.amount = amount;
            this.status = status;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public RechargePlan getPlan() {
            return plan;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public String getStatus() {
            return status;
        }

        public boolean isSuccessful() {
            return "SUCCESS".equals(status);
        }
    }
}
