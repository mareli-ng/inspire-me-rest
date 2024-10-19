package it.mdg.inspireme.services;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;


@Service
public class FirebaseStorageService {

	    public String uploadFile(MultipartFile file, String imageName) throws IOException {
	        InputStream inputStream = file.getInputStream();
	        Bucket bucket = StorageClient.getInstance().bucket();
	        bucket.create(imageName, inputStream, "image/jpeg");
	        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/inspire-me-storage.appspot.com/o/%s?alt=media";
	        return String.format(DOWNLOAD_URL, file.getOriginalFilename());
	      }

//	    public String uploadFileOld(MultipartFile file) throws IOException {
//	        String bucketName = "inspire-me-storage.appspot.com"; // Sostituisci con il nome del tuo bucket
//	        String blobName = file.getOriginalFilename(); // Usa un nome univoco per ogni file
//
//	        // Carica il file nel bucket
//	        Blob blob = storage.create(Blob.newBuilder(bucketName, blobName)
//	                .setContentType(file.getContentType())
//	                .build(), file.getBytes());
//
//	        // Restituisci l'URL del file caricato
//	        return "https://storage.googleapis.com/" + bucketName + "/" + blobName;
//	    }
}
