package com.testPayPalIntegeration.app.paypalTestApp.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PayoutBatch;
import com.paypal.base.rest.PayPalRESTException;
import com.testPayPalIntegeration.app.paypalTestApp.model.Order;
import com.testPayPalIntegeration.app.paypalTestApp.model.PayOutOrder;
import com.testPayPalIntegeration.app.paypalTestApp.service.PayPalPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.testPayPalIntegeration.app.paypalTestApp.service.PayPalPaymentService.CANCEL_URL;
import static com.testPayPalIntegeration.app.paypalTestApp.service.PayPalPaymentService.SUCCESS_URL;

@RestController
public class PayPalPaymentController {

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";


    @Autowired
    private PayPalPaymentService service;


    @RequestMapping(value = "/pay", method = {RequestMethod.POST})
    public String payment(@RequestBody Order order) {
        try {
            Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getDescription());
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:"+link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "redirect:/ http://www.paypal.com";
    }

    @RequestMapping(value = CANCEL_URL, method = RequestMethod.GET)
    public String cancelPay() {
        return "cancel";
    }

    @RequestMapping(value = SUCCESS_URL, method = RequestMethod.GET)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerId") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "success";
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/ http://www.google,com";
    }

    @RequestMapping(value = "/payout", method = RequestMethod.POST)
    public PayoutBatch payout(@RequestBody PayOutOrder payoutOrder) {
        try {
            return service.payout(payoutOrder.getTotalAmount(), payoutOrder.getCurrency(),
                    payoutOrder.getReceiver());
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return null;
    }
}
