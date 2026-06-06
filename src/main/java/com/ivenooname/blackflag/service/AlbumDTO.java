package com.ivenooname.blackflag.service;

import java.io.File;
import java.util.UUID;

public class AlbumDTO {

	private final UUID id = UUID.randomUUID();

	private String albumLink;

	private boolean finished;

	private File zipArchive;

	public AlbumDTO() {
		//empty for Jackson
	}

	public AlbumDTO(String albumLink) {
		this.albumLink = albumLink;
	}

	public AlbumDTO(String albumLink, boolean finished, File zipArchive) {
		this.albumLink = albumLink;
		this.finished = finished;
		this.zipArchive = zipArchive;
	}

	public UUID getId() {
		return id;
	}


	public String getAlbumLink() {
		return albumLink;
	}

	public void setAlbumLink(String albumLink) {
		this.albumLink = albumLink;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public File getZipArchive() {
		return zipArchive;
	}

	public void setZipArchive(File zipArchive) {
		this.zipArchive = zipArchive;
	}
}
