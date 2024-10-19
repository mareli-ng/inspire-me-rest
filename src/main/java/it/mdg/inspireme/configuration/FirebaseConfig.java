package it.mdg.inspireme.configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class FirebaseConfig {

	@PostConstruct
	public void initialize() {
		 String json = System.getenv("FIREBASE_SERVICE_ACCOUNT");
		
        // Controlla se la variabile json è stata caricata correttamente
        if (json == null || json.isEmpty()) {
            throw new IllegalArgumentException("La variabile d'ambiente FIREBASE_SERVICE_ACCOUNT non è impostata correttamente.");
        }

		try (ByteArrayInputStream serviceAccountStream = new ByteArrayInputStream(json.getBytes())) {
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
					.setDatabaseUrl("https://inspire-me-storage.firebaseio.com") // Sostituisci con il tuo ID progetto
					.setStorageBucket("inspire-me-storage.appspot.com") 
					.build();

			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante l'inizializzazione di Firebase: " + e.getMessage(), e);
        }
	}
	
}
