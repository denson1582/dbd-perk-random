package com.example.dbd_pack_random.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  // このカテゴリに属するパーク一覧（双方向から参照したい場合）
  @OneToMany(mappedBy = "category")
  private List<Perk> perks;

  protected Category() {}
  public Category(String name) { this.name = name; }

  // Getter
  public Long getId() { return id; }
  public String getName() { return name; }
}
