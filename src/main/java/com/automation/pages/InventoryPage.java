package com.automation.pages;

import com.microsoft.playwright.Page;

import java.util.List;

public class InventoryPage extends BasePage {

    private static final String PAGE_TITLE = ".title";
    private static final String INVENTORY_ITEM = ".inventory_item";
    private static final String ITEM_NAME = ".inventory_item_name";
    private static final String ITEM_PRICE = ".inventory_item_price";
    private static final String ADD_TO_CART_BUTTON = "button[data-test^='add-to-cart']";
    private static final String CART_BADGE = ".shopping_cart_badge";
    private static final String CART_ICON = ".shopping_cart_link";

    public InventoryPage(Page page) {
        super(page);
    }

    public InventoryPage waitForPageLoad() {
        waitForVisible(PAGE_TITLE);
        log.info("Inventory page loaded");
        return this;
    }

    public int getInventoryItemCount() {
        return page.locator(INVENTORY_ITEM).count();
    }

    public void addFirstItemToCart() {
        page.locator(ADD_TO_CART_BUTTON).first().click();
        log.info("First item added to cart");
    }

    public int getCartBadgeCount() {
        if (!isVisible(CART_BADGE)) return 0;
        return Integer.parseInt(getText(CART_BADGE));
    }

    public boolean isCartBadgeDisplayed() {
        return isVisible(CART_BADGE);
    }

    public CartPage goToCart() {
        click(CART_ICON);
        log.info("Navigating to cart");
        return new CartPage(page);
    }

    public List<String> getAllItemNames() {
        return page.locator(ITEM_NAME).allTextContents();
    }

    public List<String> getAllItemPrices() {
        return page.locator(ITEM_PRICE).allTextContents();
    }
}
