<script setup>
$(function () {
  //修改个人信息
  $('#updateUserNameButton').click(function () {
    $('#updateUserNameButton').attr('disabled', true)
    var userName = $('#loginUserName').val()
    var nickName = $('#nickName').val()
    if (validUserNameForUpdate(userName, nickName)) {
      //ajax提交数据
      var params = $('#userNameForm').serialize()
      $.ajax({
        type: 'POST',
        url: '/admin/profile/name',
        data: params,
        success: function (r) {
          if (r == 'success') {
            alert('修改成功')
          } else {
            alert('修改失败')
            $('#updateUserNameButton').prop('disabled', false)
          }
        }
      })
    } else {
      $('#updateUserNameButton').prop('disabled', false)
    }
  })
  //修改密码
  $('#updatePasswordButton').click(function () {
    $('#updatePasswordButton').attr('disabled', true)
    var originalPassword = $('#originalPassword').val()
    var newPassword = $('#newPassword').val()
    if (validPasswordForUpdate(originalPassword, newPassword)) {
      var params = $('#userPasswordForm').serialize()
      $.ajax({
        type: 'POST',
        url: '/admin/profile/password',
        data: params,
        success: function (r) {
          console.log(r)
          if (r == 'success') {
            alert('修改成功')
            window.location.href = '/admin/login'
          } else {
            alert('修改失败')
            $('#updatePasswordButton').attr('disabled', false)
          }
        }
      })
    } else {
      $('#updatePasswordButton').attr('disabled', false)
    }
  })
})

/**
 * 名称验证
 */
function validUserNameForUpdate(userName, nickName) {
  if (isNull(userName) || userName.trim().length < 1) {
    $('#updateUserName-info').css('display', 'block')
    $('#updateUserName-info').html('请输入登陆名称！')
    return false
  }
  if (isNull(nickName) || nickName.trim().length < 1) {
    $('#updateUserName-info').css('display', 'block')
    $('#updateUserName-info').html('昵称不能为空！')
    return false
  }
  if (!validUserName(userName)) {
    $('#updateUserName-info').css('display', 'block')
    $('#updateUserName-info').html('请输入符合规范的登录名！')
    return false
  }
  if (!validCN_ENString2_18(nickName)) {
    $('#updateUserName-info').css('display', 'block')
    $('#updateUserName-info').html('请输入符合规范的昵称！')
    return false
  }
  return true
}

/**
 * 密码验证
 */
function validPasswordForUpdate(originalPassword, newPassword) {
  if (isNull(originalPassword) || originalPassword.trim().length < 1) {
    $('#updatePassword-info').css('display', 'block')
    $('#updatePassword-info').html('请输入原密码！')
    return false
  }
  if (isNull(newPassword) || newPassword.trim().length < 1) {
    $('#updatePassword-info').css('display', 'block')
    $('#updatePassword-info').html('新密码不能为空！')
    return false
  }
  if (!validPassword(newPassword)) {
    $('#updatePassword-info').css('display', 'block')
    $('#updatePassword-info').html('请输入符合规范的密码！')
    return false
  }
  return true
}

import { ref } from 'vue'
import { userGetInfo } from '@/api/user'
const loginUserName = ref('')
const nickName = ref('')

const getUserInfo = async () => {
  const res = await userGetInfo()
  loginUserName.value = res.data.data.username
  nickName.value = res.data.data.nickname
}
getUserInfo()
</script>
<template>
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
      <div class="container-fluid"></div>
      <!-- /.container-fluid -->
    </div>
    <!-- Main content -->
    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-md-6">
            <div class="card card-primary card-outline">
              <div class="card-header">
                <h3 class="card-title">基本信息</h3>
              </div>
              <!-- /.card-body -->
              <div class="card-body">
                <form role="form" id="userNameForm">
                  <div class="form-group col-sm-8">
                    <div
                      class="alert alert-danger"
                      id="updateUserName-info"
                      style="display: none"
                    ></div>
                  </div>
                  <!-- text input -->
                  <div class="form-group">
                    <label>登陆名称</label>
                    <input
                      type="text"
                      class="form-control"
                      id="loginUserName"
                      name="loginUserName"
                      placeholder="请输入登陆名称"
                      required="true"
                      v-model="loginUserName"
                    />
                  </div>
                  <div class="form-group">
                    <label>昵称</label>
                    <input
                      type="text"
                      class="form-control"
                      id="nickName"
                      name="nickName"
                      placeholder="请输入昵称"
                      required="true"
                      v-model="nickName"
                    />
                  </div>
                  <div class="card-footer">
                    <button
                      type="button"
                      id="updateUserNameButton"
                      onsubmit="return false"
                      class="btn btn-danger float-right"
                    >
                      确认修改
                    </button>
                  </div>
                </form>
              </div>
              <!-- /.card-body -->
            </div>
          </div>
          <div class="col-md-6">
            <div class="card card-primary card-outline">
              <div class="card-header">
                <h3 class="card-title">修改密码</h3>
              </div>
              <!-- /.card-body -->
              <div class="card-body">
                <form role="form" id="userPasswordForm">
                  <div class="form-group col-sm-8">
                    <div
                      class="alert alert-danger updatePassword-info"
                      id="updatePassword-info"
                      style="display: none"
                    ></div>
                  </div>
                  <!-- input states -->
                  <div class="form-group">
                    <label class="control-label"><i class="fa fa-key"></i> 原密码</label>
                    <input
                      type="text"
                      class="form-control"
                      id="originalPassword"
                      name="originalPassword"
                      placeholder="请输入原密码"
                      required="true"
                    />
                  </div>
                  <div class="form-group">
                    <label class="control-label"><i class="fa fa-key"></i> 新密码</label>
                    <input
                      type="text"
                      class="form-control"
                      id="newPassword"
                      name="newPassword"
                      placeholder="请输入新密码"
                      required="true"
                    />
                  </div>
                  <div class="card-footer">
                    <button
                      type="button"
                      id="updatePasswordButton"
                      onsubmit="return false"
                      class="btn btn-danger float-right"
                    >
                      确认修改
                    </button>
                  </div>
                </form>
              </div>
              <!-- /.card-body -->
            </div>
          </div>
        </div>
      </div>
      <!-- /.container-fluid -->
    </section>
    <!-- /.content -->
  </div>
</template>

<style lang="less"></style>
