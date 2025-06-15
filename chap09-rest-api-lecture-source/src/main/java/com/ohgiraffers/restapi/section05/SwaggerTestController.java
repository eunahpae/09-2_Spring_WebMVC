package com.ohgiraffers.restapi.section05;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Swagger 테스트용 REST 컨트롤러
 *
 * - Swagger(OpenAPI) 어노테이션을 활용하여 API 명세를 문서화
 * - 회원 데이터에 대한 CRUD 기능 제공
 */
@Tag(name = "Spring Boot Swagger 연동 (user)")  // Swagger UI 에서 해당 컨트롤러 구분 태그
@RestController
@RequestMapping("/swagger")  // 모든 엔드포인트의 공통 Prefix 설정
public class SwaggerTestController {

    private List<UserDTO> users;

    public SwaggerTestController() {
        users = new ArrayList<>();
        users.add(new UserDTO(1, "user01", "pass01", "다람쥐", new Date()));
        users.add(new UserDTO(2, "user02", "pass02", "코알라", new Date()));
        users.add(new UserDTO(3, "user03", "pass03", "판다", new Date()));
    }

    /**
     * 전체 회원 목록 조회
     * @return 전체 회원 리스트(JSON)
     *
     * - Swagger UI에서 "전체 회원 조회"로 표시됨
     */
    @Operation(summary = "전체 회원 조회", description = "전체 회원 목록을 조회한다.")
    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers() {

        // Content-Type: application/json; charset=UTF-8
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        // 응답 데이터 구성
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", users);

        ResponseMessage responseMessage = new ResponseMessage(200, "조회 성공", responseMap);

        // 200 OK 응답 반환
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    /**
     * 회원번호로 회원 상세 조회
     * @param userNo 조회할 회원 번호
     * @return 해당 회원 정보
     */
    @Operation(summary = "회원번호로 회원 조회", description = "회원번호를 통해 해당하는 회원 정보를 조회한다.")
    @GetMapping("/users/{userNo}")
    public ResponseEntity<ResponseMessage> findUserByNo(@PathVariable int userNo) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        // userNo에 해당하는 사용자 조회
        UserDTO foundUser = users.stream()
            .filter(user -> user.getNo() == userNo)
            .collect(Collectors.toList())
            .get(0);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("user", foundUser);

        return ResponseEntity
            .ok()
            .headers(headers)
            .body(new ResponseMessage(200, "조회 성공", responseMap));
    }

    /**
     * 신규 회원 등록
     * @param newUser 등록할 사용자 정보 (요청 본문)
     * @return 201 Created 상태 응답
     *
     * - Swagger 에서는 @RequestBody에 자동으로 필드 설명을 붙일 수 있음
     */
    @Operation(summary = "신규 회원 등록")
    @PostMapping("/users")
    public ResponseEntity<?> registUser(@RequestBody UserDTO newUser) {

        System.out.println("newUser = " + newUser);

        // 새로운 회원 번호 설정 (가장 마지막 번호 + 1)
        int lastUserNo = users.get(users.size() - 1).getNo();
        newUser.setNo(lastUserNo + 1);

        users.add(newUser);

        // 201 Created 응답 + Location 헤더에 신규 리소스 URI 포함
        return ResponseEntity
            .created(URI.create("/entity/users/" + newUser.getNo()))
            .build();
    }

    /**
     * 기존 회원 정보 수정
     * @param userNo 수정할 회원 번호
     * @param modifyInfo 수정할 회원 정보
     * @return 수정 완료 응답
     *
     * - PUT은 전체 자원 대체 의미 (기존 정보 전체를 덮어쓴다고 가정)
     */
    @Operation(summary = "회원 정보 수정")
    @PutMapping("/users/{userNo}")
    public ResponseEntity<?> modifyUser(@PathVariable int userNo, @RequestBody UserDTO modifyInfo) {

        // 기존 사용자 조회
        UserDTO foundUser = users.stream()
            .filter(user -> user.getNo() == userNo)
            .collect(Collectors.toList())
            .get(0);

        // 사용자 정보 수정
        foundUser.setId(modifyInfo.getId());
        foundUser.setPwd(modifyInfo.getPwd());
        foundUser.setName(modifyInfo.getName());

        // 수정 완료 후 리소스 URI 반환 (201 Created 사용은 예외적; 일반적으론 200 또는 204 사용)
        return ResponseEntity
            .created(URI.create("/entity/users/" + userNo))
            .build();
    }

    /**
     * 회원 삭제
     * @param userNo 삭제할 회원 번호
     * @return 삭제 완료 응답 (204 No Content)
     */
    @Operation(summary = "회원 정보 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "회원정보 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못 입력된 파라미터")
    })
    @DeleteMapping("/users/{userNo}")
    public ResponseEntity<?> removeUser(@PathVariable int userNo) {

        // 삭제 대상 찾기
        UserDTO foundUser = users.stream()
            .filter(user -> user.getNo() == userNo)
            .collect(Collectors.toList())
            .get(0);

        users.remove(foundUser);

        // 204 No Content: 삭제 성공, 본문 없음
        return ResponseEntity
            .noContent()
            .build();
    }
}
