package queueit.security;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Hashing {

    public static Integer decryptPlaceInQueue(String encryptedPlaceInQueue) {
        //ValidateInput(encryptedPlaceInQueue, "encryptedPlaceInQueue");
        if (encryptedPlaceInQueue == null) {
            throw new InvalidKnownUserUrlException();
        }
        if (encryptedPlaceInQueue.length() != 36) {
            throw new InvalidKnownUserUrlException();
        }

        String e = encryptedPlaceInQueue;
        String p = e.substring(30, 31) + e.substring(3, 4) + e.substring(11, 12) + e.substring(20, 21) + e.substring(7, 8) + e.substring(26, 27) + e.substring(9, 10);
        return Integer.parseInt(p);
    }

    static String getMd5Hash(String stringToHash) {
        try {
            // Convert the input string to a byte array and compute the hash.
            byte[] bytesOfMessage = stringToHash.getBytes("UTF-8");

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(bytesOfMessage);

            BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        } catch (Exception e) {
            return "";
        }
    }
}
