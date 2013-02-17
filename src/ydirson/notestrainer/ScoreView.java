package ydirson.notestrainer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import java.lang.Math;

class Clef {
    int _svgResource;
    int _height, _top;
    public Clef(int svgResource, int height, int top) {
        _svgResource = svgResource;
        _height = height;
        _top = top;
    }
}

public class ScoreView extends View {
    int _height;
    int _width;
    Paint _paint;
    Picture _clefPic;
    Rect _clefRect;
    Clef _clef;

    int _note;

    static final int _lineInterval = 10;
    static final int _linesVPad    = 40;
    static final int _linesHpad    = 2;
    static final int _clefLeft     = 10;

    static final int   _clefToNote = 30;
    static final float _noteWidth  = 1.25f * _lineInterval;

    public static final Clef ClefG2 = new Clef(R.raw.gclef, 7 * _lineInterval,
                                               _linesVPad - _lineInterval * 13 / 10);
    public static final Clef ClefF4 = new Clef(R.raw.fclef, _lineInterval * 7 / 2,
                                               _linesVPad);
    public static final Clef ClefC3 = new Clef(R.raw.cclef, 4 * _lineInterval,
                                               _linesVPad);
    public static final Clef ClefC4 = new Clef(R.raw.cclef, 4 * _lineInterval,
                                               _linesVPad - _lineInterval);

    public ScoreView(Context context) {
        super(context);
        _paint = new Paint();
        _paint.setColor(Color.BLACK);
        _paint.setStyle(Paint.Style.FILL);

        // default clef
        setClef(ClefG2);

        // no note by default
        _note = -1;
    }

    public void setClef(Clef clef) {
        _clef = clef;
        SVG svg = SVGParser.getSVGFromResource(getResources(), clef._svgResource);
        _clefPic = svg.getPicture();
        int scaledWidth = _clefPic.getWidth() * _clef._height / _clefPic.getHeight();
        _clefRect = new Rect(_clefLeft, _clef._top,
                             _clefLeft + scaledWidth, _clef._top + _clef._height);
    }

    /*
     * note: A1 = 0, B1 = 1, ... A2 = 7, A3 = 14, etc
     * treble key range for reference: D3 - G4 = 17 - 27
     */
    public void setNote(int note) {
        _note = note;
        invalidate();
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

        // if the clef was not set yet, stop here
        if (_clefPic == null) {
            Log.d("ScoreView", "null clef");
            return;
        }

        // clef
        canvas.drawPicture(_clefPic, _clefRect);

        // note if any
        if (_note >= 0) {
            float hpos = _clefLeft + _clefRect.width() + _clefToNote;
            float vposBottom = _linesVPad - 0.5f * _lineInterval * (_note - 27);
            canvas.drawOval(new RectF(hpos, vposBottom - _lineInterval,
                                      hpos + _noteWidth, vposBottom), _paint);

            // lower supplementary lines
            for (int suplines = (18 - _note) / 2; suplines > 0; suplines--) {
                int vpos = _linesVPad + (4 + suplines) * _lineInterval;
                canvas.drawLine(hpos - 0.5f * _noteWidth, vpos,
                                hpos + 1.5f * _noteWidth, vpos, _paint);
            }
            // upper supplementary lines
            for (int suplines = (_note - 26) / 2; suplines > 0; suplines--) {
                int vpos = _linesVPad + (0 - suplines) * _lineInterval;
                canvas.drawLine(hpos - 0.5f * _noteWidth, vpos,
                                hpos + 1.5f * _noteWidth, vpos, _paint);
            }
        }
    }
}
