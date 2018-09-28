package com.genie.core.web.rest.errors;

import com.genie.core.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
public abstract class AbstractExceptionTranslator {

    private final Logger log = LoggerFactory.getLogger(AbstractExceptionTranslator.class);

    private final MessageSource messageSource;

    protected AbstractExceptionTranslator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ConcurrencyFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorVM processConcurrencyError(HttpServletRequest request, ConcurrencyFailureException ex) {
        ErrorVM error = new ErrorVM(ErrorConstants.ERR_CONCURRENCY_FAILURE, localTranslate(request.getLocale(), ErrorConstants.ERR_CONCURRENCY_FAILURE));
        error.setCause(ex.getMessage());
        log.error("ConcurrencyFailureException[" + error.getErrorId() + "] : " + ex.getMessage(), ex);
        return error;
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ResponseBody
//    public ErrorVM processValidationError(HttpServletRequest request, MethodArgumentNotValidException ex) {
//        BindingResult result = ex.getBindingResult();
//        List<FieldError> fieldErrors = result.getFieldErrors();
//
//        ErrorVM error = new ErrorVM(ErrorConstants.ERR_VALIDATION, localTranslate(request.getLocale(), ErrorConstants.ERR_VALIDATION));
//        for (FieldError fieldError : fieldErrors) {
//            FieldErrorVM field = new FieldErrorVM(
//                fieldError.getObjectName(),
//                fieldError.getField(),
//                fieldError.getCode(),
//                fieldError.getArguments(),
//                fieldError.getDefaultMessage()
//            );
//            error.add(field);
//        }
//        log.error("MethodArgumentNotValidException[" + error.getErrorId() + "] : " + ex.getMessage(), ex);
//        return error;
//    }

    /**
     * 处理参数校验异常，多个字段错误转换成错误数组
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorVM processValidationError(HttpServletRequest request, MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        List<String> messages = new ArrayList<>();
        List<String> descriptions = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {
            messages.add(fieldError.getField() + "." + fieldError.getCode());
            descriptions.add(fieldError.getDefaultMessage());
        }
        ErrorVM error = new ErrorVM(messages, descriptions, "", null);
        log.error("MethodArgumentNotValidException[" + error.getErrorId() + "] : " + ex.getMessage(), ex);
        return error;
    }

    @ExceptionHandler(CustomParameterizedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorVM processParameterizedValidationError(CustomParameterizedException ex) {
        ErrorVM error = new ErrorVM(ex.getMessage());
        error.setParams(ex.getErrorVM().getParams());
        log.error("CustomParameterizedException[" + error.getErrorId() + "] : " + ex.getMessage(), ex.getMessage());
        return error;
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorVM processServiceError(HttpServletRequest request, ServiceException ex) {
        ErrorVM error = new ErrorVM(ex.getErrorCode(), localTranslate(request.getLocale(), ex.getErrorCode(), ex.getErrorParms()));
        log.error("ServiceException[" + error.getErrorId() + "]: " + ex.getMessage(), ex);
        return error;
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorVM processMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ErrorVM error = new ErrorVM(ErrorConstants.ERR_METHOD_NOT_SUPPORTED, ex.getMessage());
        log.error("HttpRequestMethodNotSupportedException[" + error.getErrorId() + "]: " + ex.getMessage(), ex);
        return error;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorVM> processException(HttpServletRequest request, Exception ex) {
        BodyBuilder builder;
        ErrorVM error;
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            builder = ResponseEntity.status(responseStatus.value());
            error = new ErrorVM("error." + responseStatus.value().value(), responseStatus.reason());
        } else {
            builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            error = new ErrorVM(ErrorConstants.ERR_INTERNAL_SERVER_ERROR, localTranslate(request.getLocale(), ErrorConstants.ERR_INTERNAL_SERVER_ERROR));
            error.setCause(ex.getMessage());
        }
        log.error("Exception[" + error.getErrorId() + "]: " + ex.getMessage(), ex);
        return builder.body(error);
    }

    protected String localTranslate(Locale language, String errorCode, String... params) {
        try {
            return messageSource.getMessage(errorCode, params, language);
        } catch (NoSuchMessageException ex) {
            return "";
        }
    }
}
