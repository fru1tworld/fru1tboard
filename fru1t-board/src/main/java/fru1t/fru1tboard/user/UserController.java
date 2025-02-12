package fru1t.fru1tboard.user;

import fru1t.fru1tboard.user.request.SignUpForm;
import fru1t.fru1tboard.user.request.UserUpdateRequest;
import fru1t.fru1tboard.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("v1/signup")
    public UserResponse create(@RequestBody SignUpForm request) {
        log.info("[POST] User signup request: {}", request);
        UserResponse response = userService.create(request);
        log.info("[POST] User created successfully: {}", response);
        return response;
    }

    @GetMapping("v1/users/{userId}")
    public UserResponse read(@PathVariable Long userId) {
        log.info("[GET] User fetch request for userId: {}", userId);
        UserResponse response = userService.read(userId);
        log.info("[GET] User fetched successfully: {}", response);
        return response;
    }

    @DeleteMapping("v1/users/{userId}")
    public void delete(@PathVariable Long userId) {
        log.warn("[DELETE] User delete request for userId: {}", userId);
        userService.delete(userId);
        log.warn("[DELETE] User deleted successfully: userId {}", userId);
    }

    @PutMapping("v1/users")
    public UserResponse update(@RequestBody UserUpdateRequest request) {
        log.info("[PUT] User update request: {}", request);
        UserResponse response = userService.update(request);
        log.info("[PUT] User updated successfully: {}", response);
        return response;
    }
}
