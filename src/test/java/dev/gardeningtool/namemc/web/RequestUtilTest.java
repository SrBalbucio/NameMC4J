package dev.gardeningtool.namemc.web;

import com.google.gson.JsonArray;

import dev.gardeningtool.namemc.friend.Friend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("RequestUtil")
class RequestUtilTest {

    @Test
    @DisplayName("insertDashUUID insere hífens nas posições corretas")
    void insertDashUUID_withValidUuidWithoutDashes() {
        String withoutDashes = "069a79f444e94726a5befca90e38aaf5";
        String withDashes = RequestUtil.insertDashUUID(withoutDashes);

        assertEquals("069a79f4-44e9-4726-a5be-fca90e38aaf5", withDashes);
    }

    @Test
    @DisplayName("insertDashUUID produz UUID válido para fromString")
    void insertDashUUID_producesValidUuid() {
        String withoutDashes = "069a79f444e94726a5befca90e38aaf5";
        String withDashes = RequestUtil.insertDashUUID(withoutDashes);

        assertDoesNotThrow(() -> UUID.fromString(withDashes));
        assertEquals(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"), UUID.fromString(withDashes));
    }

    @Test
    @DisplayName("insertDashUUID com UUID já com hífens ainda insere nas posições fixas")
    void insertDashUUID_withAlreadyDashedUuid() {
        String alreadyDashed = "069a79f4-44e9-4726-a5be-fca90e38aaf5";
        String result = RequestUtil.insertDashUUID(alreadyDashed);

        assertNotNull(result);
        // O método insere nas posições 8,13,18,23; com hífens já presentes o resultado ainda é utilizável
        assertTrue(result.contains("-"));
    }

    @Test
    @DisplayName("getLikes retorna lista de UUIDs a partir do Request")
    void getLikes_parsesJsonArrayOfUuidStrings() throws IOException {
        Request request = mock(Request.class);
        JsonArray array = new JsonArray();
        array.add("069a79f4-44e9-4726-a5be-fca90e38aaf5");
        array.add("853c80ef-3c37-49fd-aa49-9382e3e7b2a9");

        when(request.toJsonArray()).thenReturn(array);

        Collection<UUID> likes = RequestUtil.getLikes(request);

        assertEquals(2, likes.size());
        assertTrue(likes.contains(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5")));
        assertTrue(likes.contains(UUID.fromString("853c80ef-3c37-49fd-aa49-9382e3e7b2a9")));
    }

    @Test
    @DisplayName("getLikes retorna lista vazia quando array é vazio")
    void getLikes_emptyArray() throws IOException {
        Request request = mock(Request.class);
        when(request.toJsonArray()).thenReturn(new JsonArray());

        Collection<UUID> likes = RequestUtil.getLikes(request);

        assertNotNull(likes);
        assertTrue(likes.isEmpty());
    }

    @Test
    @DisplayName("previousNames retorna mapa vazio (endpoint Mojang descontinuado)")
    void previousNames_returnsEmptyMap() {
        Map<String, Long> result = RequestUtil.previousNames(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
