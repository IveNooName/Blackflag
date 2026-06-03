async function getDownload(): Promise<Response> {

    let link: string = "https://www.deezer.com/de/album/1346746";

    const response: Response = await fetch("http://localhost:8080/api/v1/music/download?link=" + link);

    if (!response.ok) {
        throw new Error(response.statusText);
    }

    return response;
}

async function prepareZiparchive(response: Promise<Response>): Promise<void> {

    const blobFile = await (await response).blob();
    const tempDownloadUrl = window.URL.createObjectURL(blobFile); //creates a temp download link in the browser to the RAM

    //prepare the File
    const anchor = document.createElement("a");
    anchor.href = tempDownloadUrl;
    anchor.download = 'music.zip';

    //start the download
    document.body.appendChild(anchor);
    anchor.click();

    //cleanup
    anchor.remove();
    window.URL.revokeObjectURL(tempDownloadUrl);
}

function getDeezerLink(): string {
    const urlField = document.getElementById("deezer-album-url-input") as HTMLInputElement;

    if (urlField == null) {
        throw new Error("URL field is not available");
    }

    return urlField.value;

}

function isValidUrlFormat(link: string):boolean {

    const regex = /^https:\/\/www\.deezer\.com\/[a-z]{2}\/album\/[a-zA-Z0-9]+\/?$/;
    return regex.test(link);

}


function main(): void {

    const link: string = getDeezerLink();
    const status = isValidUrlFormat(link);

    if(!status) {
        throw new Error("URL is invalid");
    }



}


// const response: Promise<Response> = getDownload();
// prepareZiparchive(response);


(window as any).main = main;