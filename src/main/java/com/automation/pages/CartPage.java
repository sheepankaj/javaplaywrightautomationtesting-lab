package com.automation.pages;

import com.microsoft.playwright.Page;

public class CartPage extends BasePage {

    private static final String PAGE_TITLE = ".title";
    private static final String CART_ITEM = ".cart_item";
    private static final String CHECKOUT_BUTTON = "#checkout";

    public CartPage(Page page) {
        super(page);
    }

    public CartPage waitForPageLoad() {
        waitForVisible(PAGE_TITLE);
        log.info("Cart page loaded");
        return this;
    }

    public int getCartItemCount() {
        return page.locator(CART_ITEM).count();
    }

    public CheckoutPage proceedToCheckout() {
        click(CHECKOUT_BUTTON);
        log.info("Proceeding to checkout");
        return new CheckoutPage(page);
    }
}
