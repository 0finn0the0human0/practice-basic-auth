package org.bsr.springboot.practicebasicauth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ProdTestController {

    @GetMapping("/api/me")
    public ResponseEntity<Map<String, String>> me() {
        Map<String, String> data = new HashMap<>();
        data.put("access", "granted");
        data.put("role-level", "standard");
        return ResponseEntity.ok(data);
    }

    @GetMapping("/api/admin/me")
    public ResponseEntity<Map<String, String>> admin() {
        Map<String, String> data = new HashMap<>();
        data.put("access", "granted");
        data.put("role-level", "privileged");
        return ResponseEntity.ok(data);
    }
}
