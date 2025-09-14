package co.com.myproject.r2dbc.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleEntityTest {
    @Test
    void mustCreateRoleWithBuilder() {
        RoleEntity role = RoleEntity.builder()
                .id(1L)
                .name("ADMIN")
                .description("Administrator role with all permissions")
                .build();

        assertAll(
                () -> assertEquals(1L, role.getId()),
                () -> assertEquals("ADMIN", role.getName()),
                () -> assertEquals("Administrator role with all permissions", role.getDescription())
        );
    }

    @Test
    void mustCreateRoleWithAllArgsConstructor() {
        RoleEntity role = new RoleEntity(
                1L,
                "USER",
                "Regular user with limited permissions"
        );

        assertEquals(1L, role.getId());
        assertEquals("USER", role.getName());
        assertEquals("Regular user with limited permissions", role.getDescription());
    }

    @Test
    void mustCreateRoleWithNoArgsConstructorAndSetters() {
        RoleEntity role = new RoleEntity();
        role.setId(2L);
        role.setName("MANAGER");
        role.setDescription("Manages users and subscriptions");

        assertEquals(2L, role.getId());
        assertEquals("MANAGER", role.getName());
        assertEquals("Manages users and subscriptions", role.getDescription());
    }

    @Test
    void mustCreateNewRoleFromToBuilder() {
        RoleEntity role = RoleEntity.builder()
                .id(3L)
                .name("SUPPORT")
                .description("Support role")
                .build();

        RoleEntity updated = role.toBuilder()
                .description("Updated support role")
                .build();

        assertEquals("SUPPORT", updated.getName());
        assertEquals("Updated support role", updated.getDescription());
        assertNotSame(role, updated);
    }

    @Test
    void mustAllowNullValues() {
        RoleEntity role = RoleEntity.builder()
                .id(null)
                .name(null)
                .description(null)
                .build();

        assertNull(role.getId());
        assertNull(role.getName());
        assertNull(role.getDescription());
    }
}