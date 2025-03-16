//package com.samsung.test002;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api")
//public class TestController002 {
//	
//	@GetMapping("/test001")
//	public ResponseEntity<?> testControllerGet001(){
//		
//		log.info("======= call testControllerGet001 ======");
//		
//		return new ResponseEntity<>("test001", HttpStatus.OK);
//	}
//	
//	@GetMapping("/test002")
//	public ResponseEntity<?> testControllerGet002(){
//		
//		log.info("======= call testControllerGet002 ======");
//		
//		return new ResponseEntity<>("test002", HttpStatus.OK);
//	}
//	
//	@GetMapping("/test003")
//	public ResponseEntity<?> testControllerGet003(){
//		
//		log.info("======= call testControllerGet003 ======");
//		
//		return new ResponseEntity<>("test003", HttpStatus.OK);
//	}
//
//}
