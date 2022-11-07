package com.adlet.SocialNetwork.exception;

import com.adlet.SocialNetwork.model.response.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;


@ControllerAdvice
public class AppExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {SocialNetworkPostServiceException.class})
    public ResponseEntity<Object> handleSocialNetworkPostServiceException(SocialNetworkPostServiceException ex, WebRequest request){
        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getLocalizedMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherPostServiceException(Exception ex, WebRequest request){
        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getLocalizedMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
