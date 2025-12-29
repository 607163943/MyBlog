<script setup>
$(function () {
  $('#jqGrid').jqGrid({
    url: '/api/admin/category/list',
    datatype: 'json',
    colModel: [
      { label: 'id', name: 'categoryId', index: 'categoryId', width: 50, key: true, hidden: true },
      { label: '分类名称', name: 'categoryName', index: 'categoryName', width: 240 },
      {
        label: '分类图标',
        name: 'categoryIcon',
        index: 'categoryIcon',
        width: 120,
        formatter: imgFormatter
      },
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

  jQuery('select.image-picker').imagepicker({
    hide_select: false
  })

  jQuery('select.image-picker.show-labels').imagepicker({
    hide_select: false,
    show_label: true
  })
  $(window).resize(function () {
    $('#jqGrid').setGridWidth($('.card-body').width())
  })
  var container = jQuery('select.image-picker.masonry').next('ul.thumbnails')
  container.imagesLoaded(function () {
    container.masonry({
      itemSelector: 'li'
    })
  })
})

function imgFormatter(cellvalue) {
  return (
    "<a href='" +
    cellvalue +
    "'> <img src='" +
    cellvalue +
    '\' height="64" width="64" alt=\'icon\'/></a>'
  )
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

function categoryAdd() {
  reset()
  $('.modal-title').html('分类添加')
  $('#categoryModal').modal('show')
}

//绑定modal上的保存按钮
$('#saveButton').click(function () {
  var categoryName = $('#categoryName').val()
  if (!validCN_ENString2_18(categoryName)) {
    $('#edit-error-msg').css('display', 'block')
    $('#edit-error-msg').html('请输入符合规范的分类名称！')
  } else {
    var params = $('#categoryForm').serialize()
    var url = '/admin/categories/save'
    var id = getSelectedRowWithoutAlert()
    if (id != null) {
      url = '/admin/categories/update'
    }
    $.ajax({
      type: 'POST', //方法类型
      url: url,
      data: params,
      success: function (result) {
        if (result.resultCode == 200) {
          $('#categoryModal').modal('hide')
          swal('保存成功', {
            icon: 'success'
          })
          reload()
        } else {
          $('#categoryModal').modal('hide')
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

function categoryEdit() {
  reset()
  var id = getSelectedRow()
  if (id == null) {
    return
  }
  $('.modal-title').html('分类编辑')
  $('#categoryModal').modal('show')
  $('#categoryId').val(id)
}

function deleteCagegory() {
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
        url: '/admin/categories/delete',
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
  $('#categoryName').val('')
  $('#categoryIcon option:first').prop('selected', 'selected')
}
</script>

<template>
  <div class="content-wrapper">
    <!-- Content Header (图标header) -->
    <div class="content-header">
      <div class="container-fluid"></div>
      <!-- /.container-fluid -->
    </div>
    <!-- Main content -->
    <div class="content">
      <div class="container-fluid">
        <div class="card card-primary card-outline">
          <div class="card-header">
            <h3 class="card-title">分类管理</h3>
          </div>
          <!-- /.card-body -->
          <div class="card-body">
            <div class="grid-btn">
              <button class="btn btn-info" onclick="categoryAdd()">
                <i class="fa fa-plus"></i>&nbsp;新增
              </button>
              <button class="btn btn-info" onclick="categoryEdit()">
                <i class="fa fa-pencil-square-o"></i>&nbsp;修改
              </button>
              <button class="btn btn-danger" onclick="deleteCagegory()">
                <i class="fa fa-trash-o"></i>&nbsp;删除
              </button>
            </div>
            <br />
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
        id="categoryModal"
        tabindex="-1"
        role="dialog"
        aria-labelledby="categoryModalLabel"
      >
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
              <h6 class="modal-title" id="categoryModalLabel">Modal</h6>
            </div>
            <div class="modal-body">
              <form id="categoryForm">
                <div class="form-group">
                  <div class="alert alert-danger" id="edit-error-msg" style="display: none">
                    错误信息展示栏。
                  </div>
                </div>
                <input type="hidden" class="form-control" id="categoryId" name="categoryId" />
                <div class="form-group">
                  <label for="categoryName" class="control-label">分类名称:</label>
                  <input
                    type="text"
                    class="form-control"
                    id="categoryName"
                    name="categoryName"
                    placeholder="请输入分类名称"
                    required="true"
                  />
                </div>
                <div class="form-group">
                  <label for="categoryIcon" class="control-label">分类图标:</label>
                  <select
                    class="form-control select2 image-picker"
                    id="categoryIcon"
                    name="categoryIcon"
                  >
                    <option
                      data-img-src="/admin/dist/img/category/00.png"
                      value="/admin/dist/img/category/00.png"
                    >
                      默认图标
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/01.png"
                      value="/admin/dist/img/category/01.png"
                    >
                      图标1
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/02.png"
                      value="/admin/dist/img/category/02.png"
                    >
                      图标2
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/03.png"
                      value="/admin/dist/img/category/03.png"
                    >
                      图标3
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/04.png"
                      value="/admin/dist/img/category/04.png"
                    >
                      图标4
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/05.png"
                      value="/admin/dist/img/category/05.png"
                    >
                      图标5
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/06.png"
                      value="/admin/dist/img/category/06.png"
                    >
                      图标6
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/07.png"
                      value="/admin/dist/img/category/07.png"
                    >
                      图标7
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/08.png"
                      value="/admin/dist/img/category/08.png"
                    >
                      图标8
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/09.png"
                      value="/admin/dist/img/category/09.png"
                    >
                      图标9
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/10.png"
                      value="/admin/dist/img/category/10.png"
                    >
                      图标10
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/11.png"
                      value="/admin/dist/img/category/11.png"
                    >
                      图标11
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/12.png"
                      value="/admin/dist/img/category/12.png"
                    >
                      图标12
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/13.png"
                      value="/admin/dist/img/category/13.png"
                    >
                      图标13
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/14.png"
                      value="/admin/dist/img/category/14.png"
                    >
                      图标14
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/15.png"
                      value="/admin/dist/img/category/15.png"
                    >
                      图标15
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/16.png"
                      value="/admin/dist/img/category/16.png"
                    >
                      图标16
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/17.png"
                      value="/admin/dist/img/category/17.png"
                    >
                      图标17
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/18.png"
                      value="/admin/dist/img/category/18.png"
                    >
                      图标18
                    </option>
                    <option
                      data-img-src="/admin/dist/img/category/19.png"
                      value="/admin/dist/img/category/19.png"
                    >
                      图标19
                    </option>
                  </select>
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
