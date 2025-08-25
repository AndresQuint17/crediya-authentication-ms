package co.com.myproject.api.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

public class CustomAttibutes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = new HashMap<>();
        Throwable throwable = super.getError(request);

        if (throwable instanceof UserAlreadyExistsException userAlreadyExistsException) {
            errorAttributes.put("status", userAlreadyExistsException.getStatus());
            errorAttributes.put("message", userAlreadyExistsException.getMessage());
        }
        return errorAttributes;
    }
}
