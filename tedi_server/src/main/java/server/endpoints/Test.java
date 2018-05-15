package server.endpoints;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {
	
	@RequestMapping("/hello")
	public ResponseEntity<Object> hello() {
		return new ResponseEntity<Object>(new String("hello"), HttpStatus.OK);
	}
	
	@RequestMapping("/secret")
	public ResponseEntity<Object> secret() {
		return new ResponseEntity<Object>(new String("Secret hello"), HttpStatus.OK);
	}
	
	@RequestMapping("/admin")
	public ResponseEntity<Object> admin() {
		return new ResponseEntity<Object>(new String("Admin hello"), HttpStatus.OK);
	}

	@RequestMapping("/authenticated")
	public ResponseEntity<Object> auth() {
		return new ResponseEntity<Object>(new String("Authenticated hello"), HttpStatus.OK);
	}
	
}
