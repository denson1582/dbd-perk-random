package com.example.dbd_pack_random.model;

import jakarta.persistence.*;

@Entity
@Table(name = "perks")
public class Perk {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private Role role;

  @ManyToOne(fetch = FetchType.EAGER) // ここが重要！「多くのパークが1つのカテゴリに属する」という印
  @JoinColumn(name = "category_id")   // DBの「category_id」列と紐付けるという印
  private Category category;

  private  int stars;
  private  int weight;
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

  public String getName() {
    return name;
  }

  public Role getRole() {
    return role;
  }

  public Category getCategory() {
    return  category;
  }

  public  int getStars() {
    return stars;
  }

  public int getWeight() {
    return weight;
  }

  public boolean isExcluded() {
    return isExcluded;
  }

  @Override
  public String toString() {
    return String.format("[%s] %s (★%d) 重み:%d", category, name, stars, weight);
  }
}
