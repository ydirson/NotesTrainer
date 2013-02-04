package ydirson.notestrainer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
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

    int _note;

    int _lineInterval = 10;
    int _linesVPad    = 40;
    int _linesHpad    = 2;
    int _clefLeft     = 10;
    int _clefTop      = 10;
    int _clefHeight   = 100;

    int   _clefToNote = 30;
    float _noteWidth  = 1.25f * _lineInterval;

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

        // no note by default
        _note = -1;
    }

    /*
     * note: A1 = 0, B1 = 1, ... A2 = 7, A3 = 14, etc
     * treble key range for reference: D3 - G4 = 17 - 27
     */
    public void setNote(int note) {
        _note = note;
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
