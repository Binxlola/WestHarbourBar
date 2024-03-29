package main.java.com.app.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

/**
 * A utility class used for working with passwords, hashing, and encryption
 */
public class PasswordUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int ITERATIONS = 512;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";

    /**
     * Used to hash a password with a given plaintext password and a given salt. Will use the default
     * algorithm, iterations, and key length set as constants in the PasswordUtil class.
     *
     * @param password The plaintext password to be hashed
     * @param salt     The salt to be used by the hashing algorithm
     * @return A container with the hashed password
     */
    public static byte[] hashPassword(String password, byte[] salt) {
        char[] chars = password.toCharArray();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(chars, Character.MIN_VALUE);

        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] securePassword = keyFactory.generateSecret(spec).getEncoded();
            return Optional.of(Base64.getEncoder().encodeToString(securePassword)).get().getBytes();
        } catch (Exception e) {
            System.err.println("There was an error hashing the password in setPassword()");
        } finally {
            spec.clearPassword();
        }

        return null;
    }

    /**
     * Will generate a random string to be used as a salt in password hashing
     *
     * @return Salt string
     */
    public static byte[] generateSaltByteArray() {
        byte[] salt = new byte[KEY_LENGTH];
        RANDOM.nextBytes(salt);
        return Optional.of(Base64.getEncoder().encodeToString(salt)).get().getBytes();
    }


    /**
     * Compares a hash of a plaintext password against an already hashed password using the given salt.
     *
     * @param password       The plain text password
     * @param hashedPassword The already hashed password
     * @param salt           The salt to use when comparing passwords
     * @return A boolean if the passwords match or not
     */
    public static boolean verifyPassword(String password, byte[] hashedPassword, byte[] salt) {
        byte[] hashed = hashPassword(password, salt);
        return hashed != null && Arrays.equals(hashed, hashedPassword);
    }
}
