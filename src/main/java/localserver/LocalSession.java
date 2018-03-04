package localserver;

import core.Commands;
import core.ErrorsServer;
import core.ServiceShare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class LocalSession extends Thread {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Logger logger;
    private boolean isRegistration = false;
    private Properties loginPasswords;

    public LocalSession(Socket socket) {
        super();
        this.socket = socket;
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        logger = LoggerFactory.getLogger(getClass());
        if(!checkRootPassword())
            return;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            logger.debug("Открыта сессия");
            while (true) {
                int oneByte = inputStream.read();
                //В пришедшем пакете есть хотя бы один байт
                if (oneByte >= 0) {
                    //авторизован
                    if (isRegistration) {
                        byte[] bytes = null;
                        switch (oneByte){
                            case (Commands.LIST_PLATES):
                                bytes = new byte[2 + 1 + 3 * 24];
                                bytes[0] = (byte)((bytes.length - 2) / 256);
                                bytes[1] = (byte)((bytes.length - 2) % 256);
                                bytes[2] = Commands.LIST_PLATES;

                        }
                    } else {//не авторизован
                        //Пользователь пытается авторизоваться
                        if (oneByte == Commands.REGISTRATION && socket.getReceiveBufferSize() > 1 + 16 + 1) {
                            byte[] digest = new byte[16];
                            //собираем MD5 пароля
                            inputStream.read(digest);
                            String passwordMd5 = ServiceShare.bytesToHexString(digest, "");
                            //собираем логин
                            StringBuilder stringBuilder = new StringBuilder();
                            while (true) {
                                oneByte = inputStream.read();
                                if (oneByte == -1)
                                    break;
                                stringBuilder.append(oneByte);
                            }
                            String login = stringBuilder.toString();
                            if(loginPasswords.getProperty(login, "").equals(passwordMd5)) {
                                isRegistration = true;
                                outputStream.write(new byte[]{0x00, 0x01, Commands.REGISTRATION});
                            }else{
                                outputStream.write(ServiceLocalBlock.makeError(ErrorsServer.ERROR_AUTHORIZE));
                            }
                        } else {//неизвестная команда
                            outputStream.write(ServiceLocalBlock.makeError(ErrorsServer.ERROR_AUTHORIZE));
                        }
                    }
                } else {
                    //в пришедшем пакете нет байтов
                    outputStream.write(ServiceLocalBlock.makeError(ErrorsServer.INVALID_ARGUMENTS));
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка обработки запроса:" + e.getMessage());
        }
    }

    public boolean checkRootPassword() {
        String error;
        try {
            loginPasswords = new Properties();

            File file = new File("password");
            if (file.exists() && !file.isDirectory()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                loginPasswords.load(fileInputStream);
            }

            String rootPassword = loginPasswords.getProperty("root", "");
            if (rootPassword.equals("")) {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update("password".getBytes());
                rootPassword = ServiceShare.bytesToHexString(digest.digest(), "");
                loginPasswords.put("root", rootPassword);
                loginPasswords.store(new FileOutputStream("password"), "comment");
            }
            return true;
        } catch (IOException e) {
            error = e.getMessage();
        } catch (NoSuchAlgorithmException e) {
            error = e.getMessage();
        }
        logger.error(error);
        return false;
    }

    public static void main(String[] args) {

    }
}
