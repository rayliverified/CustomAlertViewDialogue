package stream.customalert.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class RoundedCornersDrawable extends GradientDrawable {

    public RoundedCornersDrawable(int cornerRadius){
        super(Orientation.BOTTOM_TOP, new int[]{Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT});
        setCornerRadius(cornerRadius);
    }
}
