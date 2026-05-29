package com.ivenooname.blackflag.service;

import com.ivenooname.blackflag.exception.ContainerErrorCodeException;
import com.ivenooname.blackflag.exception.ContainerException;
import com.ivenooname.blackflag.exception.CreateDirectoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zeroturnaround.zip.ZipException;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@Slf4j
public class AlbumService {

	public AlbumService() {
	}

	public Optional<String> isAlbumLinkValid(String link) {
		if(! link.matches("\"?https://www\\.deezer\\.com/[a-z]{2}/album/[a-zA-Z0-9/]+\"?")) {
			log.info("Doesnt match the link format");
			return Optional.empty();
		}

		//replace the language (as example: '/de/' from the link and additional '"'
		String newLink = link.replaceAll("\\/[a-z]{2}\\/", "/");
		newLink = newLink.replace("\"", "");

		return Optional.of(newLink);
	}

	public void startPythonDockerContainer(AlbumDTO album, String outputPath) {

		try {
			log.info("Creating directory: {}", outputPath);
			Files.createDirectories(Paths.get(outputPath));
		} catch(IOException e) {
			log.error("Error by creating the directory: {}", e.toString());
			throw new CreateDirectoryException("An Error occurred by creating the download directory: " + e);
		}

		ProcessBuilder pb = new ProcessBuilder(
				"docker", "run",
				"--rm",
				"-v", outputPath + ":/data",
				"streamrip_v1_docker",
				"--no-db", "url", album.getAlbumLink()
		);
		pb.redirectErrorStream(true);

		try {
			log.info("Starting Python Container...");
//			pb.inheritIO(); //Shows Python/Docker-Container console on the Spring console

			Process process = pb.start();
			int exit = process.waitFor();

			if(exit != 0) {
				log.error("Python Container failed with exit code: {}", exit);
				throw new ContainerErrorCodeException("Python Container failed with exit code: " + exit);
			}

		} catch(IOException | InterruptedException e) {
			log.error("Error by the Python container: {}", e.toString());
			throw new ContainerException("An Error on the Python Container occurred: " + e);
		}

		log.info("Finished Download and stopped Python container");
	}

	public void makeZipFile(AlbumDTO album, String outputPath, String zipFilePath) {

		try {
			log.info("Creating ZIP-directory: {}", zipFilePath);
			Files.createDirectories(Paths.get(zipFilePath));
		} catch(IOException e) {
			log.error("Error by creating the directory for the ZIP: {}", e.toString());
			throw new CreateDirectoryException("An Error occurred by creating the ZIP directory: " + e);
		}

		String newZipPath = zipFilePath + album.getId() + ".zip";

		try {
			log.info("Creating ZIP file: {}", newZipPath);
			ZipUtil.pack(new File(outputPath), new File(newZipPath));

			File zipArchive = new File(zipFilePath);
			album.setZipArchive(zipArchive);
		} catch(Exception e) {
			log.error("Error by creating the ZIP: {}", e.toString());
			throw new ZipException("An Error occurred by creating the ZIP: " + e);
		}

		log.info("Finished creating ZIP-file successfully");
	}

}