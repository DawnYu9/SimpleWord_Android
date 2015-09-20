package com.bubble.simpleword.drag;
/**
 * <p>Title: ItemTouchHelperViewHolder</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-9-19 下午1:20:26
 */
public interface ItemTouchHelperViewHolder {
	/**
	 * <p>Title: ItemTouchHelperViewHolder</p>
	 * <p>Description: </p>
	 * <p>Company: </p> 
	 * @version 1.0   
	 * @since JDK 1.8.0_45
	 * @author bubble
	 * @date 2015-9-19 下午1:20:27
	 */

    /**
     * Called when the {@link ItemTouchHelper} first registers an item as being moved or swiped.
     * Implementations should update the item view to indicate it's active state.
     */
    void onItemSelected();


    /**
     * Called when the {@link ItemTouchHelper} has completed the move or swipe, and the active item
     * state should be cleared.
     */
    void onItemClear();
}
