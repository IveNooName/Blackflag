import {
    closeErrorAlert,
    hideInvalidLinkField,
    hideLoadingIndicator,
    showInvalidLinkField,
    showLoadingIndicator
} from "./uiFunctions";

async function getDownload(link: string): Promise<boolean> {

    try {
        const response = await fetch(
            "http://localhost:8080/api/v1/music/download?link=" + encodeURIComponent(link)
        );

        if (!response.ok) {
            console.error("Server error: " + response.status)
            return false;
        }

        const blob = await response.blob();

        const url = URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = "archive.zip";
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
    if (regex.test(link)) {
        return true;
    }
    const trackRegex = /^https:\/\/www\.deezer\.com\/track\/[a-zA-Z0-9]+\/?$/;
    return trackRegex.test(link);

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

(window as any).main = main;
(window as any).closeErrorAlert = closeErrorAlert;
(window as any).hideInvalidLinkField = hideInvalidLinkField;