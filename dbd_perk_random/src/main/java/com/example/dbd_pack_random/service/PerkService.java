package com.example.dbd_pack_random.service;

import com.example.dbd_pack_random.model.Perk;
import com.example.dbd_pack_random.model.Role;
import com.example.dbd_pack_random.repository.PerkRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PerkService {

  private final PerkRepository perkRepository;
  private final Random random = new Random();

  public PerkService(PerkRepository perkRepository) {
    this.perkRepository = perkRepository;
  }

  /**
   * サバイバー(role=0)の抽選
   */
  public List<Perk> drawSurvivorPerks() {
    return performDraw(Role.SURVIVOR);
  }

  /**
   * キラー(role=1)の抽選
   */
  public List<Perk> drawKillerPerks() {
    return performDraw(Role.KILLER);
  }

  private List<Perk> performDraw(Role role) {
    // 1. そのロールの全パークを取得（除外されていないもの）
    List<Perk> allPerks = perkRepository.findByRoleAndIsExcludedFalse(role);

    // 2. 存在するカテゴリごとにグループ分け
    Map<Long, List<Perk>> categoryMap = allPerks.stream()
        .collect(Collectors.groupingBy(p -> p.getCategory().getId()));

    List<Perk> selectedPerks;
    int totalStars;

    // 3. 星の合計が12になるまでリセマラ
    int attempt = 0;
    while (true) {
      List<Perk> tempPerks = new ArrayList<>();
      int tempStars = 0;

      for (List<Perk> categoryList : categoryMap.values()) {
        Perk picked = weightedRandomPick(categoryList);
        if (picked != null) {
          tempPerks.add(picked);
          tempStars += picked.getStars();
        }
      }

      attempt++;

      // 条件に合致、または1000回試行してダメならその時の結果を返す
      if (tempStars == 12 || attempt >= 1000) {
        selectedPerks = tempPerks;
        totalStars = tempStars;
        break;
      }
    }

    System.out.println("試行回数: " + attempt + "回 / 合計星数: " + totalStars);
    return selectedPerks;
  }

  private Perk weightedRandomPick(List<Perk> perks) {
    int totalWeight = perks.stream().mapToInt(Perk::getWeight).sum();
    if (totalWeight == 0) return null;

    int r = random.nextInt(totalWeight);
    int current = 0;
    for (Perk p : perks) {
      current += p.getWeight();
      if (r < current) return p;
    }
    return null;
  }
}