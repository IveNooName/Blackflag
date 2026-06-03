package com.ivenooname.blackflag.presentation;

import com.ivenooname.blackflag.service.AlbumDTO;
import com.ivenooname.blackflag.service.AlbumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class AlbumController {

	private final AlbumService albumService;

	public AlbumController(AlbumService albumService) {
		this.albumService = albumService;
	}

	@GetMapping("music/download")
	public ResponseEntity<Resource> getAlbumZipArchive(@RequestParam String link) {

		if(link == null) {
			log.info("link is null");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}
		if(link.isEmpty()) {
			log.info("link is empty");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}

		try {
			Optional<String> checkedLink = albumService.isAlbumLinkValid(link);

			if(checkedLink.isEmpty()) {
				log.info("invalid album link");
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
			}

			AlbumDTO album = new AlbumDTO(checkedLink.get());

			String outputPath = "/Users/sonoma/blackflag/temp/downloads/" + album.getId();
			String zipFilePath = "/Users/sonoma/blackflag/temp/archives/";

			albumService.startPythonDockerContainer(album, outputPath);
			albumService.makeZipFile(album, outputPath, zipFilePath);


			log.info("Preparing ResponseEntity...");
			File zipFile = new File(zipFilePath + album.getId() + ".zip");

			log.info("Sending Response...");
			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.header(
							HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + zipFile.getName() + "\""
					)
					.body(new FileSystemResource(zipFile));
		} catch (Exception e) {
			log.error("An error occurred in the application - Aborting the download!");
			return ResponseEntity.badRequest().build();
		}
	}
}
