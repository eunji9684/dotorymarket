package com.fullstack2.dotori.userService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fullstack2.dotori.userRepository.Product.ProductRepository;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

@Service
public class ImageUploadService {

    @Autowired
    private ProductRepository productRepository; // ProductRepository는 상품 정보를 DB에서 조회 및 저장하는데 사용합니다.

    
    // 파일을 특정 경로에 저장하는 메서드. 반환값은 저장된 이미지의 이름.(그 전 경로 제외)
    
    public String saveProductImage(MultipartFile file, String uniqueFileName) throws IOException {
        // 이미지를 저장할 디렉토리 경로 설정
        //String uploadDirectory = "경로/상품이미지/"; // 실제 경로로 변경하세요.
        String uploadDirectory = "E:\\uploadEx"; // 외장하드 경로

        // 디렉토리가 없으면 생성
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        
        // 이미지 파일 저장
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath);

        //changeToThumbnail(filePath);
        
        
        return uniqueFileName;
    }
    
    
    
    //아래 메서드는 이미지 사이즈를 조절해서 저장하는 메서드입니다. 리턴타입은 경로를 제외한 순수 파일명입니다. (단, 썸네일화 되었기때문에 thumbnail. 이 파일명 앞에 붙었습니다.)
    public String saveProductThumbnailImage(MultipartFile file, String uniqueFileName) throws IOException {
        // 이미지를 저장할 디렉토리 경로 설정
        //String uploadDirectory = "경로/상품이미지/"; // 실제 경로로 변경하세요.
        String uploadDirectory = "E:\\uploadEx"; // 외장하드 경로

        // 디렉토리가 없으면 생성
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }


        // 이미지 파일 저장
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath);
        
        saveThumbnail(filePath);
        //String thubmnailPath = saveThumbnail(filePath);
        
        //return thubmnailPath;
        return uniqueFileName;
    }
    
   
    
    private void saveThumbnail(Path filePath) {
    	System.out.println("디버그1");
    	System.out.println(filePath);
    	System.out.println(new File(filePath.toString()));
    	try {
    		
			Thumbnails.of(new File(filePath.toString()))
						.forceSize(150, 150)
						//.toFile(filePath.toString());
						//.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
						.toFiles(Rename.NO_CHANGE);
			System.out.println("디버그2");
			
    		//return filePath;
    		
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	//return filePath.toString();
    	
    }
    
    /*
    private void saveThumbnail(Path filePath) {
        try {
            System.out.println("디버그1");
            System.out.println(filePath);

            // 파일이 존재하고 유효한 이미지 형식인지 확인
            if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                Thumbnails.of(filePath.toFile())
                    .size(200, 200)
                    .toFile(filePath.toFile());
                System.out.println("디버그2");
            } else {
                System.out.println("오류: 입력 파일이 존재하지 않거나 유효한 이미지가 아닙니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	*/
    /*
    private void saveThumbnail(Path filePath) {
    	
    	try {
    	 // 원본 이미지 파일 경로
        File inputFile = new File(filePath.toString());
        // 출력 이미지 파일 경로
        File outputFile = new File(filePath.toString());
        // 원본 파일의 확장자 추출
        String extension = getExtension(inputFile.getName());
        
        
			Thumbnails.of(inputFile)
						.size(200,200)
						.outputFormat(extension)
						.toFile(outputFile);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        		  
    	
    }
    
    // 파일명에서 확장자 추출
    private static String getExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex + 1);
        }
        return ""; // 확장자 없는 경우 빈 문자열 반환
    }
    */
    
}
