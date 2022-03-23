package com.qi.hospital.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.qi.hospital.util.VerificationCodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("verification_code")
@Validated
@CrossOrigin(origins = "*")
public class ValidationCodeController {

    public static final String LOGIN_VALIDATE_CODE = "validate_code";
    private final DefaultKaptcha captchaProducer;

    /**
     * 注册验证码
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @PostMapping("/create")
    public void createVerificationCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        VerificationCodeUtils.validateCode(request, response, captchaProducer, LOGIN_VALIDATE_CODE);
    }

    @PostMapping("/verify")
    @ResponseBody
    public HashMap checkVerificationCode(HttpServletRequest request, @RequestParam("verificationCode") String validateCode) {
        String verificationCode = request.getSession().getAttribute(LOGIN_VALIDATE_CODE).toString();

        HashMap<String, Object> map = new HashMap<>();
        if (verificationCode == null) {
            map.put("status", null);
        } else if (verificationCode.equals(validateCode)) {
            map.put("status", true);
        } else if (!verificationCode.equals(validateCode)) {
            map.put("status", false);
        }
        map.put("code", 200);
        return map;
    }

}
