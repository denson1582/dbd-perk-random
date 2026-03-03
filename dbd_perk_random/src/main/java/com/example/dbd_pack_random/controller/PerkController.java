package com.example.dbd_pack_random.controller;

import com.example.dbd_pack_random.model.Perk;
import com.example.dbd_pack_random.repository.PerkRepository;
import com.example.dbd_pack_random.service.PerkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PerkController {

  private final PerkService perkService;
  private final PerkRepository perkRepository;

  public PerkController(PerkService perkService, PerkRepository perkRepository) {
    this.perkService = perkService;
    this.perkRepository = perkRepository;
  }

  @GetMapping("/") public String index() { return "index"; }

  // サバイバー
  @GetMapping("/survivor") public String survivorPage() { return "survivor"; }
  @PostMapping("/survivor/draw")
  public String drawSurvivor(Model model) {
    model.addAttribute("survivorPerks", perkService.drawSurvivorPerks());
    return "result_survivor";
  }
  @PostMapping("/survivor/reset")
  public String resetSurvivor() {
    perkService.resetSurvivorHistory();
    return "redirect:/survivor";
  }

  // キラー
  @GetMapping("/killer") public String killerPage() { return "killer"; }
  @PostMapping("/killer/draw")
  public String drawKiller(Model model) {
    model.addAttribute("killerPerks", perkService.drawKillerPerks());
    return "result_killer";
  }

  // 管理画面
  @GetMapping("/admin")
  public String adminPage(Model model) {
    model.addAttribute("allPerks", perkRepository.findAll());
    return "admin";
  }

  @PostMapping("/admin/toggle/{id}")
  public String togglePerk(@PathVariable Long id) {
    Perk perk = perkRepository.findById(id).orElseThrow();
    perk.setExcluded(!perk.isExcluded());
    perkRepository.save(perk);
    return "redirect:/admin";
  }
}