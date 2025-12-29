<script setup>
$(function () {
  $('#jqGrid').jqGrid({
    url: '/api/admin/comment/list',
    datatype: 'json',
    colModel: [
      { label: 'id', name: 'commentId', index: 'commentId', width: 50, key: true, hidden: true },
      { label: '评论内容', name: 'commentBody', index: 'commentBody', width: 120 },
      { label: '评论时间', name: 'commentCreateTime', index: 'commentCreateTime', width: 60 },
      { label: '评论人名称', name: 'commentator', index: 'commentator', width: 60 },
      { label: '评论人邮箱', name: 'email', index: 'email', width: 90 },
      {
        label: '状态',
        name: 'commentStatus',
        index: 'commentStatus',
        width: 60,
        formatter: statusFormatter
      },
      { label: '回复内容', name: 'replyBody', index: 'replyBody', width: 120 }
    ],
    height: 700,
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
  function statusFormatter(cellvalue) {
    if (cellvalue == 0) {
      return '<button type="button" class="btn btn-block btn-secondary btn-sm" style="width: 80%;">待审核</button>'
    } else if (cellvalue == 1) {
      return '<button type="button" class="btn btn-block btn-success btn-sm" style="width: 80%;">已审核</button>'
    }
  }
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

/**
 * 批量审核
 */
function checkDoneComments() {
  var ids = getSelectedRows()
  if (ids == null) {
    return
  }
  swal({
    title: '确认弹框',
    text: '确认审核通过吗?',
    icon: 'warning',
    buttons: true,
    dangerMode: true
  }).then((flag) => {
    if (flag) {
      $.ajax({
        type: 'POST',
        url: '/admin/comments/checkDone',
        contentType: 'application/json',
        data: JSON.stringify(ids),
        success: function (r) {
          if (r.resultCode == 200) {
            swal('审核成功', {
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

/**
 * 批量删除
 */
function deleteComments() {
  var ids = getSelectedRows()
  if (ids == null) {
    return
  }
  swal({
    title: '确认弹框',
    text: '确认删除这些评论吗?',
    icon: 'warning',
    buttons: true,
    dangerMode: true
  }).then((flag) => {
    if (flag) {
      $.ajax({
        type: 'POST',
        url: '/admin/comments/delete',
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

function reply() {
  var id = getSelectedRow()
  if (id == null) {
    return
  }
  var rowData = $('#jqGrid').jqGrid('getRowData', id)
  console.log(rowData)
  if (rowData.commentStatus.indexOf('待审核') > -1) {
    swal('请先审核该评论再进行回复!', {
      icon: 'warning'
    })
    return
  }
  $('#replyBody').val('')
  $('#replyModal').modal('show')
}

//绑定modal上的保存按钮
$('#saveButton').click(function () {
  var replyBody = $('#replyBody').val()
  if (!validCN_ENString2_100(replyBody)) {
    swal('请输入符合规范的回复信息!', {
      icon: 'warning'
    })
    return
  } else {
    var url = '/admin/comments/reply'
    var id = getSelectedRow()
    var params = { commentId: id, replyBody: replyBody }
    $.ajax({
      type: 'POST', //方法类型
      url: url,
      data: params,
      success: function (result) {
        if (result.resultCode == 200) {
          $('#replyModal').modal('hide')
          swal('回复成功', {
            icon: 'success'
          })
          reload()
        } else {
          $('#replyModal').modal('hide')
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
  }
})
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
            <h3 class="card-title">评论管理</h3>
          </div>
          <!-- /.card-body -->
          <div class="card-body">
            <div class="grid-btn">
              <button class="btn btn-success" onclick="checkDoneComments()">
                <i class="fa fa-check"></i>&nbsp;批量审核
              </button>
              <button class="btn btn-info" onclick="reply()">
                <i class="fa fa-reply"></i>&nbsp;回复
              </button>
              <button class="btn btn-danger" onclick="deleteComments()">
                <i class="fa fa-trash-o"></i>&nbsp;批量删除
              </button>
            </div>
            <!-- JqGrid必要DOM,用于创建表格展示列表数据 -->
            <table id="jqGrid" class="table table-bordered"></table>
            <!-- JqGrid必要DOM,分页信息区域 -->
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
        id="replyModal"
        tabindex="-1"
        role="dialog"
        aria-labelledby="replyModalLabel"
      >
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
              <h6 class="modal-title" id="replyModalLabel">评论回复</h6>
            </div>
            <div class="modal-body">
              <form id="replyForm">
                <input type="hidden" class="form-control" id="categoryId" name="categoryId" />
                <div class="form-group">
                  <label for="replyBody" class="control-label">回复内容:</label>
                  <textarea
                    type="text"
                    class="form-control"
                    id="replyBody"
                    name="replyBody"
                    placeholder="请输入回复内容"
                    required="true"
                  ></textarea>
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
