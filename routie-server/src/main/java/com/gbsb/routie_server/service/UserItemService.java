// UserItemService.java
package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.GachaResultDto;
import com.gbsb.routie_server.dto.UserItemDto;
import com.gbsb.routie_server.entity.GachaLog;
import com.gbsb.routie_server.entity.Item;
import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.entity.UserItem;
import com.gbsb.routie_server.repository.GachaLogRepository;
import com.gbsb.routie_server.repository.ItemRepository;
import com.gbsb.routie_server.repository.UserItemRepository;
import com.gbsb.routie_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserItemService {

    private final UserItemRepository repo;
    private final ItemRepository itemRepo;
    private final UserRepository userRepo;
    private final GachaLogRepository gachaLogRepo;

    //public UserItemService(UserItemRepository repo) { this.repo = repo; }

    public int getTotalQuantity(String userId) {
        return repo.getTotalQuantityByUserId(userId);
    }

    public List<UserItemDto> getPurchasedItems(String userId) {
        List<UserItem> items = repo.findByUser_UserId(userId);
        return items.stream()
                .map(ui -> new UserItemDto(
                        ui.getItem().getItemId(),
                        ui.getItem().getName(),
                        ui.getItem().getNameEn(),
                        ui.getItem().getPrice(),
                        ui.getItem().getCategory().getName()
                ))
                .collect(Collectors.toList());
    }
    //가챠 당첨 아이템 저장(중복 방지 포함)
    public void saveGachaItem(GachaResultDto dto) {
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저"));

        Item item = null;
        if (dto.getItemId() != null) {
            item = itemRepo.findById(dto.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이템"));
        }

        //가챠 결과 로그 저장 (성공/실패 관계없이)
        GachaLog log = GachaLog.builder()
                .user(user)
                .item(item) // null 가능
                .isSuccess(dto.isSuccess())
                .isHiddenItem(dto.isHiddenItem())
                .drawTime(LocalDateTime.now())
                .build();
        gachaLogRepo.save(log);

        // 성공 시에만 아이템 지급
        if (dto.isSuccess() && item != null) {
            if (repo.findByUserAndItem(user, item).isEmpty()) {
                UserItem userItem = UserItem.builder()
                        .user(user)
                        .item(item)
                        .quantity(1)
                        .build();
                repo.save(userItem);
            }
        }
    }
}
