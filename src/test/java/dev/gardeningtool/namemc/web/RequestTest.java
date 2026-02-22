package dev.gardeningtool.namemc.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Request")
class RequestTest {

    @Test
    @DisplayName("getResponseCode lança IllegalStateException antes de connect()")
    void getResponseCodeThrowsBeforeConnect() {
        // Usa um URL válido; não chamamos connect() para que responseCode permaneça -1
        Request request = new Request("https://api.mojang.com/users/profiles/minecraft/Notch?at=0");
        request.prepare();

        IllegalStateException thrown = assertThrows(IllegalStateException.class, request::getResponseCode);

        assertTrue(thrown.getMessage().toLowerCase().contains("connection"));
    }

    @Test
    @DisplayName("getter de URL retorna o mesmo URL usado no construtor")
    void getUrlReturnsConstructedUrl() throws Exception {
        String urlString = "https://api.mojang.com/users/profiles/minecraft/Notch?at=0";
        Request request = new Request(urlString);

        assertEquals(new java.net.URL(urlString), request.getUrl());
    }

    @Test
    @DisplayName("connection não é nula após construção")
    void connectionIsNotNull() {
        Request request = new Request("https://api.mojang.com/users/profiles/minecraft/Notch?at=0");

        assertNotNull(request.getConnection());
    }
}
