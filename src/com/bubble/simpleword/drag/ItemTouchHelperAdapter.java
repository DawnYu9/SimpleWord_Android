package com.bubble.simpleword.drag;
/**
 * <p>Title: ItemTouchHelperAdapter</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-9-18 下午10:14:57
 */
public interface ItemTouchHelperAdapter {
	
    void onItemMove(int fromPosition, int toPosition);
 
    void onItemDismiss(int position);

}
