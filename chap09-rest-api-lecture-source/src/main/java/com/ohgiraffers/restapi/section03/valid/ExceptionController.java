package com.ohgiraffers.restapi.section03.valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * [예외 처리 컨트롤러]
 * 컨트롤러 전역에서 발생하는 예외를 공통으로 처리하는 클래스
 * @ControllerAdvice 어노테이션을 통해 전역 예외 처리기를 등록한다.
 */
@ControllerAdvice
public class ExceptionController {

    /**
     * [UserNotFoundException 처리 메서드]
     * 사용자가 존재하지 않을 경우 발생하는 커스텀 예외 처리
     *
     * @param e UserNotFoundException
     * @return ResponseEntity 형태의 에러 응답 객체 반환 (HTTP 404 Not Found)
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(UserNotFoundException e) {

        // 에러 코드 및 메시지 정의
        String code = "ERROR_CODE_00001";
        String description = "회원정보 조회 실패";
        String detail = e.getMessage();  // 예외에 담긴 상세 메시지 사용

        // 에러 응답 객체 생성 후 HTTP 상태 코드와 함께 반환
        return new ResponseEntity<>(new ErrorResponse(code, description, detail), HttpStatus.NOT_FOUND);
    }

    /**
     * [MethodArgumentNotValidException 처리 메서드]
     * @Valid 또는 @Validated 유효성 검사 실패 시 발생하는 예외 처리
     *
     * @param e MethodArgumentNotValidException
     * @return ResponseEntity 형태의 에러 응답 객체 반환 (HTTP 400 Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e) {

        // 에러 코드 및 메시지 초기화
        String code = "";
        String description = "";
        String detail = "";

        // 유효성 검사 오류가 존재할 경우
        if(e.getBindingResult().hasErrors()) {

            // 첫 번째 필드 오류 메시지를 상세 내용으로 설정
            detail = e.getBindingResult().getFieldError().getDefaultMessage();

            // 유효성 실패의 유형 코드 추출 (예: NotBlank, Size 등)
            String bindResultCode = e.getBindingResult().getFieldError().getCode();

            // 오류 유형에 따라 커스텀 에러 코드 및 설명 설정
            switch (bindResultCode) {
                case "NotBlank":
                    code = "ERROR_CODE_00002";
                    description = "필수 값이 누락되었습니다.";
                    break;
                case "Size":
                    code = "ERROR_CODE_00003";
                    description = "글자 수를 확인해야 합니다.";
                    break;
            }
        }

        // 에러 응답 객체 생성 후 HTTP 상태 코드와 함께 반환
        return new ResponseEntity<>(new ErrorResponse(code, description, detail), HttpStatus.BAD_REQUEST);
    }
}
