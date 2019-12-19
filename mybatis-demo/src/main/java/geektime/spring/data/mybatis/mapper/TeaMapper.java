package geektime.spring.data.mybatis.mapper;

import geektime.spring.data.mybatis.model.Tea;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface TeaMapper {
    @Select("select * from t_tea order by id")
    List<Tea> findAllWithRowBounds(RowBounds rowBounds);

    @Select("select * from t_tea order by id")
    List<Tea> findAllWithParam(@Param("pageNum") int pageNum,
                               @Param("pageSize") int pageSize);


}
