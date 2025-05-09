/*document.addEventListener("DOMContentLoaded", () => {
    const MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

    // DOM-Elemente
    const uploadForm = document.getElementById("uploadForm");
    const imageInput = document.getElementById("image");
    const previewImg = document.getElementById("preview");
    const resultCard = document.getElementById("resultCard");
    const resultText = document.getElementById("resultText");
    const labelsList = document.getElementById("labels-list");
    const modelsList = document.getElementById("models-list"); // neu

    // Anzeigefunktionen
    const previewImage = file => {
        previewImg.src = URL.createObjectURL(file);
        previewImg.style.display = "block";
    };

    const showResult = entries => {
        resultText.innerHTML = entries.map(entry => 
            <div class="mb-2">
                <div class="d-flex justify-content-between">
                    <strong>${entry.className}</strong>
                    <span>${(entry.probability * 100).toFixed(2)}%</span>
                </div>
                <div class="progress" style="height: 20px;">
                    <div class="progress-bar bg-success" role="progressbar" 
                         style="width: ${(entry.probability * 100).toFixed(2)}%;" 
                         aria-valuenow="${(entry.probability * 100).toFixed(2)}" 
                         aria-valuemin="0" aria-valuemax="100">
                    </div>
                </div>
            </div>
        ).join("");
        resultCard.classList.remove("d-none");
    };

    const showError = message => {
        resultText.textContent = message;
        resultCard.classList.remove("d-none");
    };

    // Daten-Fetcher
    const fetchLabels = async () => {
        try {
            const resp = await fetch("/labels");
            if (!resp.ok) throw new Error(Status ${resp.status});
            const labels = await resp.json();
            showLabels(labels);
        } catch (err) {
            labelsList.innerHTML =
                <li style="color:red">Fehler beim Laden: ${err.message}</li>;
            console.error(err);
        }
    };

    const fetchModels = async () => {
        try {
            const resp = await fetch("/models");
            if (!resp.ok) throw new Error(Status ${resp.status});
            const data = await resp.json();
    
            const models = Array.isArray(data) ? data : data.models || [];
            showModels(models);
    
        } catch (err) {
            modelsList.innerHTML =
                <span class="text-danger">Fehler beim Laden: ${err.message}</span>;
            console.error(err);
        }
    };
    
    

    const handleUpload = async e => {
        e.preventDefault();
        const file = imageInput.files[0];

        if (!file) {
            alert("Bitte ein Bild auswählen.");
            return;
        }
        if (file.size > MAX_FILE_SIZE) {
            alert("Datei zu groß (max. 10 MB).");
            return;
        }

        previewImage(file);

        const formData = new FormData();
        formData.append("image", file);

        try {
            const resp = await fetch("/analyze", { method: "POST", body: formData });
            if (!resp.ok) throw new Error(Status ${resp.status});
            const data = await resp.json();
            showResult(data);
        } catch (err) {
            console.error("Analyse-Fehler:", err);
            showError("Fehler bei der Klassifikation.");
        }
    };

    const showLabels = labels => {
        labelsList.innerHTML = labels.map(({ className }) => 
            <span class="badge bg-secondary badge-label">${className}</span>
        ).join("");
    };
    
    const showModels = models => {
        modelsList.innerHTML = models.map(model => 
            <div>
                <span class="badge bg-info text-dark px-3 py-2">${model.name}</span>
                <div class="ms-2 mt-1 small text-muted">
                    Accuracy: ${model.accuracy != null ? (model.accuracy * 100).toFixed(1) + "%" : "–"}<br>
                    Epochen: ${model.epochs ?? "–"}
                </div>
            </div>
        ).join("");
    };
    
    

    // Event-Binding & Initial-Load
    uploadForm.addEventListener("submit", handleUpload);
    fetchLabels();
    fetchModels();
}
); */
