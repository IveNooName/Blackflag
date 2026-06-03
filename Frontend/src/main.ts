async function getDownload(link: string): Promise<boolean> {

    try {
        const response = await fetch(
            "http://localhost:8080/api/v1/music/download?link=" + encodeURIComponent(link)
        );

        if (!response.ok) {
            throw new Error(`Server error: ${response.status}`);
        }

        const blob = await response.blob();

        const disposition = response.headers.get("Content-Disposition");
        const filename = disposition?.match(/filename="?([^"]+)"?/)?.[1] ?? "download";

        const url = URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = filename;
        a.click();
        URL.revokeObjectURL(url);

        return true;
    } catch (e) {
        console.error(e);
        return false;
    }
}

function getDeezerLink(): string {
    const urlField = document.getElementById("deezer-album-url-input") as HTMLInputElement;
    return urlField.value;

}

function isValidUrlFormat(link: string): boolean {

    if (link == null) {
        return false;
    }

    const regex = /^https:\/\/www\.deezer\.com\/[a-z]{2}\/album\/[a-zA-Z0-9]+\/?$/;
    return regex.test(link);

}

function showInvalidLinkField(): void {
    const textField = document.getElementById("deezer-album-url-input") as HTMLInputElement;
    const feedbackText = document.getElementById("deezer-album-url-feedback") as HTMLDivElement;

    feedbackText.style.display = "block";
    textField.classList.add("is-invalid");

}

function hideInvalidLinkField(): void {

    const textField = document.getElementById("deezer-album-url-input") as HTMLInputElement;
    const feedbackText = document.getElementById("deezer-album-url-feedback") as HTMLDivElement;

    feedbackText.style.display = "none";
    textField.classList.remove("is-invalid");

}

function showLoadingIndicator(): void {
    const spinner = document.getElementById("deezer-album-url-button-spinner") as HTMLButtonElement;
    const text = document.getElementById("deezer-album-url-button-text") as HTMLInputElement;

    spinner.hidden = false;
    text.innerText = "Loading...";

}

function hideLoadingIndicator(): void {
    const spinner = document.getElementById("deezer-album-url-button-spinner") as HTMLButtonElement;
    const text = document.getElementById("deezer-album-url-button-text") as HTMLInputElement;

    spinner.hidden = true;
    text.innerText = "Submit";
}

async function main(): Promise<void> {
    const urlField = document.getElementById("deezer-album-url-input") as HTMLInputElement;
    const submitButton = document.getElementById("deezer-album-url-button") as HTMLInputElement;
    const alert = document.getElementById("error-alert") as HTMLDivElement;

    try {
        urlField.readOnly = true;
        submitButton.disabled = true;

        const link: string = getDeezerLink();
        const status: boolean = isValidUrlFormat(link);

        if (!status) {
            showInvalidLinkField();
            urlField.readOnly = false;
            submitButton.disabled = false;
            console.error("URL is invalid");
            return;
        }

        showLoadingIndicator();
        const wasSuccessfully: boolean = await getDownload(link);

        hideLoadingIndicator();
        urlField.readOnly = false;
        submitButton.disabled = false;


        if (!wasSuccessfully) {
            alert.style.display = "block";
        }
    } catch (e) {
        alert.style.display = "block";
        console.error(e);
    }
}

function closeErrorAlert(): void {
    const alert = document.getElementById("error-alert") as HTMLDivElement;
    alert.style.display = "none";

}

(window as any).main = main;
(window as any).closeErrorAlert = closeErrorAlert;
(window as any).hideInvalidLinkField = hideInvalidLinkField;