package it.univaq.disim.bpd.example.simplerestservice1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTController {

	@GetMapping("/mydata/{myId}")
	public MyData getData(@PathVariable int myId) {
		MyData myData = new MyData();
		
		myData.setId(myId);
		myData.setTitle("This is the title");
		myData.setText("This is the text");
		
		return myData;
	}
	
	@PostMapping("/myData")
	public ResponseEntity<?> postData(@RequestBody MyData myData) {
		
		System.out.println(myData.getTitle());
		
		return ResponseEntity.ok().build();
	}
}
