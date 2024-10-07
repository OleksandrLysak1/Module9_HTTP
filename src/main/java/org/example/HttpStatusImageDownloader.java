package org.example;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.FileOutputStream;
import java.io.InputStream;

public class HttpStatusImageDownloader {
    private final HttpStatusChecker checker = new HttpStatusChecker();
    private final OkHttpClient client = new OkHttpClient();

    public String downloadStatusImage(int code) throws Exception {
        String url = checker.getStatusImage(code);  // Отримуємо ссилку на картинку

        // Показуємо ссилку користувачеві
        System.out.println("Image URL: " + url);

        // Завантажуємо зображення
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute();
             InputStream inputStream = response.body().byteStream();
             FileOutputStream outputStream = new FileOutputStream("http-cat-" + code + ".jpg")) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        return url;  // Повертаємо ссилку після завантаження
    }
}
