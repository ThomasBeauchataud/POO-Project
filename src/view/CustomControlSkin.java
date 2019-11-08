package view;

import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;

class CustomControlSkin extends SkinBase<CustomControl> implements Skin<CustomControl> {

	//region Constructor

	/**
	 * CustomControlSkin Constructor
	 * @param cc CustomControl
	 */
	CustomControlSkin(CustomControl cc) {
		super(cc);
	}

	//endregion
}