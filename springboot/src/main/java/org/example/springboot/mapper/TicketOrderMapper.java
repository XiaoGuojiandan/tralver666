package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.springboot.entity.TicketOrder;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TicketOrderMapper extends BaseMapper<TicketOrder> {
    
    @Select("SELECT id, order_no, user_id, ticket_id, quantity, visitor_name, visitor_phone, " +
            "id_card, visit_date, total_amount, status, payment_time, payment_method, " +
            "create_time, update_time " +
            "FROM ticket_order " +
            "WHERE create_time >= #{startDate} AND create_time <= #{endDate} " +
            "ORDER BY create_time ASC")
    List<TicketOrder> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Select("SELECT COUNT(*) FROM ticket_order WHERE DATE(create_time) = #{date}")
    long getOrderCountByDate(LocalDate date);
} 