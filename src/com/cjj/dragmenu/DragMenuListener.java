package com.cjj.dragmenu;
/**
 * 
 * 拖拉菜单监听。。。
 * @author cjj
 *
 */
public interface DragMenuListener {
	
	public void onOpen();

	public void onClose();

	public void onDragMenu(float percent);
}
