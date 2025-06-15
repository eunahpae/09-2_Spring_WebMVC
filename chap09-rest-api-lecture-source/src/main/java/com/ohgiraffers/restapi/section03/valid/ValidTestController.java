package com.ohgiraffers.restapi.section03.valid;

import com.ohgiraffers.restapi.section02.responseentity.ResponseMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/valid")
public class ValidTestController {

    private List<UserDTO> users;

    public ValidTestController() {

        users = new ArrayList<>();

        users.add(new UserDTO(1, "user01", "pass01", "다람쥐", new Date()));
        users.add(new UserDTO(2, "user02", "pass02", "코알라", new Date()));
        users.add(new UserDTO(3, "user03", "pass03", "판다", new Date()));
    }

    /* 회원 상세 조회 */
    @GetMapping("/users/{userNo}")
    public ResponseEntity<ResponseMessage> findUserByNo(@PathVariable int userNo) throws UserNotFoundException {

        /* 응답 헤더 설정 */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        List<UserDTO> foundUserList =
                users.stream().filter(user -> user.getNo() == userNo).collect(Collectors.toList());

        UserDTO foundUser = null;
        if(foundUserList.size() > 0) {
            foundUser = foundUserList.get(0);
        } else {
            throw new UserNotFoundException("회원 정보를 찾을 수 없습니다.");
        }

        /* 응답 데이터 설정 */
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("user", foundUser);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new ResponseMessage(200, "조회 성공", responseMap));
    }

    @PostMapping("/users")
    public ResponseEntity<?> registUser(@RequestBody @Valid UserDTO newUser) {

        System.out.println("newUser = " + newUser);

        return ResponseEntity
                .created(URI.create("/valid/users/" + newUser.getNo()))
                .build();

    }
}
