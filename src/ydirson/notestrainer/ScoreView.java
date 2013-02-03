package ydirson.notestrainer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.view.View;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import java.lang.Math;

public class ScoreView extends View {
    int _height;
    int _width;
    Paint _paint;
    Picture _clef;
    Rect _clefRect;

    int _lineInterval = 10;
    int _linesVPad    = 40;
    int _linesHpad    = 2;
    int _clefLeft     = 10;
    int _clefTop      = 10;
    int _clefHeight   = 100;

    public ScoreView(Context context) {
        super(context);
        _paint = new Paint();
        _paint.setColor(Color.BLACK);
        _paint.setStyle(Paint.Style.FILL);

        SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.gclef);
        _clef = svg.getPicture();
        int scaledWidth = _clef.getWidth() * _clefHeight / _clef.getHeight();
        _clefRect = new Rect(_clefLeft, _clefTop,
                             _clefLeft + scaledWidth, _clefTop + _clefHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        _width = View.MeasureSpec.getSize(widthMeasureSpec);
        _height = View.MeasureSpec.getSize(heightMeasureSpec);
        _height = Math.min(_height, 2 * _linesVPad + 5 * _lineInterval);
        setMeasuredDimension(_width, _height);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // hlines
        for (int i = 0; i < 5; i++) {
            int vpos = _linesVPad + i * _lineInterval;
            canvas.drawLine(_linesHpad, vpos,
                            _width - _linesHpad, vpos, _paint);
        }
        // vertical start bar
        canvas.drawLine(_linesHpad, _linesVPad,
                        _linesHpad, _linesVPad + 4 * _lineInterval, _paint);
        // clef
        canvas.drawPicture(_clef, _clefRect);
    }
}
