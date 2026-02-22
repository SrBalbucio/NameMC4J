package dev.gardeningtool.namemc.friend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Friend")
class FriendTest {

    private static final UUID UUID_TEST = UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5");
    private static final String USERNAME = "Notch";

    @Test
    @DisplayName("armazena UUID e username corretamente")
    void constructorAndGetters() {
        Friend friend = new Friend(UUID_TEST, USERNAME);

        assertEquals(UUID_TEST, friend.getUuid());
        assertEquals(USERNAME, friend.getUsername());
    }

    @Test
    @DisplayName("dois Friends iguais têm mesmo UUID e username")
    void equalityByFields() {
        Friend a = new Friend(UUID_TEST, USERNAME);
        Friend b = new Friend(UUID_TEST, USERNAME);

        assertEquals(a.getUuid(), b.getUuid());
        assertEquals(a.getUsername(), b.getUsername());
    }

    @Test
    @DisplayName("aceita username vazio")
    void acceptsEmptyUsername() {
        Friend friend = new Friend(UUID_TEST, "");

        assertEquals("", friend.getUsername());
        assertEquals(UUID_TEST, friend.getUuid());
    }
}
