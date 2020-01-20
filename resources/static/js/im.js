var socket = new WebSocket("ws" + window.location.href.slice(4) + "/ws");

var loadingMessage = document.getElementsByClassName("im-loading")[0];
var messageBox = document.getElementsByClassName("im-box")[0];
var messageList = document.getElementsByClassName("im-list")[0];
var inputForm = document.getElementsByClassName("im-input")[0];

socket.addEventListener("open", function () {
    inputForm.addEventListener("submit", function (e) {
        e.preventDefault();

        socket.send(inputForm.children[0].value);
        inputForm.children[0].value = "";

        return true;
    });
});

socket.addEventListener("message", function (e) {
    if (e.data === "init") {
        loadingMessage.style.display = "none";
        return;
    }

    var listItem = document.createElement("li");
    listItem.className = e.data[0] === "a" ? "im-sent-message" : "im-received-message";
    listItem.appendChild(document.createTextNode(e.data.substr(1)));
    messageList.appendChild(listItem);

    scrollMessages();
});

function scrollMessages() {
    messageBox.scrollBy(0, messageBox.scrollHeight);
}
