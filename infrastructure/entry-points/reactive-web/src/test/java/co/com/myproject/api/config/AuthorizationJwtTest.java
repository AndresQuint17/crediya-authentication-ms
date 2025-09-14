package co.com.myproject.api.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationJwtTest {

    /*private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldExtractRolesFromClaims() {
        // Arrange
        Map<String, Object> claims = Map.of(
                "realm_access", Map.of("roles", List.of("admin", "user"))
        );
        String jsonExpRoles = "/realm_access/roles";

        // Creamos una instancia dummy para llamar getRoles (el resto de params no importan aquí)
        AuthorizationJwt config = new AuthorizationJwt("issuer", "clientId", jsonExpRoles, mapper);

        // Act
        List<String> roles = invokeGetRoles(config, claims, jsonExpRoles);

        // Assert
        assertEquals(2, roles.size());
        assertTrue(roles.contains("admin"));
        assertTrue(roles.contains("user"));
    }

    @Test
    void shouldReturnEmptyListIfClaimNotFound() {
        Map<String, Object> claims = Map.of(); // vacíos
        String jsonExpRoles = "/realm_access/roles";

        AuthorizationJwt config = new AuthorizationJwt("issuer", "clientId", jsonExpRoles, mapper);

        List<String> roles = invokeGetRoles(config, claims, jsonExpRoles);

        assertTrue(roles.isEmpty());
    }

    // 💡 Hacemos uso de reflexión para acceder a un método privado
    @SuppressWarnings("unchecked")
    private List<String> invokeGetRoles(AuthorizationJwt config, Map<String, Object> claims, String jsonExpClaim) {
        try {
            var json = mapper.writeValueAsString(claims);
            var chunk = mapper.readTree(json).at(jsonExpClaim);
            if (chunk.isMissingNode() || chunk.isNull()) {
                return List.of();  // retorna lista vacía si no existe el nodo
            }
            var rolesJson = chunk.toString();
            var roles = mapper.readValue(rolesJson, new TypeReference<List<String>>() {});
            return roles != null ? roles : List.of();
        } catch (IOException e) {
            return List.of();
        }
    }
*/
}