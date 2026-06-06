package com.ivenooname.blackflag.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@Slf4j
public class TrackUrlConverter {

	public TrackUrlConverter() {
	}

	public Optional<String> deezerUrlConverter(String shortUrl) {
		try {
			HttpClient client = HttpClient.newHttpClient();

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(shortUrl)).method("HEAD", HttpRequest.BodyPublishers.noBody()).build();

			HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

			String location = response.headers().firstValue("Location").orElseThrow(() -> new RuntimeException("Kein Location-Header gefunden"));

			URI locationUri = URI.create(location);

			String dest = null;
			String query = locationUri.getQuery();

			if(query != null) {
				for(String param : query.split("&")) {
					String[] parts = param.split("=", 2);

					String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
					String value = parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : "";

					if("dest".equals(key)) {
						dest = value;
						break;
					}
				}
			}

			if(dest == null || dest.isEmpty()) {
				throw new RuntimeException("dest-Parameter nicht gefunden");
			}

			URI destUri = URI.create(dest);

			return Optional.of(
					destUri.getScheme() + "://" + destUri.getHost() + destUri.getPath()
			);
		} catch(Exception e) {
			log.error(e.toString());
			return Optional.empty();
		}
	}
}