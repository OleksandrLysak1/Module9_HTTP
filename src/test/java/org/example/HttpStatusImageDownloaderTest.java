package org.example;

import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class HttpStatusImageDownloaderTest {

    private HttpStatusImageDownloader downloader;
    private OkHttpClient mockClient;
    private HttpStatusChecker mockChecker;
    private Call mockCall;

    @BeforeEach
    void setUp() {
        mockClient = mock(OkHttpClient.class);  // Мокаємо OkHttpClient
        mockChecker = mock(HttpStatusChecker.class);  // Мокаємо HttpStatusChecker
        downloader = new HttpStatusImageDownloader(mockChecker, mockClient);  // Передаємо змоковані версії
        mockCall = mock(Call.class);  // Мокаємо Call
    }

    @Test
    void testDownloadStatusImage_validCode() throws Exception {
        // Створюємо змоковані об'єкти для відповіді
        Response mockResponse = mock(Response.class);
        InputStream mockInputStream = mock(InputStream.class);

        // Мокаємо поведінку метода getStatusImage
        when(mockChecker.getStatusImage(200)).thenReturn("https://http.cat/200.jpg");

        // Мокаємо поведінку OkHttpClient для запиту
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        // Мокаємо тіло відповіді та його поведінку
        when(mockResponse.body()).thenReturn(mock(ResponseBody.class));
        when(mockResponse.body().byteStream()).thenReturn(mockInputStream);

        // Перевіряємо, що метод не викидає винятків при коректному коді
        assertDoesNotThrow(() -> downloader.downloadStatusImage(200));

        // Перевіряємо, чи було виконано запит
        verify(mockCall).execute();
    }
}
