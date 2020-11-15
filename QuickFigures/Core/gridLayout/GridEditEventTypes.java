package gridLayout;

public interface GridEditEventTypes {
	public static int BORDER_EDIT_H=0, BORDER_EDIT_V=1, PANEL_SWAP=2, ROW_SWAP=3, COL_SWAP=4, PANEL_INSERTION=5, COL_INSERTION=6, ROW_INSERTION=7,  PANEL_REMOVAL=8, COL_REMOVAL=9, ROW_REMOVAL=10, PANEL_RESIZE_H=11, PANEL_RESIZE_V=12, COL_RESIZE=13, ROW_RESIZE=14;

	public static final int COL_ADDITION = 16, ROW_ADDITION=15, INVERSION=17;
	public static final int REPACKAGE = 19;

	public static final int LOCATION_EDIT = 18;
	public static int LABEL_SPACE_EDIT=200;
	public static int ADITIONAL_SPACE_OR_LOCATION_EDIT=400;

}
