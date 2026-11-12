package com.automation.tests.ui;

import com.automation.base.BaseTest;
import com.automation.pages.CartPage;
import com.automation.pages.InventoryPage;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("SauceDemo E-Commerce")
@Feature("Inventory / Shopping Cart")
public class InventoryTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest() {
        inventoryPage = loginAsStandardUser();
    }

    @Test(groups = "ui", description = "Verify inventory page displays products")
    @Story("Product Listing")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the inventory page loads with the expected number of products")
    public void testInventoryPageDisplaysProducts() {
        int itemCount = inventoryPage.getInventoryItemCount();

        assertThat(itemCount)
                .as("At least one product should be displayed")
                .isGreaterThan(0);

        List<String> itemNames = inventoryPage.getAllItemNames();
        assertThat(itemNames)
                .as("Item names should not be empty")
                .isNotEmpty()
                .doesNotContainNull();

        List<String> itemPrices = inventoryPage.getAllItemPrices();
        assertThat(itemPrices)
                .as("Item prices should not be empty")
                .isNotEmpty()
                .allMatch(price -> price.startsWith("$"));

        log.info("Inventory validated — {} products found", itemCount);
    }

    @Test(groups = "ui", description = "Verify adding one item to cart updates badge count")
    @Story("Add to Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that adding one item to the cart correctly updates the cart badge to 1")
    public void testAddSingleItemToCart() {
        inventoryPage.addFirstItemToCart();

        assertThat(inventoryPage.isCartBadgeDisplayed())
                .as("Cart badge should be visible after adding item")
                .isTrue();

        assertThat(inventoryPage.getCartBadgeCount())
                .as("Cart badge should show 1 after adding one item")
                .isEqualTo(1);

        log.info("Single item add to cart validated");
    }

    @Test(groups = "ui", description = "Verify cart contains added items")
    @Story("Cart Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that items added to cart appear correctly in the cart page")
    public void testCartContainsAddedItems() {
        inventoryPage.addFirstItemToCart();

        CartPage cartPage = inventoryPage.goToCart().waitForPageLoad();

        assertThat(cartPage.getCartItemCount())
                .as("Cart should contain exactly 1 item")
                .isEqualTo(1);

        log.info("Cart item count validated");
    }
}
