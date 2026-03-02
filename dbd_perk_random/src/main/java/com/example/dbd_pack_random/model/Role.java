package com.example.dbd_pack_random.model;

public enum Role {
  SURVIVOR(0), // 内部的には0
  KILLER(1);   // 内部的には1

  private final int value;
  Role(int value) { this.value = value; }
  public int getValue() { return value; }
}