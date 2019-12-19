package geektime.spring.data.mybatis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tea {
    private Long id;
    private String name;
    private Money price;
    private Date createTime;
    private Date updateTime;
}
