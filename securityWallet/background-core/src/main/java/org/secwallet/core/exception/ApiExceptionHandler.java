package org.secwallet.core.exception;


import org.secwallet.core.model.Result;
import org.secwallet.core.model.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.FileNotFoundException;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;

/**
 *
 **/
@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {


    @ExceptionHandler(BizException.class)
    @ResponseBody
    public Result<String> handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        int code = ResultCode.SYSTEM_INNER_ERROR.getCode();
        String msg = null;
        if (ex instanceof HttpMessageNotReadableException) {
            code = ResultCode.PARAM_IS_INVALID.getCode();
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            code = ResultCode.METHOD_NOT_ALLOWED.getCode();
        } else if (ex instanceof MaxUploadSizeExceededException) {
            log.info(String.format("code: %d  msg: %s", code, msg));
            return Result.fail(ResultCode.FILE_UPLOAD_OVER_SIZE);
        }

        if (ex instanceof BizException) {
            BizException bizException = (BizException) ex;
            msg = bizException.getMessage();
            code = bizException.getResultCode().getCode();
        } else if (ex instanceof AccessDeniedException) {
            msg = ResultCode.PERMISSION_NO_ACCESS.getMsg();
            code = ResultCode.PERMISSION_NO_ACCESS.getCode();
        } else if (ex instanceof FileNotFoundException) {
            msg = ResultCode.FILE_NOT_FOUND.getMsg();
            code = ResultCode.FILE_NOT_FOUND.getCode();
        } else if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException exs = (ConstraintViolationException) ex;
            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<?> item : violations) {
                stringBuilder.append(item.getPropertyPath()).append(item.getMessage()).append(",");
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            msg = stringBuilder.toString();
            code = ResultCode.PARAM_IS_INVALID.getCode();
            stringBuilder.setLength(0);
        } else if (ex instanceof UndeclaredThrowableException) {
            msg = ((UndeclaredThrowableException) ex).getUndeclaredThrowable().getCause().getMessage();
        }
        if (msg == null) {
            msg = ex.getMessage();
        }
        Result<String> resp = new Result<>();
        resp.setCode(code);
        resp.setMessage(msg);
        resp.setData("");
        if (ex instanceof BizException || ex instanceof AccessDeniedException
                || ex instanceof FileNotFoundException || ex instanceof ConstraintViolationException
        ) {
            log.info(String.format("code: %d  msg: %s", code, msg));
        } else {
            log.error(String.format("code: %d  msg: %s", code, msg), ex);
        }
        return resp;
    }


    @ExceptionHandler(DuplicateKeyException.class)
    public Result<String> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return Result.fail(ResultCode.DUPLICATE_KEY);
    }

//    @ExceptionHandler(AuthorizationException.class)
//    public ApiResult<String> handleAuthorizationException(AuthorizationException e) {
//        log.error(e.getMessage(), e);
//        return ApiResultBuilder.failed(ResultCode.UNAUTHORIZED);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<String> validationError(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> errorList = bindingResult.getFieldErrors();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : errorList) {
            if (fieldError.getField().indexOf('[') != -1) {
                continue;
            }
            // 获取@ApiModelProperty 注解里的value作为字段的中文名
            ApiModelProperty apiModelProperty = null;
            try {
                apiModelProperty = Class.forName(ex.getParameter().getGenericParameterType().getTypeName()).getDeclaredField(fieldError.getField()).getAnnotation(ApiModelProperty.class);
            } catch (NoSuchFieldException | ClassNotFoundException e) {
                log.error("Handling parity error exceptions:", e);
            }
            stringBuilder.append(apiModelProperty == null ? "" : apiModelProperty.value()).append(fieldError.getDefaultMessage()).append(",");
        }

        for (ObjectError fieldError : bindingResult.getGlobalErrors()) {
            stringBuilder.append(fieldError);
        }
        return getFromStringBuilder(stringBuilder);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> bindError(BindException ex) {
        List<FieldError> errorList = ex.getBindingResult().getFieldErrors();
        StringBuilder stringBuilder = new StringBuilder();
        Object exTarget = ex.getTarget();
        if (exTarget == null) {
            stringBuilder.append("sysError,exTarget is null!");
            return getFromStringBuilder(stringBuilder);
        }
        for (FieldError fieldError : errorList) {
            // 获取@ApiModelProperty 注解里的value作为字段的中文名
            ApiModelProperty apiModelProperty = null;
            try {
                apiModelProperty = exTarget.getClass().getDeclaredField(fieldError.getField()).getAnnotation(ApiModelProperty.class);
            } catch (NoSuchFieldException e) {
                log.error("Handling parity error exceptions:", e);
            }
            stringBuilder.append(apiModelProperty == null ? "" : apiModelProperty.value()).append(fieldError.getDefaultMessage()).append(",");
        }
        return getFromStringBuilder(stringBuilder);
    }

    private Result<String> getFromStringBuilder(StringBuilder stringBuilder) {
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        Result<String> resp = new Result<>();
        resp.setCode(ResultCode.PARAM_IS_INVALID.getCode());
        resp.setMessage(stringBuilder.toString());
        stringBuilder.setLength(0);
        return resp;
    }
}
