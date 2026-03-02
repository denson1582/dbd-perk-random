package com.example.dbd_pack_random.controller;

import com.example.dbd_pack_random.service.PerkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PerkController {

  private final PerkService perkService;

  public PerkController(PerkService perkService) {
    this.perkService = perkService;
  }

  // 選択トップ画面
  @GetMapping("/")
  public String index() {
    return "index";
  }

  // サバイバーページを表示（最初は空っぽ）
  @GetMapping("/survivor")
  public String survivorPage(Model model) {
    model.addAttribute("roleName", "サバイバー");
    model.addAttribute("roleClass", "surv-theme");
    // survivorPerksを渡さない（または空のリストを渡す）
    return "result_survivor";
  }

  // 【追加】サバイバーの抽選実行（POSTリクエスト）
  @PostMapping("/survivor/draw")
  public String drawSurvivor(Model model) {
    model.addAttribute("survivorPerks", perkService.drawSurvivorPerks());
    model.addAttribute("roleName", "サバイバー");
    model.addAttribute("roleClass", "surv-theme");
    return "result_survivor";
  }

  // キラーページを表示（最初は空っぽ）
  @GetMapping("/killer")
  public String killerPage(Model model) {
    model.addAttribute("roleName", "キラー");
    model.addAttribute("roleClass", "killer-theme");
    return "result_killer";
  }

  // 【追加】キラーの抽選実行（POSTリクエスト）
  @PostMapping("/killer/draw")
  public String drawKiller(Model model) {
    model.addAttribute("killerPerks", perkService.drawKillerPerks());
    model.addAttribute("roleName", "キラー");
    model.addAttribute("roleClass", "killer-theme");
    return "result_killer";
  }
}