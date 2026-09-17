package com.ivenooname.blackflag.presentation;

import com.ivenooname.blackflag.service.AlbumDTO;
import com.ivenooname.blackflag.service.AlbumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${app.output-path}")
	private String baseOutputPath;

	@Value("${app.zip-file-path}")
	private String baseZipFilePath;
	private final AlbumService albumService;

	public AlbumController(AlbumService albumService) {
		this.albumService = albumService;
	}

	@CrossOrigin(origins = "http://localhost:5173")
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
			String downloadLink;
			Optional<String> checkedAlbumLink = albumService.isAlbumLinkValid(link);

			if(checkedAlbumLink.isEmpty()) {
				Optional<String> checkedTrackLink = albumService.isTrackLinkValid(link);

				if(checkedTrackLink.isEmpty()) {
					log.info("invalid link");
					return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
				} else {
					downloadLink = checkedTrackLink.get();
				}
			} else {
				downloadLink = checkedAlbumLink.get();
			}

			AlbumDTO album = new AlbumDTO(downloadLink);

			String outputPath = baseOutputPath + album.getId();

			albumService.startPythonDockerContainer(album, outputPath);
			albumService.makeZipFile(album, outputPath, baseZipFilePath);


			log.info("Preparing ResponseEntity...");
			File zipFile = new File(baseZipFilePath + album.getId() + ".zip");

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
