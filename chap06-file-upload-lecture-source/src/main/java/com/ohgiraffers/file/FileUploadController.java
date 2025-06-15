package com.ohgiraffers.file;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 파일 업로드 기능을 담당하는 컨트롤러
 * 단일 파일 업로드와 다중 파일 업로드를 처리
 */
@Controller
public class FileUploadController {

    /**
     * 단일 파일 업로드를 처리하는 메서드
     *
     * @param singleFileDescription 파일에 대한 설명
     * @param singleFile 업로드할 파일 객체
     * @param model 뷰로 전달할 데이터를 담는 모델 객체
     * @return 결과 페이지 뷰 이름
     */
    @PostMapping("/single-file")
    public String singleFileUpload(@RequestParam String singleFileDescription,
        @RequestParam MultipartFile singleFile,
        Model model) {

//        System.out.println("singleFileDescription = " + singleFileDescription);
//        System.out.println("singleFileDescription = " + singleFileDescription);

        /* 업로드된 파일을 서버의 지정된 경로에 저장하기 위한 디렉토리 설정 */
        String root = "src/main/resources/static";
        String filePath = root + "/uploadFiles";
        File dir = new File(filePath);
        System.out.println(dir.getAbsolutePath());

        /* 업로드 디렉토리가 존재하지 않으면 새로 생성 */
        if(!dir.exists()) {
            dir.mkdirs();
        }

        /* 업로드된 파일의 원본 파일명 추출 및 확장자 분리 */
        String originFileName = singleFile.getOriginalFilename();

        // 파일명에서 확장자(.jpg, .png 등) 추출
        String ext = originFileName.substring(originFileName.lastIndexOf("."));
        System.out.println("ext = " + ext);

        /* 파일명 중복 방지를 위해 UUID를 사용하여 고유한 파일명 생성 */
        String savedName = UUID.randomUUID() + ext;
        System.out.println("savedName = " + savedName);

        /* 실제 파일을 서버 디렉토리에 저장하는 처리 */
        try {
            // MultipartFile의 transferTo 메서드로 파일 저장
            singleFile.transferTo(new File(filePath + "/" + savedName));
            model.addAttribute("message", "파일 업로드 완료!");
        } catch (IOException e) {
            // 파일 저장 실패 시 에러 메시지 설정
            model.addAttribute("message", "파일 업로드 실패!");
        }

        return "result";
    }

    /**
     * 다중 파일 업로드를 처리하는 메서드
     *
     * @param multiFileDescription 파일들에 대한 공통 설명
     * @param multiFile 업로드할 파일 목록
     * @param model 뷰로 전달할 데이터를 담는 모델 객체
     * @return 결과 페이지 뷰 이름
     */
    @PostMapping("/multi-file")
    public String multiFileUpload(@RequestParam String multiFileDescription,
        @RequestParam List<MultipartFile> multiFile,
        Model model) {

        System.out.println("multiFileDescription = " + multiFileDescription);
        System.out.println("multiFile = " + multiFile);

        String root = "src/main/resources/static";
        String filePath = root + "/uploadFiles";
        File dir = new File(filePath);

        if(!dir.exists()) {
            dir.mkdirs();
        }

        /* 업로드된 파일 정보를 저장할 리스트 (실패 시 롤백용) */
        List<FileDTO> files = new ArrayList<>();

        /* 다중 파일을 순차적으로 처리하여 파일명 변경 후 저장 */
        try {

            // 업로드된 각 파일에 대해 반복 처리
            for(MultipartFile file : multiFile) {

                /* 각 파일의 원본 파일명 추출 */
                String originFileName = file.getOriginalFilename();
                System.out.println("originFileName = " + originFileName);

                /* 파일 확장자 추출 */
                String ext = originFileName.substring(originFileName.lastIndexOf("."));

                /* UUID를 사용하여 고유한 저장용 파일명 생성 */
                String savedName = UUID.randomUUID() + ext;

                /* 파일 정보를 DTO 객체로 생성하여 리스트에 추가 (실패 시 롤백을 위한 정보 보관) */
                files.add(new FileDTO(originFileName, savedName, filePath, multiFileDescription));

                /* 실제 파일을 서버 디렉토리에 저장 */
                file.transferTo(new File(filePath + "/" + savedName));
            }

            /* 모든 파일 업로드 성공 시 성공 메시지 설정 */
            model.addAttribute("message", "파일 업로드 완료!");

        } catch (IOException e) {

            /* 파일 저장 중 오류 발생 시 이미 저장된 파일들을 삭제하여 데이터 정합성 유지 */
            for(FileDTO file : files) {
                new File(filePath + "/" + file.getSavedName()).delete();
            }

            /* 업로드 실패 메시지 설정 */
            model.addAttribute("message", "파일 업로드 실패");
        }

        return "result";
    }
}