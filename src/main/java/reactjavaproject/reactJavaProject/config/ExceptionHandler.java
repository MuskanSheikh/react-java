package reactjavaproject.reactJavaProject.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import reactjavaproject.reactJavaProject.web.dto.ErrorResponse;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleError(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        if (e instanceof ResponseStatusException) {
            ResponseStatusException re= (ResponseStatusException) e;
            int statusCode =re.getStatusCode().value();
            ErrorResponse errorResponse = new ErrorResponse(String.valueOf(statusCode),re.getReason(), re.getReason());
            return buildResponseEntityNew(errorResponse, (HttpStatus) ((ResponseStatusException) e).getStatusCode());
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode("500");
            errorResponse.setMessage("Please contact to your administrator");
            errorResponse.setDescription(e.getMessage());
            return buildResponseEntityNew(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private ResponseEntity<Object> buildResponseEntityNew(ErrorResponse apiError, HttpStatus status) {
        return new ResponseEntity<Object>(apiError, status);
    }
}
