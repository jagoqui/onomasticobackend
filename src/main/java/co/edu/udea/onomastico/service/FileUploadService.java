package co.edu.udea.onomastico.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import co.edu.udea.onomastico.util.DateUtil;

@Service
public class FileUploadService {
	@Value("${app.upload.dir}")
	String uploadDirectory;

	public String uploadFile(MultipartFile file) throws IOException {
		String dateTime = DateUtil.getCurrentDatetime();

		Path locationDestiny = Paths
				.get(uploadDirectory + File.separator + StringUtils.cleanPath(dateTime.toString() + file.getOriginalFilename()));
		Files.copy(file.getInputStream(), locationDestiny, StandardCopyOption.REPLACE_EXISTING);

		return locationDestiny.toString();
	}
}
