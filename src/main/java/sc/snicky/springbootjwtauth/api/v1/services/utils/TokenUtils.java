package sc.snicky.springbootjwtauth.api.v1.services.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class TokenUtils {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Byte TOKEN_SIZE = 32;
    private static final String ALGORITHM = "SHA-256";

    /**
     * Utility method to generate a random token.
     * The token is a Base64-encoded string of random bytes.
     *
     * @return a randomly generated token as a Base64-encoded string
     */
    public static String generateToken() {
        byte[] randomBytes = new byte[TOKEN_SIZE];
        RANDOM.nextBytes(randomBytes);
        return Base64.getEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /**
     * Hashes the given token using the SHA-256 algorithm.
     * The resulting hash is encoded as a Base64 string.
     *
     * @param token the token to be hashed
     * @return the hashed token as a Base64-encoded string
     * @throws RuntimeException if the SHA-256 algorithm is not available
     */
    public static String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hashedBytes = digest.digest(token.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(ALGORITHM + " algorithm not found");
        }
    }
}
