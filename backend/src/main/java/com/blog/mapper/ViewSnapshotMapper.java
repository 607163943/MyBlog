package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.pojo.po.ViewSnapshot;
import com.blog.pojo.vo.admin.ViewTrendVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 文章总浏览量快照 Mapper
 */
public interface ViewSnapshotMapper extends BaseMapper<ViewSnapshot> {

    /**
     * 查询指定起始日期之后的每日浏览量趋势
     */
    List<ViewTrendVO> selectDailyViewTrend(@Param("startDate") LocalDate startDate);
}
