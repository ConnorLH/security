package cn.corner.web.core.validate.code.image;

import cn.corner.web.core.properties.SecurityProperties;
import cn.corner.web.core.validate.code.ValidateCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 图片验证码生成器
 */
public class ImageCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 可以根据请求参数生成不同效果的验证码（具有可配置的默认值）
     * 参数
     * width
     * height
     * length
     * expire
     * @param request
     * @return
     */
    @Override
    public ImageCode buildCode(HttpServletRequest request) {
        int width = ServletRequestUtils.getIntParameter(request, "width", securityProperties.getCode().getImageCode().getWidth());
        int height = ServletRequestUtils.getIntParameter(request, "height", securityProperties.getCode().getImageCode().getHeight());
        BufferedImage bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bImg.getGraphics();
        //背景
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        //字体颜色
        g.setFont(new Font("aa", Font.BOLD, 18));
        //用随机数生成验证码:4个0~9以内的整数
        Random r = new Random();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < ServletRequestUtils.getIntParameter(request,"length",securityProperties.getCode().getImageCode().getLength()); i++) {
            int t = r.nextInt(10);//10以内的随机整数
            str.append(t);
            int y = 10 + r.nextInt(20);//上下位置:10~30
            Color c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            g.setColor(c);
            g.drawString("" + t, i * 16, y);
        }
        //画干扰线
        for (int i = 1; i < 8; i++) {
            Color c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            g.setColor(c);
            g.drawLine(r.nextInt(width), r.nextInt(height), r.nextInt(width), r.nextInt(height));
        }

        //把图形刷到bImg对象中
        g.dispose();//相当于IO中的close()方法带自动flush();
        return new ImageCode(bImg, str.toString(), ServletRequestUtils.getIntParameter(request,"expire",securityProperties.getCode().getImageCode().getExpire()));
    }
}
