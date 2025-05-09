package ch.zhaw.wonders.controller;

import ai.djl.modality.Classifications;
import ch.zhaw.wonders.dto.ClassificationResult;
import ch.zhaw.wonders.dto.Label;
import ch.zhaw.wonders.dto.Model;
import ch.zhaw.wonders.service.Inference;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ClassificationController {

    private final Inference inference;

    public ClassificationController() {
        this.inference = new Inference(); // Konstruktor ohne Exception
    }


    @GetMapping("/ping")
    public String ping() {
        return "🎯 Wonders classifier is up and running!";
    }

    /**
     * Healthcheck-Endpunkt
     */
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
                "status", "UP",
                "version", "1.0.0",
                "timestamp", Instant.now().toString()
        );
    }

    /**
     * Gibt alle verfügbaren Klassen/Labels aus synset.txt zurück.
     */
    @GetMapping("/labels")
    public List<Label> getLabels() {
        return inference.getLabelInfos();
    }

    /**
     * Gibt verfügbare Modelle im Ordner `models/` zurück.
     */
    @GetMapping("/models")
    public List<Model> listModels() {
        return inference.availableModels().stream()
                .map(name -> {
                    return switch (name) {
                        case "wonders-classifier-0001" -> new Model(name, 0.91, 10);
                        default -> new Model(name, null, null);
                    };
                })
                .collect(Collectors.toList());
    }

    /**
     * Nimmt ein Bild entgegen, klassifiziert es mit dem Modell und liefert Ergebnisse zurück.
     */
    @PostMapping(path = "/analyze", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ClassificationResult> predict(@RequestParam("image") MultipartFile file) throws Exception {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Kein Bild übermittelt.");
        }

        System.out.println("🔍 Empfangenes Bild: " + file.getOriginalFilename());

        Classifications result = inference.predict(file);

        return result.items().stream()
                .map(item -> new ClassificationResult(item.getClassName(), item.getProbability()))
     
                .sorted((a, b) -> Double.compare(b.getProbability(), a.getProbability()))
                .limit(5)
                .collect(Collectors.toList());
    }
}
    