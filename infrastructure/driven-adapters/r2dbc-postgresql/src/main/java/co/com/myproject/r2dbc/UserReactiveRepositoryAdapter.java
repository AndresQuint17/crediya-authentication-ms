package co.com.myproject.r2dbc;

import co.com.myproject.model.user.User;
import co.com.myproject.model.user.gateways.UserRepository;
import co.com.myproject.r2dbc.entity.UserEntity;
import co.com.myproject.r2dbc.helper.ReactiveAdapterOperations;
import co.com.myproject.model.exceptions.RoleDoesNotExistException;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Long,
        UserReactiveRepository
        > implements UserRepository {
    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, domainEntity -> mapper.map(domainEntity, User.class));
    }

    @Override
    public Mono<User> registerUser(User user) {
        return super.save(user);
    }

    @Override
    public Mono<User> updateUser(User user) {
        return null;
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(entity -> mapper.map(entity, User.class));
    }

    @Override
    public Mono<User> findByIdCard(String idCard) {
        return repository.findByIdCard(idCard)
                .map(entity -> mapper.map(entity, User.class));
    }

    @Override
    public Mono<Long> getUserRole(String email) {
        return repository.findByEmail(email)
                .flatMap(userEntity -> {
                    if (userEntity.getRoleId()==null) {
                        return Mono.error(new RoleDoesNotExistException(
                                404,
                                "User does not have a role assigned"
                        ));
                    }
                    return Mono.just(userEntity.getRoleId());
                });
    }

    @Override
    public Mono<String> getUserCredentials(String idCard) {
        return repository.findByIdCard(idCard)
                .flatMap(userEntity -> Mono.just(userEntity.getPassword()));
    }
}
