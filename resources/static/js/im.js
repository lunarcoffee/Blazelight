var socket = new WebSocket("ws" + window.location.href.slice(4) + "/ws");

var messageList = document.getElementById("im-list");
var inputForm = document.getElementById("im-input");

socket.addEventListener("open", function () {
    console.log("open"); // TODO:

    inputForm.addEventListener("submit", function (e) {
        e.preventDefault();

        socket.send(inputForm.children[0].value);
        inputForm.children[0].value = "";

        return false;
    });
});

socket.addEventListener("message", function (e) {
    var listItem = document.createElement("li");
    listItem.className = "im-message";
    listItem.appendChild(document.createTextNode(e.data));
    messageList.appendChild(listItem);

    scrollMessages();
});

function scrollMessages() {
    messageList.scrollBy(0, 30);
}
