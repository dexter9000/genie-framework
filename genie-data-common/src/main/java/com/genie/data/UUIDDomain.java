package com.genie.data;

/**
 * UUID 作主键的Domain类接口
 * 实现该接口的domain类可以在持久化前生成UUID格式的主键
 */
public interface UUIDDomain {

  String getId();

  void setId(String id);

}
