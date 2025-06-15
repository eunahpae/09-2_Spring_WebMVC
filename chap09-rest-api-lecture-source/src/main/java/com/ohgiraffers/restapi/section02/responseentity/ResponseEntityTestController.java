package com.ohgiraffers.restapi.section02.responseentity;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entity")
public class ResponseEntityTestController {

    /*
     * [ResponseEntity]
     * - Spring Framework에서 응답 정보를 구성할 수 있는 객체이다.
     * - HttpStatus (상태 코드), HttpHeaders (응답 헤더), HttpBody (응답 본문)를 직접 설정할 수 있다.
     * - 컨트롤러에서 반환 값으로 사용하여 클라이언트에 더 명확하고 유연한 응답을 전달할 수 있다.
     */

    private List<UserDTO> users;

    public ResponseEntityTestController() {
        // 초기 사용자 목록 설정 (테스트용 데이터)
        users = new ArrayList<>();
        users.add(new UserDTO(1, "user01", "pass01", "다람쥐", new Date()));
        users.add(new UserDTO(2, "user02", "pass02", "코알라", new Date()));
        users.add(new UserDTO(3, "user03", "pass03", "판다", new Date()));
    }

    /* 전체 회원 조회 API */
    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers() {

        // 1. 응답 헤더 설정 (JSON 형식, UTF-8 인코딩)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        // 2. 응답 바디에 포함할 데이터 설정
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", users); // 전체 사용자 리스트 추가

        // 3. 커스텀 응답 메시지 객체 생성
        ResponseMessage responseMessage = new ResponseMessage(200, "조회 성공", responseMap);

        // 4. ResponseEntity로 응답 (헤더 + 바디 + 상태코드)
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    /* 회원 상세 조회 API (PathVariable로 특정 회원 조회) */
    @GetMapping("/users/{userNo}")
    public ResponseEntity<ResponseMessage> findUserByNo(@PathVariable int userNo) {

        // 1. 응답 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        // 2. userNo에 해당하는 사용자 찾기
        UserDTO foundUser = users.stream()
            .filter(user -> user.getNo() == userNo)
            .collect(Collectors.toList())
            .get(0); // 존재하지 않으면 예외 발생 가능 (간단한 테스트용 구현)

        // 3. 사용자 정보를 응답 바디에 포함
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("user", foundUser);

        // 4. ResponseEntity를 이용해 응답 구성
        return ResponseEntity
            .ok() // HTTP 200 OK
            .headers(headers)
            .body(new ResponseMessage(200, "조회 성공", responseMap));
    }

    /* 신규 회원 등록 API */
    @PostMapping("/users")
    public ResponseEntity<?> registUser(@RequestBody UserDTO newUser) {

        System.out.println("newUser = " + newUser);

        // 새로운 사용자 번호 지정 (가장 마지막 번호 + 1)
        int lastUserNo = users.get(users.size() - 1).getNo();
        newUser.setNo(lastUserNo + 1);

        // 사용자 리스트에 추가
        users.add(newUser);

        // ResponseEntity.created(): HTTP 201 상태 + Location 헤더 포함
        // 신규 리소스의 URI를 Location에 명시
        return ResponseEntity
            .created(URI.create("/entity/users/" + newUser.getNo()))
            .build(); // 응답 바디는 비워도 됨
    }

    /* 기존 회원 정보 수정 API */
    @PutMapping("/users/{userNo}")
    public ResponseEntity<?> modifyUser(@PathVariable int userNo, @RequestBody UserDTO modifyInfo) {

        // userNo에 해당하는 기존 사용자 정보 조회
        UserDTO foundUser = users.stream()
            .filter(user -> user.getNo() == userNo)
            .collect(Collectors.toList())
            .get(0); // 존재하지 않으면 예외 발생 가능

        // 사용자 정보 수정
        foundUser.setId(modifyInfo.getId());
        foundUser.setPwd(modifyInfo.getPwd());
        foundUser.setName(modifyInfo.getName());

        // 수정 후에도 ResponseEntity.created() 사용 (수정된 자원 위치 반환)
        return ResponseEntity
            .created(URI.create("/entity/users/" + userNo))
            .build(); // 바디 없이 Location 헤더만 포함
    }

    /* 회원 삭제 API */
    @DeleteMapping("/users/{userNo}")
    public ResponseEntity<?> removeUser(@PathVariable int userNo) {

        // 삭제 대상 사용자 조회 (람다식 사용)
        UserDTO foundUser = users.stream()
            .filter(user -> user.getNo() == userNo)
            .collect(Collectors.toList())
            .get(0);

        // 리스트에서 사용자 제거
        users.remove(foundUser);

        // HTTP 204 No Content 응답 (응답 바디 없이 성공 표시)
        return ResponseEntity
            .noContent()
            .build();


//        // 삭제 대상 사용자 찾기 (반복문 사용)
//        UserDTO foundUser = null;
//        for (UserDTO user : users) {
//            if (user.getNo() == userNo) {
//                foundUser = user;
//                break; // 찾았으면 반복문 종료
//            }
//        }
//
//        // 사용자가 존재하면 삭제
//        if (foundUser != null) {
//            users.remove(foundUser);
//            // HTTP 204 No Content 응답 반환
//            return ResponseEntity
//                .noContent()
//                .build();
//        } else {
//            // 사용자가 존재하지 않는 경우 404 Not Found 응답 반환
//            return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .body("해당 사용자를 찾을 수 없습니다.");
//        }
    }

}
