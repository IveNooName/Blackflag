export function showInvalidLinkField(): void {
    const textField = document.getElementById("deezer-album-url-input") as HTMLInputElement;
    const feedbackText = document.getElementById("deezer-album-url-feedback") as HTMLDivElement;

    feedbackText.style.display = "block";
    textField.classList.add("is-invalid");

}

export function hideInvalidLinkField(): void {

    const textField = document.getElementById("deezer-album-url-input") as HTMLInputElement;
    const feedbackText = document.getElementById("deezer-album-url-feedback") as HTMLDivElement;

    feedbackText.style.display = "none";
    textField.classList.remove("is-invalid");

}

export function showLoadingIndicator(): void {
    const spinner = document.getElementById("deezer-album-url-button-spinner") as HTMLButtonElement;
    const text = document.getElementById("deezer-album-url-button-text") as HTMLInputElement;

    spinner.hidden = false;
    text.innerText = "Loading...";

}

export function hideLoadingIndicator(): void {
    const spinner = document.getElementById("deezer-album-url-button-spinner") as HTMLButtonElement;
    const text = document.getElementById("deezer-album-url-button-text") as HTMLInputElement;

    spinner.hidden = true;
    text.innerText = "Submit";
}

export function closeErrorAlert(): void {
    const alert = document.getElementById("error-alert") as HTMLDivElement;
    alert.style.display = "none";

}