package com.fullstack2.dotori.userController;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fullstack2.dotori.userDTO.Product.ProductDTO;
import com.fullstack2.dotori.userDTO.Product.ProductRequestDTO;
import com.fullstack2.dotori.userEntity.User;
import com.fullstack2.dotori.userEntity.Product.ProductEntity;
import com.fullstack2.dotori.userRepository.Product.ProductRepository;
import com.fullstack2.dotori.userRepository.Product.ReplyRepository;
import com.fullstack2.dotori.userService.UserService;
import com.fullstack2.dotori.userService.Product.LikeService;
import com.fullstack2.dotori.userService.Product.ProductService;
import com.fullstack2.dotori.userService.Product.ProductServiceImpl;
import com.fullstack2.dotori.userService.Product.PurchaseService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private PurchaseService purchaseService;
    
    
    @GetMapping("/productList")
    public String productList(HttpSession session, Model model, Pageable pageable
    					,String searchKeyword) {
    	
    	User user = (User)session.getAttribute("user");
    	model.addAttribute("user", user);
    	
    	//기본 정렬 방식은 productId 기준 역순, pageSize 는 한 페이지에 몇 개의 상품을 표시할 지 
    	int pageSize = 16;
    	int pageNumber = pageable.getPageNumber();
    	pageable = PageRequest.of(pageNumber, pageSize, Sort.by("productId").descending());
    	
    	//검색기능 시작 정우-추가부분
    	Page<ProductEntity> products = null;
    	if (searchKeyword == null) {
    		 products = productRepository.findAll(pageable);
		}else {
			products = productService.ProductSearchList(searchKeyword, pageable);
		}
    	

       
        model.addAttribute("products", products);
        return "/productList"; // productList.html 뷰를 반환합니다.
    }
    
    
    @GetMapping("/productDetail")
    public String productDetail(HttpSession session,Model model, @RequestParam("productId") Long productId) {
        
    	User user = (User)session.getAttribute("user");
    	model.addAttribute("user", user);
    	
    	ProductEntity productEntity = productRepository.getReferenceById(productId);
    	//View 올리는 메서드
    	productEntity.incrementViewCount();
    	productRepository.save(productEntity);
        ProductDTO productDTO = productService.entityToDto(productEntity);
        model.addAttribute("product", productDTO);
        
        if (user != null) {
            boolean isLike = likeService.checkLike(user.getId(), productId);
            model.addAttribute("isLike", isLike);
            
            session.setAttribute("isProductLiked", isLike);
		}else {
			model.addAttribute("isLike", false);
			session.setAttribute("isProductLiked", false);
		}
        //댓글 달기
        model.addAttribute("replies", replyRepository.findAllReplyEntitiesByProductEntityAndParentIsNull(productEntity));
        
        return "/productDetail";
    }
    
	@PostMapping("/addPurchase")
	@ResponseBody
    public ResponseEntity<String> addPurchase(HttpServletRequest request, HttpSession session,
			HttpServletResponse response){
    	
    	
    	Long user = ((User)session.getAttribute("user")).getId();
		
		Long productId = Long.parseLong(request.getParameter("productId"));
  	
    	purchaseService.addPurchase(user, productId);
    	
    	return ResponseEntity.ok("{\"success\": true}");
    }
    
    @PostMapping("/removePurchase")
    @ResponseBody
	public ResponseEntity<String> removePurchase(HttpServletRequest request, HttpSession session,
										HttpServletResponse response) {
		
		Long user = ((User)session.getAttribute("user")).getId();
		Long productId = Long.parseLong(request.getParameter("productId"));
		
		purchaseService.removePurchase(user, productId);
		
		return ResponseEntity.ok("{\"success\": true}");
	}
    

    @GetMapping("/registerP")
    public String registerP(HttpSession session, Model model) {
    	
    	//유저가 아닌데 들어오면 리스트 페이지로 보내기
    	if(session.getAttribute("user") == null) {
    		return "redirect:/productList";
    	}
    	
    	User user = (User)session.getAttribute("user");
    	model.addAttribute("user", user);

    	//model.addAttribute("product" , new ProductDTO());
    	model.addAttribute("ProductRequestDTO" , new ProductRequestDTO());
    	
    	return "/registerP";
    }
  
    //신규 글 등록 후 리스트로 리다이렉트, id는 유저의 id(PK)
    @PostMapping("/registerP")
    public String registerP(@Valid @ModelAttribute ProductRequestDTO productRequestDTO, 
    						BindingResult bindingResult,
    						Model model,
    						HttpSession session
    						) {
    	
    	//User user = userService.getLoginUserById(id);
    	User user = (User)session.getAttribute("user");
    	//유저가 아닌데 들어오면 리스트 페이지로 보내기
    	if(session.getAttribute("user") == null) {
    		return "redirect:/productList";
    	}
    	
    	if (bindingResult.hasErrors()) {
    		    		
        	model.addAttribute("user", user);
    		//model.addAttribute("ProductRequestDTO" , new ProductRequestDTO());
    		model.addAttribute("ProductRequestDTO" , productRequestDTO);
			return "/registerP";
		}
    	
    	ProductEntity productEntity = ProductEntity.builder()
    												.ptitle(productRequestDTO.getPtitle())
    												.pPrice(productRequestDTO.getPPrice())
    												.pContent(productRequestDTO.getPContent())
    												.pictureUrl(productRequestDTO.getPictureUrl())
    												.user(user)
    												.build();
    	
    	productRepository.save(productEntity);
    	return "redirect:/productList";
    }
    
    //productDetail.html에서 productDTO를 전달 받음
    @GetMapping("productModify")
    public String productModify(@RequestParam Long productId,HttpSession session, Model model) {
    	
    	if(session.getAttribute("user") == null) {
    		return "redirect:/productList";
    	}
    	
    	User user = (User)session.getAttribute("user");
    	ProductEntity productEntity = productRepository.getReferenceById(productId);
    	ProductDTO productDTO = productService.entityToDto(productEntity, user);
    	
    	model.addAttribute("product", productDTO);
    	
    	return "/productModify";
    }
    
    
  //상품 정보 업데이트 하기
    @PostMapping("/productModify")
    public String productModify(@ModelAttribute ProductDTO productDTO) {
    	

    	ProductEntity productEntity = productRepository.findById(productDTO.getProductId())
    			.orElseThrow(() -> new NoSuchElementException("상품을 찾을 수 없습니다."));

    	
    	
    	productEntity.setPtitle(productDTO.getPtitle());
    	productEntity.setPPrice(productDTO.getPPrice());
    	productEntity.setPContent(productDTO.getPContent());
    	
    	productRepository.save(productEntity);
    	
    	//수정하면 바꾼 상품 번호 상세페이지로 보내기
    	return "redirect:/productDetail?productId="+productDTO.getProductId();
    }
    
    
    //폼으로 전송은 delete 메서드를 지원하지 않는다고 한다.부득이하게 post로 처리함. 나중에 수정가능하면 좋을듯
    @PostMapping("/productDelete")
    public String productDelete(@ModelAttribute ProductDTO productDTO) {
    	
    	productRepository.deleteById(productDTO.getProductId());
    	return "redirect:/productList";
    }
    
    
}
