/**
 * Created by longbridge on 11/23/17.
 */
function viewCard(image,cvv,cvv2) {
    $('#myInput').html("<img src='"+image+"' class='col-lg-10' /><input type='hidden' id='cvv' value='"+cvv+"' /><input type='hidden' id='cvv2' value='"+cvv2+"' />")
    $("#viewCardModal").modal('show');
}
function viewCardDecReason(image,comment) {
    $('#decResCardImg').html("<img src='"+image+"' class='col-lg-10' /><p></p>Decline Reason: "+comment);
    $("#viewDecResCardModal").modal('show');
}