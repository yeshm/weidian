<script type="text/javascript">
    <#--切换用户的状态，冻结或者激活-->
    function togglePartyStatus(partyId, statusId) {
        var message = "确定要解除冻结吗？";
        if (statusId == "PARTY_ENABLED") {
            message = "确定要冻结该用户吗？"
        }
        app.confirm(message, function () {
            app.ajaxHelper.submitRequest({
                url: "<@ofbizUrl>togglePartyStatus</@ofbizUrl>",
                data: {partyId: partyId},
                success: function (data) {
                    if (app.ajaxHelper.handleAjaxMsg(data)) {
                        app.showSuccess("操作成功", {
                            callback: function () {
                                app.page.reload();
                            }
                        });
                    } else {
                        app.showError(data.message);
                    }
                }
            });
        });
        return false;
    }
</script>