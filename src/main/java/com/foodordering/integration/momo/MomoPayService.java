package com.foodordering.integration.momo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * MoMo Pay Integration Service
 * This service adapts MoMo Pay API to our payment interface (Adapter Pattern)
 * 
 * Note: This is a simplified implementation. In production, you would:
 * - Use MoMo's official SDK
 * - Handle webhooks for payment callbacks
 * - Implement proper error handling and retries
 * - Store credentials securely (use Spring Cloud Config or environment variables)
 */
@Service
public class MomoPayService {

    @Value("${momo.partner-code:}")
    private String partnerCode;

    @Value("${momo.access-key:}")
    private String accessKey;

    @Value("${momo.secret-key:}")
    private String secretKey;

    @Value("${momo.api-endpoint:https://test-payment.momo.vn/v2/gateway/api/create}")
    private String apiEndpoint;

    @Value("${momo.return-url:http://localhost:8080/api/payments/callback}")
    private String returnUrl;

    @Value("${momo.notify-url:http://localhost:8080/api/payments/webhook}")
    private String notifyUrl;

    /**
     * Create payment request with MoMo Pay
     * 
     * @param orderId Order ID
     * @param amount Payment amount
     * @param orderInfo Order information
     * @return MoMo payment response containing payment URL
     */
    public MomoPaymentResponse createPayment(String orderId, Double amount, String orderInfo) {
        try {
            String requestId = UUID.randomUUID().toString();
            String orderIdMoMo = "ORDER_" + orderId + "_" + System.currentTimeMillis();
            long amountLong = (long) (amount * 100); // MoMo expects amount in cents
            
            // Create request data
            Map<String, String> requestData = new HashMap<>();
            requestData.put("partnerCode", partnerCode);
            requestData.put("partnerName", "Food Ordering System");
            requestData.put("storeId", "FoodOrderingStore");
            requestData.put("requestId", requestId);
            requestData.put("amount", String.valueOf(amountLong));
            requestData.put("orderId", orderIdMoMo);
            requestData.put("orderInfo", orderInfo != null ? orderInfo : "Payment for order " + orderId);
            requestData.put("redirectUrl", returnUrl);
            requestData.put("ipnUrl", notifyUrl);
            requestData.put("lang", "vi");
            requestData.put("extraData", "");
            requestData.put("requestType", "captureWallet");
            requestData.put("autoCapture", "true");

            // Create signature
            String rawSignature = buildRawSignature(requestData);
            String signature = signHmacSHA256(rawSignature, secretKey);
            requestData.put("signature", signature);

            // In production, make HTTP POST request to MoMo API
            // For now, we'll simulate the response
            MomoPaymentResponse response = new MomoPaymentResponse();
            response.setResultCode("0");
            response.setMessage("Success");
            response.setPayUrl("https://test-payment.momo.vn/v2/gateway?orderId=" + orderIdMoMo);
            response.setOrderId(orderIdMoMo);
            response.setRequestId(requestId);
            response.setAmount(amountLong);
            
            return response;

        } catch (Exception e) {
            MomoPaymentResponse errorResponse = new MomoPaymentResponse();
            errorResponse.setResultCode("-1");
            errorResponse.setMessage("Payment creation failed: " + e.getMessage());
            return errorResponse;
        }
    }

    /**
     * Verify payment callback from MoMo
     * 
     * @param callbackData Callback data from MoMo
     * @return Verification result
     */
    public boolean verifyPaymentCallback(Map<String, String> callbackData) {
        try {
            String receivedSignature = callbackData.get("signature");
            String calculatedSignature = signHmacSHA256(buildRawSignature(callbackData), secretKey);
            return receivedSignature != null && receivedSignature.equals(calculatedSignature);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Build raw signature string from request data
     */
    private String buildRawSignature(Map<String, String> data) {
        return "accessKey=" + data.get("accessKey") +
               "&amount=" + data.get("amount") +
               "&extraData=" + (data.get("extraData") != null ? data.get("extraData") : "") +
               "&ipnUrl=" + data.get("ipnUrl") +
               "&orderId=" + data.get("orderId") +
               "&orderInfo=" + data.get("orderInfo") +
               "&partnerCode=" + data.get("partnerCode") +
               "&redirectUrl=" + data.get("redirectUrl") +
               "&requestId=" + data.get("requestId") +
               "&requestType=" + data.get("requestType");
    }

    /**
     * Sign data using HMAC SHA256
     */
    private String signHmacSHA256(String data, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * MoMo Payment Response DTO
     */
    public static class MomoPaymentResponse {
        private String resultCode;
        private String message;
        private String payUrl;
        private String orderId;
        private String requestId;
        private Long amount;

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPayUrl() {
            return payUrl;
        }

        public void setPayUrl(String payUrl) {
            this.payUrl = payUrl;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public Long getAmount() {
            return amount;
        }

        public void setAmount(Long amount) {
            this.amount = amount;
        }
    }
}

