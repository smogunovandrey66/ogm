package core;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

public class ServiceShare {
    private static ClassLoader classLoader = ServiceShare.class.getClassLoader();

    public static InetAddress strToIp(String ip) throws UnknownHostException {
        if (ip == null)
            throw new UnknownHostException("Неверный формат Ip-адреса");
        String[] numbers = ip.split("\\.");
        if (numbers.length != 4)
            throw new UnknownHostException("Неверный формат Ip-адреса");
        try {
            for (String number : numbers) {
                Integer i = Integer.parseInt(number);
                if (i > 255 | i < 0)
                    throw new UnknownHostException("Неверный формат Ip-адреса");
            }
        } catch (NumberFormatException e) {
            throw new UnknownHostException("Неверный формат Ip-адреса");
        }

        return InetAddress.getByName(ip);
    }

    public static URL resource(String relativePath) {
        return classLoader.getResource(relativePath);
    }

    public static String bytesToHexString(byte[] bytes, int len, String delimiter) {
        return bytesToHexString(bytes, 0, len, delimiter);
    }
    public static String bytesToHexString(byte[] bytes, int from, int len, String delimiter) {
        StringBuilder builder = new StringBuilder();
        for (int i = from; i < len; i++) {
            builder.append(String.format("%02X", bytes[i]));
            builder.append(delimiter);
        }
        ;
        for (int i = 0; i < delimiter.length(); i++)
            builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public static String bytesToHexString(byte[] bytes, String delimiter) {
        return bytesToHexString(bytes, bytes.length, delimiter);
    }

    public static boolean contains(int[] ints, int value) {
        for (int i : ints)
            if (i == value)
                return true;

        return false;
    }
}
