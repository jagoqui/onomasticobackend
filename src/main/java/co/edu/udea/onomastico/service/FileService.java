package co.edu.udea.onomastico.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import co.edu.udea.onomastico.config.FileStorageProperties;
import co.edu.udea.onomastico.exceptions.FileStorageException;

@Service
public class FileService {
	 private final Path fileStorageLocation;

	    @Autowired
	    public FileService(FileStorageProperties fileStorageProperties) {
	        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
	                .toAbsolutePath().normalize();

	        try {
	            Files.createDirectories(this.fileStorageLocation);
	        } catch (Exception ex) {
	            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
	        }
	    }
	    
	    public String storeFile(MultipartFile file, String name) {
	        // Normalize file name
	        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	        try {
	            // Check if the file's name contains invalid characters
	            if(fileName.contains("..")) {
	                throw new FileStorageException("Filename contains invalid sequence " + fileName);
	            }
	            fileName = StringUtils.cleanPath(name);
	            Path targetLocation = this.fileStorageLocation.resolve(fileName);
	            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
	            return fileName;
	        } catch (IOException ex) {
	            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
	        }
	    }
	    
	    public String deleteFile(String name) {
	        // Normalize file name
	    	 String fileName = StringUtils.cleanPath(name);
	        try {
	            // Check if the file's name contains invalid characters
	        	if(fileName.contains("..")) {
	                throw new FileStorageException("Filename contains invalid sequence " + fileName);
	            }
	            Path targetLocation = this.fileStorageLocation.resolve(fileName);
	            Files.delete(targetLocation);
	            return name;
	        } catch (IOException ex) {
	            throw new FileStorageException("Could not delete file " + fileName + ". Please try again!", ex);
	        }
	    }
	    
	    public Resource loadFileAsResource(String fileName) {
	        try {
	            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
	            Resource resource = new UrlResource(filePath.toUri());
	            if(resource.exists()) {
	                return resource;
	            } else {
	                throw new FileStorageException("File not found " + fileName);
	            }
	        } catch (MalformedURLException ex) {
	            throw new FileStorageException("File not found " + fileName, ex);
	        }
	    }
}
