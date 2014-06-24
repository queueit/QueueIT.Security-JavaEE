package queueit.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.UUID;

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
    
    public static String encryptPlaceInQueue(Integer placeInQueue) {
        char[] placeInQueueChars = String.format("%07d", placeInQueue != null ? placeInQueue : 0).toCharArray();
        
        char[] encryptedPlaceInQueue = UUID.randomUUID().toString().toCharArray();
        encryptedPlaceInQueue[9] = placeInQueueChars[6];
        encryptedPlaceInQueue[26] = placeInQueueChars[5];
        encryptedPlaceInQueue[7] = placeInQueueChars[4];
        encryptedPlaceInQueue[20] = placeInQueueChars[3];
        encryptedPlaceInQueue[11] = placeInQueueChars[2];
        encryptedPlaceInQueue[3] = placeInQueueChars[1];
        encryptedPlaceInQueue[30] = placeInQueueChars[0];

        return new String(encryptedPlaceInQueue);
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
