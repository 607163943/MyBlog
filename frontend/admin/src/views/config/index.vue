<script setup>
$(function () {
  //修改站点信息
  $('#updateWebsiteButton').click(function () {
    $('#updateWebsiteButton').attr('disabled', true)
    //ajax提交数据
    var params = $('#websiteForm').serialize()
    $.ajax({
      type: 'POST',
      url: '/admin/configurations/website',
      data: params,
      success: function (result) {
        if (result.resultCode == 200 && result.data) {
          swal('保存成功', {
            icon: 'success'
          })
        } else {
          swal(result.message, {
            icon: 'error'
          })
        }
      },
      error: function () {
        swal('操作失败', {
          icon: 'error'
        })
      }
    })
  })
  //个人信息
  $('#updateUserInfoButton').click(function () {
    $('#updateUserInfoButton').attr('disabled', true)
    var params = $('#userInfoForm').serialize()
    $.ajax({
      type: 'POST',
      url: '/admin/configurations/userInfo',
      data: params,
      success: function (result) {
        if (result.resultCode == 200 && result.data) {
          swal('保存成功', {
            icon: 'success'
          })
        } else {
          swal(result.message, {
            icon: 'error'
          })
        }
      },
      error: function () {
        swal('操作失败', {
          icon: 'error'
        })
      }
    })
  })
  //修改底部设置
  $('#updateFooterButton').click(function () {
    $('#updateFooterButton').attr('disabled', true)
    var params = $('#footerForm').serialize()
    $.ajax({
      type: 'POST',
      url: '/admin/configurations/footer',
      data: params,
      success: function (result) {
        if (result.resultCode == 200 && result.data) {
          swal('保存成功', {
            icon: 'success'
          })
        } else {
          swal(result.message, {
            icon: 'error'
          })
        }
      },
      error: function () {
        swal('操作失败', {
          icon: 'error'
        })
      }
    })
  })
})

import { configGetListService } from '@/api/config'
import { ref } from 'vue'
// 获取配置列表
const configurations = ref({
  websiteName: '',
  websiteDescription: '',
  websiteLogo: '',
  websiteIcon: '',
  yourAvatar: '',
  yourName: '',
  yourEmail: '',
  footerAbout: '',
  footerICP: '',
  footerCopyRight: '',
  footerPoweredBy: '',
  footerPoweredByURL: ''
})
const getConfigList = async () => {
  const res = await configGetListService()
  configurations.value = res.data.data
}

getConfigList()
</script>

<template>
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
      <div class="container-fluid"></div>
      <!-- /.container-fluid -->
    </div>
    <!-- Main content -->
    <div class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-md-4">
            <div class="card card-primary card-outline">
              <div class="card-header">
                <h3 class="card-title">站点信息</h3>
              </div>
              <!-- /.card-body -->
              <div class="card-body">
                <form role="form" id="websiteForm">
                  <!-- text input -->
                  <div class="form-group">
                    <label>站点名称</label>
                    <input
                      type="text"
                      class="form-control"
                      id="websiteName"
                      name="websiteName"
                      placeholder="站点名称"
                      v-model="configurations.websiteName"
                    />
                  </div>
                  <div class="form-group">
                    <label>站点描述</label>
                    <input
                      type="text"
                      class="form-control"
                      id="websiteDescription"
                      name="websiteDescription"
                      v-model="configurations.websiteDescription"
                      placeholder="站点描述"
                    />
                  </div>
                  <div class="form-group">
                    <label>站点Logo</label>
                    <input
                      type="text"
                      class="form-control"
                      id="websiteLogo"
                      name="websiteLogo"
                      v-model="configurations.websiteLogo"
                      placeholder="站点Logo"
                    />
                  </div>
                  <div class="form-group">
                    <label>favicon.ico</label>
                    <input
                      type="text"
                      class="form-control"
                      id="websiteIcon"
                      v-model="configurations.websiteIcon"
                      name="websiteIcon"
                      placeholder="favicon.ico"
                    />
                  </div>
                  <div class="card-footer">
                    <button
                      type="button"
                      id="updateWebsiteButton"
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
          <div class="col-md-4">
            <div class="card card-primary card-outline">
              <div class="card-header">
                <h3 class="card-title">个人信息</h3>
              </div>
              <!-- /.card-body -->
              <div class="card-body">
                <form role="form" id="userInfoForm">
                  <!-- input states -->
                  <div class="form-group">
                    <label class="control-label">个人头像</label>
                    <input
                      type="text"
                      class="form-control"
                      id="yourAvatar"
                      name="yourAvatar"
                      v-model="configurations.yourAvatar"
                      placeholder="个人头像"
                    />
                  </div>
                  <div class="form-group">
                    <label class="control-label">个人名称</label>
                    <input
                      type="text"
                      class="form-control"
                      id="yourName"
                      name="yourName"
                      v-model="configurations.yourName"
                      placeholder="个人名称"
                    />
                  </div>
                  <div class="form-group">
                    <label class="control-label">个人邮箱</label>
                    <input
                      type="email"
                      class="form-control"
                      id="yourEmail"
                      name="yourEmail"
                      v-model="configurations.yourEmail"
                      placeholder="个人邮箱"
                    />
                  </div>
                  <div class="card-footer">
                    <button
                      type="button"
                      id="updateUserInfoButton"
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
          <div class="col-md-4">
            <div class="card card-primary card-outline">
              <div class="card-header">
                <h3 class="card-title">底部设置</h3>
              </div>
              <!-- /.card-body -->
              <div class="card-body">
                <form role="form" id="footerForm">
                  <!-- input states -->
                  <div class="form-group">
                    <label class="control-label">底部About</label>
                    <input
                      type="text"
                      class="form-control"
                      id="footerAbout"
                      name="footerAbout"
                      v-model="configurations.footerAbout"
                      placeholder="About"
                    />
                  </div>
                  <div class="form-group">
                    <label class="control-label">底部备案号</label>
                    <input
                      type="text"
                      class="form-control"
                      id="footerICP"
                      name="footerICP"
                      v-model="configurations.footerICP"
                      placeholder="备案号"
                    />
                  </div>
                  <div class="form-group">
                    <label class="control-label">底部Copy Right</label>
                    <input
                      type="text"
                      class="form-control"
                      id="footerCopyRight"
                      name="footerCopyRight"
                      v-model="configurations.footerCopyRight"
                      placeholder="Copy Right"
                    />
                  </div>
                  <div class="form-group">
                    <label class="control-label">底部Powered By</label>
                    <input
                      type="text"
                      class="form-control"
                      id="footerPoweredBy"
                      name="footerPoweredBy"
                      v-model="configurations.footerPoweredBy"
                      placeholder="Powered By"
                    />
                  </div>
                  <div class="form-group">
                    <label class="control-label">底部Powered By URL</label>
                    <input
                      type="text"
                      class="form-control"
                      id="footerPoweredByURL"
                      name="footerPoweredByURL"
                      v-model="configurations.footerPoweredByURL"
                      placeholder="Powered By URL"
                    />
                  </div>
                  <div class="card-footer">
                    <button
                      type="button"
                      id="updateFooterButton"
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
    </div>
    <!-- /.content -->
  </div>
</template>

<style lang="less"></style>
