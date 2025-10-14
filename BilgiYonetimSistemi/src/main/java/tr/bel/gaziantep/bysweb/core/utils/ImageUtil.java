package tr.bel.gaziantep.bysweb.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.file.UploadedFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 26.09.2025 11:30
 */
@Slf4j
public class ImageUtil implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -5861814698454600770L;

    public static void saveImage(UploadedFile file, String imagePath) {
        try {
            File result = new File(imagePath);

            InputStream inputStream;
            try (FileOutputStream fileOutputStream = new FileOutputStream(result)) {
                byte[] buffer = new byte[8192];
                int bulk;
                inputStream = file.getInputStream();
                while (true) {
                    bulk = inputStream.read(buffer);
                    if (bulk < 0) {
                        break;
                    }
                    fileOutputStream.write(buffer, 0, bulk);
                    fileOutputStream.flush();
                }
            }
            inputStream.close();

        } catch (Exception ex) {
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public static BufferedImage readBufferedImage(String path) throws IOException {
        File imageFile = new File(path);
        FileInputStream in = new FileInputStream(imageFile);
        return ImageIO.read(in);
    }

    public static byte[] readBytesFromFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {

            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return bytesArray;

    }

    public static InputStream compressImage(InputStream inputStream, File file) {
        try (InputStream is = inputStream;
             OutputStream out = new FileOutputStream(file)) {

            BufferedImage img = ImageIO.read(is);
            BufferedImage scaledImg;
            if (img.getWidth() >= img.getHeight())
                scaledImg = Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 800, 1000);
            else
                scaledImg = Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, 1000, 800);
            ImageIO.write(scaledImg, "jpg", out);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(scaledImg, "jpg", os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
