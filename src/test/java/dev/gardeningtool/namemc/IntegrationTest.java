package dev.gardeningtool.namemc;

import dev.gardeningtool.namemc.profile.Profile;
import dev.gardeningtool.namemc.server.Server;
import dev.gardeningtool.namemc.web.RequestUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração que chamam as APIs reais (Mojang, NameMC, OptiFine).
 * Não rodam por padrão no {@code mvn test}. Para rodar: remova o exclude de
 * IntegrationTest no pom.xml ou use o profile "integration".
 */
@Disabled("Integração com APIs externas - habilitar manualmente para validar")
@DisplayName("Integração com APIs (Mojang / NameMC)")
class IntegrationTest {

    private static final UUID NOTCH_UUID = UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5");
    private static final String NOTCH_USERNAME = "Notch";

    @Test
    @DisplayName("RequestUtil.username retorna nome para UUID válido (Notch)")
    void username_fromValidUuid() {
        String name = RequestUtil.username(NOTCH_UUID);
        assertNotNull(name);
        assertEquals(NOTCH_USERNAME, name);
    }

    @Test
    @DisplayName("RequestUtil.uuid retorna UUID para username válido (Notch)")
    void uuid_fromValidUsername() {
        UUID uuid = RequestUtil.uuid(NOTCH_USERNAME);
        assertNotNull(uuid);
        assertEquals(NOTCH_UUID, uuid);
    }

    @Test
    @DisplayName("Profile por username carrega dados básicos")
    void profile_byUsername_hasBasicData() {
        Profile profile = new Profile(NOTCH_USERNAME);

        assertEquals(NOTCH_USERNAME, profile.getUsername());
        assertEquals(NOTCH_UUID, profile.getUuid());
        assertNotNull(profile.getFriends());
        assertNotNull(profile.getPreviousNames());
        assertTrue(profile.getCachedTimestamp() > 0);
    }

    @Test
    @DisplayName("Profile por UUID carrega mesmo username")
    void profile_byUuid_hasSameUsername() {
        Profile byUuid = new Profile(NOTCH_UUID);
        Profile byUsername = new Profile(NOTCH_USERNAME);

        assertEquals(byUsername.getUsername(), byUuid.getUsername());
        assertEquals(byUsername.getUuid(), byUuid.getUuid());
    }

    @Test
    @DisplayName("Server retorna coleção de likes (exemplo: purpleprison)")
    void server_likes_isCollection() {
        Server server = new Server("purpleprison.net");

        assertNotNull(server.getLikedUsers());
        // hasLiked depende de quem já deu like; apenas garantimos que não quebra
        assertFalse(server.hasLiked(UUID.randomUUID()));
    }
}
