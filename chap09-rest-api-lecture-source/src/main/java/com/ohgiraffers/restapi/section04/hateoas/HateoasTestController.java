package com.ohgiraffers.restapi.section04.hateoas;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Spring HATEOAS(Hypermedia As The Engine Of Application State) 적용 컨트롤러
 *
 * comment. HATEOAS 란?
 * - REST API의 확장된 원칙 중 하나로,
 *   클라이언트가 API의 사용 방법(다음에 어떤 동작이 가능한지)을
 *   응답 안의 "링크(link)" 정보를 통해 동적으로 이해할 수 있게 하는 것.
 * - 즉, 응답 데이터에 관련 리소스의 링크를 포함하여,
 *   클라이언트가 사전 문서 없이도 API를 탐색(navigate)할 수 있게 한다.
 *
 * 예시:
 * - GET /users 응답 시 각 사용자마다 "self" 링크(자기 자신 조회),
 *   그리고 "users" 링크(전체 목록) 등을 함께 포함하여 제공.
 *
 * Spring HATEOAS 지원:
 * - `EntityModel<T>` : 리소스 객체(T)에 하이퍼미디어 링크를 감싸는 Wrapper 클래스
 * - `linkTo()` + `methodOn()` : 컨트롤러 메서드 기반으로 하이퍼링크 생성
 */

@RestController
@RequestMapping("/hateoas")
public class HateoasTestController {

    /** 샘플 사용자 목록 (임시 메모리 DB 역할) */
    private List<UserDTO> users;

    /** 생성자에서 테스트용 사용자 데이터 초기화 */
    public HateoasTestController() {
        users = new ArrayList<>();
        users.add(new UserDTO(1, "user01", "pass01", "다람쥐", new Date()));
        users.add(new UserDTO(2, "user02", "pass02", "코알라", new Date()));
        users.add(new UserDTO(3, "user03", "pass03", "판다", new Date()));
    }

    /**
     * 전체 사용자 목록 조회 엔드포인트
     * - 응답 본문에 각 사용자에 대한 HATEOAS 링크를 포함
     * - 각 사용자마다:
     *     - "self" 링크: 개별 사용자 조회 링크
     *     - "users" 링크: 전체 목록 다시 조회 링크
     */
    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers() {

        // 응답 헤더 설정 (Content-Type: application/json, UTF-8 인코딩)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        // 각 사용자 객체에 하이퍼미디어 링크 추가
        List<EntityModel<UserDTO>> userWithRel = users.stream()
            .map(user -> EntityModel.of(
                user,
                // 🔗 Self 링크: /hateoas/users/{userNo}
                linkTo(methodOn(HateoasTestController.class).findUserByNo(user.getNo())).withSelfRel(),
                // 🔗 Relational 링크: 전체 사용자 목록 링크
                linkTo(methodOn(HateoasTestController.class).findAllUsers()).withRel("users")
            ))
            .collect(Collectors.toList());

        // 응답 데이터 구성
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", userWithRel);

        // 최종 응답 메시지 객체 구성
        ResponseMessage responseMessage = new ResponseMessage(200, "조회성공", responseMap);

        // 200 OK 응답 반환
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    /**
     * 개별 사용자 상세 조회
     * - 사용자의 번호(userNo)를 경로 변수로 받아서 일치하는 사용자 반환
     * - (참고) HATEOAS 링크는 여기선 따로 포함하지 않음
     */
    @GetMapping("/users/{userNo}")
    public ResponseEntity<ResponseMessage> findUserByNo(@PathVariable int userNo) {

        // 응답 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        // userNo에 해당하는 사용자 필터링
        // (주의) 실무에서는 IndexOutOfBoundsException 방지를 위한 Optional 처리 권장
        UserDTO foundUser = users.stream()
            .filter(user -> user.getNo() == userNo)
            .collect(Collectors.toList())
            .get(0);

        // 응답 데이터 구성
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("user", foundUser);

        // 200 OK 응답 반환
        return ResponseEntity.ok()
            .headers(headers)
            .body(new ResponseMessage(200, "조회성공", responseMap));
    }
}
