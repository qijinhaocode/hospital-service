package com.qi.hospital.util;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.commons.io.FileUtils;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;

public class VerificationCodeUtils {

    /**
     * 生成验证码图片
     * @param request 设置session
     * @param response 转成图片
     * @param captchaProducer 生成图片方法类
     * @param validateSessionKey session名称
     * @throws Exception
     */
    public static void validateCode(HttpServletRequest request, HttpServletResponse response, DefaultKaptcha captchaProducer, String validateSessionKey) throws Exception{

        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = captchaProducer.createText();

        // store the text in the session
        request.getSession().setAttribute(validateSessionKey, capText);

        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);

        //  OutputStream out = response.getOutputStream();
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();

        // write the data out
        ImageIO.write(bi, "jpg", os);

        FileUtils.writeByteArrayToFile(new File("src/main/resources/static/verification_code.jpg"), os.toByteArray());
        try {
            os.flush();
        } finally {
            os.close();
        }
    }
}
