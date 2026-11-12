package com.automation.pages;

import com.microsoft.playwright.Page;

public class CheckoutPage extends BasePage {

    private static final String FIRST_NAME_INPUT = "#first-name";
    private static final String LAST_NAME_INPUT = "#last-name";
    private static final String POSTAL_CODE_INPUT = "#postal-code";
    private static final String CONTINUE_BUTTON = "#continue";
    private static final String FINISH_BUTTON = "#finish";
    private static final String SUMMARY_TOTAL = ".summary_total_label";
    private static final String COMPLETE_HEADER = ".complete-header";

    private static final String ERROR_MESSAGE = "[data-test='error']";

    public CheckoutPage(Page page) {
        super(page);
    }

    public CheckoutPage waitForStepOne() {
        waitForVisible(FIRST_NAME_INPUT);
        log.info("Checkout Step One loaded");
        return this;
    }

    public CheckoutPage fillShippingInfo(String firstName, String lastName, String postalCode) {
        fill(FIRST_NAME_INPUT, firstName);
        fill(LAST_NAME_INPUT, lastName);
        fill(POSTAL_CODE_INPUT, postalCode);
        log.info("Shipping info filled: {} {} {}", firstName, lastName, postalCode);
        return this;
    }

    public CheckoutPage clickContinue() {
        click(CONTINUE_BUTTON);
        return this;
    }

    public CheckoutPage waitForStepTwo() {
        waitForVisible(FINISH_BUTTON);
        log.info("Checkout Step Two loaded");
        return this;
    }

    public String getOrderTotal() {
        return getText(SUMMARY_TOTAL);
    }

    public CheckoutPage clickFinish() {
        click(FINISH_BUTTON);
        log.info("Order placed");
        return this;
    }

    public CheckoutPage waitForOrderConfirmation() {
        waitForVisible(COMPLETE_HEADER);
        log.info("Order confirmation displayed");
        return this;
    }

    public String getOrderConfirmationHeader() {
        return getText(COMPLETE_HEADER);
    }

    public boolean isErrorDisplayed() {
        return isVisible(ERROR_MESSAGE);
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }
}
