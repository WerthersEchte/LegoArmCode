package application.background;

public enum DefaultMarkerPositions {
	

    _RedCube(470.0f, -200.0f, -100.0f, -90.0f, 0.0f, -90.0f),
    _3d15PaperCube(482.0f, 201.0f, -75.0f, -90.0f, 0.0f, -90.0f),
    _3d15GravurCube(482.0f, -201.0f, -75.0f, -90.0f, 0.0f, -90.0f);

    private final float mXDefault, mYDefault, mZDefault, mAlphaDefault, mBetaDefault, mGammaDefault;

    DefaultMarkerPositions(){
        mXDefault = 0;
        mYDefault = 0;
        mZDefault = 0;
        mAlphaDefault = 0;
        mBetaDefault = 0;
        mGammaDefault = 0;
    }

    DefaultMarkerPositions(float aXDefault, float aYDefault, float aZDefault, float aAlphaDefault, float aBetaDefault, float aGammaDefault) {
        mXDefault = aXDefault;
        mYDefault = aYDefault;
        mZDefault = aZDefault;
        mAlphaDefault = aAlphaDefault;
        mBetaDefault = aBetaDefault;
        mGammaDefault = aGammaDefault;
    }

    public float getXDefault() {
        return mXDefault;
    }

    public float getYDefault() {
        return mYDefault;
    }

    public float getZDefault() {
        return mZDefault;
    }

    public float getAlphaDefault() {
        return mAlphaDefault;
    }

    public float getBetaDefault() {
        return mBetaDefault;
    }

    public float getGammaDefault() {
        return mGammaDefault;
    }
}
