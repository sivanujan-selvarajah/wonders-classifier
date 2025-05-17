# Wonders Image Classifier – DJL + Spring Boot

Dieses Projekt implementiert eine Webservice-Anwendung zur Klassifikation von Sehenswürdigkeiten („Weltwundern“) anhand von Bildern.  
Es nutzt die **Deep Java Library (DJL)** mit PyTorch zur Inferenz eines trainierten CNN-Modells und stellt die Funktionalität über eine **Spring Boot REST-API** bereit.

---

## Features

- Klassifikation von Sehenswürdigkeiten (Weltwunder) via Bild-Upload
- DJL Serving als separates Modell-Backend
- REST-API mit Endpunkten für Healthcheck, Labels, Modelle und Inferenz
- Eingebettetes UI (HTML/JavaScript) für Bild-Upload und Ergebnisanzeige
- Multi-Container-Setup mit Docker & Azure Deployment

---

## Projektstruktur

```plaintext
wonders-classifier/
├── [README.md](http://_vscodecontentref_/1)                         # Projektbeschreibung mit Setup, Screenshots und API-Doku
├── Dockerfile                        # Dockerfile für die Spring Boot App
├── [Dockerfile.model](http://_vscodecontentref_/2)                  # (Optional) Dockerfile für DJL-Serving oder Modell
├── mvnw, [mvnw.cmd](http://_vscodecontentref_/3)                    # Maven Wrapper für Linux/macOS und Windows
├── [pom.xml](http://_vscodecontentref_/4)                           # Projektdefinition mit Dependencies (Spring Boot, DJL etc.)
├── target/                           # Generierte Build-Dateien (nach Maven-Build)
├── wonders/                          # Trainingsdaten für das Modell (nicht Teil der Abgabe)
│
├── models/                           # Modellverzeichnis (für Inference & Deployment)
│   ├── wonders-classifier-0001.params  # Trainiertes ResNet-Modell mit DJL gespeichert
│   ├── synset.txt                    # Liste aller Klassen (eine pro Zeile)
│   ├── serving.properties            # Konfiguration für DJL-Serving (optional)
│   └── config.properties             # Weitere Modell-Parameter (optional)
│
├── src/
│   └── main/
│       ├── java/
│       │   └── ch/
│       │       └── zhaw/
│       │           └── wonders/
│       │               ├── dto/                  # Datenübertragungsobjekte (DTOs)
│       │               │   ├── ClassificationResult.java  # Ergebnis-Objekt mit Label & Wahrscheinlichkeit
│       │               │   ├── Label.java                 # Einzelnes Label mit Zusatzinfos
│       │               │   └── Model.java                 # Modell-Metadaten für die API
│       │               │
│       │               ├── service/              # Businesslogik für ML & DJL
│       │               │   ├── Training.java              # Training des Modells (lokal mit DJL)
│       │               │   ├── Inference.java             # Modell laden + Bildklassifikation
│       │               │   └── Models.java                # Modellstruktur (ResNet) + Hilfsmethoden
│       │               │
│       │               ├── controller/           # REST-API-Controller
│       │               │   └── ClassificationController.java  # `/analyze`, `/ping`, `/labels`, ...
│       │               │
│       │               └── WeltwunderClassifierApplication.java # Einstiegspunkt für Spring Boot
│       │
│       └── resources/              # (UI, HTML, JS, evtl. Templates)
│
├── test/                            # (derzeit leer – optional für Unit Tests)
└── [HELP.md](http://_vscodecontentref_/5)                          # Auto-generiertes Maven-Hilfe-File (nicht notwendig)
```
---

### Verwendete Technologien

| Bereich              | Tools                                                 |
|----------------------|-------------------------------------------------------|
| **Backend**          | Java 21, Spring Boot                                  |
| **ML-Inferenz**      | Deep Java Library (DJL), PyTorch                      |
| **Modellformat**     | `.params` + `synset.txt`                              |
| **Konfigurationsdateien** | `serving.properties`, `config.properties`       |
| **Containerisierung**| Docker                                                |
| **Deployment**       | Docker Hub, Azure App Service                         |

---

### Dataset

Das Dataset für das Training des Modells stammt von Kaggle.  
Es enthält Bilder von Sehenswürdigkeiten, die in verschiedene Klassen unterteilt sind.

**Kaggle-Link:** [Hier klicken](https://www.kaggle.com/datasets/balabaskar/wonders-of-the-world-image-classification) *(Bitte den tatsächlichen Link einfügen)*

---

## Setup & Lokale Ausführung

### Voraussetzungen

- Java 21
- Docker
- Internetverbindung (für Modelldownload + DJL-Setup)

### Lokale Ausführung

```bash
docker build -t wonders-classifier .
docker run -p 8080:8080 wonders-classifier

docker build -t wonders-model -f Dockerfile.model .
docker run -p 8081:8080 wonders-model

# Test-Endpunkte

curl http://localhost:8082/ping
curl -X POST http://localhost:8081/predictions/wonders-classifier -T test.jpg
```

## REST API – Übersicht

| Methode | Pfad       | Beschreibung                                |
|---------|------------|---------------------------------------------|
| GET     | `/ping`    | Health-Check, zeigt ob App läuft            |
| GET     | `/labels`  | Gibt Liste aller Klassennamen zurück        |
| GET     | `/models`  | Listet verfügbare Modelle im System         |
| POST    | `/analyze` | Klassifiziert hochgeladenes Bild (Form-Upload) |

Azure Deployment

Spring Boot App (Frontend/API)
```
az webapp create \
  --resource-group wonders-rg-we \
  --plan wonders-plan-we \
  --name wonders-classifier-app \
  --deployment-container-image-name sivanujan26/wonders-classifier:latest

  az webapp config appsettings set \
  --resource-group wonders-rg-we \
  --name wonders-classifier-app \
  --settings WEBSITES_PORT=8080
```


    Modellinformationen

	•	Selbst trainiertes CNN mit DJL Training API
	•	Ausgabeformat .params + synset.txt
	•	Konfiguration über serving.properties und config.properties
	•	Optimiert für CPU-Inferenz (Azure-kompatibel)


    UI-Integration

	•	Datei: src/main/resources/static/index.html
	•	JavaScript fetch() POSTet Bild an /analyze
	•	Vorschau des Bildes und JSON-Antwort im Browser


Autor:
Sivanujan Selvarajah
ZHAW – FS2025 – Modul Model Deployment & Maintenance
https://github.com/sivanujan-selvarajah/wonders-classifier

