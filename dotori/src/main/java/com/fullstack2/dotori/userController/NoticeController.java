package com.fullstack2.dotori.userController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fullstack2.dotori.userDTO.notice.NoticeDTO;
import com.fullstack2.dotori.userDTO.notice.NoticeRequestDTO;
import com.fullstack2.dotori.userEntity.User;
import com.fullstack2.dotori.userEntity.notice.NoticeEntity;
import com.fullstack2.dotori.userRepository.NoticeRepository;
import com.fullstack2.dotori.userService.notice.NoticeServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/notices") // 공지사항 관련 URL 경로 설정
public class NoticeController {

    @Autowired
    private NoticeServiceImpl noticeService;
    @Autowired
    private NoticeRepository noticeRepository;
    
    @GetMapping("/list")
    public String listNotices(HttpSession session ,Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        Page<NoticeDTO> notices = noticeService.getNotices(PageRequest.of(page - 1, size));

        User user = ((User) session.getAttribute("user"));
        model.addAttribute("user", user);
        
        model.addAttribute("notices", notices); // Page 객체 전달
        model.addAttribute("currentPage", page); // 현재 페이지 번호

        return "notices/list";
    }

	/*
	 * @GetMapping("/list") public String listNotices(Model
	 * model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue
	 * = "10") int size) { Page<NoticeDTO> notices =
	 * noticeService.getNotices(PageRequest.of(page - 1, size));
	 * 
	 * model.addAttribute("notices", notices.getContent()); // Page's content
	 * model.addAttribute("totalPages", notices.getTotalPages()); // Total pages
	 * model.addAttribute("currentPage", page); // Current page number
	 * 
	 * return "notices/list"; }
	 */
    
    // 공지사항 등록 폼 페이지로 이동
    
    @GetMapping("/create")
    public String showRegisterForm(Model model) {
        model.addAttribute("noticeRequestDTO", new NoticeRequestDTO());
        return "notices/create"; // 공지사항 등록 폼을 나타내는 템플릿 경로를 반환
    }

    @PostMapping("/create")
    public String registerNotice(HttpSession session, NoticeRequestDTO noticeRequestDTO) {
    	
    	if(session.getAttribute("user") != null) {
    		User user = (User) session.getAttribute("user");
    		String Author = user.getUname();
    		
    		noticeRequestDTO.setUser(user);
    		noticeRequestDTO.setAuthor(Author);
    	}
    
        // NoticeRequestDTO에서 NoticeEntity로 변환
        NoticeEntity noticeEntity = noticeService.dtoToEntity(noticeRequestDTO);
        
        // 공지사항을 저장
        Long noticeId = noticeService.createNotice(noticeEntity);

        if (noticeId != null) {
            // 등록에 성공한 경우, 목록 페이지로 리다이렉트
            return "redirect:/notices/list";
        } else {
            // 등록에 실패한 경우, 오류 처리를 원하는 대로 수행
            // 여기서는 실패 메시지와 함께 등록 폼 페이지로 이동
            return "notices/create";
        }
    }

    @GetMapping("/detail/{id}")
    public String showNoticeDetail(@PathVariable("id") Long id, Model model) {
        // 해당 ID에 대한 공지사항 정보를 가져온다.
        NoticeEntity noticeEntity = noticeService.getNoticeById(id);

        if (noticeEntity == null) {
            // 해당 ID의 공지사항이 없을 경우 오류 처리를 원하는 대로 수행
            // 여기서는 오류 메시지를 모델에 추가하여 상세 페이지로 이동
            model.addAttribute("error", "해당 공지사항을 찾을 수 없습니다.");
            return "notices/detail";
        }

        noticeEntity.incrementViewCount();
        noticeRepository.save(noticeEntity);
        
        // 가져온 공지사항 정보를 모델에 추가한다.
        model.addAttribute("noticeEntity", noticeEntity);

        return "notices/detail";
    }



    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long noticeId, Model model) {
        NoticeEntity noticeEntity = noticeService.getNoticeById(noticeId);
        
        if (noticeEntity == null) {
            // 해당 ID의 공지사항이 없을 경우 오류 처리
            model.addAttribute("error", "해당 공지사항을 찾을 수 없습니다.");
            return "notices/error"; // 오류 페이지로 이동하거나 다른 처리를 수행할 수 있습니다.
        }
        
        NoticeRequestDTO noticeRequestDTO = new NoticeRequestDTO();
        noticeRequestDTO.setId(noticeEntity.getId()); // id 설정
        
        // 기타 필요한 정보를 noticeRequestDTO에 설정
        
        model.addAttribute("noticeRequestDTO", noticeRequestDTO);
        return "notices/edit"; // 공지사항 수정 폼을 나타내는 템플릿 경로를 반환
    }


 // 공지사항 수정 처리
    @PostMapping("/edit/{id}")
    public String editNotice(HttpSession session,@PathVariable("id") Long id, NoticeRequestDTO noticeRequestDTO) {
        // 해당 ID에 대한 공지사항 정보를 가져온다.
        NoticeEntity noticeEntity = noticeService.getNoticeById(id);
        
        if(session.getAttribute("user") != null) {
        	User user = (User) session.getAttribute("user");
    		String author = user.getUname();
    		
    		noticeEntity.setUser(user);
    		noticeEntity.setAuthor(author);
        }

        if (noticeEntity == null) {
            // 해당 ID의 공지사항이 없을 경우 오류 처리를 원하는 대로 수행
            // 여기서는 오류 메시지를 추가하고 목록 페이지로 리다이렉트
            return "redirect:/notices/list";
        }

        // NoticeRequestDTO에서 NoticeEntity로 변환하여 업데이트
        noticeService.updateNotice(noticeEntity, noticeRequestDTO);

        // 수정 후 목록 페이지로 리다이렉트
        return "redirect:/notices/list";
    }


    // 공지사항 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteNotice(@PathVariable("id") Long id) {
        // 해당 ID에 대한 공지사항 정보를 가져온다.
        NoticeEntity noticeEntity = noticeService.getNoticeById(id);

        if (noticeEntity != null) {
            // 공지사항이 존재하는 경우 삭제
            noticeService.deleteNotice(noticeEntity);
        }

        // 삭제 후 목록 페이지로 리다이렉트
        return "redirect:/notices/list";
    }
    
    @GetMapping("/search")
    public String searchNotices(@RequestParam("keyword") String keyword, Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        // 검색어를 이용하여 공지사항을 검색
        Page<NoticeEntity> searchResults = noticeService.noticeSearchList(keyword, PageRequest.of(page - 1, size));

        // 디버그 메시지 추가
        System.out.println("검색 결과: " + searchResults.getContent());

        model.addAttribute("notices", searchResults);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);

        return "notices/search"; // 검색 결과를 보여줄 템플릿 경로
    }

}
