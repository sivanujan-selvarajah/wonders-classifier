package ch.zhaw.wonders.service;

import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.translate.Translator;
import ch.zhaw.wonders.dto.Label;
import ai.djl.ndarray.types.Shape;
import ai.djl.basicmodelzoo.cv.classification.ResNetV1;
import ai.djl.nn.Block;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class Inference {

    private static final Path MODEL_DIR = Paths.get("models");
    private static final String MODEL_NAME = "wonders-classifier";

    private Predictor<Image, Classifications> predictor;

    public Inference() {
        try {
            Model model = Model.newInstance(MODEL_NAME);

            // Block manuell wie beim Training setzen
            Block resNet = ResNetV1.builder()
                    .setImageShape(new Shape(3, Models.IMAGE_HEIGHT, Models.IMAGE_WIDTH))
                    .setNumLayers(50)
                    .setOutSize(Models.NUM_OF_OUTPUT)
                    .build();
            model.setBlock(resNet);

            // Modell laden (lädt wonders-classifier.params)
            model.load(MODEL_DIR, MODEL_NAME);

            Translator<Image, Classifications> translator = ImageClassificationTranslator.builder()
                    .addTransform(new Resize(Models.IMAGE_WIDTH, Models.IMAGE_HEIGHT))
                    .addTransform(new ToTensor())
                    .optApplySoftmax(true)
                    .build();

            predictor = model.newPredictor(translator);

        } catch (Exception e) {
            System.err.println("❌ Fehler beim Laden des Modells: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Classifications predict(MultipartFile file) {
        try (InputStream is = new ByteArrayInputStream(file.getBytes())) {
            BufferedImage bufferedImage = ImageIO.read(is);
            Image img = ImageFactory.getInstance().fromImage(bufferedImage);
            return predictor.predict(img);
        } catch (Exception e) {
            System.err.println("❌ Fehler bei der Bildklassifikation: " + e.getMessage());
            return new Classifications(List.of(), List.of());
        }
    }

    public List<String> availableModels() {
        try (Stream<Path> files = Files.list(MODEL_DIR)) {
            return files
                    .filter(p -> p.toString().endsWith(".params"))
                    .map(p -> p.getFileName().toString().replaceFirst("\\.params$", ""))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Fehler beim Lesen der Modelle: " + e.getMessage());
            return List.of();
        }
    }

    public List<Label> getLabelInfos() {
        Path synset = MODEL_DIR.resolve("synset.txt");
        try {
            return Files.readAllLines(synset).stream()
                    .map(label -> new Label(label, ""))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Fehler beim Laden der Labels: " + e.getMessage());
            return List.of();
        }
    }
}