package tr.bel.gaziantep.bysweb.core.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tr.bel.gaziantep.bysweb.core.utils.CaptchaUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;
import java.util.Random;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.08.2025 11:11
 */
@WebServlet("/captcha")
public class CaptchaServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = -863604456364042514L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int width = 200;
        int height = 50;
        String captchaText = CaptchaUtil.generateCaptchaText(5);

        // CAPTCHA metnini oturumda sakla
        request.getSession().setAttribute("captcha", captchaText);

        // Görsel oluştur
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        Random random = new Random();

        // Arka plan
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, width, height);

        // Çizgiler
        for (int i = 0; i < 10; i++) {
            g2d.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g2d.drawLine(random.nextInt(width), random.nextInt(height), random.nextInt(width), random.nextInt(height));
        }

        // CAPTCHA metni
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.setColor(Color.BLACK);
        g2d.drawString(captchaText, 10, 35);

        g2d.dispose();

        // Görseli yanıt olarak gönder
        response.setContentType("image/png");
        ImageIO.write(bufferedImage, "png", response.getOutputStream());
    }
}