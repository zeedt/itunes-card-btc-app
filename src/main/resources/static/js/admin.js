/**
 * Created by longbridge on 11/23/17.
 */
function verifyCard(){
    $.ajax({
        type: "POST",
        url:"/verifyCard",
        data : JSON.stringify({username:$("#user").val(),cardId:$("#id").val(),}),
        async:false,
        contentType : "application/json; charset=utf-8",
        success : function (result) {
            if (result.status=="success"){
                document.getElementById("admdeclineFormId").reset();
                $("#admdeclineFormId").html("");
                $("#verDecButtons").html("");
                $("#admdeclineMessage").html(result.message);

            }else{
                $("#admdeclineMessage").html(result.message);
            }
        },
        error : function (error) {
            console.log("Error");
        }
    })
}
function declineCard(){
    $("#declineformbutton").click();
}
function declineform(form){
    var formData = new FormData(form);
    formData.append("username",$("#user").val());
    formData.append("cardId",$("#id").val());
    var valid = true;
    if($("#itunescardDecline").val()==undefined || $("#itunescardDecline").val()==""){
        valid = false;$("#decfileM").html("Please select a file");
    }else{
        $("#decfileM").html("");
    }
    if($("#declineReason").val()==undefined || $("#declineReason").val()==""){
        valid = false;$("#decReasM").html("Provide a comment or description");
    }else{
        $("#decReasM").html("");
    }
    if(valid) {
        $.ajax({
            type: "POST",
            url: "/declineCard",
            data: formData,
            async: false,
            processData: false,
            contentType: false,
            cache: false,
            success: function (result) {
                if (result.status == "success") {
                    document.getElementById("admdeclineFormId").reset();
                    $("#admdeclineFormId").html("");
                    $("#verDecButtons").html("");
                    $("#admdeclineMessage").html(result.message);

                } else {
                    $("#admdeclineMessage").html(result.message);
                }
            },
            error: function (error) {
                console.log("Error");
            }
        })
    }
}

function loadCard(user,id){
    $.ajax({
        type: "POST",
        url:"/modalVerDecDet",
        data : JSON.stringify({user:user,cardid:id}),
        contentType: "application/json; charset=utf-8",
        async:false,
        success : function (result) {
            if (result!="null" && result!=""){
                $("#verdecbody").html(result);
                $("#verdeclineCardModal").modal('show');
                $('#verdeclineCardModal').on('shown.bs.modal', function () {
                    $('#tobePassedInput').html("<input type='hidden' id='user' value='"+user+"' /><input type='hidden' id='id' value='"+id+"' />")
                });

            }else{
                $("#declineMessage").html(result.message);
            }
        },
        error : function (error) {
            console.log("Error");
        }
    })
}
function viewProfile(user,id){
    console.log("User passed "+JSON.stringify(user));
    $.ajax({
        type: "POST",
        url:"/profileModal",
        data : JSON.stringify({user:user,cardid:id}),
        contentType: "application/json; charset=utf-8",
        async:false,
        success : function (result) {
            if (result!="null" && result!=""){
                $("#verdecbody").html(result);
                $("#verdeclineCardModal").modal('show');
            }else{
                $("#declineMessage").html(result.message);
            }
        },
        error : function (error) {
            console.log("Error");
        }
    })
}
function makeEnquiry(){
    $("#getEnquiry").click();
}
function getEnquiryDet(form) {
    console.log("form is "+JSON.stringify(form));
    $.ajax({
        type: "POST",
        url:"/getuserEnquiry",
        data : JSON.stringify({username:form.username.value}),
        contentType: "application/json; charset=utf-8",
        async:false,
        success : function (result) {
            if (result!="null" && result!=""){
                $("#userDetDiv").html(result);
            }else{
                $("#userDetDiv").html("<p style='color: red;'>User not found</p>");
            }
        },
        error : function (error) {
            $("#userDetDiv").html("<p style='color: red'>Errror occured. Please contact admin</p>");
            console.log("Error");
        }
    })

}