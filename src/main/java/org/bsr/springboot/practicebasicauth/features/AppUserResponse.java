/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    A simple record to model a response dto for returning the authenticated user's non-sensitive profile
 *          details and granted roles
 * Created: 05/26/2026
 * Version: 1.0
 * */

package org.bsr.springboot.practicebasicauth.features;

import java.util.List;

public record AppUserResponse(
        String username,
        String status,
        List<String> roles
) {
}
