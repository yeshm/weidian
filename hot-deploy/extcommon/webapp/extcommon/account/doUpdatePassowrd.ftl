<div class="control-group">
    <label class="control-label" for="newPassword">新密码：</label>
    <div class="controls">
        <input type="password" name="newPassword" id="newPassword" class="input-large" data-rules="{required:true}"/>
    </div>
</div>

<div class="control-group">
    <label class="control-label" for="newPasswordVerify">确认新密码：</label>
    <div class="controls">
        <input type="password" name="newPasswordVerify" id="newPasswordVerify" class="input-large" data-rules="{equalTo:'#newPassword'}"/>
    </div>
</div>