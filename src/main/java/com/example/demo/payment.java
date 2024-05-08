package com.example.demo;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;

public class payment {
    public static void main(String[] args) {
// Set your secret key here
        Stripe.apiKey = "sk_test_51PAbVp07iLNjInuxTndXXU1mKAckFb9pzU4NS0VpvbAUpkyaTwfQ4DYGpC2ZJdAi3LkusNRs0wNDIHFggk9aBGHV00V5bV2IJa";

        try {
// Retrieve your account information
            Account account = Account.retrieve();
            System.out.println("Account ID: " + account.getId());
// Print other account information as needed
        } catch (StripeException e) {
            e.printStackTrace();
        }
}}
