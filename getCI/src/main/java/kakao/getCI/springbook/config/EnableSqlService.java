package kakao.getCI.springbook.config;

import kakao.getCI.springbook.user.sqlservice.SqlService;
import org.springframework.context.annotation.Import;

@Import(value = SqlServiceContext.class)
public @interface EnableSqlService {
}
