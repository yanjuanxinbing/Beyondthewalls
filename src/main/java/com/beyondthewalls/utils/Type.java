package com.beyondthewalls.utils;

/**
 * 实体分类。与 Tiled 中 object 的 {@code class} 属性一一对应，
 * 但允许一类 tmx class 映射到同一个 Type（如两个外观变体共用 {@link #girl}）。
 */
public enum Type {
    /** 女孩（两个外观变体共用，type 见 PlayerFactory 的 girlNoPin / girlWithPin） */
    girl,
    /** 小猫，方向键操控 */
    cat,
    /** 第五关的小猫，按住蓄力、松开起跳 */
    catJumper,

    /** 压力板，触发对应 id 的 pressPlatform */
    pressurePlate,
    /** 受压力板驱动往返移动的平台 */
    pressPlatform,
    /** 撞墙自动折返的移动平台（无贴图） */
    patrolPlatform,
    /** 同上，但带贴图 */
    patrolPlatformVisible,

    /** 静态障碍，地刺。碰到会把跳跳猫弹开 */
    hazard,
    /** 上下移动的升降板 */
    lift,

    wall,
    barrier,
    bed,
    box,
    water,
    end,
    key,
    lock,
    treasure,
    hairpin,
    secret
}
