/**
 * Created by longbridge on 12/2/17.
 */
$(document).ready(function () {
    var fetching = false;
   setInterval(function () {
           updateDashB();
   },10000);
    function updateDashB() {
        $.ajax({
            method: "POST",
            data : JSON.stringify({last:$("#lastFetched").html()}),
            contentType : "application/json;charset=utf-8",
            url: "/updateDashB",
            async : false,
            success : function (result) {
                if(result!="null" && result!=""){
                    $("#outerHold").remove();
                    $("#CardDetails").prepend(result);
                    var audio  = document.getElementById("myaudio");
                    audio.play();
                    fetching = false;
                }
            },
            error : function (error) {
                console.log("Error is "+error);
                fetching = false;
            }
        })
    }
});

