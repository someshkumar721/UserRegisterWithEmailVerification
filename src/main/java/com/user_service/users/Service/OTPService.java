package com.user_service.users.Service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OTPService {
    private static final long OTP_VALID_DURATION_MS = 5 * 60 * 1000;      // 5 minutes
    private static final long VERIFIED_VALID_DURATION_MS = 15 * 60 * 1000; // 15 minutes to complete registration after verifying

    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();
    private final Map<String, Long> verifiedEmails = new ConcurrentHashMap<>();

    private record OtpEntry(String otp, long expiryTime) {
    }

    public String generateOtp(String email) {
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        otpStore.put(email, new OtpEntry(otp, Instant.now().toEpochMilli() + OTP_VALID_DURATION_MS));
        verifiedEmails.remove(email); // reset any prior verification when a new OTP is requested
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        OtpEntry entry = otpStore.get(email);
        if (entry == null) return false;

        boolean isValid = entry.otp().equals(otp) && Instant.now().toEpochMilli() <= entry.expiryTime();
        if (isValid) {
            otpStore.remove(email);
            verifiedEmails.put(email, Instant.now().toEpochMilli() + VERIFIED_VALID_DURATION_MS);
        }
        return isValid;
    }

    public boolean isEmailVerified(String email) {
        Long expiry = verifiedEmails.get(email);
        if (expiry == null) return false;
        if (Instant.now().toEpochMilli() > expiry) {
            verifiedEmails.remove(email);
            return false;
        }
        return true;
    }

    public void clearVerification(String email) {
        verifiedEmails.remove(email);
    }
}
