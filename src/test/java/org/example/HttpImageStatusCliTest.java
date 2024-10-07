package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class HttpImageStatusCliTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private HttpStatusImageDownloader mockDownloader;
    private HttpImageStatusCli cli;

    @BeforeEach
    public void setUp() {
        // Мокування HttpStatusImageDownloader
        mockDownloader = Mockito.mock(HttpStatusImageDownloader.class);
        cli = new HttpImageStatusCli(mockDownloader);

        // Перенаправлення системного виходу
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testAskStatus_validCode() throws Exception {
        // Введення правильного статус-коду
        String input = "200\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        cli.askStatus();

        // Перевірка виводу
        assertEquals("Image downloaded successfully.\n", outputStream.toString());

        // Перевірка, що downloader.downloadStatusImage був викликаний
        verify(mockDownloader).downloadStatusImage(200);
    }

    @Test
    void testAskStatus_invalidCode() {
        // Введення некоректного статус-коду
        String input = "not-a-number\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        cli.askStatus();

        // Перевірка виводу
        assertEquals("Please enter a valid number.\n", outputStream.toString());
    }

    @Test
    void testAskStatus_downloadError() throws Exception {
        // Введення статус-коду, який викликає помилку
        String input = "404\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Мокування downloader для генерування помилки
        doThrow(new RuntimeException("Image not found")).when(mockDownloader).downloadStatusImage(404);

        cli.askStatus();

        // Перевірка виводу
        assertEquals("Error: Image not found\n", outputStream.toString());
    }
}