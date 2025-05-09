# 🏛️ Wonders Image Classifier – DJL + Spring Boot

Dieses Projekt implementiert eine Webservice-Anwendung zur Klassifikation von Sehenswürdigkeiten („Weltwundern“) anhand von Bildern.  
Es nutzt die **Deep Java Library (DJL)** mit PyTorch zur Inferenz eines trainierten CNN-Modells und stellt die Funktionalität über eine **Spring Boot REST-API** bereit.

---

## ✨ Features

- Klassifikation von Sehenswürdigkeiten (Weltwunder) via Bild-Upload
- DJL Serving als separates Modell-Backend
- REST-API mit Endpunkten für Healthcheck, Labels, Modelle und Inferenz
- Eingebettetes UI (HTML/JavaScript) für Bild-Upload und Ergebnisanzeige
- Multi-Container-Setup mit Docker & Azure Deployment

---

## 🧠 Verwendete Technologien

| Bereich           | Tools                                 |
|-------------------|----------------------------------------|
| Backend           | Java 21, Spring Boot                   |
| ML-Inferenz       | Deep Java Library (DJL), PyTorch       |
| Modellformat      | `.params` + `synset.txt`               |
| Konfigurationsdateien | `serving.properties`, `config.properties` |
| Containerisierung | Docker                                |
| Deployment        | Docker Hub, Azure App Service          |

---

## 🗂️ Projektstruktur

wonders-classifier/
├── models/
│   ├── wonders-classifier-0001.params   # Modell
│   ├── synset.txt                       # Klassen
│   ├── serving.properties               # DJL-Serving Konfiguration
│   └── config.properties                # weitere Konfigurationen
├── Dockerfile                           # Spring Boot App
├── Dockerfile.model                     # DJL Serving Image
├── src/main/java/…                    # Spring Boot REST-API
└── README.md

---

## ⚙️ Setup & Lokale Ausführung

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

## 🧪 REST API – Übersicht

| Methode | Pfad       | Beschreibung                                |
|---------|------------|---------------------------------------------|
| GET     | `/ping`    | Health-Check, zeigt ob App läuft            |
| GET     | `/labels`  | Gibt Liste aller Klassennamen zurück        |
| GET     | `/models`  | Listet verfügbare Modelle im System         |
| POST    | `/analyze` | Klassifiziert hochgeladenes Bild (Form-Upload) |

☁️ Azure Deployment

Spring Boot App (Frontend/API)

az webapp create \
  --resource-group wonders-rg-we \
  --plan wonders-plan-we \
  --name wonders-classifier-app \
  --deployment-container-image-name sivanujan26/wonders-classifier:latest

  az webapp config appsettings set \
  --resource-group wonders-rg-we \
  --name wonders-classifier-app \
  --settings WEBSITES_PORT=8080

  DJL Serving (Backend/Model)

  az webapp create \
  --resource-group wonders-rg-we \
  --plan wonders-plan-we \
  --name wonders-model-app \
  --deployment-container-image-name sivanujan26/djl-wonders-model:latest

    
az webapp config appsettings set \
  --resource-group wonders-rg-we \
  --name wonders-model-app \
  --settings WEBSITES_PORT=8080


  🧾 Modellinformationen

	•	Selbst trainiertes CNN mit DJL Training API
	•	Ausgabeformat .params + synset.txt
	•	Konfiguration über serving.properties und config.properties
	•	Optimiert für CPU-Inferenz (Azure-kompatibel)


    📸 UI-Integration (optional)
	•	Datei: src/main/resources/static/index.html
	•	JavaScript fetch() POSTet Bild an /analyze
	•	Vorschau des Bildes und JSON-Antwort im Browser

    👨‍💻 Autor

Sivanujan Selvarajah
ZHAW – FS2025 – Modul Model Deployment & Maintenance
https://github.com/sivanujan-selvarajah/wonders-classifier

