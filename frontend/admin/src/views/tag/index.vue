<script setup>
$(function () {
  $('#jqGrid').jqGrid({
    url: '/api/admin/tag/list',
    datatype: 'json',
    colModel: [
      { label: 'id', name: 'tagId', index: 'tagId', width: 50, key: true, hidden: true },
      { label: '标签名称', name: 'tagName', index: 'tagName', width: 240 },
      { label: '添加时间', name: 'createTime', index: 'createTime', width: 120 }
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

function tagAdd() {
  var tagName = $('#tagName').val()
  if (!validCN_ENString2_18(tagName)) {
    swal('标签名称不规范', {
      icon: 'error'
    })
  } else {
    var url = '/admin/tags/save?tagName=' + tagName
    $.ajax({
      type: 'POST', //方法类型
      url: url,
      success: function (result) {
        if (result.resultCode == 200) {
          $('#tagName').val('')
          swal('保存成功', {
            icon: 'success'
          })
          reload()
        } else {
          $('#tagName').val('')
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
}

function deleteTag() {
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
        url: '/admin/tags/delete',
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
            <h3 class="card-title">标签管理</h3>
          </div>
          <!-- /.card-body -->
          <div class="card-body">
            <div class="grid-btn">
              <input
                type="text"
                class="form-control col-1"
                id="tagName"
                name="tagName"
                placeholder="标签名称"
                required="true"
              />&nbsp;&nbsp;&nbsp;
              <button class="btn btn-info" onclick="tagAdd()">
                <i class="fa fa-plus"></i>&nbsp;新增
              </button>
              <button class="btn btn-danger" onclick="deleteTag()">
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
  </div>
</template>

<style lang="scss"></style>
