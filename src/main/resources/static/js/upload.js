/**
 * Created by longbridge on 11/21/17.
 */
function triggerSubmission() {
    $("#uploadForm").click();
}
function uploadform(form) {
    var valid = true;
    if($("#itunescard").val()==undefined || $("#itunescard").val()==""){
        valid = false;$("#cardM").html("Please select a file");
    }else{
        $("#cardM").html("");
    }
    if($("#desc").val()==undefined || $("#desc").val()==""){
        valid = false;$("#descM").html("Provide a comment or description");
    }else{
        $("#descM").html("");
    }
    if($("#amount").val()==undefined || $("#amount").val()==""){
        valid = false;$("#amountM").html("Provide an amount");
    }else{
        $("#amountM").html("");
    }
    var formData = new FormData(form);
    if(valid) {
        $.ajax({
            type: "POST",
            url: "/user/uploadCard",
            data: formData,
            async: false,
            processData: false,
            contentType: false,
            cache: false,
            success: function (result) {
                if (result.status == "success") {
                    document.getElementById("uploadFormId").reset();
                    $("#uploadSMessage").html(result.message);
                } else {
                    $("#uploadEMessage").html(result.message);
                }
            },
            error: function (error) {
                console.log("Error");
            }
        })
    }
}