package dev.gardeningtool.namemc.web;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.gardeningtool.namemc.friend.Friend;
import lombok.SneakyThrows;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class RequestUtil {

    /**
     * UUID to username. Uses the documented endpoint.
     * @see <a href="https://mojang-api-docs.gapple.pw/no-auth/uuid-to-username">UUID to username</a>
     * @param uuid The UUID of the target to obtain the username of
     * @return The username associated with the target UUID
     * @throws IllegalArgumentException If the UUID is invalid (204 No Content, 400 Bad Request, or empty response)
     */
    @SneakyThrows
    public static String username(UUID uuid) throws IllegalArgumentException {
        Request request = getRequest("https://api.mojang.com/user/profile/" + uuid.toString());
        Map<?, ?> map = request.map();
        if (map == null || !map.containsKey("name")) {
            throw new IllegalArgumentException(String.format("%s is not a valid UUID!", uuid.toString()));
        }
        return map.get("name").toString();
    }

    /**
     * Username to UUID (GET). Uses the documented endpoint.
     * @see <a href="https://mojang-api-docs.gapple.pw/no-auth/username-to-uuid-get">Username to UUID (GET)</a>
     * @param username The username associated with a UUID
     * @return The UUID of the specified argument
     * @throws IllegalArgumentException If the username is invalid (204 No Content, 400 Bad Request, or empty response)
     */
    @SneakyThrows
    public static UUID uuid(String username) throws IllegalArgumentException {
        Request request = getRequest("https://api.mojang.com/users/profiles/minecraft/" + username);
        Map<?, ?> map = request.map();
        if (map == null || !map.containsKey("id")) {
            throw new IllegalArgumentException(String.format("%s is not a valid username!", username));
        }
        return UUID.fromString(insertDashUUID(map.get("id").toString()));
    }

    /**
     * @param request the request to parse the likes of
     */
    @SneakyThrows
    public static Collection<UUID> getLikes(Request request) {
        JsonArray array = request.toJsonArray();
        List<UUID> uuids = new ArrayList<>();
        for(JsonElement element : array) {
            uuids.add(UUID.fromString(element.getAsString()));
        }
        return uuids;
    }

    /**
     * @param uuid The UUID of the player to obtain the friends of
     * @return The players that the player in the argument has added on NameMC
     */
    @SneakyThrows
    public static List<Friend> friends(UUID uuid) {
        Request request = getRequest(String.format("https://api.namemc.com/profile/%s/friends/", uuid.toString()));
        JsonArray array = request.toJsonArray();
        List<Friend> friends = new ArrayList<>();
        for(JsonElement element : array) {
            JsonObject object = element.getAsJsonObject();
            friends.add(new Friend(UUID.fromString(object.get("uuid").getAsString()), object.get("name").getAsString()));
        }
        return friends;
    }

    /**
     * Name history for the given UUID. The Mojang endpoint for this was removed in September 2022,
     * so this method always returns an empty map. It is kept for API compatibility.
     * @param uuid The UUID of the player
     * @return An empty map (Mojang no longer provides name history publicly)
     */
    public static Map<String, Long> previousNames(UUID uuid) {
        return Collections.emptyMap();
    }

    @SneakyThrows
    private static Request getRequest(String url) {
        Request request = new Request(url);
        request.prepare();
        request.connect();
        return request;
    }

    /**
     * @author KingTux on SpigotMC
     * @param uuid The UUID of the target
     * @return A String with hyphens inserted
     */
    public static String insertDashUUID(String uuid) {
        StringBuilder sb = new StringBuilder(uuid);
        sb.insert(8, "-");
        sb = new StringBuilder(sb.toString());
        sb.insert(13, "-");
        sb = new StringBuilder(sb.toString());
        sb.insert(18, "-");
        sb = new StringBuilder(sb.toString());
        sb.insert(23, "-");
        return sb.toString();
    }

    /**
     * @param username The target username
     * @return Whether or not the player has an OptiFine cape
     */
    @SneakyThrows
    public static boolean optifineCape(String username) {
        URL url = new URL(String.format("http://s.optifine.net/capes/%s.png", username));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        return connection.getResponseCode() != 404;
    }
}
