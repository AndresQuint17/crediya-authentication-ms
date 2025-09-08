package co.com.myproject.model.role.gateways;

import co.com.myproject.model.role.Role;
import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Role> getUserRoleNameByIdRol(Long idRol);
    Mono<Boolean> isRoleExisting(Long idRol);
}
