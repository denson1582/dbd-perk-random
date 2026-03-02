package com.example.dbd_pack_random.repository;

import com.example.dbd_pack_random.model.Perk;
import com.example.dbd_pack_random.model.Role;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerkRepository extends JpaRepository<Perk, Long> {

  // ロール（サバイバー/キラー）で絞り込んで、かつ除外されていないパークを取得
  List<Perk> findByRoleAndIsExcludedFalse(Role role);

  // 特定のロールかつ特定のカテゴリで、除外されていないパークを取得
  List<Perk> findByRoleAndCategoryAndIsExcludedFalse(String role, String category);
}
