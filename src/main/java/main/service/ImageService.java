package main.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import main.api.response.StatusResponse;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

  @Value("${upload.path}")
  private String uploadPath;

  public StatusResponse postImage(MultipartFile image) {
    Map<String, String> errors = checkErrors(image);
    if (!errors.isEmpty()) {
      StatusResponse statusResponse = new StatusResponse(false, errors, null);
      return statusResponse;
    }
    String path = "";
    try {
      path = uploadFileAndGetPath(image, false);
    } catch (IOException e) {
      e.getStackTrace();                     //logger add
    }
    return new StatusResponse(true, null, path);
  }

  private Map<String, String> checkErrors(MultipartFile image) {
    Map<String, String> result = new HashMap<>();
    String originalPhotoName = image.getOriginalFilename();
    String imageType = originalPhotoName.substring(originalPhotoName.lastIndexOf(".") + 1);

    boolean typeJpg = imageType.equals("jpg");
    boolean typeJpeg = imageType.equals("jpeg");
    boolean typePng = imageType.equals("png");

    if (typeJpg || typePng || typeJpeg) {
      return result;
    }
    result.put("image", "Недопустимый формат файла. Используйте только файлы png, jpeg/jpg.");
    return result;
  }

  public String uploadFileAndGetPath(MultipartFile image, boolean userPhoto) throws IOException {
    Path root = Paths.get(uploadPath);
    if (!Files.exists(root)) {
      Files.createDirectory(root);
    }

    if (image != null) {
      StringBuilder photoPath = new StringBuilder(root.toString());
      String originalPhotoName = image.getOriginalFilename();
      String imageType = image.getContentType().split("/")[1];
      originalPhotoName = originalPhotoName.substring(0, originalPhotoName.lastIndexOf("."));
      for (int i = 0; i < 3; i++) {
        String dirName = "/" + RandomStringUtils.randomAlphabetic(2).toLowerCase();
        File dir = new File(photoPath.append(dirName).toString());
        if (!dir.exists()) {
          dir.mkdir();
        }
      }
      String withoutCollisionName = RandomStringUtils.randomAlphabetic(5).toLowerCase();
      BufferedImage userImage = ImageIO.read(image.getInputStream());
      photoPath.append("/")
          .append(withoutCollisionName)
          .append(".")
          .append(originalPhotoName)
          .append(".")
          .append(imageType);
      File file = new File(photoPath.toString());
      if (userPhoto) {
        userImage = Thumbnails.of(userImage).size(36, 36).asBufferedImage();
      }
      ImageIO.write(userImage, imageType, file);
      photoPath.insert(0, "/");
      return photoPath.toString();
    }
    return "";
  }
}
