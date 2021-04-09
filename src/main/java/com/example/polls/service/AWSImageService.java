package com.example.polls.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.polls.model.Amazon.Image;
import com.example.polls.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AWSImageService  extends AWSClientService{

    private final FileRepository fileRepository;

    public Image store(MultipartFile multipartFile) throws IOException {
        String extension = multipartFile.getOriginalFilename()
                .substring(multipartFile.getOriginalFilename().lastIndexOf('.')).toLowerCase();

        if (ImageIO.read(multipartFile.getInputStream()) == null)
            throw new IOException("Can't read file");

        String fileName = generateFileName(multipartFile.getOriginalFilename());

        String url = resizeAndUpload(fileName + extension, multipartFile);
        Image image = new Image(url, multipartFile.getContentType());
        return fileRepository.save(image);
    }

    public Image storeResourceImage(MultipartFile multipartFile, String type) throws IOException {
        String extension = multipartFile.getOriginalFilename()
                .substring(multipartFile.getOriginalFilename().lastIndexOf('.')).toLowerCase();

        if (ImageIO.read(multipartFile.getInputStream()) == null)
            throw new IOException("Can't read file");

        String fileName = generateFileName(multipartFile.getOriginalFilename());

        String url = uploadResourceImage(fileName + extension, multipartFile, type);
        Image image = new Image(url, multipartFile.getContentType());
        return fileRepository.save(image);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

    private String upload(String fileName, MultipartFile multipartFile) throws IOException {
        String fileUrl = getEndPoint() + "/images/" + fileName;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());
        getClient().putObject(new PutObjectRequest(getBucketName(), "images/" + fileName,
                multipartFile.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return fileUrl;
    }

    private String uploadResourceImage(String fileName, MultipartFile multipartFile, String type) throws IOException {
        String fileUrl = getEndPoint() + "/resources/" + type + "/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        getClient().putObject(new PutObjectRequest(getBucketName(), "resources/" + type + "/" + fileName,
                multipartFile.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return fileUrl;
    }

    private String resizeAndUpload(String fileName, MultipartFile multipartFile) throws IOException {
        String fileUrl = getEndPoint() + "/images/" + fileName;

        BufferedImage og = ImageIO.read(multipartFile.getInputStream());
//        BufferedImage scaledImage = Scalr.resize(og, 400, 400);
        BufferedImage scaledImage = Scalr.resize(og, Scalr.Mode.AUTOMATIC, 400);
        String formatName = multipartFile.getOriginalFilename()
                .substring(multipartFile.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase();

        File output = new File(multipartFile.getOriginalFilename());
        OutputStream stream = new FileOutputStream(multipartFile.getOriginalFilename());
        ImageIO.write(scaledImage, formatName, stream);
        stream.close();

        getClient().putObject(new PutObjectRequest(getBucketName(), "images/" + fileName, output)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        output.delete();

        return fileUrl;
    }

    public void deleteImage(Image image) {
        String key = image.getUrl().substring(getEndPoint().length() + 1);
        fileRepository.delete(image);
        getClient().deleteObject(getBucketName(), key);
    }

    private String generateFileName(String originalName) {
        return DigestUtils.md5Hex(originalName + LocalDateTime.now());
    }

    public Image getFile(Long fileId) {
        return (Image) fileRepository.findByFileId(fileId);
    }
}

