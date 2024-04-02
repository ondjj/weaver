package com.task.weaver.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 상태코드와 데이터, 메시지를 반환할 경우 응답 형식
 *
 * @param <T> 응답 데이터를 담는 dto가 주로 들어감
 */
@Getter
public class DataResponse<T> extends DefaultResponse{

    private final T data;
    private final Boolean isSuccess;

    protected DataResponse(HttpStatus status, String message, T data, Boolean isSuccess) {
        super(status.value(), message);
        this.data = data;
        this.isSuccess = isSuccess;
    }

    public static <T> DataResponse<T> of(HttpStatus status, String message, T data, Boolean isSuccess) {
        return new DataResponse<>(status, message, data, isSuccess);
    }
}
