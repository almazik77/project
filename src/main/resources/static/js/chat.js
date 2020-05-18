function sendMessage(pageId, text) {
    let body = {
        pageId: pageId,
        text: text
    };
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({

        url: "/messages",
        method: "POST",
        data: JSON.stringify(body),
        contentType: "application/json",
        dataType: "json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token)
        },
        complete: function () {
            receiveMessage(pageId)
        }
    });
}

// LONG POLLING
function receiveMessage(pageId) {
    $.ajax({
        url: "/messages?pageId=" + pageId,
        method: "GET",
        dataType: "json",
        contentType: "application/json",
        success: function (response) {
            $('#messages').first().after('<li>' + response[0]['text'] + '</li>');
            receiveMessage(pageId);
        }
    })
}