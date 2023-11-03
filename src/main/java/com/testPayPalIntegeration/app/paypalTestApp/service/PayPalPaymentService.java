package com.testPayPalIntegeration.app.paypalTestApp.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PayPalPaymentService {

    @Autowired
    private APIContext apiContext;

    public static final String SUCCESS_URL = "https://bard.google.com/chat/2938ecc0e54abb6";
    public static final String CANCEL_URL = "https://khouloudkhezami.medium.com/integrating-paypal-payment-in-springboot-backend-full-guide-with-all-the-steps-charge-and-payout-413013a993b3";
    private static final String INTENT = "sale";
    private static final String METHOD = "paypal";

    private static final String SENDER_BATCH = "yavar3492";
    private static final String EMAIL_SUBJECT = "yavarkhan892300@gmail.com";
    private static final String RECIPIENT_TYPE = "receiver";




    public Payment createPayment(Double total, String currency, String description) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(METHOD);

        Payment payment = new Payment();
        payment.setIntent(INTENT);
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(CANCEL_URL);
        redirectUrls.setReturnUrl(SUCCESS_URL);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }

    public PayoutBatch payout(double total, String currency, String receiverEmail) throws PayPalRESTException {
        Date currentDate = new Date(System.currentTimeMillis());
        PayoutSenderBatchHeader payoutSenderBatchHeader = new PayoutSenderBatchHeader();
        payoutSenderBatchHeader.setSenderBatchId(SENDER_BATCH + " " + currentDate.toString());
        payoutSenderBatchHeader.setEmailSubject(EMAIL_SUBJECT);
        payoutSenderBatchHeader.setRecipientType(RECIPIENT_TYPE);
        List<PayoutItem> payoutItems = new ArrayList<>();

        payoutItems.add(new PayoutItem(new Currency(currency, String.format("%.2f", total)), receiverEmail));
        Payout payout = new Payout();

        payout.setSenderBatchHeader(payoutSenderBatchHeader);
        payout.setItems(payoutItems);

        return payout.create(apiContext, null);
    }

}
