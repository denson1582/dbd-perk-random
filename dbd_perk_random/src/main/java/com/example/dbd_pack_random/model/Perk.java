package com.example.dbd_pack_random.model;

import jakarta.persistence.*;

@Entity
@Table(name = "perks")
public class Perk {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Enumerated(EnumType.ORDINAL)
  private Role role;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id")
  private Category category;

  private int stars;
  private int weight;

  @Column(name = "is_excluded")
  private boolean isExcluded;

  protected Perk() {}

  public Perk(String name, Role role, Category category, int stars, int weight, boolean isExcluded) {
    this.name = name;
    this.role = role;
    this.category = category;
    this.stars = stars;
    this.weight = weight;
    this.isExcluded = isExcluded;
  }

  // --- Getter ---
  public Long getId() { return id; }
  public String getName() { return name; }
  public Role getRole() { return role; }
  public Category getCategory() { return  category; }
  public int getStars() { return stars; }
  public int getWeight() { return weight; }
  public boolean isExcluded() { return isExcluded; }

  // --- Setter (★これが必要！) ---
  public void setExcluded(boolean excluded) {
    this.isExcluded = excluded;
  }

  @Override
  public String toString() {
    return String.format("[%s] %s (★%d) 重み:%d", category, name, stars, weight);
  }
}