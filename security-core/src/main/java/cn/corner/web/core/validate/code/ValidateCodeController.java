package cn.corner.web.core.validate.code;

import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

@RestController
public class ValidateCodeController {

    public static final String SESSION_KEY="SESSION_KEY_IMAGE_CODE";

    private SessionStrategy strategy = new HttpSessionSessionStrategy();

    @GetMapping("/code/image")
    public void createImageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode = buildImageCode(request);
        strategy.setAttribute(new ServletWebRequest(request),SESSION_KEY,imageCode);
        ImageIO.write(imageCode.getImage(),"JPEG",response.getOutputStream());
    }

    private ImageCode buildImageCode(HttpServletRequest request) {
        int width=64;
        int height=40;
        BufferedImage bImg=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g=bImg.getGraphics();
        //背景
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        //字体颜色
        g.setFont(new Font("aa", Font.BOLD,18));
        //用随机数生成验证码:4个0~9以内的整数
        Random r=new Random();
        StringBuilder str = new StringBuilder();
        for(int i=0;i<=4;i++){
            int t=r.nextInt(10);//10以内的随机整数
            str.append(t);
            int y=10+r.nextInt(20);//上下位置:10~30
            Color c=new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            g.setColor(c);
            g.drawString(""+t, i*16, y);
        }
        //画干扰线
        for(int i=1;i<8;i++){
            Color c=new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            g.setColor(c);
            g.drawLine(r.nextInt(width), r.nextInt(height), r.nextInt(width), r.nextInt(height));
        }

        //把图形刷到bImg对象中
        g.dispose();//相当于IO中的close()方法带自动flush();
        return new ImageCode(bImg,str.toString(),3600);
    }
}
