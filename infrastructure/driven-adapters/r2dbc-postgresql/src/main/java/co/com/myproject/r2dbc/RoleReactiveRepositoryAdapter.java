package co.com.myproject.r2dbc;

import co.com.myproject.model.role.Role;
import co.com.myproject.model.role.gateways.RoleRepository;
import co.com.myproject.r2dbc.entity.RoleEntity;
import co.com.myproject.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class RoleReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Role,
        RoleEntity,
        Long,
        RoleReactiveRepository
        > implements RoleRepository {
    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, domainEntity -> mapper.map(domainEntity, Role.class));
    }

    @Override
    public Mono<Role> getUserRoleNameByIdRol(Long idRol) {
        return super.findById(idRol);
    }
}
