package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HttpStatusCheckerTest {

    @Test
    public void testGetStatusImage_validStatusCode() throws Exception {
        HttpStatusChecker checker = new HttpStatusChecker();
        String url = checker.getStatusImage(200);

        assertEquals("https://http.cat/200.jpg", url);
    }

    @Test
    public void testGetStatusImage_invalidStatusCode() {
        HttpStatusChecker checker = new HttpStatusChecker();

        Exception exception = assertThrows(Exception.class, () -> {
            checker.getStatusImage(1000);  // Некоректний код статусу
        });

        assertEquals("Image not found for HTTP status 1000", exception.getMessage());
    }
}
