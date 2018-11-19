package com.briup.util;
/**
 * 实现该接口，那么配置模块在初始化该模块的同时
 * 将自身的引用传递给该模块
 * 然后在该模块就可以通过配置模块获取需要的其他类对象
 * 避免了重复new对象的操作
 * @author lw
 *
 */
public interface ConfigurationAware {
	/**
	 * 用于传递配置模块
	 * @param con	配置模块对象
	 */
	public void setConfiguration(Configuration con);
}
