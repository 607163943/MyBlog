<script setup>
$(function () {
  $('#jqGrid').jqGrid({
    url: '/api/admin/article/list',
    datatype: 'json',
    colModel: [
      { label: 'id', name: 'blogId', index: 'blogId', width: 50, key: true, hidden: true },
      { label: '标题', name: 'blogTitle', index: 'blogTitle', width: 140 },
      {
        label: '预览图',
        name: 'blogCoverImage',
        index: 'blogCoverImage',
        width: 120,
        formatter: coverImageFormatter
      },
      { label: '浏览量', name: 'blogViews', index: 'blogViews', width: 60 },
      {
        label: '状态',
        name: 'blogStatus',
        index: 'blogStatus',
        width: 60,
        formatter: statusFormatter
      },
      { label: '博客分类', name: 'blogCategoryName', index: 'blogCategoryName', width: 60 },
      { label: '添加时间', name: 'createTime', index: 'createTime', width: 90 }
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

  function coverImageFormatter(cellvalue) {
    return "<img src='/public" + cellvalue + '\' height="120" width="160" alt=\'coverImage\'/>'
  }

  function statusFormatter(cellvalue) {
    if (cellvalue == 0) {
      return '<button type="button" class="btn btn-block btn-secondary btn-sm" style="width: 50%;">草稿</button>'
    } else if (cellvalue == 1) {
      return '<button type="button" class="btn btn-block btn-success btn-sm" style="width: 50%;">发布</button>'
    }
  }
})

/**
 * 搜索功能
 */
function search() {
  //标题关键字
  var keyword = $('#keyword').val()
  if (!validLength(keyword, 20)) {
    swal('搜索字段长度过大!', {
      icon: 'error'
    })
    return false
  }
  //数据封装
  var searchData = { keyword: keyword }
  //传入查询条件参数
  $('#jqGrid').jqGrid('setGridParam', { postData: searchData })
  //点击搜索按钮默认都从第一页开始
  $('#jqGrid').jqGrid('setGridParam', { page: 1 })
  //提交post并刷新表格
  $('#jqGrid').jqGrid('setGridParam', { url: '/admin/blogs/list' }).trigger('reloadGrid')
}

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

function addBlog() {
  window.location.href = '/admin/blogs/edit'
}

function editBlog() {
  var id = getSelectedRow()
  if (id == null) {
    return
  }
  window.location.href = '/admin/blogs/edit/' + id
}

function deleteBlog() {
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
        url: '/admin/blogs/delete',
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
            <h3 class="card-title">博客管理</h3>
          </div>
          <!-- /.card-body -->
          <div class="card-body">
            <div class="grid-btn">
              <button class="btn btn-success" onclick="addBlog()">
                <i class="fa fa-plus"></i>&nbsp;新增
              </button>
              <button class="btn btn-info" onclick="editBlog()">
                <i class="fa fa-edit"></i>&nbsp;修改
              </button>
              <button class="btn btn-danger" onclick="deleteBlog()">
                <i class="fa fa-trash-o"></i>&nbsp;删除</button
              >&nbsp;&nbsp;
              <input
                type="text"
                placeholder="关键字(标题/分类)"
                id="keyword"
                class="form-control col-2"
              />&nbsp;
              <button class="btn btn-info" onclick="search()">
                <i class="fa fa-search"></i>&nbsp;搜索
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
  </div>
</template>

<style scoped></style>
