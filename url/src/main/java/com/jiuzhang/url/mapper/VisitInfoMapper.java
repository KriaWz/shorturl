package com.jiuzhang.url.mapper;

import com.jiuzhang.url.entity.VisitInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuzhang.url.vo.LatestSumMax;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wenzhen
 * @since 2020-09-07
 */
@Repository
public interface VisitInfoMapper extends BaseMapper<VisitInfo> {

    List<LatestSumMax> countList();

    List<VisitInfo> listLatestVisitInfo();

}
