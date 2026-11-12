package com.automation.tests.ui;

import com.automation.base.BaseTest;
import com.automation.constants.AppConstants;
import com.automation.pages.CartPage;
import com.automation.pages.CheckoutPage;
import com.automation.pages.InventoryPage;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("SauceDemo E-Commerce")
@Feature("Checkout")
public class CheckoutTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndAddItemBeforeTest() {
        inventoryPage = loginAsStandardUser();
        inventoryPage.addFirstItemToCart();
    }

    @Test(groups = "ui", description = "Complete full end-to-end checkout flow")
    @Story("Checkout Flow")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a user can add item to cart, proceed through checkout, and receive order confirmation")
    public void testCompleteCheckoutFlow() {
        CartPage cartPage = inventoryPage.goToCart().waitForPageLoad();

        assertThat(cartPage.getCartItemCount())
                .as("Cart should have 1 item before checkout")
                .isEqualTo(1);

        CheckoutPage checkoutPage = cartPage.proceedToCheckout().waitForStepOne();

        checkoutPage.fillShippingInfo("John", "Doe", "10001")
                .clickContinue()
                .waitForStepTwo();

        assertThat(checkoutPage.getOrderTotal())
                .as("Order total should be displayed")
                .isNotEmpty()
                .contains("Total");

        checkoutPage.clickFinish().waitForOrderConfirmation();

        assertThat(checkoutPage.getOrderConfirmationHeader())
                .as("Order confirmation header should match expected text")
                .isEqualToIgnoringCase(AppConstants.CHECKOUT_COMPLETE_HEADER);

        log.info("End-to-end checkout flow completed successfully");
    }

    @Test(groups = "ui", description = "Verify checkout fails without required shipping info")
    @Story("Checkout Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the checkout form shows an error when first name is missing")
    public void testCheckoutRequiresFirstName() {
        CartPage cartPage = inventoryPage.goToCart().waitForPageLoad();
        CheckoutPage checkoutPage = cartPage.proceedToCheckout().waitForStepOne();

        checkoutPage.fillShippingInfo("", "Doe", "10001").clickContinue();

        assertThat(checkoutPage.isErrorDisplayed())
                .as("Error should be shown when first name is missing")
                .isTrue();

        assertThat(checkoutPage.getErrorMessage())
                .as("Error should mention first name is required")
                .contains("First Name");

        log.info("Checkout validation test passed");
    }
}
