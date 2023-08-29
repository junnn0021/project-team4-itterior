package com.office.house.user;

import java.util.HashMap;
import java.util.Map;

import com.office.house.user.util.UploadFileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UploadFileService uploadFileService;

	// create account confirm
    @ResponseBody
    @PostMapping("/create_account_confirm")
    public Map<String, Object> createAccountConfirm(@RequestBody Map<String, String> msgMap){
        log.info("[UserController] createAccountConfirm()");

        Map<String, Object> resultMap = userService.createAccountConfirm(msgMap);

        return resultMap;
    }

    // login confirm
    @ResponseBody
    @PostMapping("/user_login_confirm")
    public Map<String, Object> userLoginConfirm(@RequestBody Map<String, String> msgMap, HttpSession session, Model model){
        log.info("[UserController] userLoginConfirm()");

        Map<String, Object> resultMap = userService.userLoginConfirm(msgMap);

        UserDto loginedMemberDto =  (UserDto)resultMap.get("loginedMemberDto");

        if(loginedMemberDto != null){
            session.setAttribute("loginedMemberDto",loginedMemberDto);
            session.setMaxInactiveInterval(60*30);
        }
        model.addAttribute("loginedMemberDto", loginedMemberDto);
        return resultMap;
    }

    // logout confirm
    @GetMapping("/user_logout_confirm")
    public String userLogoutConfirm(HttpSession session){
        log.info("[UserController] userLogoutConfirm()");

        String nextPage = "redirect:/";
        session.removeAttribute("loginedMemberDto");

        return nextPage;
    }

    // modify form
    @GetMapping("/user_modify_form")
    public String userModifyForm(){
        log.info("[UserController] userModifyForm()");

        String nextPage = "user/user_modify_form";

        return nextPage;
    }

    // modify confirm
    @PostMapping("/user_modify_confirm")
    public String userModifyConfirm(UserDto userDto, HttpSession session, @RequestParam("file") MultipartFile file){
        log.info("[UserController] userModifyConfirm()");

        String nextPage = "user/user_modify_success";
        UserDto loginedMemberDto = (UserDto) session.getAttribute("loginedMemberDto");

//        String savedFileName = uploadFileService.upload(loginedMemberDto.getU_id(), file);

        String savedFileName = null;

        log.info(file.getSize());
        log.info(session.getAttribute("u_img"));
        log.info(loginedMemberDto.getU_img());

        if(file.getSize()>0) {
            savedFileName = uploadFileService.upload(loginedMemberDto.getU_id(), file);
        } else {
            userDto.setU_img(loginedMemberDto.getU_img());
        }

        if(savedFileName!=null){
            userDto.setU_id(loginedMemberDto.getU_id());
            userDto.setU_img(savedFileName);
        }

        loginedMemberDto = userService.userModifyConfirm(userDto);

        if(loginedMemberDto!=null){
            session.setAttribute("loginedMemberDto",loginedMemberDto);
            session.setMaxInactiveInterval(60*30);

        } else{
            nextPage = "user/user_modify_fail";
        }

        return nextPage;
    }

    // delete confirm
    @PostMapping("/user_delete_confirm")
    @ResponseBody
    public Object userDeleteConfirm(@RequestBody Map<String, String> msgMap, HttpSession session){
        log.info("[UserController] userDeleteConfirm()");

        Map<String, Object> map = userService.userDeleteConfirm(msgMap.get("u_no"));
        if(((int)map.get("result"))>0)
            session.removeAttribute("loginedMemberDto");

        return map;
    }

    // My Page
    @GetMapping("/user_myPage")
    public String usermyPage(){
        log.info("[UserController] usermyPage()");

        String nextPage = "user/user_myPage";

        return nextPage;
    }

    @PostMapping("/get_logged_user_info")
    public ResponseEntity<Map<String, Object>> getLoggedInUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDto loginedMemberDto = (UserDto) session.getAttribute("loginedMemberDto");

        Map<String, Object> response = new HashMap<>();
        if (loginedMemberDto != null) {
            response.put("u_no", loginedMemberDto.getU_no());
        } else {
            response.put("u_no", null);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user_like_list")
    public String userLikeList(){
        log.info("[UserController] userLikeList()");

        String nextPage = "user/user_like_list";

        return nextPage;
    }
}
