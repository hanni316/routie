package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.entity.Item;
import com.gbsb.routie_server.entity.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserItemRepository extends JpaRepository<UserItem, Long> {
    List<UserItem> findByUser_UserId(String userId);  // 유저의 모든 아이템 조회
    Optional<UserItem> findByUserAndItem(User user, Item item);  // 특정 아이템 구매 확인

    //유저 아이템 개수 합계
    @Query("SELECT COALESCE(SUM(ui.quantity), 0) "
            + "FROM UserItem ui "
            + "WHERE ui.user.userId = :userId")
    int getTotalQuantityByUserId(@Param("userId") String userId);
}
