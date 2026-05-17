/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    Stub controller to test authenticated and privileged endpoints through security slice
 * Created: 05/16/2026
 * Version: 1.0
 * */

package org.bsr.springboot.practicebasicauth.testUtil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestSecuredController {

    @GetMapping("/api/admin")
    public String adminEnd() {
        return "Secret: SUCCESS";
    }

    @GetMapping("/api/me")
    public String userEnd() {
        return "SUCCESS";
    }
}
