<script setup>
$(function () {
  $('#jqGrid').jqGrid({
    url: '/api/admin/link/list',
    datatype: 'json',
    colModel: [
      { label: 'id', name: 'linkId', index: 'linkId', width: 50, key: true, hidden: true },
      { label: '网站名称', name: 'linkName', index: 'linkName', width: 100 },
      { label: '网站链接', name: 'linkUrl', index: 'linkUrl', width: 120 },
      { label: '网站描述', name: 'linkDescription', index: 'linkDescription', width: 120 },
      { label: '排序值', name: 'linkRank', index: 'linkRank', width: 30 },
      { label: '添加时间', name: 'createTime', index: 'createTime', width: 100 }
    ],
    height: 560,
    rowNum: 10,
    rowList: [10, 20, 50],
    styleUI: 'Bootstrap',
    loadtext: '信息读取中...',
    rownumbers: false,
    rownumWidth: 20,
    autowidth: true,
    multiselect: true,
    pager: '#jqGridPager',
    jsonReader: {
      root: 'data.list',
      page: 'data.currPage',
      total: 'data.totalPage',
      records: 'data.totalCount'
    },
    prmNames: {
      page: 'page',
      rows: 'limit',
      order: 'order'
    },
    gridComplete: function () {
      //隐藏grid底部滚动条
      $('#jqGrid').closest('.ui-jqgrid-bdiv').css({ 'overflow-x': 'hidden' })
    }
  })
  $(window).resize(function () {
    $('#jqGrid').setGridWidth($('.card-body').width())
  })
})

/**
 * jqGrid重新加载
 */
function reload() {
  var page = $('#jqGrid').jqGrid('getGridParam', 'page')
  $('#jqGrid')
    .jqGrid('setGridParam', {
      page: page
    })
    .trigger('reloadGrid')
}

function linkAdd() {
  reset()
  $('.modal-title').html('友链添加')
  $('#linkModal').modal('show')
}

//绑定modal上的保存按钮
$('#saveButton').click(function () {
  var linkId = $('#linkId').val()
  var linkName = $('#linkName').val()
  var linkUrl = $('#linkUrl').val()
  var linkDescription = $('#linkDescription').val()
  var linkRank = $('#linkRank').val()
  if (!validCN_ENString2_18(linkName)) {
    $('#edit-error-msg').css('display', 'block')
    $('#edit-error-msg').html('请输入符合规范的名称！')
    return
  }
  if (!isURL(linkUrl)) {
    $('#edit-error-msg').css('display', 'block')
    $('#edit-error-msg').html('请输入符合规范的网址！')
    return
  }
  if (!validCN_ENString2_100(linkDescription)) {
    $('#edit-error-msg').css('display', 'block')
    $('#edit-error-msg').html('请输入符合规范的描述！')
    return
  }
  if (isNull(linkRank) || linkRank < 0) {
    $('#edit-error-msg').css('display', 'block')
    $('#edit-error-msg').html('请输入符合规范的排序值！')
    return
  }
  var params = $('#linkForm').serialize()
  var url = '/admin/links/save'
  if (linkId != null && linkId > 0) {
    url = '/admin/links/update'
  }
  $.ajax({
    type: 'POST', //方法类型
    url: url,
    data: params,
    success: function (result) {
      if (result.resultCode == 200 && result.data) {
        $('#linkModal').modal('hide')
        swal('保存成功', {
          icon: 'success'
        })
        reload()
      } else {
        $('#linkModal').modal('hide')
        swal('保存失败', {
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

function linkEdit() {
  var id = getSelectedRow()
  if (id == null) {
    return
  }
  reset()
  //请求数据
  $.get('/admin/links/info/' + id, function (r) {
    if (r.resultCode == 200 && r.data != null) {
      //填充数据至modal
      $('#linkName').val(r.data.linkName)
      $('#linkUrl').val(r.data.linkUrl)
      $('#linkDescription').val(r.data.linkDescription)
      $('#linkRank').val(r.data.linkRank)
      //根据原linkType值设置select选择器为选中状态
      if (r.data.linkType == 1) {
        $('#linkType option:eq(1)').prop('selected', 'selected')
      }
      if (r.data.linkType == 2) {
        $('#linkType option:eq(2)').prop('selected', 'selected')
      }
    }
  })
  $('.modal-title').html('友链修改')
  $('#linkModal').modal('show')
  $('#linkId').val(id)
}

function deleteLink() {
  var ids = getSelectedRows()
  if (ids == null) {
    return
  }
  swal({
    title: '确认弹框',
    text: '确认要删除数据吗?',
    icon: 'warning',
    buttons: true,
    dangerMode: true
  }).then((flag) => {
    if (flag) {
      $.ajax({
        type: 'POST',
        url: '/admin/links/delete',
        contentType: 'application/json',
        data: JSON.stringify(ids),
        success: function (r) {
          if (r.resultCode == 200) {
            swal('删除成功', {
              icon: 'success'
            })
            $('#jqGrid').trigger('reloadGrid')
          } else {
            swal(r.message, {
              icon: 'error'
            })
          }
        }
      })
    }
  })
}

function reset() {
  $('#linkName').val('')
  $('#linkUrl').val('')
  $('#linkDescription').val('')
  $('#linkRank').val(0)
  $('#linkId').val(0)
  $('#edit-error-msg').css('display', 'none')
  $('#linkType option:first').prop('selected', 'selected')
}
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
        <div class="card card-primary card-outline">
          <div class="card-header">
            <h3 class="card-title">友情链接管理</h3>
          </div>
          <!-- /.card-body -->
          <div class="card-body">
            <div class="grid-btn">
              <button class="btn btn-info" onclick="linkAdd()">
                <i class="fa fa-plus"></i>&nbsp;新增
              </button>
              <button class="btn btn-info" onclick="linkEdit()">
                <i class="fa fa-pencil-square-o"></i>&nbsp;修改
              </button>
              <button class="btn btn-danger" onclick="deleteLink()">
                <i class="fa fa-trash-o"></i>&nbsp;删除
              </button>
            </div>
            <table id="jqGrid" class="table table-bordered"></table>
            <div id="jqGridPager"></div>
          </div>
          <!-- /.card-body -->
        </div>
      </div>
      <!-- /.container-fluid -->
    </div>
    <!-- /.content -->
    <div class="content">
      <!-- 模态框（Modal） -->
      <div
        class="modal fade"
        id="linkModal"
        tabindex="-1"
        role="dialog"
        aria-labelledby="linkModalLabel"
      >
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
              <h6 class="modal-title" id="linkModalLabel">Modal</h6>
            </div>
            <div class="modal-body">
              <form id="linkForm">
                <div class="form-group">
                  <div class="alert alert-danger" id="edit-error-msg" style="display: none">
                    错误信息展示栏。
                  </div>
                </div>
                <input type="hidden" class="form-control" id="linkId" name="linkId" />
                <div class="form-group">
                  <label for="linkType" class="control-label">友链类型:</label>
                  <select class="form-control" id="linkType" name="linkType">
                    <option selected="selected" value="0">友链</option>
                    <option value="1">推荐网站</option>
                    <option value="2">个人链接</option>
                  </select>
                </div>
                <div class="form-group">
                  <label for="linkName" class="control-label">网站名称:</label>
                  <input
                    type="text"
                    class="form-control"
                    id="linkName"
                    name="linkName"
                    placeholder="请输入网站名称"
                    required="true"
                  />
                </div>
                <div class="form-group">
                  <label for="linkUrl" class="control-label">网站链接:</label>
                  <input
                    type="url"
                    class="form-control"
                    id="linkUrl"
                    name="linkUrl"
                    placeholder="请输入网站链接"
                    required="true"
                  />
                </div>
                <div class="form-group">
                  <label for="linkDescription" class="control-label">网站描述:</label>
                  <input
                    type="url"
                    class="form-control"
                    id="linkDescription"
                    name="linkDescription"
                    placeholder="请输入网站描述"
                    required="true"
                  />
                </div>
                <div class="form-group">
                  <label for="linkRank" class="control-label">排序值:</label>
                  <input
                    type="number"
                    class="form-control"
                    id="linkRank"
                    name="linkRank"
                    placeholder="请输入排序值"
                    required="true"
                  />
                </div>
              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
              <button type="button" class="btn btn-primary" id="saveButton">确认</button>
            </div>
          </div>
        </div>
      </div>
      <!-- /.modal -->
    </div>
  </div>
</template>
<style lang="less"></style>
