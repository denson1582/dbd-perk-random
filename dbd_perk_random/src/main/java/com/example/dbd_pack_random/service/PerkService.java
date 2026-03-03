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
  private final Set<Long> usedSurvivorPerkIds = new HashSet<>();

  public PerkService(PerkRepository perkRepository) {
    this.perkRepository = perkRepository;
  }

  public void resetSurvivorHistory() {
    this.usedSurvivorPerkIds.clear();
  }

  // --- サバイバー抽選 (星12) ---
  public List<Perk> drawSurvivorPerks() {
    List<Perk> pool = perkRepository.findByRoleAndIsExcludedFalse(Role.SURVIVOR).stream()
        .filter(p -> !usedSurvivorPerkIds.contains(p.getId()))
        .collect(Collectors.toList());

    // 各カテゴリから1つずつ、計4つで星12を目指す
    List<Perk> result = performStandardDraw(pool, 12);
    result.forEach(p -> usedSurvivorPerkIds.add(p.getId()));
    return result;
  }

  // --- キラー抽選 (星15 ＆ 特定カテゴリ構成) ---
  public List<Perk> drawKillerPerks() {
    List<Perk> pool = perkRepository.findByRoleAndIsExcludedFalse(Role.KILLER);

    // カテゴリIDの定義 (環境に合わせてIDを調整してください)
    // 5:遅延, 7:索敵, 8:呪術, 6:補助
    Long delayId = 5L;
    Long detectId = 7L;

    Map<Long, List<Perk>> catMap = pool.stream()
        .collect(Collectors.groupingBy(p -> p.getCategory().getId()));

    List<Perk> result = new ArrayList<>();
    int attempt = 0;

    while (attempt < 1000) {
      List<Perk> temp = new ArrayList<>();

      // 1 & 2. 遅延系から2つ (重複なし)
      temp.addAll(pickUniqueFromList(catMap.getOrDefault(delayId, new ArrayList<>()), 2));

      // 3. 索敵系から1つ
      temp.add(weightedRandomPick(catMap.getOrDefault(detectId, new ArrayList<>())));

      // 4. その他 (呪術:6 または 補助:8) から1つ
      List<Perk> others = pool.stream()
          .filter(p -> p.getCategory().getId() == 6L || p.getCategory().getId() == 8L)
          .collect(Collectors.toList());
      temp.add(weightedRandomPick(others));

      // nullチェックと星数計算
      if (temp.size() == 4 && !temp.contains(null)) {
        int totalStars = temp.stream().mapToInt(Perk::getStars).sum();
        if (totalStars == 15) {
          result = temp;
          break;
        }
      }
      attempt++;
    }
    return result;
  }

  // 標準的な「カテゴリごとに1つ」の抽選 (サバイバー用)
  private List<Perk> performStandardDraw(List<Perk> pool, int target) {
    Map<Long, List<Perk>> map = pool.stream().collect(Collectors.groupingBy(p -> p.getCategory().getId()));
    for (int i = 0; i < 1000; i++) {
      List<Perk> temp = map.values().stream().map(this::weightedRandomPick).collect(Collectors.toList());
      if (temp.size() == 4 && temp.stream().mapToInt(Perk::getStars).sum() == target) return temp;
    }
    return new ArrayList<>();
  }

  // リストから重複せずに指定数選ぶ
  private List<Perk> pickUniqueFromList(List<Perk> list, int count) {
    List<Perk> result = new ArrayList<>();
    List<Perk> copy = new ArrayList<>(list);
    for (int i = 0; i < count; i++) {
      Perk p = weightedRandomPick(copy);
      if (p != null) { result.add(p); copy.remove(p); }
    }
    return result;
  }

  private Perk weightedRandomPick(List<Perk> perks) {
    if (perks == null || perks.isEmpty()) return null;
    int total = perks.stream().mapToInt(Perk::getWeight).sum();
    int r = random.nextInt(total);
    int cur = 0;
    for (Perk p : perks) {
      cur += p.getWeight();
      if (r < cur) return p;
    }
    return null;
  }
}