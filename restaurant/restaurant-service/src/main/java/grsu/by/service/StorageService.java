package grsu.by.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String upload(String folder, MultipartFile file);

    void delete(String objectUrl);
}
