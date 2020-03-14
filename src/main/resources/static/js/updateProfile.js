/**
 * Created by longbridge on 11/25/17.
 */
function calluserUpdate(){
    $("#userUpdateId").click();
}
function callpasswordUpdate(){
    $("#passwordUpdateId").click();
}
function userUpdate(form) {
    console.log("Called "+JSON.stringify(form));
    console.log("Called "+JSON.stringify(form.accountNumber.value));
    var valid = true;
    if(form.bank.value.length<1){
        valid=false;$("#bankM").html("You must select a bank");
    }else{
        $("#bankM").html("");
    }
    if(form.gender.value.length<1){
        valid=false;$("#genM").html("You must select a gender");
    }else{
        $("#genM").html("");
    }
    if(form.firstName.value.length<4){
        valid=false;$("#firstM").html("First name cannot be less than 4 characters");
    }else{
        $("#firstM").html("");
    }
    if(form.lastName.value.length<4){
        valid=false;$("#lastM").html("Last name cannot be less than 4 characters");
    }else{
        $("#lastM").html("");
    }
    if(form.email.value.length<10 || form.email.value.indexOf("@")<0){
        valid=false;$("#emailM").html("Invalid email address");
    }else{
        $("#emailM").html("");
    }
    if(form.accountNumber.value.length!=10){
        valid=false;$("#accountM").html("Invalid account number");
    }else{
        $("#accountM").html("");
    }
    if(valid){
        $.ajax({
            type: "POST",
            url:"/user/updateProfileDetails",
            data : JSON.stringify({bank:form.bank.value,firstName:form.firstName.value,lastName:form.lastName.value,
                gender:form.gender.value,accountNumber:form.accountNumber.value,email:form.email.value}),
            contentType: "application/json; charset=utf-8",
            async:false,
            success : function (result) {
                if (result.status=="success"){
                    $("#updateMessage").html("<p style='color: green'>"+result.message+"</p>");
                }else{
                    $("#updateMessage").html("<p style='color: red;'>"+result.message+"</p>");
                }
            },
            error : function (error) {
                $("#updateMessage").html("<p style='color: red'>Errror occured. Please contact admin</p>");
                console.log("Error");
            }
        })
    }
}
function passwordUpdate(form) {
    console.log("Called "+JSON.stringify(form));
    console.log("Called "+JSON.stringify(form.oldpassword.value));
    console.log("Called "+JSON.stringify(form.newpassword.value));
    var valid = true;
    if(form.oldpassword.value.length<8){
        valid=false;$("#oldM").html("password cannot be less than 8 characters");
    }else{
        $("#oldM").html("");
    }
    if(form.newpassword.value.length<8){
        valid=false;$("#newM").html("password cannot be less than 8 characters");
    }else{
        $("#newM").html("");
    }
    if(valid){
        $.ajax({
            type: "POST",
            url:"/user/updatePasswordDetails",
            data : JSON.stringify({oldpassword:form.oldpassword.value,newpassword:form.newpassword.value}),
            contentType: "application/json; charset=utf-8",
            async:false,
            success : function (result) {
                if (result.status=="success"){
                    $("#updateMessage").html("<p style='color: green'>"+result.message+"</p>");
                }else{
                    $("#updateMessage").html("<p style='color: red;'>"+result.message+"</p>");
                }
            },
            error : function (error) {
                $("#updateMessage").html("<p style='color: red'>Errror occured. Please contact admin</p>");
                console.log("Error");
            }
        })
    }
}