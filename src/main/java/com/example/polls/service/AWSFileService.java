package com.example.polls.service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.polls.exception.AppException;
import com.example.polls.model.Amazon.AWSFile;
import com.example.polls.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AWSFileService extends AWSClientService {

    private final FileRepository fileRepository;

    public AWSFile store(MultipartFile multipartFile) throws IOException {
        String extension = multipartFile.getOriginalFilename()
                .substring(multipartFile.getOriginalFilename().lastIndexOf('.')).toLowerCase();

        String fileName = generateFileName(multipartFile.getOriginalFilename());

        String url = upload(fileName + extension, multipartFile);
        AWSFile file = AWSFile.builder()
                .url(url)
                .type(multipartFile.getContentType())
                .build();
        return fileRepository.save(file);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

    private String upload(String fileName, MultipartFile multipartFile) throws IOException {
        String fileUrl = getEndPoint() + "excel/" + fileName;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());
        getClient().putObject(new PutObjectRequest(getBucketName(), "excel/" + fileName,
                multipartFile.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return fileUrl;
    }

    public void deleteFile(AWSFile file) {
        String key = file.getUrl().substring(getEndPoint().length() + 1);
        getClient().deleteObject(getBucketName(), key);
        fileRepository.delete(file);
    }

    private String generateFileName(String originalName) {
        return DigestUtils.md5Hex(originalName + LocalDateTime.now());
    }

    public AWSFile getFile(Long fileId) {
        return fileRepository.findById(fileId).orElseThrow(() -> new AppException("файл не найден"));
    }
}

